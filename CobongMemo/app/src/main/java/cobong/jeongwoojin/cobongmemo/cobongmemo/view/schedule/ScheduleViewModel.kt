package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule

import androidx.databinding.ObservableField
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import io.reactivex.disposables.CompositeDisposable


class ScheduleViewModel : ViewModel() {

    lateinit var navigator: ScheduleNavigator

    var date: ObservableField<String> = ObservableField()
    var startTime: ObservableField<String> = ObservableField()
    var endTime: ObservableField<String> = ObservableField()

    val disposable: CompositeDisposable = CompositeDisposable()

    var placeInfo: MutableLiveData<MutableList<Document>> = MutableLiveData()

    var isEnd: Boolean = false

    var place:ObservableField<String> = ObservableField()

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

    //get from kakao
    fun getKeywordPlace(key: String, query: String, page: Int) {

        disposable.add(
            ScheduleRepository.getInstance().getKeywordPlace(key, query, page)
                .subscribe({ data ->

                    if(!isEnd)
                        placeInfo.postValue(data.documents)

                    if (data.meta.is_end)
                        isEnd = true


                }, { e ->
                    e.printStackTrace()
                })
        )


    }


    fun onDocumentClick(document: Document) {
        navigator.onDocumentClick(document)
    }


}