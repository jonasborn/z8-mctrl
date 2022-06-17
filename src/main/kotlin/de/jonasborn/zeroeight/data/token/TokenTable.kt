package de.jonasborn.zeroeight.data.token

import de.jonasborn.zeroeight.data.device.Device
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.User
import de.jonasborn.zeroeight.data.UserTable
import de.jonasborn.zeroeight.data.money.Deposit
import de.jonasborn.zeroeight.data.money.DepositTable
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
