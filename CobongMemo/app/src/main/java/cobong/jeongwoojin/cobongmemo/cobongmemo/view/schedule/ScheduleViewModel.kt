package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel


class ScheduleViewModel : ViewModel() {

    lateinit var navigator:ScheduleNavigator

    var date:ObservableField<String> = ObservableField()
    var startTime:ObservableField<String> = ObservableField()
    var endTime:ObservableField<String> = ObservableField()

    fun onAddScheduleStartClick() {
        navigator.onAddScheduleStartClick()
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

}