package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem

interface ScheduleNavigator {
    fun onAddScheduleStartClick()
    fun onScheduleClick(schedule: ScheduleItem)

}