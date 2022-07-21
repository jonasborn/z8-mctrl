package z8.mctrl.companion.token

import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.Token.Companion.TOKEN
import z8.mctrl.jooq.tables.daos.UserDao
import z8.mctrl.util.IdUtils


class Tokens(val rds: RDS) {


    private val idLength = 16
    fun generate(device: String): String {
        val id = IdUtils.generateLuhn(idLength)
            .joinToString("")

        rds.dsl().insertInto(
            TOKEN, TOKEN.ID, TOKEN.TERMINAL, TOKEN.TIME
        ).values(
            id, device, System.currentTimeMillis()
        )
        return id
    }

    fun getUser(tokenId: String): UserDao? {
        return null //TODO
    }


}