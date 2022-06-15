package de.jonasborn.zeroeight.data.token

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table

object InactiveTokens : Table() {
    val id = long("id")
    override val primaryKey = PrimaryKey(id)
}