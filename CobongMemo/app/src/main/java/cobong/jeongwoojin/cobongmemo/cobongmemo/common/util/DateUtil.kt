package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun curDate(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyyMMddhhmmss")

        return sdf.format(date)
    }

}
