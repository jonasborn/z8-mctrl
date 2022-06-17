package de.jonasborn.zeroeight.data

import de.jonasborn.zeroeight.data.device.Device
import de.jonasborn.zeroeight.data.device.DeviceTable
import de.jonasborn.zeroeight.data.money.Operator
import de.jonasborn.zeroeight.data.money.PayoutTable
import de.jonasborn.zeroeight.data.token.TokenActionTable
import de.jonasborn.zeroeight.data.token.Token
import de.jonasborn.zeroeight.data.user.UserActionType
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