package z8.mctrl.companion.token

import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.Token.Companion.TOKEN
import z8.mctrl.jooq.tables.daos.UserDao
import z8.mctrl.util.IdUtils


class Tokens {

    companion object {
        private const val idLength = 16
        fun generate(device: String): String {
            val id = IdUtils.generateLuhn(idLength)
                .joinToString("")

            RDS.dsl().insertInto(
                TOKEN, TOKEN.ID, TOKEN.DEVICE, TOKEN.TIME
            ).values(
                id, device, System.currentTimeMillis()
            )
            return id
        }

        fun getUser(tokenId: String): UserDao? {
            return null //TODO
        }


    }

}