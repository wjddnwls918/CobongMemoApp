package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.MemoApplication
import cobong.jeongwoojin.cobongmemo.cobongmemo.common.Event
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import io.reactivex.disposables.CompositeDisposable


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    val disposable: CompositeDisposable = CompositeDisposable()

    var transDate: String = ""

    var allSchedulesByRoomByDate: MutableLiveData<MutableList<ScheduleItem>> = MutableLiveData()

    val allSchedulesByRoom: LiveData<List<ScheduleItem>>


    private val _scheduleClickEvent = MutableLiveData<Event<ScheduleItem>>()
    val scheduleClickEvent: LiveData<Event<ScheduleItem>> = _scheduleClickEvent


    private val _addScheduleStartClickEvent = MutableLiveData<Event<Unit>>()
    val addScheduleStartClickEvent: LiveData<Event<Unit>> = _addScheduleStartClickEvent

    init {
        allSchedulesByRoom = MemoApplication.scheduleRepository.schedule
    }


    fun onAddScheduleStartClick() {
        _addScheduleStartClickEvent.value = Event(Unit)
    }


    fun onScheduleClick(schedule: ScheduleItem) {
        _scheduleClickEvent.value = Event(schedule)
    }


    /*  From Room
    *
    *
     */

    fun getAllScheduleByDate(date: String) {
        disposable.add(
            MemoApplication.scheduleRepository.getAllScheduleByDate(date)
                .subscribe({ data ->

                    data.add(
                        0,
                        ScheduleItem(
                            0,
                            "", transDatetoHangul(), "", "", "", "", 0, 0.0, 0.0
                        )
                    )

                    if (data.size == 0)
                        allSchedulesByRoomByDate.postValue(mutableListOf())
                    else
                        allSchedulesByRoomByDate.postValue(data)

                }, { e ->
                    e.printStackTrace()
                })
        )
    }

    fun transDatetoHangul(): String {
        val temp = transDate.split("-")
        var result = ""

        result += (temp[0] + "년 ")
        result += (temp[1] + "월 ")
        result += (temp[2] + "일")

        return result

    }

}