package z8.mctrl.controller.payments

import org.butterfaces.event.TableSingleSelectionListener
import org.butterfaces.model.table.DefaultTableModel
import org.butterfaces.model.table.SortType
import org.butterfaces.model.table.TableModel
import org.butterfaces.model.table.TableToolbarRefreshListener
import z8.mctrl.jooq.tables.pojos.Payment
import javax.annotation.PostConstruct
import javax.faces.view.ViewScoped
import javax.inject.Named


@Named("PaymentsController")
@ViewScoped
class PaymentsController : TableSingleSelectionListener<Payment>, TableToolbarRefreshListener {

    class PaymentsControllerData(time: Long, token: String, what: String)

    val tableModel: TableModel = DefaultTableModel()
    private var selectedRow: Payment? = null
    private var filterValue: String? = null

    @PostConstruct
    fun init() {
        // initial table ordering by first column
        tableModel.tableRowSortingModel
            .sortColumn("table", "column1", null, SortType.ASCENDING)
    }

    fun getValue(): List<Payment> {
        return arrayListOf(
            Payment(
                1, System.currentTimeMillis(), "internal", "2", "8483", 4.2
            ),
            Payment(
                2, System.currentTimeMillis(), "internal", "3", "1234", 4.2
            ),
            Payment(
                4, System.currentTimeMillis(), "internal", "1", "4324", 4.2
            )
        )
    }

    override fun processTableSelection(data: Payment?) {
        this.selectedRow = data
    }

    override fun isValueSelected(data: Payment?): Boolean {
        return if (selectedRow != null) data!!.id === selectedRow!!.id else false
    }

    override fun onPreRefresh() {

    }


    fun filterByValue(pairs: List<Payment?>?, filterValue: String?): List<Payment?>? {
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