package z8.mctrl.controller.nav

import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named
import javax.servlet.http.HttpServletRequest


@Named("DefaultNavController")
@ViewScoped
class DefaultNavController {

    companion object {
        val TERMINAL_PAGES = listOf(
            Page.TERMINAL_HOME,
            Page.TERMINAL_PAYMENTS,
            Page.TERMINAL_DEPOSITS,
            Page.TERMINAL_PAYOUTS,
            Page.TERMINAL_CHARGE,
            Page.TERMINAL_SETTINGS
        )

        val PUBLIC_PAGES = listOf(
            Page.PUBLIC_HOME,
            Page.PUBLIC_PAYMENTS,
            Page.PUBLIC_DEPOSITS,
            Page.PUBLIC_PAYOUTS,
            Page.PUBLIC_CHARGE,
            Page.PUBLIC_SETTINGS
        )

    }

    enum class Page(val title: String, val url: String) {
        NONE("", ""),
        TERMINAL_HOME("Home", "/terminal/index.xhtml"),
        TERMINAL_PAYMENTS("Payments", "/terminal/payments.xhtml"),
        TERMINAL_DEPOSITS("Deposits", "/terminal/deposits.xhtml"),
        TERMINAL_PAYOUTS("Payouts", "/terminal/payouts.xhtml"),
        TERMINAL_CHARGE("Charge", "/terminal/charges.xhtml"),
        TERMINAL_SETTINGS("Setting", "/terminal/settings.xhtml"),

        PUBLIC_HOME("Home", "/public/index.xhtml"),
        PUBLIC_PAYMENTS("Payments", "/public/payments.xhtml"),
        PUBLIC_DEPOSITS("Deposits", "/public/deposits.xhtml"),
        PUBLIC_PAYOUTS("Payouts", "/public/payouts.xhtml"),
        PUBLIC_CHARGE("Charge", "/public/charges.xhtml"),
        PUBLIC_SETTINGS("Setting", "/public/settings.xhtml")

    }

    var available: List<Page> =  DefaultNavController.PUBLIC_PAGES
    var active: Page = Page.NONE

    var admin = true

    @PostConstruct
    fun post() {
        val request = FacesContext.getCurrentInstance().externalContext.request as HttpServletRequest
        val uri = request.requestURI
        Page.values().find { it.url == uri }.let {
            if (it != null) active = it
        }
    }


}