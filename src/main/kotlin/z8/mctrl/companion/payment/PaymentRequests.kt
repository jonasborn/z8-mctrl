package z8.mctrl.companion.payment

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.redisson.api.RBlockingQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.config.Config
import z8.mctrl.db.KVS
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.jooq.tables.pojos.PaymentRequestObject
import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage
import z8.proto.alpha.TokenAwait
import java.util.function.Consumer
import kotlin.math.log

@Component
class PaymentRequests @Autowired constructor(
    val config: Config,
    val rds: RDS,
    val kvs: KVS,
    val internalDevices: InternalDevices
) {

    class PaymentRequestDetails(val id: String, val source: String, val target: String, val amount: Double)

    val logger: Logger = LogManager.getLogger()

    /**
     * Used to add a payment request (mostly given by a external device) to
     * the kvs queue of the matching internal device
     */
    fun addToQueue(internalDeviceId: String, paymentRequestId: String) {
        kvs.paymentRequestQueue(internalDeviceId)?.add(paymentRequestId)
    }

    /**
     * Used to listen to any payment requests inserted by a device. A server holds a set of devices, as a device is
     * always connected to one server only, but there my be multiple servers.
     */
    fun listenToQueue(internalDeviceId: String, consumer: Consumer<String>): Pair<Int?, RBlockingQueue<String>?> {
        return kvs.paymentRequestQueue(internalDeviceId, consumer)
    }

    fun get(id: String): PaymentRequestObject? {
        return rds.paymentRequest().fetchOneById(id)
    }

    fun add(pr: PaymentRequestObject) {
        rds.paymentRequest().insert(pr)
    }

    fun update(pr: PaymentRequestObject) {
        rds.paymentRequest().update(pr)
    }

    fun canHandle(conn: WSServer.WSSocket, cm: ClientMessage): Boolean {
        if (cm.hasWelcomeMessage()) return true
        if (cm.hasTokenFoundEvent()) return true
        return false
    }

    /**
     * Used to register a internal device to it's own queue.
     * When receiving a payment request from a external device, a
     * element is added to the queue and TokenAwait is sent to the related
     * device
     */
    fun onMessage(conn: WSServer.WSSocket, cm: ClientMessage) {
        if (cm.hasWelcomeMessage()) {
            logger.debug("Registered listener for internal device {} on server {}", cm.id, config.string("server.id"))
            val (id, q) = listenToQueue(cm.source) { id ->
                logger.debug("Received PaymentRequest request for {} on {}", id, config.string("server.id"))
                val paymentRequest = get(id)

                if (paymentRequest?.internal != null) {
                    logger.debug("Payment request found")
                    val ind = internalDevices.get(paymentRequest.internal!!)
                    if (ind?.secret != null) {
                        logger.debug("Internal device {} found for payment request {}", ind.id, paymentRequest.id)

                        conn.sendWithSecret(
                            ServerMessage.newBuilder()
                                .setBarer(paymentRequest.id) //Used to identify the payment of this token await
                                .setTokenAwait(
                                    TokenAwait.newBuilder()
                                ).build(), ind.secret!!
                        )

                        logger.debug("Token await sent to device {}", ind.id)

                        paymentRequest.status = PaymentRequestStatus.STARTED
                        update(paymentRequest)
                    } else {
                        paymentRequest.status = PaymentRequestStatus.INTERNAL_DEVICE_MISSING
                        update(paymentRequest)
                    }
                } else {
                    logger.error("No payment request in database found!")
                }
            }

            id?.let { listenerId ->
                conn.instance.onClose("PaymentRequestsCloseHandler", id) {
                    q?.unsubscribe(listenerId)
                }
            }


        }
    }

    fun tokenAuthenticated(tokenId: String) {

    }


}