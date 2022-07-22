package z8.mctrl.util.table

import org.butterfaces.model.table.*
import java.util.function.Consumer

class SortableTable(private val consumer: Consumer<SortableTableModel.SortingInformation>): TableModel {

    private val tableRowSortingModel: TableRowSortingModel = SortableTableModel(consumer)
    private val tableColumnVisibilityModel: TableColumnVisibilityModel = DefaultTableColumnVisibilityModel()
    private val tableColumnOrderingModel: TableColumnOrderingModel = DefaultTableOrderingModel()


    override fun getTableRowSortingModel(): TableRowSortingModel {
        return tableRowSortingModel
    }

    override fun getTableColumnVisibilityModel(): TableColumnVisibilityModel {
        return tableColumnVisibilityModel
    }

    override fun getTableColumnOrderingModel(): TableColumnOrderingModel {
        return tableColumnOrderingModel
    }
}