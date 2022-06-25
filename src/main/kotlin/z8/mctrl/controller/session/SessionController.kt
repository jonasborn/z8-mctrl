package z8.mctrl.controller.session

import java.util.*
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named
import javax.servlet.http.Cookie

import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Named("SessionController")
@ViewScoped
class SessionController {

    companion object {
        const val COOKIE_NAME = "z8session"
    }

    private var sessionId: String? = null;

    fun getSession(): String {
        val content = FacesContext.getCurrentInstance().externalContext
        val request = content.request as HttpServletRequest //Get request from external context
        val response = content.response as HttpServletResponse

        val cookie = request.cookies.find { it.name == COOKIE_NAME }
        if (cookie == null) {
            sessionId = UUID.randomUUID().toString()
            response.addCookie(
                Cookie(COOKIE_NAME, sessionId)
            )
        } else {
            sessionId = cookie.value
        }

        return sessionId!!
    }

}