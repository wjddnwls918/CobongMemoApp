package cobong.jeongwoojin.cobongmemo.cobongmemo.view.schedule.placeInfo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.ScheduleRepository
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.schedule.placeinfo.Document
import io.reactivex.disposables.CompositeDisposable

class PlaceInfoViewModel (application: Application) : AndroidViewModel(application) {

    lateinit var navigator: PlaceInfoNavigator
    private val disposable: CompositeDisposable = CompositeDisposable()

    var isEnd: Boolean = false
    var placeInfo: MutableLiveData<MutableList<Document>> = MutableLiveData()

    fun onDocumentClick(document: Document) {
        navigator.onDocumentClick(document)
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


}