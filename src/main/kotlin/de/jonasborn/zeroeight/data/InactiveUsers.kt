package de.jonasborn.zeroeight.data

import de.jonasborn.zeroeight.data.token.InactiveTokens
import org.jetbrains.exposed.sql.Table

object InactiveUsers : Table() {
    val id = InactiveTokens.long("id")
    override val primaryKey = PrimaryKey(id)
}