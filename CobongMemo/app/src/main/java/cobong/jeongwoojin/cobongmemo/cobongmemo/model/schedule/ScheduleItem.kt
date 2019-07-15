package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleItem (
    @PrimaryKey(autoGenerate = true)
    var index: Int?,
    var title: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var place: String,
    var description: String,
    var alarmType: Int?,
    var y: Double?,
    var x: Double?
){
}