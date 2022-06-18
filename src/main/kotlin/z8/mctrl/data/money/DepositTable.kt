package z8.mctrl.data.money

import z8.mctrl.data.device.Device
import z8.mctrl.data.device.DeviceTable
import z8.mctrl.data.token.Token
import z8.mctrl.data.token.TokenTable
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