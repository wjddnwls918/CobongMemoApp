package cobong.jeongwoojin.cobongmemo.cobongmemo.view.todolist

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository


class TodoListViewModel(application: Application) : AndroidViewModel(application) {

    //Room
    private var repository: ScheduleRepository

    val todayItems: LiveData<List<ScheduleItem>>
    val tomorrowItems: LiveData<List<ScheduleItem>>

    private val _openTaskEvent = MutableLiveData<Event<ScheduleItem>>()
    val openTaskEvent: LiveData<Event<ScheduleItem>> = _openTaskEvent

    init {
        repository = ScheduleRepository.getInstance(application)
        todayItems =
            repository.getAllTodayOrTomorrowSchedules(DateUtil.getTodayOrTomorrow("today"))
        tomorrowItems =
            repository.getAllTodayOrTomorrowSchedules(DateUtil.getTodayOrTomorrow("tomorrow"))
    }

    fun onScheduleClick(schedule: ScheduleItem) {
        _openTaskEvent.value = Event(schedule)
    }

}