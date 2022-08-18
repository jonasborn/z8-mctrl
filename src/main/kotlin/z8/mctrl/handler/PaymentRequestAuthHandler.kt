package z8.mctrl.handler

import des.UltralightC
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.db.KVS
import z8.mctrl.db.RDS
import z8.mctrl.server.WSServer
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage
import java.util.function.BiConsumer
import java.util.function.Consumer
import javax.annotation.PostConstruct

@Component
class PaymentRequestAuthHandler @Autowired constructor(val rds: RDS, val wsServer: WSServer) {

    val logger: Logger = LogManager.getLogger()

    class AuthenticationEvent(val internalDevice: String, val token: String, val barer: String)

    val authenticationListeners: ArrayList<Consumer<AuthenticationEvent>> = arrayListOf()

    @PostConstruct
    fun init() {
        wsServer.onMessage("PRAHandler") {
            val cm = it.second
            val internalDevice = rds.internalDevice().fetchOneById(cm.source)

            if (cm.hasTokenFoundEvent()) {
                logger.debug("Token {} detected", cm.tokenFoundEvent.token)

                it.first.sendWithSecret(
                    ServerMessage.newBuilder()
                        .setUltralightCAuthentication(
                            UltralightC.requestStage1(cm.tokenFoundEvent)
                        ).build(), internalDevice.secret
                )

            }
            if (cm.hasUltralightCAuthentication()) {
                val nextStage = cm.ultralightCAuthentication.nextStage
                logger.debug(
                    "UltralightC next stage {} message for {} received",
                    nextStage,
                    cm.ultralightCAuthentication.token
                )
                if (nextStage == 2) {
                    it.first.sendWithSecret(
                        ServerMessage.newBuilder()
                            .setUltralightCAuthentication(
                                UltralightC.requestStage3(
                                    cm.ultralightCAuthentication
                                )
                            ).build(), internalDevice.secret
                    )
                } else if (nextStage == 4) {
                    val tt = UltralightC.requestStage5(cm.ultralightCAuthentication)
                    if (tt == null) {
                        logger.debug("Authentication failed")
                    } else {
                        logger.debug("Authentication succeeded")
                        //Use to trigger succeeded auth
                        authenticationListeners.forEach { ae ->
                            ae.accept(
                                AuthenticationEvent(
                                    cm.source,
                                    cm.ultralightCAuthentication.token,
                                    cm.barer
                                )
                            )
                        }
                        it.first.sendWithSecret(
                            ServerMessage.newBuilder()
                                .setTrustToken(
                                    UltralightC.requestStage5(cm.ultralightCAuthentication)
                                ).build(), internalDevice.secret
                        )
                    }

                } else {
                    logger.warn("Stage not supported")
                }
            }
        }
    }


}