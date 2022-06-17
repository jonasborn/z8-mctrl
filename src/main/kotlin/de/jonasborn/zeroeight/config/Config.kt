package de.jonasborn.zeroeight.config

import mu.KotlinLogging
import java.io.File
import java.io.FileInputStream
import java.util.*
import kotlin.system.exitProcess

private val log = KotlinLogging.logger {}
class Config {

    companion object {
        val props : Properties? = null

        init {
            try {
                val source = File("zeroeight.properties")
                val pr = Properties()
                pr.load(FileInputStream(source))
                log.debug { "Config loaded from $source"}
            } catch (e: Exception) {
                log.error { "Unable to load config! See stacktrace for info and fix the issue" }
                e.printStackTrace()
                exitProcess(1)
            }
        }

        fun get(key: String): String? {
            if (props == null) return null
            if (!props.containsKey(key)) return null
            return props.getProperty(key)
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

}