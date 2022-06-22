package z8.mctrl.util

import java.security.SecureRandom

class LuhnUtils {

    companion object {

        val sr = SecureRandom()



        fun generateLuhn(length: Int): IntArray {
            val create = fun(): IntArray {
                val array = IntArray(length)
                for (i in 0 until length) {
                    array[i] = sr.nextInt(9 - 0 + 1) + 0
                }
                return array;
            }

            while (true) {
                val generated = create();
                if (checkLuhn(generated)) {
                    return generated;
                }
            }
        }

        fun checkLuhn(digits: IntArray): Boolean {
            var sum = 0
            val length = digits.size
            for (i in 0 until length) {
                // get digits in reverse order
                var digit = digits[length - i - 1]
                // every 2nd number multiply with 2
                if (i % 2 == 1) {
                    digit *= 2
                }
                sum += if (digit > 9) digit - 9 else digit
            }
            return sum % 10 == 0
        }

        fun stringify(id: IntArray): String {
            return id.toList().chunked(4).joinToString(
                separator = " ", transform = {
                    it.joinToString("")
                }
            )
        }
    }

}