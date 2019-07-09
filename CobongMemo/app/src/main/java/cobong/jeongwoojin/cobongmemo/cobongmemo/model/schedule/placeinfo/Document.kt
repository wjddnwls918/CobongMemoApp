package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo

import android.os.Parcel
import android.os.Parcelable

data class Document(
    val id: String,
    val place_name: String,
    val category_name: String,
    val category_group_code: String,
    val category_group_name: String,
    val phone: String,
    val address_name: String,
    val road_address_name: String,
    val x: String,
    val y: String,
    val place_url: String,
    val distance: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
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
        writeString(id)
        writeString(place_name)
        writeString(category_name)
        writeString(category_group_code)
        writeString(category_group_name)
        writeString(phone)
        writeString(address_name)
        writeString(road_address_name)
        writeString(x)
        writeString(y)
        writeString(place_url)
        writeString(distance)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Document> = object : Parcelable.Creator<Document> {
            override fun createFromParcel(source: Parcel): Document = Document(source)
            override fun newArray(size: Int): Array<Document?> = arrayOfNulls(size)
        }
    }
}