package z8.mctrl.function.token

import z8.mctrl.util.IdUtils

class TokenId() {

    var base: String? = null
    var check: String? = null

    init {
        base = IdUtils.generateLuhn(14).joinToString("")
        check = IdUtils.generateISO7064Check(base!!)
    }

    constructor(total: String) : this() {
        base = total.substring(0, 12)
        check = total.substring(12, 14)
    }

    fun getBaseParts(): List<String> {
        return base!!.toCharArray().toList().chunked(4).map {
            it.joinToString("")
        }
    }

    fun getParts(): List<String> {
        return get().toCharArray().toList().chunked(4).map {
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