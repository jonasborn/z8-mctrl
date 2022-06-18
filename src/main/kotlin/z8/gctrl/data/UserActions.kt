package z8.gctrl.data

import z8.gctrl.data.device.Device
import z8.gctrl.data.device.DeviceTable
import z8.gctrl.data.money.Operator
import z8.gctrl.data.money.PayoutTable
import z8.gctrl.data.token.TokenActionTable
import z8.gctrl.data.token.Token
import z8.gctrl.data.user.UserActionType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Table
import java.util.*

object UserActions : IntIdTable() {
    val device = reference("device", DeviceTable);
    val user = reference("user", UserTable)
    val time = long("time")
    val issuer = reference("issuer", UserTable)
    val action = enumeration("target", UserActionType::class)
}

class InactiveUser(id: EntityID<Int>): IntEntity(id) {
    companion object : IntEntityClass<InactiveUser>(UserActions)
    var device by Device referencedOn UserActions.device
    var user by User referencedOn UserActions.user
    var time by UserActions.time
    var issuer by User referencedOn UserActions.issuer
    var action by UserActions.action


}