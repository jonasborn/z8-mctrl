package z8.mctrl.controller.twofa

import java.security.SecureRandom
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

    var result: Consumer<Boolean>? = null

    fun send(string: String) {
        println(">>>>>>>>>>>>>>>>>>>> $string")
    }

    var renderInput = false

    var renderEverything = true

    var failed = false

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

    fun request() {
        failed = false
        renderEverything = false
        renderInput = true
        generate()
        code?.let { send(it) }
    }

    fun check() {
        println("CODE: $code, INPUT: $input")
        if (code == input && result != null) {
            failed = false
            result!!.accept(true)
        } else {
            failed = true
            result!!.accept(false)
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