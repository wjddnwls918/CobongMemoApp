package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo

import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class KakaoApiCreator {

    companion object{
        val BASE_URL = "https://dapi.kakao.com"

        private fun defaultRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }

        fun <T> create(service: Class<T>): T {
            return defaultRetrofit()
                .create(service)
        }

        private fun getClient(): OkHttpClient {
            return OkHttpClient.Builder()
                .addNetworkInterceptor(StethoInterceptor())
                .retryOnConnectionFailure(true)
                .build()
        }

    }

}