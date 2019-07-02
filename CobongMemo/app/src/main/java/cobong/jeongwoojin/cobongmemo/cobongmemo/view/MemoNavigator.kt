package cobong.jeongwoojin.cobongmemo.cobongmemo.view

import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem

interface MemoNavigator {
    fun sendMemo(item: MemoListItem) {}
    fun deleteMemo(memo: MemoListItem) {}
    fun editMemo(memo: MemoListItem) {}
}
