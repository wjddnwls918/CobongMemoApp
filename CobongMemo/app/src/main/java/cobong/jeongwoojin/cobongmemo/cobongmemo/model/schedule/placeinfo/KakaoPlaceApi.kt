package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface KakaoPlaceApi {

    @GET("/v2/local/search/keyword.json")
    fun getKeywordPlace(
        @Header("Authorization") key: String,
        @Query("query") query: String,
        @Query("page") page:Int
        ): Single<KakaoResponseModel>

}