package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.scheduleadd

import android.app.Application
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ScheduleAddViewModel(application: Application) : AndroidViewModel(application) {

    var date: ObservableField<String> = ObservableField()
    var startTime: ObservableField<String> = ObservableField()
    var endTime: ObservableField<String> = ObservableField()
    var place: ObservableField<String> = ObservableField()
    var alarmType: MutableLiveData<Int> = MutableLiveData()

    var document: MutableLiveData<Document> = MutableLiveData()


    //일정 작성
    private val _scheduleWriteFinishClickEvent = MutableLiveData<Event<Unit>>()
    val scheduleWriteFinishClickEvent: LiveData<Event<Unit>> = _scheduleWriteFinishClickEvent

    //알람 설정
    private val _setAlarmClickEvent = MutableLiveData<Event<Unit>>()
    val setAlarmClickEvent: LiveData<Event<Unit>> = _setAlarmClickEvent

    //시간 설정열기
    private val _startTimeSettingClickEvent = MutableLiveData<Event<Unit>>()
    val startTimeSettingClickEvent: LiveData<Event<Unit>> = _startTimeSettingClickEvent

    //시간 설정완료
    private val _endTimeSettingClickEvent = MutableLiveData<Event<Unit>>()
    val endTimeSettingClickEvent: LiveData<Event<Unit>> = _endTimeSettingClickEvent

    //날짜 설정
    private val _dateClickEvent = MutableLiveData<Event<Unit>>()
    val dateClickEvent: LiveData<Event<Unit>> = _dateClickEvent


    fun onScheduleWriteFinishClick() {
        _scheduleWriteFinishClickEvent.value = Event(Unit)
    }

    fun onSetAlarmClick() {
        _setAlarmClickEvent.value = Event(Unit)
    }

    fun onStartTimeSettingClick() {
        _startTimeSettingClickEvent.value = Event(Unit)
    }

    fun onEndTimeSettingClick() {
        _endTimeSettingClickEvent.value = Event(Unit)
    }

    fun onDateClick() {
        _dateClickEvent.value = Event(Unit)
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
            MemoApplication.scheduleRepository.insertByRoom(
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