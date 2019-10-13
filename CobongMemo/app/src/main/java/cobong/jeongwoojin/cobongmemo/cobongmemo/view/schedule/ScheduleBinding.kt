package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.widget.TextView
import androidx.databinding.BindingAdapter

object ScheduleBinding {

    @JvmStatic
    @BindingAdapter("startTime","endTime")
    fun setScheduleDate(view: TextView, startTime: String, endTime:String) {
        view.setText(startTime +" ~ " + endTime)
    }

}