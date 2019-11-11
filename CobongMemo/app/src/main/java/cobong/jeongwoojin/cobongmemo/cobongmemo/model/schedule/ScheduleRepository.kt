package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule

import android.app.Application
import androidx.lifecycle.LiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoDatabase
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoApiCreator
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoPlaceApi
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoResponseModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ScheduleRepository(application: Application) {

    val service: KakaoPlaceApi = KakaoApiCreator.create(KakaoPlaceApi::class.java)

    private val memoDatabase =
        MemoDatabase.getDatabase(
            application
        )
    private val scheduleDao: ScheduleDao = memoDatabase.scheduleDao()


    companion object {
        private var INSTANCE: ScheduleRepository? = null

        fun getInstance(application: Application): ScheduleRepository {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(ScheduleRepository::class) {
                val instance = ScheduleRepository(application)
                INSTANCE = instance
                return instance
            }
        }
    }

    /*  From kakao api
    *
    *
     */
    fun getKeywordPlace(key: String, query: String, page: Int): Single<KakaoResponseModel> {
        return service.getKeywordPlace(key, query, page)
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io())
    }


    /*  From Room
    *
    *
     */
    val schedule: LiveData<List<ScheduleItem>> = scheduleDao.getAllSchedule()


    fun getAllTodayOrTomorrowSchedules(date: String): LiveData<List<ScheduleItem>> =
        scheduleDao.getAllTodayOrTomorrowSchedules(date)

    suspend fun insertByRoom(schedule: ScheduleItem) {
        scheduleDao.insert(schedule)
    }

    fun getAllScheduleByDate(date: String)
            : Single<MutableList<ScheduleItem>> =
        scheduleDao.getAllScheduleByDate(date).subscribeOn(Schedulers.io())

    suspend fun deleteSchedule(schedule: ScheduleItem) {
        scheduleDao.deleteSchedule(schedule)
    }

}