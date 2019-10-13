package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document

interface ScheduleNavigator {
    fun onAddScheduleStartClick() {}
    fun onScheduleWriteFinishClick() {}
    fun onSetAlarmClick() {}
    fun onStartTimeSettingClick() {}
    fun onEndTimeSettingClick() {}
    fun onDateClick() {}
    fun onDocumentClick(document: Document) {}
    fun onScheduleClick(schedule: ScheduleItem) {}
}