package z8.gctrl.data.money

import z8.gctrl.data.device.Device
import z8.gctrl.data.device.DeviceTable
import z8.gctrl.data.token.Token
import z8.gctrl.data.token.TokenTable
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