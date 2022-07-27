package z8.mctrl.flow

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jasypt.encryption.pbe.StandardPBEByteEncryptor
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.RandomIvGenerator
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.config.Config
import java.nio.ByteBuffer

@Component
class Flow @Autowired constructor(val config: Config) {


    val gson: Gson = Gson()
    final var jasypt: StandardPBEStringEncryptor? = null

    init {
        jasypt = StandardPBEStringEncryptor()
        val password = config.get("flow.secret")
        jasypt!!.setPassword(password)
        jasypt!!.setIvGenerator(RandomIvGenerator())
    }

    fun pack(obj: Any): String? {
        try {
            return jasypt!!.encrypt(gson.toJson(obj))
        } catch (e: Exception) {

            return null
        }
    }

    fun <T> unpack(s: String): T? {
        return try {
            gson.fromJson(jasypt!!.decrypt(s), object : TypeToken<T>() {}.type)
        } catch (e: Exception) {
            null
        }
    }


}