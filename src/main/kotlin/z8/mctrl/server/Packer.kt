package z8.mctrl.server

import com.google.common.hash.Hashing
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import z8.proto.alpha.ServerMessage
import java.io.ByteArrayOutputStream

class Packer {

    companion object {
        private val logger: Logger = LogManager.getLogger()
        public fun pack(sm: ServerMessage, secret: String): ByteArray {
            val op = ByteArrayOutputStream()
            sm.writeTo(op)
            val secretBytes = secret.toByteArray()
            op.writeBytes(secretBytes)
            return sm.toBuilder().build().toByteArray()
        }
    }

}