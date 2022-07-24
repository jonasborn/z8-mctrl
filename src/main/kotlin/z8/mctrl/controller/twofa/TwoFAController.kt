package z8.mctrl.controller.twofa

import java.security.SecureRandom
import java.util.function.BiConsumer
import java.util.function.Consumer
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("TwoFAController")
@ViewScoped
class TwoFAController {

    companion object {
        val sr: SecureRandom = SecureRandom()
    }

    var code: String? = null

    var input: String? = null

    var result: BiConsumer<String?, Boolean>? = null

    var message = "No reasin given"

    fun send(string: String) {
        println(">>>>>>>>>>>>>>>>>>>> $string")
    }

    var renderInput = false

    var renderEverything = true

    var failed = false

    var id: String? = null

    @PostConstruct
    fun generate() {
        val parts = listOf<Int>(
            sr.nextInt(9),
            sr.nextInt(9),
            sr.nextInt(9),
            sr.nextInt(9)
        )
        code = parts.joinToString("")
    }

    fun request(id: String, message: String) {
        this.id = id
        this.message = message
        failed = false
        renderEverything = false
        renderInput = true
        generate()
        code?.let { send(it) }
    }

    fun check() {
        Thread.sleep(2000)
        if (code == input) {
            failed = false
            result?.accept(id, true)
            renderEverything = true
            renderInput = false
        } else {
            failed = true
            result?.accept(id, false)
        }
    }

    fun abort() {
        failed = false
        renderEverything = true
        renderInput = false
    }

    fun retry() {

    }




}