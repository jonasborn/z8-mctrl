package z8.mctrl.handler

import des.UltralightC
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage

class UltralightCHandler : WSHandler {

    val log: Logger = LogManager.getLogger()

    override fun canHandle(cm: ClientMessage): Boolean {
        if (cm.hasTokenFoundEvent()) return true
        if (cm.hasUltralightCAuthentication()) return true
        return false
    }

    override fun handle(cm: ClientMessage): ServerMessage? {
        if (cm.hasTokenFoundEvent()) {
            log.debug("Token {} detected", cm.tokenFoundEvent.token)
            return ServerMessage.newBuilder()
                .setUltralightCAuthentication(
                    UltralightC.requestStage1(cm.tokenFoundEvent)
                ).build()
        }
        if (cm.hasUltralightCAuthentication()) {
            val stage = cm.ultralightCAuthentication.stage
            log.debug("UltralightC stage {} response for {} received", stage, cm.ultralightCAuthentication.token)
            if (stage == 2) {
                return ServerMessage.newBuilder()
                    .setUltralightCAuthentication(
                        UltralightC.requestStage3(
                            cm.ultralightCAuthentication
                        )
                    ).build()
            }
            if (stage == 3) {
                return ServerMessage.newBuilder()
                    .setTokenAuthenticatedEvent(
                        UltralightC.requestStage5(cm.ultralightCAuthentication)
                    ).build()
            }
        }
        return null
    }

}