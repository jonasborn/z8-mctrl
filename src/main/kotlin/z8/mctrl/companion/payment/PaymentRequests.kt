package z8.mctrl.companion.payment

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import org.redisson.api.RBlockingQueue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.config.Config
import z8.mctrl.db.KVS
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.jooq.tables.pojos.Paymentrequest
import z8.mctrl.server.Packer
import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage
import z8.proto.alpha.TokenAwait
import java.util.function.Consumer

@Component
class PaymentRequests @Autowired constructor(val config: Config, val rds: RDS, val kvs: KVS, val internalDevices: InternalDevices) {


    class PaymentRequestDetails(val id: String, val source: String, val target: String, val amount: Double)

    val logger: Logger = LogManager.getLogger()

    fun addToQueue(internalDeviceId: String, id: String) {
        kvs.paymentRequestQueue(internalDeviceId)?.add(id)
    }

    fun listenToQueue(internalDeviceId: String, consumer: Consumer<String>): Pair<Int?, RBlockingQueue<String>?> {
        return kvs.paymentRequestQueue(internalDeviceId, consumer)
    }

    fun get(id: String): Paymentrequest? {
        return rds.paymentRequest().fetchOneById(id)

    }

    fun add(pr: Paymentrequest) {
        rds.paymentRequest().insert(pr)
    }

    fun update(pr: Paymentrequest) {
        rds.paymentRequest().update(pr)
    }

    /**
     * Used to register a internal device to it's own queue.
     * When receiving a payment request from a external device, a
     * element is added to the queue and TokenAwait is sent to the related
     * device
     */
    fun onMessage(conn: WSServer.WSSocket, cm: ClientMessage) {
        if (cm.hasWelcomeMessage()) {
            val (id, q) = listenToQueue(cm.source) { id ->

                logger.debug("Received PaymentRequest request for {} on {}", id, config.get("server.id"))
                val paymentRequest = get(id)

                if (paymentRequest?.target != null) {
                    val ind = internalDevices.get(paymentRequest.target!!)
                    if (ind?.secret != null) {

                        conn.sendWithSecret(
                            ServerMessage.newBuilder().setTokenAwait(
                                TokenAwait.newBuilder()
                            ).build(), ind.secret!!
                        )

                        paymentRequest.status = PaymentRequestStatus.STARTED
                        update(paymentRequest)
                    } else {
                        paymentRequest.status = PaymentRequestStatus.INTERNAL_DEVICE_MISSING
                        update(paymentRequest)
                    }
                } else {
                    logger.error("PaymentRequest is empty!")
                }
            }

            id?.let { listenerId ->
                conn.instance.onClose("PaymentRequestsCloseHandler", id) {
                    q?.unsubscribe(listenerId)
                }
            }


        }
    }


}