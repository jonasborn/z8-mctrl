package z8.mctrl

import org.springframework.web.context.annotation.RequestScope
import javax.inject.Named

@Named
@RequestScope
open class LoginBean() {
    var userName = "admin"
    var password = "admin"

    fun login(): String {
        userName = "Fux"
        return "Hallo"
    }
}