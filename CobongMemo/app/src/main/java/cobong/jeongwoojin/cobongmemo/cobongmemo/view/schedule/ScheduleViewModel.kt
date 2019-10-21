package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import io.reactivex.disposables.CompositeDisposable


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var navigator: ScheduleNavigator

    val disposable: CompositeDisposable = CompositeDisposable()

    var transDate: String = ""

    //Room
    private var repository: ScheduleRepository

    var allSchedulesByRoomByDate: MutableLiveData<List<ScheduleItem>> = MutableLiveData()

    val allSchedulesByRoom: LiveData<List<ScheduleItem>>

    init {
        repository = ScheduleRepository.getInstance(application)
        allSchedulesByRoom = repository.schedule
    }


    fun onAddScheduleStartClick() {
        navigator.onAddScheduleStartClick()
    }


    fun onScheduleClick(schedule: ScheduleItem) {
        navigator.onScheduleClick(schedule)
    }


    /*  From Room
    *
    *
     */


    fun getAllScheduleByDate(date: String) {
        /*viewModelScope.launch (Dispatchers.IO){
            allSchedulesByRoom = repository.getAllScheduleByDate(date)
        }*/

        disposable.add(
            repository.getAllScheduleByDate(date)
                .subscribe({ data ->

                    if (data.size == 0)
                        allSchedulesByRoomByDate.postValue(listOf())
                    else
                        allSchedulesByRoomByDate.postValue(data)

                    Log.d("checkdate", data.size.toString())

                }, { e ->
                    Log.d("checkdate", "error occured")
                    e.printStackTrace()
                })
        )

    }



}