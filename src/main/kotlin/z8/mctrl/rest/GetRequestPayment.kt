package z8.mctrl.rest

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.companion.device.InternalDevices
import z8.mctrl.db.RDS
import z8.mctrl.companion.payment.PaymentRequests
import z8.mctrl.handler.IncomingPaymentRequestHandler
import z8.mctrl.voyager.Voyager

@RestController //357

class GetRequestPayment @Autowired constructor(
    val rds: RDS,
    val paymentRequests: PaymentRequests,
    val internalDevices: InternalDevices,
    val preIncomingPaymentRequestHandler: IncomingPaymentRequestHandler,
    val voyager: Voyager
) {

    @GetMapping("/payment/request")
    fun request(
        @RequestParam(name = "source", required = true) externalDevice: String,
        @RequestParam(name = "amount", required = true) amount: Double,
        @RequestParam(name = "secret", required = true) secret: String
    ) {

        val id = preIncomingPaymentRequestHandler.registerPaymentRequest(
            externalDevice, amount, secret
        )
        ResponseEntity.accepted().body(id)



    }

    @GetMapping("/payment/status")
    fun status(
        @RequestParam(name = "id", required = true) id: String
    ) {
        val entry = paymentRequests.get(id)
        if (entry?.status == null) ResponseEntity.notFound()
        ResponseEntity.accepted().body(entry!!.status)
    }

}