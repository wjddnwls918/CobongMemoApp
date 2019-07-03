package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.util.DateUtil

@Entity(tableName = "memo")
data class MemoItem(
    @PrimaryKey(autoGenerate = true)
    var index: Int?,
    var title: String?,
    var subTitle: String?,
    var memoType: String?,
    var inputTime: String? = DateUtil.curDate(),
    var content: String?,
    var voiceId: String?,
    var handwriteId: String?


) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(index)
        writeString(title)
        writeString(subTitle)
        writeString(memoType)
        writeString(inputTime)
        writeString(content)
        writeString(voiceId)
        writeString(handwriteId)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MemoItem> = object : Parcelable.Creator<MemoItem> {
            override fun createFromParcel(source: Parcel): MemoItem = MemoItem(source)
            override fun newArray(size: Int): Array<MemoItem?> = arrayOfNulls(size)
        }
    }
}