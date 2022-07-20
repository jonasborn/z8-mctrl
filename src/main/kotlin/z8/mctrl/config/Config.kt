package z8.mctrl.config

import mu.KotlinLogging
import org.apache.logging.log4j.LogManager
import org.springframework.stereotype.Component
import z8.mctrl.db.RDS
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.system.exitProcess

private val log = KotlinLogging.logger {}

@Component
class Config() {

    val logger = LogManager.getLogger()

    var props: Properties? = null

    init {
        try {
            val source = File("z8-mctrl.properties")
            props = Properties()
            props!!.load(FileInputStream(source))
            log.debug { "Config loaded from $source" }
            check(props!!)
        } catch (e: Exception) {
            log.error { "Unable to load config! See stacktrace for info and fix the issue" }
            e.printStackTrace()
            exitProcess(1)
        }
    }

    private fun check(props: Properties) {
        logger.info("Checking config")
        val reqs = Properties()
        reqs.load(RDS::class.java.getResourceAsStream("/requirements.properties")!!)
        val itr = reqs.keys()
        while (itr.hasMoreElements()) {
            val key = itr.nextElement()
            val value = reqs[key] as String
            if (props.containsKey(key)) {
                val remaining = (props[key] as String).replace(value.toRegex(), "")
                if (remaining.isNotEmpty()) {
                    logger.error("Required key {} does not match the required regex {}", key, value)
                } else {
                    logger.debug("Key {} was accepted, matching {}", key, value)
                }
            } else {
                logger.error("Required key {}, matching {} was not found in config", key, value)
            }
        }
        logger.info("Config accepted")
    }

    fun get(key: String): String? {
        if (!props!!.containsKey(key)) return null
        return props!!.getProperty(key)
    }

    fun string(key: String, alternative: String): String {
        return get(key) ?: return alternative
    }

    fun int(key: String, alternative: Int): Int {
        val value = get(key) ?: return alternative
        return try {
            value.toInt()
        } catch (e: Exception) {
            alternative
        }
    }

    fun long(key: String, alternative: Long): Long {
        val value = get(key) ?: return alternative
        return try {
            value.toLong()
        } catch (e: Exception) {
            alternative
        }
    }

    fun float(key: String, alternative: Float): Float {
        val value = get(key) ?: return alternative
        return try {
            value.toFloat()
        } catch (e: Exception) {
            alternative
        }
    }

    fun double(key: String, alternative: Double): Double {
        val value = get(key) ?: return alternative
        return try {
            value.toDouble()
        } catch (e: Exception) {
            alternative
        }
    }


}