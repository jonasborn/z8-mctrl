package z8.mctrl.handler

import org.apache.logging.log4j.LogManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.config.Config
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.server.WSServer
import z8.proto.alpha.ServerMessage
import z8.proto.alpha.TokenAwait
import javax.annotation.PostConstruct

/**
 * Used to register subscribe to the matching kvs queue of a new device
 * As this is a multi-server based system, every server may hold x devices and
 * needs to talk to all registered devices over all servers.
 *
 */

@Component
class PaymentRequestWelcomeHandler @Autowired constructor(
    val config: Config,
    val rds: RDS,
    val wsServer: WSServer,
    val paymentRequests: PaymentRequests,
    val internalDevices: InternalDevices
) {

    val logger = LogManager.getLogger()

    @PostConstruct
    fun init() {
        wsServer.onMessage("PRWelcomeHandler") {
            val conn = it.first
            val cm = it.second

            if (cm.hasWelcomeMessage()) {
                logger.debug(
                    "Registered listener for internal device {} on server {}",
                    cm.id,
                    config.string("server.id")
                )
                val (id, q) = paymentRequests.listenToQueue(cm.source) { id ->
                    logger.debug("Received PaymentRequest request for {} on {}", id, config.string("server.id"))
                    val paymentRequest = paymentRequests.get(id)

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
                            paymentRequests.update(paymentRequest)
                        } else {
                            paymentRequest.status = PaymentRequestStatus.INTERNAL_DEVICE_MISSING
                            paymentRequests.update(paymentRequest)
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
    }

}