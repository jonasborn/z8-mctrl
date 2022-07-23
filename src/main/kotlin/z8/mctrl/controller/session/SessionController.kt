package z8.mctrl.controller.session

import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.daos.UserDao
import z8.mctrl.jooq.tables.pojos.UserObject
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

    @Autowired
    var rds: RDS? = null;

    private var sessionId: String? = null;

    fun getSessionId(): String {
        val content = FacesContext.getCurrentInstance().externalContext
        val request = content.request as HttpServletRequest //Get request from external context
        val response = content.response as HttpServletResponse

        var cookie:Cookie? = null;
        if (request.cookies != null) cookie = request.cookies.find { it.name == COOKIE_NAME }
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

    fun getUser(): UserObject? {
        return rds!!.user().fetchOneById("7139be96d7684a4cba85dd9cc1400289")
    }



}