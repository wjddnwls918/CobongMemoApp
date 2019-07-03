package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MemoListDao {

    @Query("SELECT * from memo order by `index` desc")
    fun getAllMemos(): LiveData<List<MemoItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(memo: MemoItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMemos(vararg memos: MemoItem)

    @Update
    fun updateMemos(vararg memos: MemoItem)

    @Delete
    fun deleteMemos(vararg memos: MemoItem)

    @Delete
    fun deleteMemoNullable(memo: MemoItem?)

    @Query("delete from memo")
    fun deleteAll()

}