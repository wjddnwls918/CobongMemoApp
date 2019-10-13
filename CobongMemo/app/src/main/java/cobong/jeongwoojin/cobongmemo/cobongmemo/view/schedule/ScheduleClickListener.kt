package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem

interface ScheduleClickListener {
    fun onScheduleClick(item: ScheduleItem)
}