package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository

class MemoViewModel : ViewModel() {

    var navigator: MemoNavigator? = null
    private val memoRepository: MemoRepository? = null

    internal var allMemos: MutableLiveData<List<MemoItem>>? = null

    fun sendMemo(item: MemoListItem) {
        navigator!!.sendMemo(item)
    }

    fun onDeleteMemoClick(memo: MemoListItem) {
        navigator!!.deleteMemo(memo)
    }

    fun onEditMemoClick(memo: MemoListItem) {
        navigator!!.editMemo(memo)
    }
}
