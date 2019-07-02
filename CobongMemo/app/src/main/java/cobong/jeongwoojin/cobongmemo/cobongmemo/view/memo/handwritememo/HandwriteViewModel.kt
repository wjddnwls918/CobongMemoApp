package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo

import android.app.Application

import androidx.lifecycle.AndroidViewModel
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository

class HandwriteViewModel(application: Application) : AndroidViewModel(application) {

    var item: MemoListItem? = null
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

    //insert handwrite memo
    fun insertHandwriteMemo(title: String, subTitle: String, handwriteId: String) {
        MemoRepository.getInstance(getApplication())
            .insertHandwriteMemo(title, subTitle, handwriteId)
    }

    //delete handwrite memo
    fun deleteHandwriteMemo(index: Int) {
        MemoRepository.getInstance(getApplication()).deleteTextMemo(index)
    }
}
