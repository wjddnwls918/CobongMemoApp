package cobong.jeongwoojin.cobongmemo.cobongmemo.model

import android.os.Parcel
import android.os.Parcelable

class MemoListItem : Parcelable {

    var index: Int = 0
    var title: String? = null
    var subTitle: String? = null
    var memoType: String? = null
    var inputTime: String? = null
    var content: String? = null
    var voiceId: String? = null
    var handwriteId: String? = null

    constructor() {

    }

    constructor(
        index: Int,
        title: String,
        subTitle: String,
        memoType: String,
        inputTime: String,
        content: String,
        voiceId: String,
        handwriteId: String
    ) {
        this.index = index
        this.title = title
        this.subTitle = subTitle
        this.memoType = memoType
        this.inputTime = inputTime
        this.content = content
        this.voiceId = voiceId
        this.handwriteId = handwriteId
    }

    protected constructor(`in`: Parcel) {
        index = `in`.readInt()
        title = `in`.readString()
        subTitle = `in`.readString()
        memoType = `in`.readString()
        inputTime = `in`.readString()
        content = `in`.readString()
        voiceId = `in`.readString()
        handwriteId = `in`.readString()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(index)
        dest.writeString(title)
        dest.writeString(subTitle)
        dest.writeString(memoType)
        dest.writeString(inputTime)
        dest.writeString(content)
        dest.writeString(voiceId)
        dest.writeString(handwriteId)
    }

    companion object {

        val CREATOR: Parcelable.Creator<MemoListItem> = object : Parcelable.Creator<MemoListItem> {
            override fun createFromParcel(`in`: Parcel): MemoListItem {
                return MemoListItem(`in`)
            }

            override fun newArray(size: Int): Array<MemoListItem> {
                return arrayOfNulls(size)
            }
        }
    }
}
