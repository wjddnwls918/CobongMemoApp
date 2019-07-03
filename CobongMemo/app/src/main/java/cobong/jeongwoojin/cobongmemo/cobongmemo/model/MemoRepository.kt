package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import java.io.File

class MemoRepository(application: Application) {

    private val memoDatabase = MemoDatabase.getDatabase(application)
    private val memoDao: MemoListDao = memoDatabase.memolistDao()

    val memos: LiveData<List<MemoItem>> = memoDao.getAllMemos()

    /*
   *  local database By Room
   *
   *
    */


    suspend fun insertByRoom(memo: MemoItem) {
        memoDao.insert(memo)
    }

    suspend fun deleteByRoom(memo: MemoItem) {

        //저장 파일 삭제
        val file: File
        var path: String

        if (memo.memoType == "voice") {
            path =
                MemoApplication.root + "/" + memo.voiceId + ".mp3"
            file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("filedelete", "삭제완료")
                } else {
                    Log.d("filedelete", "실패")
                }
            }

        } else if (memo.memoType == "handwrite") {
            path =
                MemoApplication.root + "/saved_images/" + memo.handwriteId + ".jpg"
            file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("filedelete", "삭제완료")
                } else {
                    Log.d("filedelete", "실패")
                }
            }

        }


        memoDao.deleteMemos(memo)
    }

    suspend fun deleteMemoNullableByRoom(memo: MemoItem?) {
        memoDao.deleteMemoNullable(memo)
    }

    suspend fun updateByRoom(memo: MemoItem) {
        memoDao.updateMemos(memo)
    }

    companion object {
        private var INSTANCE: MemoRepository? = null

        fun getInstance(application: Application): MemoRepository {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(MemoRepository::class) {
                val instance = MemoRepository(application)
                INSTANCE = instance
                return instance
            }
        }
    }

}