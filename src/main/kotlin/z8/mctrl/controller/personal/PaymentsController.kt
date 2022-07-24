package z8.mctrl.controller.personal

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.controller.nav.DefaultNavController
import z8.mctrl.controller.session.SessionController
import z8.mctrl.db.RDS

import z8.mctrl.jooq.tables.Externaldevice.EXTERNALDEVICE
import z8.mctrl.jooq.tables.Payment.PAYMENT
import z8.mctrl.jooq.tables.Token
import z8.mctrl.util.table.AbstractRDSTable
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("PaymentController")
@ViewScoped
class PaymentsController : AbstractRDSTable<PaymentsController.BetterPaymentsControllerData>(
    BetterPaymentsControllerData::class.java
) {

    class BetterPaymentsControllerData(
        val id: String,
        val time: Long?,
        val token: String?,
        var amount: String?,
        var details: String?,
        val title: String?


    ) {
        override fun toString(): String {
            return "BetterPaymentsControllerData(id='$id', time=$time, token=$token, amount=$amount, details=$details, title=$title)"
        }
    }

    @Autowired
    val rds: RDS? = null

    @Autowired
    val nv: DefaultNavController? = null

    @Autowired
    val sc: SessionController? = null


    @PostConstruct
    fun init() {
        nv!!.available = DefaultNavController.PUBLIC_PAGES
        nv.active = DefaultNavController.Page.PUBLIC_PAYMENTS

        onSelect {
            FacesContext.getCurrentInstance().partialViewContext.evalScripts.add("butter.modal.open('selectionDetails');")
        }

        registerColumn("column1", PAYMENT.TIME)
        registerColumn("column2", PAYMENT.TOKEN)
        registerColumn("column3", EXTERNALDEVICE.TITLE)
        registerColumn("column4", PAYMENT.DETAILS)
        registerColumn("column5", PAYMENT.AMOUNT)
        rds?.let {
            super.initialize(
                {
                    rds.dsl().fetchCount(
                        DSL.selectFrom(PAYMENT).where(
                            PAYMENT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                    )
                },
                {
                    rds.dsl()
                        .select(
                            PAYMENT.ID,
                            PAYMENT.TIME,
                            PAYMENT.TOKEN,
                            PAYMENT.AMOUNT,
                            PAYMENT.DETAILS,
                            EXTERNALDEVICE.TITLE,
                        ).from(PAYMENT)
                        .innerJoin(EXTERNALDEVICE)
                        .on(PAYMENT.EXTERNAL.eq(EXTERNALDEVICE.ID))
                        .where(
                            PAYMENT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                }

            )
        }
    }

}