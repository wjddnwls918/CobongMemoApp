package cobong.jeongwoojin.cobongmemo.cobongmemo

import android.content.Intent
import android.os.Environment
import androidx.multidex.MultiDexApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.error.CobongMemoExceptionHandler
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.alarm.AlarmReceiver
import com.crashlytics.android.Crashlytics
import com.facebook.stetho.Stetho
import com.facebook.stetho.okhttp3.StethoInterceptor
import io.fabric.sdk.android.Fabric
import okhttp3.OkHttpClient

class MemoApplication : MultiDexApplication() {
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


        lateinit var memoRepository: MemoRepository
        lateinit var scheduleRepository: ScheduleRepository

    }



    override fun onCreate() {
        super.onCreate()

        Stetho.initializeWithDefaults(this)

        OkHttpClient.Builder()
            .addNetworkInterceptor(StethoInterceptor())
            .build()

        intent = Intent(applicationContext, AlarmReceiver::class.java)
        intent.action = "ALARM_ACTION"


        memoRepository = MemoRepository.getInstance(this)
        scheduleRepository = ScheduleRepository(this)

        setCrashHandler()

    }

    private fun setCrashHandler() {
        val defaultExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { _, _ ->
            // Crashlytics에서 기본 handler를 호출하기 때문에 이중으로 호출되는것을 막기위해 빈 handler로 설정
        }
        Fabric.with(this, Crashlytics())
        val fabricExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler(
            CobongMemoExceptionHandler(
                this,
                defaultExceptionHandler,
                fabricExceptionHandler
            )
        )
    }

    override fun onTerminate() {
        super.onTerminate()
    }
}
