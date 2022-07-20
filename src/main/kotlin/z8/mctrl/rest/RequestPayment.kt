package z8.mctrl.rest

import org.jooq.meta.derby.sys.Sys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.db.RDS
import z8.mctrl.exception.*
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.db.forced.PaymentRequestStatus
import z8.mctrl.jooq.tables.pojos.Paymentrequest
import z8.mctrl.voyager.Voyager
import java.util.*

@RestController //357

class RequestPayment @Autowired constructor(
    val rds: RDS,
    val paymentRequests: PaymentRequests,
    val internalDevices: InternalDevices,
    val voyager: Voyager
    ) {

    @GetMapping("/payment/request")
    fun request(
        @RequestParam(name = "source", required = true) externalDevice: String,
        @RequestParam(name = "amount", required = true) amount: Double,
        @RequestParam(name = "secret", required = true) secret: String
    ) {
        val sourceDevices = rds!!.externalDevice().fetchById(externalDevice)
        if (sourceDevices.isEmpty()) throw NotFoundException("Device not found", NotFoundType.ID_UNKNOWN)
        val sourceDevice = sourceDevices.first()

        if (sourceDevice.secret != secret) throw InvalidCredentialsException(
            "Access denied",
            InvalidCredentialsType.INITIATOR_DEVICE
        )

        val internalDevice = sourceDevice.target
        val status = voyager.getStatus(internalDevice!!)
        if (!status.active) throw NotFoundException("Device not connected", NotFoundType.DEVICE_UNKNOWN)

        val id = UUID.randomUUID().toString()

        rds!!.paymentRequest().insert(
            Paymentrequest(
                id, System.currentTimeMillis(), externalDevice, internalDevice, amount, PaymentRequestStatus.STARTED
            )
        )

        paymentRequests!!.addToQueue(internalDevice, id)

        ResponseEntity.accepted().body(id)

    }

    @GetMapping("/payment/status")
    fun status(
        @RequestParam(name = "id", required = true) id: String
    ) {
        val entry = paymentRequests!!.get(id)
        if (entry?.status == null) ResponseEntity.notFound()
        ResponseEntity.accepted().body(entry!!.status)

    }

}