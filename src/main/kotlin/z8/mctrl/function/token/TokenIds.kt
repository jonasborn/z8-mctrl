package z8.mctrl.function.token

import z8.mctrl.util.IdUtils

class TokenIds {

    companion object {

        fun generate(): List<String> {
            return IdUtils.generateLuhn(16).toList().chunked(4).map {
                it.joinToString("")
            }
        }

        fun generateReadable(joined: String) {
            joined.toCharArray().toList().chunked(4)
                .map { it.toString() }.joinToString(" ")
        }

        fun generateJoined(base: String, check: String): String {
            return base + check;
        }

        fun generateBase(): String {
            return IdUtils.generateLuhn(14).joinToString("")
        }

        fun generateCheck(base: String) {
            IdUtils.generateISO7064Check(base);
        }

        fun generateCheck(base: List<String>) {
            return generateCheck(base)
        }
    }

}