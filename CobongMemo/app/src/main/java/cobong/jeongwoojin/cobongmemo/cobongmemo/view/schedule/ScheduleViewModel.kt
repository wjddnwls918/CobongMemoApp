package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import android.app.Application
import android.util.Log
import androidx.databinding.ObservableField
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import io.reactivex.disposables.CompositeDisposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ScheduleViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var navigator: ScheduleNavigator

    val disposable: CompositeDisposable = CompositeDisposable()
    var isEnd: Boolean = false

    var date: ObservableField<String> = ObservableField()
    var startTime: ObservableField<String> = ObservableField()
    var endTime: ObservableField<String> = ObservableField()
    var place: ObservableField<String> = ObservableField()
    var alarmType: MutableLiveData<Int?> = MutableLiveData()

    var clickDate: ObservableField<String> = ObservableField()

    var placeInfo: MutableLiveData<MutableList<Document>> = MutableLiveData()

    var document: MutableLiveData<Document> = MutableLiveData()


    //Room

    private var repository: ScheduleRepository

    var allSchedulesByRoom: MutableLiveData<List<ScheduleItem>> = MutableLiveData()

    init {
        repository = ScheduleRepository.getInstance(application)
        //allSchedulesByRoom = repository.schedule
    }


    fun onAddScheduleStartClick() {
        navigator.onAddScheduleStartClick()
    }

    fun onScheduleWriteFinishClick() {
        navigator.onScheduleWriteFinishClick()
    }

    fun onSetAlarmClick() {
        navigator.onSetAlarmClick()
    }

    fun onStartTimeSettingClick() {
        navigator.onStartTimeSettingClick()
    }

    fun onEndTimeSettingClick() {
        navigator.onEndTimeSettingClick()
    }

    fun onDateClick() {
        navigator.onDateClick()
    }

    fun onDocumentClick(document: Document) {
        navigator.onDocumentClick(document)
    }

    fun onScheduleClick(schedule: ScheduleItem) {
        Log.d("checkarrive","hihi")
        navigator.onScheduleClick(schedule)
    }

    fun setClickDate(month: String, day: String) {
        clickDate.set(month + "." + day)
    }


    /*  From kakao api
    *
    *
     */
    fun getKeywordPlace(key: String, query: String, page: Int) {

        disposable.add(
            ScheduleRepository.getInstance(getApplication()).getKeywordPlace(key, query, page)
                .subscribe({ data ->

                    if (!isEnd)
                        placeInfo.postValue(data.documents)

                    if (data.meta.is_end)
                        isEnd = true


                }, { e ->
                    e.printStackTrace()
                })
        )


    }


    /*  From Room
    *
    *
     */


    fun inserScheduleByRoom(
        title: String,
        date: String,
        startTime: String,
        endTime: String,
        place: String,
        description: String,
        alarmType: Int?,
        y: Double?,
        x: Double?
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertByRoom(
                ScheduleItem(
                    index = null,
                    title = title,
                    date = date,
                    startTime = startTime,
                    endTime = endTime,
                    place = place,
                    description = description,
                    alarmType = alarmType,
                    y = y,
                    x = x
                )
            )
        }
    }

    fun getAllScheduleByDate(date: String) {
        /*viewModelScope.launch (Dispatchers.IO){
            allSchedulesByRoom = repository.getAllScheduleByDate(date)
        }*/

        disposable.add(
            repository.getAllScheduleByDate(date)
                .subscribe({ data ->

                    if (data.size == 0)
                        allSchedulesByRoom.postValue(listOf())
                    else
                        allSchedulesByRoom.postValue(data)

                    Log.d("checkdate", data.size.toString())

                }, { e ->
                    Log.d("checkdate", "error occured")
                    e.printStackTrace()
                })
        )

    }

}