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
            val nextStage = cm.ultralightCAuthentication.nextStage
            log.debug("UltralightC next stage {} message for {} received", nextStage, cm.ultralightCAuthentication.token)
            if (nextStage == 2) {
                return ServerMessage.newBuilder()
                    .setUltralightCAuthentication(
                        UltralightC.requestStage3(
                            cm.ultralightCAuthentication
                        )
                    ).build()
            } else if (nextStage == 4) {
                return ServerMessage.newBuilder()
                    .setTokenAuthenticatedEvent(
                        UltralightC.requestStage5(cm.ultralightCAuthentication)
                    ).build()
            } else {
                log.warn("Stage not supported")
            }
        }
        return null
    }

}