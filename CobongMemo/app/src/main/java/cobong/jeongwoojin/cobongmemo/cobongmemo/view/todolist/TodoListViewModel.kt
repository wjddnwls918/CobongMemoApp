package cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem


class TodoListViewModel(application: Application) : AndroidViewModel(application) {
    val todayItems: LiveData<List<ScheduleItem>> by lazy {
        MemoApplication.scheduleRepository.getAllTodayOrTomorrowSchedules(
            DateUtil.getTodayOrTomorrow(
                "today"
            )
        )
    }
    val tomorrowItems: LiveData<List<ScheduleItem>> by lazy {
        MemoApplication.scheduleRepository.getAllTodayOrTomorrowSchedules(
            DateUtil.getTodayOrTomorrow(
                "tomorrow"
            )
        )
    }

    private val _openTodoListEvent = MutableLiveData<Event<ScheduleItem>>()
    val openTodoListEvent: LiveData<Event<ScheduleItem>> = _openTodoListEvent

    fun onScheduleClick(schedule: ScheduleItem) {
        _openTodoListEvent.value = Event(schedule)
    }

}