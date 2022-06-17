package de.jonasborn.zeroeight.data.money

import de.jonasborn.zeroeight.data.device.Device
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.token.Token
import de.jonasborn.zeroeight.data.token.TokenTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object PayoutTable : LongIdTable(){
    val device = reference("device", DeviceTable)
    val token = reference("token", TokenTable)
    val amount = float("amount")
    val time = long("time")
    val target = enumeration<Operator>("target", Operator::class)
    val details = varchar("details", 255)
}

class Payout(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<Payout>(PayoutTable)
    var device by Device referencedOn PayoutTable.device
    var token by Token referencedOn PayoutTable.token
    var amount by PayoutTable.amount
    var time by PayoutTable.time
    var target by PayoutTable.target
    var details by PayoutTable.details
}