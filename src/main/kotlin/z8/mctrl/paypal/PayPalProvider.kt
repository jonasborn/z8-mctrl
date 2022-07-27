package z8.mctrl.paypal

import com.paypal.core.PayPalEnvironment
import com.paypal.core.PayPalHttpClient
import com.paypal.http.HttpResponse
import com.paypal.orders.*
import com.paypal.payments.AuthorizationsCaptureRequest
import com.paypal.payments.Capture
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import z8.mctrl.config.Config
import z8.mctrl.controller.session.SessionController
import z8.mctrl.flow.Flow
import java.math.BigDecimal
import java.math.RoundingMode
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext


@Component
class PayPalProvider @Autowired constructor(val config: Config, val flow: Flow, val sessionController: SessionController) {


    final var ppe: PayPalEnvironment? = null
    var client: PayPalHttpClient? = null
    val logger: Logger = LogManager.getLogger()

    var moneyWanted: Double = 0.00
    var fee: Double = 0.00
    var moneyNeeded: Double = 0.00


    init {
        ppe = PayPalEnvironment.Sandbox(
            config.string("provider.paypal.id", ""),
            config.string("provider.paypal.secret", "")
        )
        client = PayPalHttpClient(ppe)
    }

    fun calculateFee(d: Double): Double {
        return BigDecimal((((d / 100) * 2.49) + 0.35)).setScale(2, RoundingMode.HALF_EVEN).toDouble()
    }

    fun update() {
        fee = calculateFee(moneyWanted)
        moneyNeeded = moneyWanted + fee
    }

    fun createReturnUrl() {

    }

    class PayPalFlow(val user: String, val success: Boolean)

    fun create() {

        val orderRequest = OrderRequest()
        orderRequest.checkoutPaymentIntent("AUTHORIZE")

        val successFlow = flow.pack(PayPalFlow(sessionController.getUser()!!.id, true))
        val failureFlow = flow.pack(PayPalFlow(sessionController.getUser()!!.id, false))

        println(successFlow)

        val context = ApplicationContext().brandName("z8")
            .landingPage("BILLING")
            .returnUrl("http://localhost:8080/personal/charge.xhtml?flow=$successFlow")
            .cancelUrl("http://localhost:8080/personal/charge.xhtml?flow=$failureFlow")
            .shippingPreference("NO_SHIPPING")
        orderRequest.applicationContext(context)

        val purchaseUnitRequests: ArrayList<PurchaseUnitRequest> = ArrayList()
        val purchaseUnitRequest = PurchaseUnitRequest().referenceId("PUHF")
            .description("Sporting Goods").customId("CUST-HighFashions").softDescriptor("HighFashions")
            .amountWithBreakdown(
                AmountWithBreakdown().currencyCode("EUR").value(moneyNeeded.toString())
                    .amountBreakdown(
                        AmountBreakdown().itemTotal(Money().currencyCode("EUR").value(moneyNeeded.toString()))
                    )
            )

        purchaseUnitRequests.add(purchaseUnitRequest)
        orderRequest.purchaseUnits(purchaseUnitRequests)

        val request = OrdersCreateRequest()
        request.header("prefer", "return=representation")
        request.requestBody(
            orderRequest
        )

        val response: HttpResponse<Order> = client!!.execute(request)

        if (response.statusCode() === 201) {
            println("Order with Complete Payload: ")
            println("Status Code: " + response.statusCode())
            println("Status: " + response.result().status())
            println("Order ID: " + response.result().id())
            println("Intent: " + response.result().checkoutPaymentIntent())
            println("Links: ")
            for (link in response.result().links()) {
                System.out.println(
                    "\t" + link.rel().toString() + ": " + link.href().toString() + "\tCall Type: " + link.method()
                )
            }
            val first = response.result().links().find { it.rel() == "approve" }
            if (first == null) return
            FacesContext.getCurrentInstance().externalContext.redirect(first.href().toString())
        }

        return
    }

    @PostConstruct
    fun post() {
        println("PC")
        FacesContext.getCurrentInstance()?.externalContext?.requestParameterMap.let { map ->
            map?.get("token")?.let {
                approve(it)
            }
        }
    }

    fun approve(id: String) {
        val request = OrdersAuthorizeRequest(id)
        request.requestBody(OrderRequest())
        val captureOrderResponse: HttpResponse<Order> = client!!.execute(request)
        if (true) {
            captureOrderResponse.result().purchaseUnits().forEach { purchaseUnit ->
                purchaseUnit.payments().authorizations().forEach {
                    auth(it.id())
                }
            }
            println("Link Descriptions: ")
            for (link in captureOrderResponse.result().links()) {
                System.out.println("\t" + link.rel().toString() + ": " + link.href())
            }
            println("Full response body:")
            System.out.println(captureOrderResponse.result())
        }


    }

    fun auth(id: String) {
        val request2 = AuthorizationsCaptureRequest(id)
        request2.requestBody(OrderRequest())
        val response: HttpResponse<Capture> = client!!.execute(request2)

        println("Status Code: " + response.statusCode())
        System.out.println("Status: " + response.result().status())
        System.out.println("Capture ID: " + response.result().id())
        println("Links: ")
        for (link in response.result().links()) {
            System.out.println(
                "\t" + link.rel().toString() + ": " + link.href().toString() + "\tCall Type: " + link.method()
            )
        }
        println("Full response body:")
        System.out.println(response.result())
    }

}