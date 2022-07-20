package z8.mctrl.db

import org.redisson.Redisson
import org.redisson.api.*
import org.redisson.config.Config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.db.forced.PaymentRequestStatus
import java.util.function.Consumer

@Component
class KVS @Autowired constructor(val config: z8.mctrl.config.Config) {

    //TODO https://github.com/redisson/redisson/wiki/7.-Distributed-collections
        var client: RedissonClient? = null

        fun init() {
            val c = Config()
            c.useClusterServers()
                .addNodeAddress(
                    "redis://"
                            + config.get("redis.host")
                            + ":" + config.get("redis.port")
                )
            client = Redisson.create(c)
        }

        fun <T> set(value: T, vararg key: String) {
            var b = client!!.getBucket<T>(key.joinToString("/"))
            b.set(value)
        }

        fun <T> get(vararg key: String): T? {
            var b = client!!.getBucket<T>(key.joinToString("/"))
            return b.get()
        }

        fun getALong(vararg key: String): RAtomicLong? {
            return client!!.getAtomicLong(key.joinToString("/"))
        }

        fun <T> getBucket(vararg key: String): RBucket<T>? {
            return client!!.getBucket(key.joinToString("/"))
        }

        fun <T> getBlockingQueue(vararg key: String): RBlockingQueue<T>? {
            return client!!.getBlockingQueue(key.joinToString("/"))
        }

        fun paymentRequestDetails(id: String): RBucket<PaymentRequests.PaymentRequestDetails>? {
            return getBucket(
                "payment", "request", id, "details"
            )
        }

        fun paymentRequestStatus(id: String): RBucket<PaymentRequestStatus>? {
            return getBucket(
                "payment", "request", id, "status"
            )
        }

        fun paymentRequestQueue(internalDeviceId: String): RBlockingQueue<String>? {
            return getBlockingQueue(
                "payment", internalDeviceId, "requests"
            )
        }

        fun paymentRequestQueue(internalDeviceId: String, func: Consumer<String>): Pair<Int?, RBlockingQueue<String>?> {
            val queue = getBlockingQueue<String>(
                    "payment", internalDeviceId, "requests"
            )
            val id = queue?.subscribeOnElements(func)
            return Pair(id, queue)
        }


}
