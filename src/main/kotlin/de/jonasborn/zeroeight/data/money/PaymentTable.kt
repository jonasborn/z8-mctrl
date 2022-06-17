package de.jonasborn.zeroeight.data.money

import de.jonasborn.zeroeight.data.device.Device
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.token.Token
import de.jonasborn.zeroeight.data.token.TokenTable
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable

object PaymentTable: LongIdTable() {
    val device = reference("device", DeviceTable)
    val token = reference("token", TokenTable)
    val amount = float("amount")
    val time = long("time")
}

class Payment(id: EntityID<Long>): LongEntity(id) {
    companion object : LongEntityClass<Payment>(PaymentTable)
    var device by Device referencedOn PaymentTable.device
    var token by Token referencedOn PaymentTable.token
    var amount by PaymentTable.amount
    var time by PaymentTable.time
}