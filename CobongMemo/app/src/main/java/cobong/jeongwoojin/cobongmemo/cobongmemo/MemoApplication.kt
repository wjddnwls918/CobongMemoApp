package cobong.jeongwoojin.cobongmemo.cobongmemo

import android.app.Application
import android.content.Intent
import android.os.Environment
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm.AlarmReceiver
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient

class MemoApplication : Application() {
    companion object {
        var language = ""

        val RECORDED_FILE = Environment.getExternalStorageDirectory()
        
        //
        val root = Environment.getExternalStorageDirectory().toString()

        /**
         * 외장 메모리 패스
         */
        var ExternalPath = "/mnt/sdcard/"

        /**
         * 외장 메모리 패스 체크 여부
         */
        var ExternalChecked = false

        /**
         * 사진 저장 위치
         */
        var FOLDER_PHOTO = "memo/photo/"

        /**
         * 동영상 저장 위치
         */
        var FOLDER_VIDEO = "memo/video/"

        /**
         * 녹음 저장 위치
         */
        var FOLDER_VOICE = "memo/voice/"

        /**
         * 손글씨 저장 위치
         */
        var FOLDER_HANDWRITING = "memo/handwriting/"

        /**
         * 미디어 포맷
         */
        val URI_MEDIA_FORMAT = "content://media"

        /**
         * 데이터베이스 이름
         */
        var DATABASE_NAME = "memo/memo.db"


        lateinit var intent:Intent
    }

    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.action = "ALARM_ACTION"
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
