package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import z8.proto.alpha.ClientMessage
import java.nio.ByteBuffer

class UnPacker {

    companion object {
        private val logger: Logger = LogManager.getLogger()

        fun unpack(conn: WebSocket, message: ByteBuffer): ClientMessage? {
            try {
                val m = ClientMessage.parseFrom(message)
                if (m.hasTokenReadEvent()) {
                    println(m.tokenReadEvent.token)
                }
                if (m.hasLogMessage()) {
                    println(
                        m.logMessage.level.toString() + " - " +
                        m.logMessage.source + ": " + m.logMessage.message
                    )
                }
                if (m.hasDesFireAuthentication()) {
                    if (m.desFireAuthentication.stage == 0) {

                    }
                }

            } catch (e: Exception) {
                logger.warn("Unable to parse message", e)
            }
            return null;
        }
    }

}