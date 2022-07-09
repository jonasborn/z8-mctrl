package z8.mctrl.server

import com.google.common.io.BaseEncoding
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.java_websocket.WebSocket
import z8.mctrl.handler.UltralightCHandler
import z8.mctrl.handler.WSHandler
import z8.proto.alpha.ClientMessage
import z8.proto.alpha.ServerMessage
import java.nio.ByteBuffer

class UnPacker {

    companion object {
        private val logger: Logger = LogManager.getLogger()

        private val HANDLERS: ArrayList<WSHandler> = ArrayList()

        init {
            HANDLERS.add(UltralightCHandler())
        }

        fun unpack(conn: WebSocket, message: ByteBuffer): ClientMessage? {
            try {
                val m = ClientMessage.parseFrom(message)
                logger.debug("Received client message: <<<<<<<<<<<<< {}", m)
                HANDLERS.filter { it.canHandle(m) }.forEach {
                    logger.debug("Handler {} matches", it.javaClass)
                    val sm = it.handle(m)
                    if (sm != null) {
                        logger.debug(
                            "Handler returning\n>>>>>>>>>>>>> {}\n{}",
                            sm.toString(),
                            BaseEncoding.base16().encode(sm.toByteArray())
                        )
                        conn.send(sm.toByteArray())
                    }
                }

            } catch (e: Exception) {
                logger.warn("Unable to parse message", e)
            }
            return null
        }
    }

}