package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Single


@Dao
interface ScheduleDao {

    @Query("SELECT * from schedule order by `index` desc")
    fun getAllSchedule(): LiveData<List<ScheduleItem>>

    @Query( "SELECT * from schedule where date = :date order by `startTime` asc")
    fun getAllScheduleByDate(date: String): Single<MutableList<ScheduleItem>>

    @Query( "SELECT * from schedule where date = :date order by `startTime` asc")
    fun getAllTodayOrTomorrowSchedules(date: String): LiveData<List<ScheduleItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(schedule: ScheduleItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSchedule(vararg schedule: ScheduleItem)

    @Update
    fun updateSchedule(vararg schedule: ScheduleItem)

    @Delete
    fun deleteSchedule(vararg schedule: ScheduleItem)

    @Delete
    fun deleteScheduleNullable(schedule: ScheduleItem?)

    @Query("delete from schedule")
    fun deleteAll()
}