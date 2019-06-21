package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.app.Application
import android.database.Cursor
import android.util.Log
import androidx.lifecycle.LiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.BasicInfo
import cobong.jeongwoojin.cobongmemo.cobongmemo.view.MemoAdapter
import java.io.File

class MemoRepository(application: Application) {

    private val memoDatabase = MemoDatabase.getDatabase(application)
    private val memoDao: MemoListDao = memoDatabase.memolistDao()
    private val memos: LiveData<List<MemoItem>> = memoDao.getAllMemos()

    var helper: DBHelper = DBHelper(application.applicationContext)
    var db = helper.writableDatabase


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

    //모든 값 반환
    /* fun getAll(): LiveData<List<MemoItem>> {
         return memos
     }

     //값 저장
     fun insert(memo: MemoItem) = {

         try {
             val thread = Thread(Runnable {
                 memoDao.insert(memo)
             })
             thread.start()

         } catch (e: Exception) {
             e.printStackTrace()
         }
         *//*
        var insertJob = GlobalScope.launch(Dispatchers.IO) {
            memoDao.insert(memo)
        }*//*

    }

    //값 삭제
    fun deleteAll() = {

        try {
            val thread = Thread(Runnable {
                memoDao.deleteAll()
            })
            thread.start()

        } catch (e: Exception) {
            e.printStackTrace()
        }*/

    /*var deleteAllJob = GlobalScope.launch(Dispatchers.IO) {
        memoDao.deleteAll()
    }

}
*/

    /*
    *  local database with sqlite
    *
    *
     */


    //get all memos
    fun getAllMemo(): List<MemoListItem> {
        val list = ArrayList<MemoListItem>()

        //list = database.memolistDao().getAllMemos();

        val cursor: Cursor?
        val sel = "select * from memo order by idx desc"
        try {
            cursor = db!!.rawQuery(sel, null)

            Log.d("dbarrive", "hello")
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    val index = cursor.getInt(0)
                    val temTitle = cursor.getString(2)
                    val temSubTitle = cursor.getString(3)
                    val temcontent = cursor.getString(4)
                    val temMemoType = cursor.getString(5)
                    val temInputTime = cursor.getString(1)
                    val temVoiceId = cursor.getString(6)
                    val temHandwriteId = cursor.getString(7)
                    list.add(
                        MemoListItem(
                            index,
                            temTitle,
                            temSubTitle,
                            temMemoType,
                            temInputTime,
                            temcontent,
                            temVoiceId,
                            temHandwriteId
                        )
                    )
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }


        return list
    }

    //delete memo
    fun deleteMemo(memo: MemoListItem, memoAdapter: MemoAdapter) {

        val del = "delete from memo where `idx`=" + memo.index
        db!!.execSQL(del)


        //저장 파일 삭제
        val file: File
        var path = ""
        val curRealPos = memoAdapter!!.getItemPosition(memo)

        if (memoAdapter!!.getItem(curRealPos)!!.memoType == "voice") {
            path =
                BasicInfo.root + "/" + memoAdapter!!.getItem(curRealPos)!!.voiceId + ".mp3"
            file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("filedelete", "삭제완료")
                } else {
                    Log.d("filedelete", "실패")
                }
            }

        } else if (memoAdapter!!.getItem(curRealPos)!!.memoType == "handwrite") {
            path =
                BasicInfo.root + "/saved_images/" + memoAdapter!!.getItem(curRealPos)!!.handwriteId + ".jpg"
            file = File(path)
            if (file.exists()) {
                if (file.delete()) {
                    Log.d("filedelete", "삭제완료")
                } else {
                    Log.d("filedelete", "실패")
                }
            }

        }

    }


    //insert voice record
    fun insertVoiceRecord(resultDate: String) {

        val insertVoice: String
        insertVoice = "insert into memo(title,memo_type,ID_VOICE) values(?,?,?)"
        val args = arrayOf(resultDate, "voice", resultDate)
        try {
            db.execSQL(insertVoice, args)

        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //insert text memo
    fun insertTextMemo(title: String, subTitle: String, content: String) {
        val insertMemo: String
        insertMemo = "insert into memo(title,subtitle,content,memo_type) values(?,?,?,?)"
        val args = arrayOf(
            title,
            subTitle,
            content,
            "text"
        )
        db.execSQL(insertMemo, args)

    }

    //update text memo
    fun updateTextMemo(index: Int, title: String, subTitle: String, content: String) {
        val updateMemo: String
        updateMemo = "update memo set title=?,subtitle=?,content=? where idx=" + index;
        val args = arrayOf(title, subTitle, content);
        db.execSQL(updateMemo, args);
    }

    //delete text memo
    fun deleteTextMemo(index: Int) {
        val del = "delete from memo where `idx`=" + index
        db.execSQL(del)
    }

    //insert handwrite memo
    fun insertHandwriteMemo(title: String, subTitle:String, handwriteId: String) {
        val insertHandwrite: String

        insertHandwrite =
            "insert into memo(title,SUBTITLE,MEMO_TYPE,ID_HANDWRITING) values(?,?,?,?)"
        val args = arrayOf(
            title,
            subTitle,
            "handwrite",
            handwriteId
        )
        try {
            db.execSQL(insertHandwrite, args)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //delete handwrite memo
    fun deleteHandwriteMemo(index: Int) {

        val del = "delete from memo where `idx`=" + index
        db.execSQL(del)

    }

}