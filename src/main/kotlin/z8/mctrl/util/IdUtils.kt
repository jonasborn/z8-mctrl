package z8.mctrl.util

import org.edumdum.iso.Iso7064
import java.security.SecureRandom
import java.util.*

class IdUtils {

    companion object {

        private val sr = SecureRandom()

        fun generateLuhn(length: Int): IntArray {
            val create = fun(): IntArray {
                val array = IntArray(length)
                for (i in 0 until length) {
                    array[i] = sr.nextInt(9 - 0 + 1) + 0
                }
                return array
            }

            while (true) {
                val generated = create()
                if (checkLuhn(generated)) {
                    return generated
                }
            }
        }

        fun generateLuhnString(length: Int): String {
            return generateLuhn(length).joinToString("")
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

        fun generateReadable(s: String, b: Int = 4): String{
            return generateReadable(s.toCharArray().map { it.toString() }, b)
        }

        fun generateReadable(id: List<Any>, b: Int = 4): String {
            return id.chunked(b).joinToString(
                separator = " ", transform = {
                    it.joinToString("")
                }
            )
        }

        fun generateISO7064Check(input: String): String {
            var check = Iso7064.compute(input.uppercase(Locale.getDefault())).toString()
            if (check.length < 2) check = "0$check";
            return check;
        }

        fun generateDefaultId(): String {
            return UUID.randomUUID().toString().replace("-", "")
        }
    }

}