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


    fun getTodayOrTomorrow(date:String):String {
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.DAY_OF_WEEK, 1)
        val tomorrow = calendar.time

        val df = "yyyy-MM-dd"
        val todayString = SimpleDateFormat(df).format(today)
        val tomorrowString = SimpleDateFormat(df).format(tomorrow)

        return when(date) {
            "today" -> {
                todayString
            }
            else -> {
                tomorrowString
            }
        }

    }

    fun getCurrentTime(): String {
        val now = System.currentTimeMillis()
        val date = Date(now)

        val sdf = SimpleDateFormat("HH:mm:ss")
        val currentTime = sdf.format(date)

        return currentTime
    }


}
