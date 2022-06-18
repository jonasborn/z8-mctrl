package z8.gctrl.data.device

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*


object DeviceTable : UUIDTable() {
    val time = long("time")
    var secret = varchar("secret", 40)
}

class Device(id: EntityID<UUID>): UUIDEntity(id) {
    companion object : UUIDEntityClass<Device>(DeviceTable)
    var time by DeviceTable.time
    var secret by DeviceTable.secret
}
