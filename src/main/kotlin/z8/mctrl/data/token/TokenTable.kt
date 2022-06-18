package z8.mctrl.data.token

import z8.mctrl.data.device.Device
import z8.mctrl.data.device.DeviceTable
import z8.mctrl.data.User
import z8.mctrl.data.UserTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

//https://stackoverflow.com/questions/57885360/how-to-do-a-onetoone-relation-using-exposed

object TokenTable : UUIDTable() {
    val device = reference("device", DeviceTable)
    val user = reference("user", UserTable)
    val time = long("time")
}

class Token(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<Token>(TokenTable)
    var device by Device referencedOn TokenTable.device
    var user by User referencedOn TokenTable.user
    var time by TokenTable.time
}
