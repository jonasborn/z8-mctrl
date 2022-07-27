package z8.mctrl.controller.personal

import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.paypal.PayPalProvider
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("ChargeController")
@ViewScoped
class ChargeController {

    @Autowired
    val ppp: PayPalProvider? = null


}