package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.memolist

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.memo.MemoItem

interface MemoNavigator {
    fun sendMemo(item: MemoItem) {}
    fun deleteMemo(memo: MemoItem) {}
    fun editMemo(memo: MemoItem) {}
}
