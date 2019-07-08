package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoApiCreator
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoPlaceApi
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.KakaoResponseModel
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers

class ScheduleRepository {

    val service: KakaoPlaceApi = KakaoApiCreator.create(KakaoPlaceApi::class.java)

    companion object {
        private var INSTANCE: ScheduleRepository? = null

        fun getInstance(): ScheduleRepository {
            val tempInstance =
                INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(ScheduleRepository::class) {
                val instance = ScheduleRepository()
                INSTANCE = instance
                return instance
            }
        }
    }

    fun getKeywordPlace(key: String, query: String, page:Int): Single<KakaoResponseModel> {
        return service.getKeywordPlace(key, query, page)
            //.observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(Schedulers.io());
    }

}