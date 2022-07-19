package z8.mctrl.companion.payment

import org.java_websocket.WebSocket
import org.redisson.api.RBlockingQueue
import z8.mctrl.companion.device.InternalDevice
import z8.mctrl.db.KVS
import z8.mctrl.server.Packer
import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage
import z8.proto.alpha.TokenAwait
import java.util.function.Consumer

class PaymentRequests {

    class PaymentRequestDetails(val id: String, val source: String, val target: String, val amount: Double)

    enum class PaymentRequestStatus {
        STARTED,
        PENDING,
        FINISHED,
        DECLINED,
        TWO_FACTOR
    }

    companion object {
        fun getStatus(id: String): PaymentRequestStatus? {
            val b = KVS.paymentRequestStatus(id) ?: return null
            return b.get()
        }

        fun setStatus(id: String, status: PaymentRequestStatus) {
            KVS.paymentRequestStatus(id)?.set(status)
        }

        fun getDetails(id: String): PaymentRequestDetails? {
            val b = KVS.paymentRequestDetails(id) ?: return null
            return b.get()
        }

        fun setDetails(id: String, status: PaymentRequestDetails) {
            KVS.paymentRequestDetails(id)?.set(status)
        }

        fun addToQueue(internalDeviceId: String, id: String) {
            KVS.paymentRequestQueue(internalDeviceId)?.add(id)
        }

        fun listenToQueue(internalDeviceId: String, consumer: Consumer<String>): Pair<Int?, RBlockingQueue<String>?> {
            return KVS.paymentRequestQueue(internalDeviceId, consumer)
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
                    val details = getDetails(id)

                    if (details != null) {
                        val packed = Packer.pack(
                            ServerMessage.newBuilder().setTokenAwait(
                                TokenAwait.newBuilder()
                            ).build(),
                            InternalDevice.getPassword(details.target)
                        )
                        conn.send(packed)
                        setStatus(details.id, PaymentRequestStatus.PENDING)
                    }
                }

                WSServer.onClose { ws ->
                    id?.let { it -> q?.unsubscribe(it) }
                }
            }
        }

    }


}