package z8.gctrl.data.money

import z8.gctrl.data.device.Device
import z8.gctrl.data.device.DeviceTable
import z8.gctrl.data.token.Token
import z8.gctrl.data.token.TokenTable
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