package z8.mctrl.db

import org.jooq.Configuration
import org.jooq.meta.derby.sys.Sys
import z8.mctrl.function.Tokens
import z8.mctrl.jooq.tables.daos.TokenDao
import z8.mctrl.jooq.tables.pojos.Token
import z8.mctrl.util.LuhnUtils

class TokenDaoExtended(conf: Configuration) : TokenDao(conf) {

    fun generate(device: String, user: String): String {
        val id = LuhnUtils.generateLuhn(16)
            .joinToString("")
        this.insert(
            Token(id, device, user, System.currentTimeMillis())
        )
        return id
    }

}