package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.os.Parcel
import android.os.Parcelable

data class MemoListItem(

    var index: Int,
    var title: String?,
    var subTitle: String?,
    var memoType: String?,
    var inputTime: String?,
    var content: String?,
    var voiceId: String?,
    var handwriteId: String?

) : Parcelable {

    constructor(source: Parcel) : this(
        source.readInt(),
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
        writeInt(index)
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
        val CREATOR: Parcelable.Creator<MemoListItem> = object : Parcelable.Creator<MemoListItem> {
            override fun createFromParcel(source: Parcel): MemoListItem = MemoListItem(source)
            override fun newArray(size: Int): Array<MemoListItem?> = arrayOfNulls(size)
        }
    }
}
