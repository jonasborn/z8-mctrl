package z8.mctrl.data

import z8.mctrl.data.device.Device
import z8.mctrl.data.device.DeviceTable
import z8.mctrl.data.user.UserActionType
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable

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