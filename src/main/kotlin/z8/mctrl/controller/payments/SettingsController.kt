package z8.mctrl.controller.payments

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.controller.nav.DefaultNavController
import z8.mctrl.controller.session.SessionController
import z8.mctrl.controller.twofa.TwoFAController
import z8.mctrl.db.RDS
import z8.mctrl.jooq.Tables.EXTERNALDEVICE
import z8.mctrl.jooq.Tables.PAYMENT
import z8.mctrl.jooq.tables.Externaldevice
import z8.mctrl.jooq.tables.Payment
import z8.mctrl.jooq.tables.Token
import z8.mctrl.jooq.tables.pojos.UserObject
import z8.mctrl.util.table.AbstractRDSTable
import java.util.function.Consumer
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("SettingsController")
@ViewScoped
class SettingsController {

    @Autowired
    val rds: RDS? = null

    @Autowired
    val sessionController: SessionController? = null

    @Autowired
    val twoFAController: TwoFAController? = null

    var user: UserObject? = null
    var mail: String? = null


    @PostConstruct
    fun post() {
        val sessionUser = sessionController!!.getUser()
        user = rds!!.user().fetchOneById(sessionUser!!.id)
        this.mail = user!!.mail
        twoFAController!!.result = Consumer {
            if (it) println("SAVED!!!")
            else println("NOT SAVED")
        }
    }

    fun save() {
        user!!.mail = mail
        rds!!.user().update(user)
        println("Finished")
    }

}