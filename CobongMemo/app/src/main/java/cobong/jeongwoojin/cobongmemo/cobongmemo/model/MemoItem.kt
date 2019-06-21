package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class MemoItem(
    @PrimaryKey(autoGenerate = true)
    var index: Int,
    var title: String,
    var subTitle: String?,
    var memoType: String?,
    var inputType: String?,
    var content: String?,
    var voiceId: String?,
    var handwriteId: String?

)