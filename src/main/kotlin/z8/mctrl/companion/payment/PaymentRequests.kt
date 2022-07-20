package z8.mctrl.companion.payment

import org.java_websocket.WebSocket
import org.redisson.api.RBlockingQueue
import z8.mctrl.companion.device.InternalDevices
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

class PaymentRequests {

    class PaymentRequestDetails(val id: String, val source: String, val target: String, val amount: Double)



    companion object {

        fun addToQueue(internalDeviceId: String, id: String) {
            KVS.paymentRequestQueue(internalDeviceId)?.add(id)
        }

        fun listenToQueue(internalDeviceId: String, consumer: Consumer<String>): Pair<Int?, RBlockingQueue<String>?> {
            return KVS.paymentRequestQueue(internalDeviceId, consumer)
        }

        fun get(id: String): Paymentrequest? {
            return   RDS.paymentRequest().fetchOneById(id)

        }

        fun add(pr: Paymentrequest) {
            RDS.paymentRequest().insert(pr)
        }

        fun update(pr: Paymentrequest) {
            RDS.paymentRequest().update(pr)
        }

        /**
         * Used to register a internal device to it's own queue.
         * When receiving a payment request from a external device, a
         * element is added to the queue and TokenAwait is sent to the related
         * device
         */
        fun handleWelcome(conn: WebSocket, cm: ClientMessage) {
            if (cm.hasWelcomeMessage()) {
                val (id, q) = listenToQueue(cm.source) { id ->

                    val paymentRequest = get(cm.id)

                    if (paymentRequest?.target != null) {
                        val ind = InternalDevices.get(paymentRequest.target!!)
                        if (ind?.secret != null) {
                            val packed = Packer.pack(
                                ServerMessage.newBuilder().setTokenAwait(
                                    TokenAwait.newBuilder()
                                ).build(),
                                ind.secret!!
                            )
                            conn.send(packed)
                            paymentRequest.status = PaymentRequestStatus.STARTED
                            update(paymentRequest)
                        } else {
                            paymentRequest.status = PaymentRequestStatus.FAILURE
                        }
                    }
                }

                WSServer.onClose { ws ->
                    id?.let { it -> q?.unsubscribe(it) }
                }
            }
        }

    }


}