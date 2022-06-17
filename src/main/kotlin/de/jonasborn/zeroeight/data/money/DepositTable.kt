package de.jonasborn.zeroeight.data.money

import de.jonasborn.zeroeight.data.device.Device
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.token.Token
import de.jonasborn.zeroeight.data.token.TokenTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object DepositTable : LongIdTable() {
    val device = reference("device", DeviceTable)
    val token = reference("token", TokenTable)
    val amount = float("amount")
    val time = long("time")
    val origin = enumeration<Operator>("origin", Operator::class)
    val details = varchar("details", 255)
}

class Deposit(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<Deposit>(DepositTable)
    var device by Device referencedOn DepositTable.device
    var token by Token referencedOn DepositTable.token
    var amount by DepositTable.amount
    var time by DepositTable.time
    var origin by DepositTable.origin
    var details by DepositTable.details
}