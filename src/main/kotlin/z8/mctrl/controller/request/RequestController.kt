package z8.mctrl.controller.request

import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Named("RequestController")
@ViewScoped
class RequestController {

    fun getRequest(): HttpServletRequest {
        val content = FacesContext.getCurrentInstance().externalContext
        return content.request as HttpServletRequest
    }

    fun getResponse(): HttpServletResponse {
        val content = FacesContext.getCurrentInstance().externalContext
        return content.response as HttpServletResponse
    }

    fun isHighContrast(): Boolean {
        getRequest().getHeader("enable-high-contrast") ?: return false
        return true
    }

}