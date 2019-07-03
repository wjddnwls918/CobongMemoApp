package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HandwriteViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoItem? = null
    var navigator: HandwriteNavigator? = null

    //뒤로가기
    fun onExitClick() {
        navigator!!.onExitClick()
    }

    //삭제
    fun onDeleteClick() {
        navigator!!.onDeleteClick()
    }

    //작성 완료
    fun onWriteClick() {
        navigator!!.onWriteClick()
    }

    fun insertHandWriteMemoByRoom(title: String, subTitle: String, handwriteId: String) {
        viewModelScope.launch (Dispatchers.IO){
            MemoRepository.getInstance(getApplication()).insertByRoom(
                MemoItem(
                    index = null,
                    title = title,
                    subTitle = subTitle,
                    memoType = "handwrite",
                    content = null,
                    voiceId = null,
                    handwriteId = handwriteId
                )
            )
        }
    }

    fun deleteHandwriteMemoByRoom() = viewModelScope.launch(Dispatchers.IO) {
        MemoRepository.getInstance(getApplication()).deleteMemoNullableByRoom(item)
    }

}
