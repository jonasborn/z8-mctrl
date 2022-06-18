package z8.gctrl.data

import z8.gctrl.data.device.Device
import z8.gctrl.data.device.DeviceTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*


object UserTable : UUIDTable() {
    val device = reference("device", DeviceTable)
    val time = long("time")
    val recovery = varchar("recovery", 40)
}

class User(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<User>(DeviceTable)
    var device by Device referencedOn UserTable.device
    var time by UserTable.time
    var recovery by UserTable.recovery

}
