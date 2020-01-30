package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import cobong.jeongwoojin.cobongmemo.cobongmemo.R
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.MemoAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.ScheduleAdapter
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist.TodoListAdapter
import com.google.android.material.textfield.TextInputEditText

    //memo
    @BindingAdapter("items")
    fun setItems(listView: RecyclerView, items: List<MemoItem>?) {
        items?.let {
            (listView.adapter as MemoAdapter).submitList(items)
        }
    }

    @BindingAdapter("bind_adapter")
    fun setBindAdapter(view: RecyclerView, adapter: MemoAdapter?) {
        adapter?.let {
            view.adapter = it
        }
    }

    @BindingAdapter("setRecyclerViewVisible")
    fun setRecyclerViewVisible(listView: RecyclerView, size: Int) {
        when (size) {
            0 ->listView.visibility = View.GONE
            else -> listView.visibility = View.VISIBLE
        }
    }

    @BindingAdapter("setTextViewVisible")
    fun setTextViewVisible(textView: TextView, size: Int) {
        when (size) {
            0 ->textView.visibility = View.VISIBLE
            else -> textView.visibility = View.GONE
        }
    }
    @BindingAdapter("memoType")
    fun setMemoType(view: ImageView, memoType: String) {
        //check memoType
        if (memoType == "text") {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_text_format_black_24dp))
        } else if (memoType == "handwrite") {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_palette_black_24dp))
        } else {
            view.setImageDrawable(ContextCompat.getDrawable(view.context, R.drawable.ic_keyboard_voice_black_24dp))
        }

    }

    //schedule
    @BindingAdapter("setScheduleItems")
    fun setScheduleItems(listView: RecyclerView, items: List<ScheduleItem>?) {
        items?.let {
            (listView.adapter as ScheduleAdapter).submitList(items)
        }
    }

    @BindingAdapter("bind_adapter")
    fun setBindAdapter(view: RecyclerView, adapter: ScheduleAdapter?) {
        adapter?.let {
            view.adapter = it
        }
    }

    @BindingAdapter("startTime","endTime")
    fun setScheduleDate(view: TextView, startTime: String, endTime:String) {
        view.setText(startTime +" ~ " + endTime)
    }


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

    @BindingAdapter("setTodoListItems")
    fun setTodoListItems(listView: RecyclerView, items: List<ScheduleItem>?) {
        (listView.adapter as? TodoListAdapter)?.submitList(items ?: listOf())
    }

    @BindingAdapter("remainTime", "date")
    fun setRemainTime(textView: TextView, startTime: String, date: String) {

        var string:String

        if (date.equals(DateUtil.getTodayOrTomorrow("today"))) {

            val currentTime = DateUtil.getCurrentTime()

            val currentTrans = currentTime.split(":")
            val startTimeTrans = startTime.split(":")

            //Log.d("checkArrive",   "startTime hour: "+startTimeTrans[0]+" minute: "+ startTimeTrans[1]+ "cur hour : "+ currentTrans[0] +" minute: "+ currentTrans[1])

            when {
                startTimeTrans[0].toInt() < currentTrans[0].toInt() -> {
                    textView.setTextColor( ContextCompat.getColor(textView.context,R.color.cobongRed))
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
                            textView.setTextColor( ContextCompat.getColor(textView.context,R.color.cobongRed))
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

