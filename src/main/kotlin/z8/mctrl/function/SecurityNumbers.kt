package z8.mctrl.function

import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import com.google.common.primitives.Ints
import org.jasypt.digest.StandardByteDigester
import org.jasypt.salt.ZeroSaltGenerator
import org.jasypt.util.text.AES256TextEncryptor
import z8.mctrl.config.Config
import java.math.BigInteger

class SecurityNumbers {


    companion object {

        private val digist: StandardByteDigester = StandardByteDigester()
        private var secret: String? = null

        init {
            digist.setIterations(100)
            digist.setAlgorithm("SHA-256")
            digist.setSaltGenerator(ZeroSaltGenerator())
            secret = Config.get("sn.secret")
        }

        fun generateSecurityNumberBase(id: String): String {
            var input: ByteArray = (id.toByteArray() + secret!!.toByteArray())
            println(BaseEncoding.base32Hex().encode(input))
            val bytes = Hashing.sha256().hashBytes(
                digist.digest(input )
            ).asBytes()
            return BigInteger(bytes).abs().toString().substring(0, 7)
        }
    }

}