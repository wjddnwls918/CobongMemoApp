package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    fun curDate(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")

        return sdf.format(date)
    }

    fun curDateForVoiceMemo(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)
        val sdf = SimpleDateFormat("yyyy년MM월dd일hh시mm분_음성메모")

        return sdf.format(date)
    }

    fun dateTransForCalendar(date: Long): String {
        try {
            val formatter = SimpleDateFormat("yyyy년 MM월", Locale.ENGLISH)
            val d = Date(date)
            return formatter.format(d).toUpperCase()
        } catch (e: Exception) {
            return " "
        }
    }

}
