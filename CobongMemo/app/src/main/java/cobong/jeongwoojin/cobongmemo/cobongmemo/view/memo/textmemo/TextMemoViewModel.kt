package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository

class TextMemoViewModel(application: Application) : AndroidViewModel(application) {

    var item = MutableLiveData<MemoListItem>()
    var navigator: TextMemoNavigator? = null

    //메모 수정
    fun onEditClick() {
        navigator!!.onEditClick()
    }

    //메모 삭제
    fun onDeleteClick() {
        navigator!!.onDeleteClick()
    }

    //뒤로가기
    fun onExitClick() {
        navigator!!.onExitClick()
    }

    //작성완료
    fun onWriteClick() {
        navigator!!.onWriteClick()
    }

    fun insertTextMemo(title: String, subTitle: String, content: String) {
        MemoRepository.getInstance(getApplication()).insertTextMemo(title, subTitle, content)
    }

    fun updateTextMemo(index: Int, title: String, subTitle: String, content: String) {
        MemoRepository.getInstance(getApplication()).updateTextMemo(index, title, subTitle, content)
    }

    fun deleteTextMemo() {
        MemoRepository.getInstance(getApplication()).deleteTextMemo(item.value!!.index)
    }

}
