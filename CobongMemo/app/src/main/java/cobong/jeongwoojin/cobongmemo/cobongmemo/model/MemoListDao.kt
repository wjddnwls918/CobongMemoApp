package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

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