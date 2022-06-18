package z8.mctrl.data.token

import z8.mctrl.data.User
import z8.mctrl.data.UserTable
import z8.mctrl.data.device.Device
import z8.mctrl.data.device.DeviceTable
import org.jetbrains.exposed.dao.*
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

object TokenActionTable : IntIdTable() {
    val token = reference("token", TokenTable)
    val device = reference("device", DeviceTable);
    val time = long("time")
    val issuer = reference("user", UserTable)
    val action = enumeration<TokenActionType>("action", TokenActionType::class)
}

class InteractiveToken(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<InteractiveToken>(TokenActionTable)
    var device by Device referencedOn TokenActionTable.device
    var token by Token referencedOn TokenActionTable.token
    var time by TokenActionTable.time
    var issuer by User referencedOn TokenActionTable.issuer
    var action by TokenActionTable.action
}