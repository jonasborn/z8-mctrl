package z8.mctrl.db

import org.jooq.Configuration
import org.jooq.meta.derby.sys.Sys
import z8.mctrl.jooq.tables.daos.TokenDao
import z8.mctrl.jooq.tables.pojos.TokenObject
import z8.mctrl.util.IdUtils

class TokenDaoExtended(conf: Configuration) : TokenDao(conf) {

    fun generate(device: String, user: String): String {
        val id = IdUtils.generateLuhn(16)
            .joinToString("")
        this.insert(
            TokenObject(id, System.currentTimeMillis(), user, device)
        )
        return id
    }

}