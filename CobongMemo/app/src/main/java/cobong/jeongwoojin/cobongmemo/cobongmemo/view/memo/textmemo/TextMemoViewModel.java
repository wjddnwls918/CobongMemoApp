package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;

import androidx.lifecycle.ViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class TextMemoViewModel extends ViewModel {

    private MemoListItem item;
    private TextMemoNavigator navigator;

    public TextMemoViewModel() {

    }

    //뒤로가기
    public void onExitClick() {
        navigator.onExitClick();
    }

    //작성완료
    public void onWriteClick() {
        navigator.onWriteClick();
    }

    public TextMemoNavigator getNavigator() {
        return navigator;
    }

    public void setNavigator(TextMemoNavigator navigator) {
        this.navigator = navigator;
    }

    public TextMemoViewModel(MemoListItem item) {
        this.item = item;
    }

    public MemoListItem getItem() {
        return item;
    }

    public void setItem(MemoListItem item) {
        this.item = item;
    }
}
