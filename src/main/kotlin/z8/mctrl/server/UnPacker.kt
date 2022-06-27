package z8.mctrl.server

import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import z8.proto.alpha.ClientMessage
import java.nio.ByteBuffer

class UnPacker {

    companion object {
        private val logger: Logger = LogManager.getLogger()

        fun unpack(message: ByteBuffer): ClientMessage? {
            try {
                ClientMessage.parseFrom(message)
            } catch (e: Exception) {
                logger.warn("Unable to parse message", e)
            }
            return null;
        }
    }

}