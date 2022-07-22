package z8.mctrl.util.table

import org.butterfaces.model.table.SortType
import org.butterfaces.model.table.TableRowSortingModel
import java.util.function.Consumer

class SortableTableModel(private val consumer: Consumer<SortingInformation>) : TableRowSortingModel {

    class SortingInformation(
        val tableUniqueIdentifier: String?,
        val columnUniqueIdentifier: String?,
        val sortBy: String?,
        val sortType: SortType?
    )

    var current: SortingInformation? = null

    override fun sortColumn(
        tableUniqueIdentifier: String?,
        columnUniqueIdentifier: String?,
        sortBy: String?,
        sortType: SortType?
    ) {

        current = SortingInformation(
            tableUniqueIdentifier,
            columnUniqueIdentifier,
            sortBy,
            sortType
        )

        consumer.accept(
            current!!
        )
    }

    override fun getSortType(tableUniqueIdentifier: String?, columnUniqueIdentifier: String?): SortType {
        if (current?.sortType == null) return SortType.NONE
        if (current!!.columnUniqueIdentifier != columnUniqueIdentifier) return SortType.NONE
        return current!!.sortType!!
    }
}