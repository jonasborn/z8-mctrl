package z8.mctrl.controller.payments

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.db.RDS
import z8.mctrl.jooq.Tables.EXTERNALDEVICE
import z8.mctrl.jooq.Tables.PAYMENT
import z8.mctrl.jooq.tables.Externaldevice
import z8.mctrl.jooq.tables.Payment
import z8.mctrl.jooq.tables.Token
import z8.mctrl.util.table.AbstractRDSTable
import javax.annotation.PostConstruct
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("PaymentController")
@ViewScoped
class PaymentController : AbstractRDSTable<PaymentController.BetterPaymentsControllerData>(
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

    @PostConstruct
    fun init() {
        registerColumn("column1", PAYMENT.TIME)
        registerColumn("column2", PAYMENT.TOKEN)
        registerColumn("column3", EXTERNALDEVICE.TITLE)
        registerColumn("column4", PAYMENT.DETAILS)
        registerColumn("column5", PAYMENT.AMOUNT)
        rds?.let {
            super.initialize(
                {
                    rds.dsl().fetchCount(
                        DSL.selectFrom(Payment.PAYMENT).where(
                            Payment.PAYMENT.TOKEN.`in`(
                                DSL.select(Token.TOKEN.ID).from(Token.TOKEN)
                                    .where(Token.TOKEN.USER.eq("7139be96d7684a4cba85dd9cc1400289"))
                            )
                        )
                    )
                },
                {
                    rds.dsl()
                        .select(
                            Payment.PAYMENT.ID,
                            Payment.PAYMENT.TIME,
                            Payment.PAYMENT.TOKEN,
                            Payment.PAYMENT.AMOUNT,
                            Payment.PAYMENT.DETAILS,
                            Externaldevice.EXTERNALDEVICE.TITLE,
                        ).from(Payment.PAYMENT)
                        .innerJoin(Externaldevice.EXTERNALDEVICE)
                        .on(Payment.PAYMENT.EXTERNAL.eq(Externaldevice.EXTERNALDEVICE.ID))
                        .where(Payment.PAYMENT.EXTERNAL.eq(Externaldevice.EXTERNALDEVICE.ID))
                }

            )
        }
    }

}