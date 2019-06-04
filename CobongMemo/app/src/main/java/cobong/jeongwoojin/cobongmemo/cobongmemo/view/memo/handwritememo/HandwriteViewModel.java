package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo;

import androidx.lifecycle.ViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class HandwriteViewModel extends ViewModel {

    private MemoListItem item;
    private HandwriteNavigator navigator;

    public MemoListItem getItem() {
        return item;
    }


    //뒤로가기
    public void onExitClick() {
        navigator.onExitClick();
    }

    //삭제
    public void onDeleteClick() {
        navigator.onDeleteClick();
    }

    public void setItem(MemoListItem item) {
        this.item = item;
    }

    public HandwriteNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(HandwriteNavigator navigator) {
        this.navigator = navigator;
    }
}
