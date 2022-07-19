package z8.mctrl.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.config.Config
import z8.mctrl.db.DB
import z8.mctrl.exception.*
import z8.mctrl.jooq.tables.daos.DeviceDao
import z8.mctrl.server.WSServer

@RestController //357
class RequestPayment {

    companion object;

    @GetMapping("/payment/request")

    fun request(
        @RequestParam(name = "device", required = true) deviceId: String,
        @RequestParam(name = "amount", required = true) amount: Double,
        @RequestParam(name = "secret", required = true) secret: String
    ) {
        val devices = DB.device().fetchById(deviceId)
        if (devices.isEmpty()) throw NotFoundException("Device not found", NotFoundType.DEVICE_NOT_FOUND)
        val device = devices.first()
        if (device.secret != secret) throw InvalidCredentialsException("Access denied", InvalidCredentialsType.INITIATOR_DEVICE)
    }

}