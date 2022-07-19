package z8.mctrl.rest

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.db.RDS
import z8.mctrl.exception.*
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.voyager.Voyager
import java.util.*

@RestController //357
class RequestPayment {


    @GetMapping("/payment/request")
    fun request(
        @RequestParam(name = "source", required = true) externalDevice: String,
        @RequestParam(name = "amount", required = true) amount: Double,
        @RequestParam(name = "secret", required = true) secret: String
    ) {
        val sourceDevices = RDS.externalDevice().fetchById(externalDevice)
        if (sourceDevices.isEmpty()) throw NotFoundException("Device not found", NotFoundType.ID_UNKNOWN)
        val sourceDevice = sourceDevices.first()

        if (sourceDevice.secret != secret) throw InvalidCredentialsException(
            "Access denied",
            InvalidCredentialsType.INITIATOR_DEVICE
        )

        val internalDevice = sourceDevice.target
        val status = Voyager.getStatus(internalDevice!!)
        if (!status.active) throw NotFoundException("Device not connected", NotFoundType.DEVICE_UNKNOWN)

        val id = UUID.randomUUID().toString()

        PaymentRequests.setStatus(id, PaymentRequests.PaymentRequestStatus.PENDING)

        PaymentRequests.setDetails(
            id, PaymentRequests.PaymentRequestDetails(
                id,
                externalDevice,
                internalDevice,
                amount
            )
        )

        PaymentRequests.addToQueue(internalDevice, id)

        ResponseEntity.accepted().body(id)

    }

    @GetMapping("/payment/status")
    fun status(
        @RequestParam(name = "id", required = true) id: String
    ) {
        val status = PaymentRequests.getStatus(id) ?: ResponseEntity.noContent()
        ResponseEntity.accepted().body(status)

    }

}