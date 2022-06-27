package z8.mctrl.server

import com.google.common.hash.Hashing
import z8.proto.alpha.ServerMessage
import java.io.ByteArrayOutputStream

class Packer {

    companion object {
        fun pack(sm: ServerMessage, secret: String): ServerMessage {
            val op = ByteArrayOutputStream()
            sm.writeTo(op)
            val secretBytes = secret.toByteArray()
            op.writeBytes(secretBytes)
            return sm.toBuilder()
                .setSecret(Hashing.sha256().hashBytes(op.toByteArray()).toString()).build()
        }
    }

}