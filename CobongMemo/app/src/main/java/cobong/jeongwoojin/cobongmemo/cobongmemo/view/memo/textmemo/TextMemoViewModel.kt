package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TextMemoViewModel(application: Application) : AndroidViewModel(application) {

    var item = MutableLiveData<MemoItem>()
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

    fun insertTextMemoByRoom(title: String, subTitle: String, content: String) =
        viewModelScope.launch (Dispatchers.IO){
            MemoRepository.getInstance(getApplication()).insertByRoom(
                MemoItem(
                    index = null,
                    title = title,
                    subTitle = subTitle,
                    memoType = "text",
                    content = content,
                    voiceId = null,
                    handwriteId = null
                )
            )
        }

    fun updateTextMemoByRoom(title: String, subTitle: String, content: String) =
        viewModelScope.launch (Dispatchers.IO){
            MemoRepository.getInstance(getApplication()).updateByRoom(
                MemoItem(
                    index = item.value?.index,
                    title = title,
                    subTitle = subTitle,
                    memoType = item.value?.memoType,
                    content = content,
                    voiceId = item.value?.voiceId,
                    handwriteId = item.value?.handwriteId
                )
            )
        }

    fun deleteTextMemoByRoom() =
        viewModelScope.launch (Dispatchers.IO){
            MemoRepository.getInstance(getApplication()).deleteMemoNullableByRoom(
               item.value
            )
        }

    fun insertTextMemo(title: String, subTitle: String, content: String) {
        //MemoRepository.getInstance(getApplication()).insertTextMemo(title, subTitle, content)
    }

    fun updateTextMemo(index: Int, title: String, subTitle: String, content: String) {
        //MemoRepository.getInstance(getApplication()).updateTextMemo(index, title, subTitle, content)
    }

    fun deleteTextMemo() {
        //MemoRepository.getInstance(getApplication()).deleteTextMemo(item.value!!.index)
    }

}
