package z8.mctrl.util.table

import org.apache.logging.log4j.LogManager
import org.butterfaces.event.TableSingleSelectionListener
import org.butterfaces.model.table.SortType
import org.butterfaces.model.table.TableModel
import org.butterfaces.model.table.TableToolbarRefreshListener
import org.jooq.SelectConditionStep
import org.jooq.TableField
import java.util.function.Supplier

abstract class AbstractRDSTable<T>(val cls: Class<T>) :
    TableSingleSelectionListener<T>,
    TableToolbarRefreshListener {

    val logger = LogManager.getLogger()

    var countQuery: Supplier<Int>? = null
    var selectQuery: Supplier<SelectConditionStep<*>>? = null

    var page = 1
    val pageSize = 1
    var totalAmount = 0
    var totalPages = 0

    var sortingDetails: HashMap<String, TableField<*, *>> = hashMapOf()
    var data: List<T> = arrayListOf()

    var selectedRow: T? = null

    val model: TableModel = SortableTable {
        queryData(page, it)
    }

    fun registerColumn(col: String, field: TableField<*, *>) {
        sortingDetails[col] = field
    }


    fun initialize(countSupplier: Supplier<Int>, selectQuerySupplier: Supplier<SelectConditionStep<*>>) {
        this.countQuery = countSupplier
        this.selectQuery = selectQuerySupplier
        totalAmount = queryTotalAmount()
        totalPages = (totalAmount / pageSize)
        if ((totalAmount % pageSize) > 0) totalPages++
        queryData(1, SortableTableModel.SortingInformation(null, null, null, null))
        logger.debug("RDSTable initialized")
    }

    private fun queryTotalAmount(): Int {
        if (selectQuery == null) {
            logger.error("Not initialized, unable to get total amount")
            return 0
        }
        return countQuery!!.get()

    }

    private fun queryData(page: Int, si: SortableTableModel.SortingInformation) {
        if (countQuery == null) {
            logger.error("Not initialized, unable to query")
            return
        }
        val start = pageSize * (page - 1)
        logger.debug("Requesting page {}, start {}, limit {}, sorting by {} - {}", page, start, pageSize, si.columnUniqueIdentifier, si.sortType)

        val keyNeeded = si.columnUniqueIdentifier
        val detailsFound = sortingDetails[keyNeeded]
        println(detailsFound)
        if (detailsFound != null) {
            if (si.sortType == SortType.DESCENDING) {
                logger.debug("Sorting by {}, des", detailsFound.table!!.name)
                data = selectQuery!!.get().orderBy(detailsFound.desc()).offset(start).limit(pageSize)
                    .fetchInto(cls)
            } else {
                logger.debug("Sorting by {}, asc", detailsFound.table!!.name)
                data = selectQuery!!.get().orderBy(detailsFound.asc()).offset(start).limit(pageSize)
                    .fetchInto(cls)
            }

        } else {
            logger.debug("Nothing to sort")
            data = selectQuery!!.get().offset(start).limit(pageSize)
                .fetchInto(cls)
        }

    }

    fun changePage(p: Int) {
        logger.debug("Requested page {}", p)
        if (p <= totalPages) {
            page = p
            queryData(page, SortableTableModel.SortingInformation(null, null, null, null))
        } else {
            logger.debug("Page {} out of range of max {}", p, totalPages)
        }
    }

    override fun processTableSelection(data: T) {
        this.selectedRow = data
    }

    override fun isValueSelected(data: T): Boolean {
        return if (selectedRow != null) data!! === selectedRow!! else false
    }

    override fun onPreRefresh() {

    }
}