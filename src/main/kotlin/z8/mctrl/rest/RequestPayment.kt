package z8.mctrl.rest

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import z8.mctrl.config.Config
import z8.mctrl.server.WSServer

@RestController //357
class RequestPayment {

    companion object {

    }

    @GetMapping("/payment/request")
    fun request(device: String, amount: Double) {

    }

}