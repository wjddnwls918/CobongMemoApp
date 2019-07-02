package cobong.jeongwoojin.cobongmemo.cobongmemo

import android.app.Application
import android.os.Environment

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
    }


}
