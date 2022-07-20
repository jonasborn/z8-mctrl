package z8.mctrl.companion.twofa

import z8.mctrl.config.Config
import z8.mctrl.db.KVS

class TwoFA(val config: Config, val kvs: KVS) {



        fun isRequired(userId: String, amount: Double): Boolean {
            if (amount > getThreshold()) return true
            val l = getUserPayments(userId)
            if (l > getThreshold()) return true
            return false
        }

        fun getThreshold(): Double {
            return config.double("payment.twofa.threshold", 5.0)
        }

        fun getTimeout() {
            config.int("payment.twofa.timeout", 3)
        }

        fun getUserPayments(userId: String): Long {
            val a = kvs.getALong("twofa", "payments", userId) ?: return 0L
            return a.get()
        }

        fun resetUserPayments(userId: String) {
            kvs.getALong("twofa", "payments", userId)?.set(0)
        }

        fun addUserPayments(userId: String) {
            kvs.getALong("twofa", "payments", userId)?.incrementAndGet()
        }


}
