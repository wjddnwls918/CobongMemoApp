package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleshow

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleShowViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var navigator: ScheduleShowNavigator
    lateinit var curSchedule: ScheduleItem

    private var repository: ScheduleRepository

    init {
        repository = ScheduleRepository.getInstance(application)
    }

    fun onScheduleDeleteClick() {
        navigator.onScheduleDeleteClick()
    }

    /*  From Room
   *
   *
    */

    //delete Schedule
    fun deleteSchedule() {
        viewModelScope.launch(Dispatchers.IO) { repository.deleteSchedule(curSchedule) }
    }
}
