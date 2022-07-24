package z8.mctrl.controller.personal

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.controller.nav.DefaultNavController
import z8.mctrl.controller.session.SessionController
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentProvider
import z8.mctrl.jooq.Tables.DEPOSIT
import z8.mctrl.jooq.tables.*
import z8.mctrl.jooq.tables.Payout.PAYOUT
import z8.mctrl.jooq.tables.Terminaldevice.TERMINALDEVICE
import z8.mctrl.util.table.AbstractRDSTable
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("DepositsController")
@ViewScoped
class DepositsController : AbstractRDSTable<DepositsController.PayoutsControllerRow>(
    PayoutsControllerRow::class.java
) {

    class PayoutsControllerRow (
        val id: String,
        val time: Long?,
        val title: String?,
        val token: String?,
        val origin: PaymentProvider?,
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
        nv.active = DefaultNavController.Page.PUBLIC_DEPOSITS

        onSelect {
            FacesContext.getCurrentInstance().partialViewContext.evalScripts.add("butter.modal.open('selectionDetails');")
        }

        registerColumn("column1", DEPOSIT.TIME)
        registerColumn("column2", DEPOSIT.TOKEN)
        registerColumn("column3", TERMINALDEVICE.TITLE)
        registerColumn("column4", DEPOSIT.ORIGIN)
        registerColumn("column5", DEPOSIT.DETAILS)
        registerColumn("column6", DEPOSIT.AMOUNT)
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
                            DEPOSIT.ID,
                            DEPOSIT.TIME,
                            DEPOSIT.TOKEN,
                            TERMINALDEVICE.TITLE,
                            DEPOSIT.ORIGIN,
                            DEPOSIT.DETAILS,
                            DEPOSIT.AMOUNT,
                        ).from(DEPOSIT)
                        .innerJoin(TERMINALDEVICE)
                        .on(DEPOSIT.TERMINAL.eq(TERMINALDEVICE.ID))
                        .where(
                            DEPOSIT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                }

            )
        }
    }

}