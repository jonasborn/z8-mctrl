package z8.mctrl.companion.bill

import com.google.common.hash.Hashing
import com.google.common.io.BaseEncoding
import de.vandermeer.asciitable.AsciiTable
import de.vandermeer.asciithemes.a7.A7_Grids
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment
import org.apache.pdfbox.pdmodel.PDDocument
import org.apache.pdfbox.pdmodel.PDPage
import org.apache.pdfbox.pdmodel.PDPageContentStream
import org.apache.pdfbox.pdmodel.common.PDRectangle
import org.apache.pdfbox.pdmodel.font.PDType1Font
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor
import org.jasypt.iv.RandomIvGenerator
import org.jooq.meta.derby.sys.Sys
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import org.vandeseer.easytable.TableDrawer
import org.vandeseer.easytable.settings.HorizontalAlignment
import org.vandeseer.easytable.structure.Row
import org.vandeseer.easytable.structure.Table
import org.vandeseer.easytable.structure.cell.TextCell
import z8.mctrl.config.Config
import z8.mctrl.controller.util.DateUtils
import z8.mctrl.db.RDS
import z8.mctrl.jooq.tables.pojos.PaymentObject
import java.awt.Color
import java.io.ByteArrayOutputStream
import java.nio.file.Path
import java.util.*


@Component
class Bills @Autowired constructor(val config: Config, val rds: RDS) {

    final var jasypt: StandardPBEStringEncryptor? = null

    init {
        jasypt = StandardPBEStringEncryptor()
        val password = config.get("flow.secret")
        jasypt!!.setPassword(password)
        jasypt!!.setIvGenerator(RandomIvGenerator())
    }


    private fun cell(content: String): TextCell {
        return TextCell.builder()
            .text(content)
            .borderWidth(1f)
            .build()
    }

    class RowData(
        val id: String,
        val dateTime: String,
        val token: String,
        val details: String,
        val externalDevice: String
    ) {
        fun hash(): String {
            val data = arrayListOf(id, dateTime, token, details, externalDevice).joinToString("")
            val hash = Hashing.md5().hashString(data, Charsets.UTF_8).asBytes()
            return BaseEncoding.base16().encode(hash)
        }
    }

    private fun row(rd: RowData): Row? {
        val b = Row.builder().add(
            cell(rd.id)
        ).add(
            cell(rd.dateTime)
        ).add(
            cell(rd.token)
        ).add(
            cell(rd.details)
        ).add(
            cell(rd.externalDevice)
        )

        return b.build()
    }

    private fun header(title: String): TextCell? {
        return TextCell.builder()
            .text(title)
            .backgroundColor(Color.BLUE)
            .textColor(Color.WHITE)
            .borderWidth(1f)
            .build()
    }

    fun createTable(payments: List<PaymentObject>): Table? {
        val tableBuilder: Table.TableBuilder = Table.builder()
            .addColumnOfWidth(100f)
            .addColumnOfWidth(100f)
            .addColumnOfWidth(50f)
            .addColumnOfWidth(100f)
            .addColumnOfWidth(140f)

        val hashes = arrayListOf<String>()
        val entries = arrayListOf<RowData>()

        for (payment in payments) {
            val externalDevice = rds.externalDevice().fetchOneById(payment.external)
            var externalDeviceTitle = ""
            if (externalDevice != null) externalDeviceTitle = externalDevice.title

            val rowData = RowData(
                payment.id.toString(),
                DateUtils.toHumanDateTime(payment.time),
                payment.token.substring(0, 4) + "...",
                payment.details,
                externalDeviceTitle
            )
            hashes.add(rowData.hash())
            entries.add(rowData)
        }

        val time = System.currentTimeMillis()
        hashes.add(time.toString())

        val hash = BaseEncoding.base16().encode(
            Hashing.md5().hashString(
                hashes.joinToString(""), Charsets.UTF_8
            ).asBytes()
        ).lowercase(Locale.getDefault())

        tableBuilder.addRow(
            Row.builder()
                .add(
                    TextCell.builder()
                        .text("z8 - Your bill from " + DateUtils.toHumanDateTime(time))
                        .colSpan(5)
                        .lineSpacing(1f)
                        .borderWidthTop(1f)
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.BLUE)
                        .borderWidth(1f)
                        .build()
                )
                .horizontalAlignment(HorizontalAlignment.RIGHT)
                .build()
        )

        tableBuilder.addRow(
            Row.builder()
                .add(
                    TextCell.builder()
                        .text("Hash: $hash")
                        .colSpan(5)
                        .lineSpacing(1f)
                        .borderWidthTop(1f)
                        .textColor(Color.WHITE)
                        .backgroundColor(Color.BLUE)
                        .borderWidth(1f)
                        .build()
                )
                .horizontalAlignment(HorizontalAlignment.RIGHT)
                .build()
        )

        tableBuilder.addRow(
            Row.builder()
                .add(header("Id"))
                .add(header("Time"))
                .add(header("Token"))
                .add(header("Details"))
                .add(header("Device"))
                .build()
        )

        for (entry in entries) {
            tableBuilder.addRow(row(entry))
        }

        return tableBuilder.build()
    }

    fun generate(payments: List<PaymentObject>): ByteArray {
        val doc = PDDocument()
        TableDrawer.builder()
            .table(createTable(payments))
            .startX(50f)
            .startY(50f)
            .endY(50f) // note: if not set, table is drawn over the end of the page
            .build()
            .draw(
                { doc },
                { PDPage(PDRectangle.A4) }, 50f
            )

        val os = ByteArrayOutputStream()
        doc.save(os)
        return os.toByteArray()
    }





}