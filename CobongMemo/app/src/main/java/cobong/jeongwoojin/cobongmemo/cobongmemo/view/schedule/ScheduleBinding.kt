package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText

object ScheduleBinding {

    @JvmStatic
    @BindingAdapter("startTime","endTime")
    fun setScheduleDate(view: TextView, startTime: String, endTime:String) {
        view.setText(startTime +" ~ " + endTime)
    }

    @JvmStatic
    @BindingAdapter("alarmType")
    fun setAlarmType(view: TextInputEditText, alarmType:Int) {


        val alarmTypeNumToString = arrayOf<CharSequence>(
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ontime).toString(),
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_ten_minutes_ago).toString(),
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_thirty_minutes_ago).toString(),
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_hours_ago).toString(),
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_two_hours_ago).toString(),
            view.context.resources?.getString(cobong.jeongwoojin.cobongmemo.cobongmemo.R.string.alarm_one_day_ago).toString()

        )

        view.setText(alarmTypeNumToString[alarmType])

    }

}