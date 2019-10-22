package cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem

/**
 * [BindingAdapter]s for the [ScheduleItem]s list.
 */
@BindingAdapter("app:items")
fun setItems(listView: RecyclerView, items: List<ScheduleItem>?) {
    (listView.adapter as? TodoListAdapter)?.submitList(items ?: listOf())
}

@BindingAdapter("app:remainTime", "app:date")
fun setRemainTime(textView: TextView, startTime: String, date: String) {

    var string:String = ""

    if (date.equals(DateUtil.getTodayOrTomorrow("today"))) {

        val currentTime = DateUtil.getCurrentTime()

        val currentTrans = currentTime.split(":")
        val startTimeTrans = startTime.split(":")

        //Log.d("checkArrive",   "startTime hour: "+startTimeTrans[0]+" minute: "+ startTimeTrans[1]+ "cur hour : "+ currentTrans[0] +" minute: "+ currentTrans[1])

        when {
            startTimeTrans[0].toInt() < currentTrans[0].toInt() -> {
                textView.setTextColor(textView.resources.getColor(R.color.cobongRed))
                textView.text = textView.resources.getString(R.string.todolist_time_over)
                return
            }

            startTimeTrans[0].toInt() == currentTrans[0].toInt() -> {
                when {
                    startTimeTrans[1].toInt() >= currentTrans[1].toInt() -> {
                        var hour = startTimeTrans[0].toInt() - currentTrans[0].toInt()
                        var minute = startTimeTrans[1].toInt() - currentTrans[1].toInt()

                        if (minute < 0) {
                            hour -= 1
                            minute += 60
                        }

                        string = hour.toString() + "시간 " + minute + "분 남았습니다"

                        textView.text = string
                        return
                    }
                    startTimeTrans[1].toInt() < currentTrans[1].toInt() -> {
                        textView.setTextColor(textView.resources.getColor(R.color.cobongRed))
                        textView.text = textView.resources.getString(R.string.todolist_time_over)
                        return
                    }
                }
            }
            else -> {
                var hour = startTimeTrans[0].toInt() - currentTrans[0].toInt()
                var minute = startTimeTrans[1].toInt() - currentTrans[1].toInt()

                if (minute < 0) {
                    hour -= 1
                    minute += 60
                }

                string = hour.toString() + "시간 " + minute + "분 남았습니다"

                textView.text = string
                return
            }
        }
    } else {
        textView.setText(startTime)
        return
    }

}