package z8.mctrl.controller.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import javax.faces.view.ViewScoped
import javax.inject.Named

@Named("DateUtils")
@ViewScoped
class DateUtils {

    fun toHumanTime(long: Long): String? {
        return Instant.ofEpochMilli(long).atZone(
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("HH:mm"))
    }

    fun toHumanDate(long: Long): String? {
        return Instant.ofEpochMilli(long).atZone(
            ZoneId.systemDefault()
        ).format(DateTimeFormatter.ofPattern("dd.MM.yy"))
    }

}