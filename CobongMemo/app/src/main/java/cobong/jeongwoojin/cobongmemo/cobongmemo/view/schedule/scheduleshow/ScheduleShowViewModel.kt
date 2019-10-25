package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleShowViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var curSchedule: ScheduleItem

    //메모 보기
    private val _scheduleDeleteClickEvent = MutableLiveData<Event<Unit>>()
    val scheduleDeleteClickEvent: LiveData<Event<Unit>> = _scheduleDeleteClickEvent

    fun onScheduleDeleteClick() {
        _scheduleDeleteClickEvent.value = Event(Unit)
    }
    /*  From Room
   *
   *
    */

    //delete Schedule
    fun deleteSchedule() {
        viewModelScope.launch(Dispatchers.IO) {
            MemoApplication.scheduleRepository.deleteSchedule(
                curSchedule
            )
        }
    }
}
