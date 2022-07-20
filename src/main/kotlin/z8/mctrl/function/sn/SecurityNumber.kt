package z8.mctrl.function.sn

import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import org.jasypt.digest.StandardByteDigester
import org.jasypt.salt.ZeroSaltGenerator
import z8.mctrl.config.Config
import z8.mctrl.function.token.TokenId
import z8.mctrl.util.IdUtils
import java.math.BigInteger

class SecurityNumbers(config: Config) {

    class SecurityNumber(securityNumbers: SecurityNumbers, id: String) {
        var base: String? = null
        var check: String? = null


        init {
            base = securityNumbers.generateSecurityNumberBase(id)
            check = IdUtils.generateISO7064Check(base!!)
        }

        fun getBaseParts(): List<String> {
            return base!!.toCharArray().toList().chunked(3).map {
                it.joinToString("")
            }
        }

        fun getParts(): List<String> {
            return get().toCharArray().toList().chunked(3).map {
                it.joinToString("")
            }
        }

        fun get(): String {
            return base!! + check!!
        }

        fun getReadable(): String {
            return getParts().joinToString(" ")
        }
    }

    private val digest: StandardByteDigester = StandardByteDigester()
    private var secret: String? = null

    init {
        digest.setIterations(100)
        digest.setAlgorithm("SHA-256")
        digest.setSaltGenerator(ZeroSaltGenerator())
        secret = config.get("sn.secret")
    }

    fun generateSecurityNumberBase(id: String): String {
        val input: ByteArray = (id.toByteArray() + secret!!.toByteArray())
        val bytes = Hashing.sha256().hashBytes(
            digest.digest(input)
        ).asBytes()
        return BigInteger(bytes).abs().toString().substring(0, 7)
    }


}