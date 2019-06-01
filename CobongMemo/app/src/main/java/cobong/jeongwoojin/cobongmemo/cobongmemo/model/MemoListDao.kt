package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface MemoListDao {

    @Query("SELECT * from MemoListItem order by idx desc")
    fun getAllMemos(): List<MemoListItem>

    @Insert
    fun insert(memo: MemoListItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemos(vararg memos: MemoListItem)

    @Query("delete from MemoListItem")
    fun deleteAll()

}