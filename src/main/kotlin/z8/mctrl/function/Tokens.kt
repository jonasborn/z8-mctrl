package z8.mctrl.function

import z8.mctrl.db.DB
import z8.mctrl.jooq.tables.Token.Companion.TOKEN
import z8.mctrl.jooq.tables.daos.TokenDao
import z8.mctrl.util.LuhnUtils


class Tokens {

    companion object {
        private const val idLength = 16
        fun generate(device: String): String {
            val id = LuhnUtils.generateLuhn(idLength)
                .joinToString("")

            DB.dsl().insertInto(
                TOKEN, TOKEN.ID, TOKEN.DEVICE, TOKEN.TIME
            ).values(
                id, device, System.currentTimeMillis()
            )
            return id
        }


    }

}