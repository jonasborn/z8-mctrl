package z8.mctrl.controller.payments

import org.butterfaces.event.TableSingleSelectionListener
import org.butterfaces.model.table.DefaultTableModel
import org.butterfaces.model.table.SortType
import org.butterfaces.model.table.TableModel
import org.butterfaces.model.table.TableToolbarRefreshListener
import org.jetbrains.exposed.sql.transactions.inTopLevelTransaction
import org.springframework.beans.factory.annotation.Autowired
import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.Externaldevice.Companion.EXTERNALDEVICE
import z8.mctrl.jooq.tables.Payment.Companion.PAYMENT
import z8.mctrl.jooq.tables.Token.Companion.TOKEN
import javax.annotation.PostConstruct
import javax.faces.view.ViewScoped
import javax.inject.Named
import org.jooq.impl.DSL.*


@Named("PaymentsController")
@ViewScoped
class PaymentsController : TableSingleSelectionListener<PaymentsController.PaymentsControllerData>, TableToolbarRefreshListener {

    class PaymentsControllerData(val id: String, val time: Long?, val token: String?, val target: String?)

    @Autowired
    var rds: RDS? = null

    val tableModel: TableModel = DefaultTableModel()
    private var selectedRow: PaymentsControllerData? = null
    private var filterValue: String? = null
    public var totalAmount = 0
    public var totalPages = 0
    public var pageSize = 10


    @PostConstruct
    fun init() {
        totalAmount = queryTotalAmount()
        totalPages = (totalAmount / pageSize) + 1
        selectPage(1)
        // initial table ordering by first column
        tableModel.tableRowSortingModel
            .sortColumn("table", "column1", null, SortType.ASCENDING)
    }

    var data = arrayListOf<PaymentsControllerData>()

    fun queryTotalAmount(): Int {
        return rds!!.dsl().fetchCount(
            selectFrom(PAYMENT).where(
                PAYMENT.TOKEN.`in`(
                    select(TOKEN.ID).from(TOKEN).where(TOKEN.USER.eq("7139be96d7684a4cba85dd9cc1400289"))
                )
            )
        )

    }


    fun selectPage(page: Int) {
        val start = pageSize * (page - 1)

        val res = rds!!.dsl()
            .select(
                PAYMENT.ID, PAYMENT.TIME, PAYMENT.TOKEN, EXTERNALDEVICE.TARGET
            ).from(PAYMENT)
            .innerJoin(EXTERNALDEVICE).on(PAYMENT.EXTERNAL.eq(EXTERNALDEVICE.ID))
            .where(PAYMENT.EXTERNAL.eq(EXTERNALDEVICE.ID))
            .offset(start).limit(pageSize)
            .fetchInto(PaymentsControllerData::class.java)

        data = ArrayList(res)
    }


    override fun processTableSelection(data: PaymentsControllerData?) {
        this.selectedRow = data
    }

    override fun isValueSelected(data: PaymentsControllerData?): Boolean {
        return if (selectedRow != null) data!!.id === selectedRow!!.id else false
    }

    override fun onPreRefresh() {

    }


    fun filterByValue(pairs: List<PaymentsControllerData?>?, filterValue: String?): List<PaymentsControllerData?>? {
        // TODO implement me
        return pairs
    }

    fun getFilterValue(): String? {
        return filterValue
    }

    fun setFilterValue(filterValue: String?) {
        this.filterValue = filterValue
    }

}