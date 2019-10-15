package cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "schedule")
data class ScheduleItem(
    @PrimaryKey(autoGenerate = true)
    var index: Int?,
    var title: String,
    var date: String,
    var startTime: String,
    var endTime: String,
    var place: String,
    var description: String,
    var alarmType: Int?,
    var y: Double,
    var x: Double
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readString(),
        source.readValue(Int::class.java.classLoader) as Int?,
        source.readDouble(),
        source.readDouble()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeValue(index)
        writeString(title)
        writeString(date)
        writeString(startTime)
        writeString(endTime)
        writeString(place)
        writeString(description)
        writeValue(alarmType)
        writeDouble(y)
        writeDouble(x)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<ScheduleItem> = object : Parcelable.Creator<ScheduleItem> {
            override fun createFromParcel(source: Parcel): ScheduleItem = ScheduleItem(source)
            override fun newArray(size: Int): Array<ScheduleItem?> = arrayOfNulls(size)
        }
    }
}