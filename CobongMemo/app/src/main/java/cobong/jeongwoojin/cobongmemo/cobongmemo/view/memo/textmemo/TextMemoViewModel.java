package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;

public class TextMemoViewModel extends ViewModel {

    private MutableLiveData<MemoListItem> item = new MutableLiveData<>();
    //private MemoListItem item;
    private TextMemoNavigator navigator;

    public TextMemoViewModel() {

    }

    public TextMemoViewModel(MemoListItem item) {
        this.item.setValue(item);
    }

    //메모 수정
    public void onEditClick() {
        navigator.onEditClick();
    }

    //메모 삭제
    public void onDeleteClick() {
        navigator.onDeleteClick();
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

    public MutableLiveData<MemoListItem> getItem() {
        return item;
    }
    public void setItem(MemoListItem item) {
        this.item.setValue(item);
    }
}
