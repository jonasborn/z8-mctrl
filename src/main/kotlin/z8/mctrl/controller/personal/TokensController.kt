package z8.mctrl.controller.personal

import org.jooq.impl.DSL
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.controller.nav.DefaultNavController
import z8.mctrl.controller.session.SessionController
import z8.mctrl.db.RDS
import z8.mctrl.db.forced.PaymentProvider
import z8.mctrl.db.forced.TokenAction
import z8.mctrl.jooq.tables.*
import z8.mctrl.jooq.tables.Payout.PAYOUT
import z8.mctrl.jooq.tables.Terminaldevice.TERMINALDEVICE
import z8.mctrl.jooq.tables.Token.TOKEN
import z8.mctrl.jooq.tables.Tokenaction.TOKENACTION
import z8.mctrl.util.table.AbstractRDSTable
import javax.annotation.PostConstruct
import javax.faces.context.FacesContext
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("TokensController")
@ViewScoped
class TokensController : AbstractRDSTable<TokensController.PayoutsControllerRow>(
    PayoutsControllerRow::class.java
) {


        public val TOKEN_DEACTIVATED = TokenAction.DEACTIVATED


    class PayoutsControllerRow (
        val time: Long?,
        val id: String?,
        val action: TokenAction?
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
            //FacesContext.getCurrentInstance().partialViewContext.evalScripts.add("butter.modal.open('selectionDetails');")
        }

        registerColumn("column1", TOKEN.TIME)
        registerColumn("column2", TOKEN.ID)

        rds?.let {
            super.initialize(
                {
                    rds.dsl().fetchCount(
                        DSL.selectFrom(TOKEN).where(
                            TOKEN.ID.`in`(
                                DSL.select(TOKEN.ID).from(TOKEN)
                                    .where(TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                    )
                },
                {
                    rds.dsl()
                        .select(
                            TOKEN.ID,
                            TOKEN.TIME,
                            DSL.field(
                                DSL.select(TOKENACTION.ACTION).from(TOKENACTION)
                                    .where(TOKENACTION.TOKEN.eq(TOKEN.ID))
                                    .orderBy(TOKENACTION.TIME.desc())
                                    .limit(1)
                            ).`as`("action")
                        ).from(TOKEN)
                        .where(
                            TOKEN.ID.`in`(
                                DSL.select(TOKEN.ID).from(TOKEN)
                                    .where(TOKEN.USER.eq(sc!!.getUser()!!.id))
                            )
                        )
                }

            )
        }
    }

}