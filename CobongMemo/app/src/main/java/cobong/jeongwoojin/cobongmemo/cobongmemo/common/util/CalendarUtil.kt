package cobong.jeongwoojin.cobongmemo.cobongmemo.common.util

import java.util.*

object CalendarUtil {

    fun setTime(dateArray: Array<String>, startTimeArray: Array<String>, alarmType: Int): Calendar {

        return Calendar.getInstance().apply {
            set(Calendar.YEAR, dateArray[0].toInt())
            when (dateArray[1].toInt()) {
                1 ->
                    set(Calendar.MONTH, Calendar.JANUARY)
                2 ->
                    set(Calendar.MONTH, Calendar.FEBRUARY)
                3 ->
                    set(Calendar.MONTH, Calendar.MARCH)
                4 ->
                    set(Calendar.MONTH, Calendar.APRIL)
                5 ->
                    set(Calendar.MONTH, Calendar.MAY)
                6 ->
                    set(Calendar.MONTH, Calendar.JUNE)
                7 ->
                    set(Calendar.MONTH, Calendar.JULY)
                8 ->
                    set(Calendar.MONTH, Calendar.AUGUST)
                9 ->
                    set(Calendar.MONTH, Calendar.SEPTEMBER)
                10 ->
                    set(Calendar.MONTH, Calendar.OCTOBER)
                11 ->
                    set(Calendar.MONTH, Calendar.NOVEMBER)
                12 ->
                    set(Calendar.MONTH, Calendar.DECEMBER)
            }
            set(Calendar.DATE, dateArray[2].toInt())
            set(Calendar.HOUR_OF_DAY, startTimeArray[0].toInt())
            set(Calendar.MINUTE, startTimeArray[1].toInt())
            set(Calendar.SECOND, 0)

            addTimeByAlarmType(alarmType, this)
        }

    }


    fun addTimeByAlarmType(alarmType: Int, calendar: Calendar) {
        when (alarmType) {

            //10분 전
            1 -> calendar.add(Calendar.MINUTE, -10)

            //30분 전
            2 -> calendar.add(Calendar.MINUTE, -30)

            //1시간 전
            3 -> calendar.add(Calendar.HOUR_OF_DAY, -1)

            //2시간 전
            4 -> calendar.add(Calendar.HOUR_OF_DAY, -2)

            //1일 전
            5 -> calendar.add(Calendar.DATE, -1)

        }
    }

}