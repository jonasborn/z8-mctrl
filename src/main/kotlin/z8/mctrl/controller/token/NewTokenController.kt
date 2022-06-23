package z8.mctrl.controller.token

import z8.mctrl.function.SecurityNumbers
import z8.mctrl.util.IdUtils
import javax.faces.view.ViewScoped
import javax.inject.Named


@Named("NewTokenController")
@ViewScoped
class NewTokenController() {

    public var baseId: String? = null
    public var checkId: String? = null
    public var readableId: String? = null
    public var totalId: String? = null

    public var snBase: String? = null
    public var snCheck: String? = null
    public var snTotal: String? = null
    public var snReadable: String? = null

    init {
        regenerate()
    }

    public fun regenerate() {
        baseId = IdUtils.generateLuhnString(14)
        checkId = IdUtils.generateISO7064Check(baseId!!)
        totalId = baseId + checkId
        readableId = IdUtils.generateReadable(totalId!!)

        snBase = SecurityNumbers.generateSecurityNumberBase(totalId!!)
        for (i in 0 until 10) {
            println(SecurityNumbers.generateSecurityNumberBase(totalId!!))
        }
        snCheck = IdUtils.generateISO7064Check(snBase!!)

        snTotal = snBase + snCheck

        snReadable = IdUtils.generateReadable(snTotal!!, 3)
    }
}