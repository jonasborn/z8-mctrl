package z8.mctrl.companion.twofa

import z8.mctrl.config.Config
import z8.mctrl.db.KVS

class TwoFA {

    companion object {

        fun isRequired(userId: String, amount: Double): Boolean {
            if (amount > getThreshold()) return true
            val l = getUserPayments(userId)
            if (l > getThreshold()) return true
            return false
        }

        fun getThreshold(): Double {
            return Config.double("payment.2fa.threshold", 5.0)
        }

        fun getTimeout() {
            Config.int("payment.2fa.timeout", 3)
        }

        fun getUserPayments(userId: String): Long {
            val a = KVS.getALong("2fa", "payments", userId) ?: return 0L
            return a.get()
        }

        fun resetUserPayments(userId: String) {
            KVS.getALong("2fa", "payments", userId)?.set(0)
        }

        fun addUserPayments(userId: String) {
            KVS.getALong("2fa", "payments", userId)?.incrementAndGet()
        }
    }

}
