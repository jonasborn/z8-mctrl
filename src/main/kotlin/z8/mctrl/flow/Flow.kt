package z8.mctrl.flow

import org.jasypt.encryption.pbe.StandardPBEByteEncryptor
import org.jasypt.iv.RandomIvGenerator
import z8.mctrl.config.Config
import java.nio.ByteBuffer

class Flow(val config: Config) {

        var jasypt: StandardPBEByteEncryptor? = null;

        init {
            jasypt = StandardPBEByteEncryptor()
            val password = config.get("session.password")
            jasypt!!.setPassword(password);
            jasypt!!.setIvGenerator(RandomIvGenerator())
        }

        fun encrypt(array: ByteArray): ByteArray? {
            return jasypt!!.encrypt(array)
        }

        fun decrypt(array: ByteArray): ByteArray? {
            return jasypt!!.decrypt(array)
        }

        fun pack(flow: Flow): ByteArray? {
            return null
        }

        fun unpack(byte: ByteArray) {

        }

}