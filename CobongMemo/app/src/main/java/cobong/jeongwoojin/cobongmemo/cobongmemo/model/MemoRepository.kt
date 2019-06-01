package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.support.annotation.WorkerThread

class MemoRepository(private val memolistDao: MemoListDao) {

    val allMemos: List<MemoListItem> = memolistDao.getAllMemos()

    @WorkerThread
    suspend fun insert(memolistItem: MemoListItem) {
        memolistDao.insert(memolistItem)
    }

}