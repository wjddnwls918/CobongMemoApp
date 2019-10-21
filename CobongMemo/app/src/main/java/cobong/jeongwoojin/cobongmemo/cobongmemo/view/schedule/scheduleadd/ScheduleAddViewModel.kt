package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleadd

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleAddViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var navigator: ScheduleAddNavigator

    var date: ObservableField<String> = ObservableField()
    var startTime: ObservableField<String> = ObservableField()
    var endTime: ObservableField<String> = ObservableField()
    var place: ObservableField<String> = ObservableField()
    var alarmType: MutableLiveData<Int> = MutableLiveData()

    var document: MutableLiveData<Document> = MutableLiveData()

    private var repository: ScheduleRepository

    init {
        repository = ScheduleRepository.getInstance(application)
    }

    fun onScheduleWriteFinishClick() {
        navigator.onScheduleWriteFinishClick()
    }

    fun onSetAlarmClick() {
        navigator.onSetAlarmClick()
    }

    fun onStartTimeSettingClick() {
        navigator.onStartTimeSettingClick()
    }

    fun onEndTimeSettingClick() {
        navigator.onEndTimeSettingClick()
    }

    fun onDateClick() {
        navigator.onDateClick()
    }


    /*  From Room
   *
   *
    */


    fun insertScheduleByRoom(
        title: String,
        date: String,
        startTime: String,
        endTime: String,
        place: String,
        description: String,
        alarmType: Int,
        y: Double,
        x: Double
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertByRoom(
                ScheduleItem(
                    0,
                    title = title,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    place = place,
                    description = description,
                    alarmType = alarmType,
                    y = y,
                    x = x
                )
            )
        }
    }
}