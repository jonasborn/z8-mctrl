package de.jonasborn.zeroeight.data.token

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Table

object Tokens : LongIdTable() {
    val user = long("user")
}
