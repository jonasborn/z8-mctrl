package z8.mctrl.controller.personal

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.controller.nav.DefaultNavController
import z8.mctrl.controller.session.SessionController
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentProvider
import z8.mctrl.jooq.tables.*
import z8.mctrl.jooq.tables.Payout.PAYOUT
import z8.mctrl.jooq.tables.Terminaldevice.TERMINALDEVICE
import z8.mctrl.util.table.AbstractRDSTable
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("PayoutsController")
@ViewScoped
class PayoutsController : AbstractRDSTable<PayoutsController.PayoutsControllerRow>(
    PayoutsControllerRow::class.java
) {

    class PayoutsControllerRow (
        val id: String,
        val time: Long?,
        val token: String?,
        val title: String?,
        val target: PaymentProvider?,
        var details: String?,
        var amount: String?,
        )

    @Autowired
    val rds: RDS? = null

    @Autowired
    val nv: DefaultNavController? = null

    @Autowired
    val sc: SessionController? = null


    @PostConstruct
    fun init() {
        nv!!.available = DefaultNavController.PUBLIC_PAGES
        nv.active = DefaultNavController.Page.PUBLIC_PAYOUTS

        onSelect {
            FacesContext.getCurrentInstance().partialViewContext.evalScripts.add("butter.modal.open('selectionDetails');")
        }

        registerColumn("column1", PAYOUT.TIME)
        registerColumn("column2", PAYOUT.TOKEN)
        registerColumn("column3", TERMINALDEVICE.TITLE)
        registerColumn("column4", PAYOUT.TARGET)
        registerColumn("column5", PAYOUT.DETAILS)
        registerColumn("column6", PAYOUT.AMOUNT)
        rds?.let {
            super.initialize(
                {
                    rds.dsl().fetchCount(
                        DSL.selectFrom(PAYOUT).where(
                            PAYOUT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                    )
                },
                {
                    rds.dsl()
                        .select(
                            PAYOUT.ID,
                            PAYOUT.TIME,
                            PAYOUT.TOKEN,
                            PAYOUT.AMOUNT,
                            TERMINALDEVICE.TITLE,
                            PAYOUT.TARGET,
                            PAYOUT.DETAILS,
                        ).from(PAYOUT)
                        .innerJoin(TERMINALDEVICE)
                        .on(PAYOUT.TERMINAL.eq(TERMINALDEVICE.ID))
                        .where(
                            PAYOUT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                }

            )
        }
    }

}