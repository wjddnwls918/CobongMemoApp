package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.textmemo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository;

public class TextMemoViewModel extends AndroidViewModel {

    private MutableLiveData<MemoListItem> item = new MutableLiveData<>();
    private TextMemoNavigator navigator;

    public TextMemoViewModel(Application application) {
        super(application);
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


    public void insertTextMemo(String title, String subTitle, String content) {
        MemoRepository.Companion.getInstance(getApplication()).insertTextMemo(title, subTitle, content);
    }

    public void updateTextMemo(int index, String title, String subTitle, String content) {
        MemoRepository.Companion.getInstance(getApplication()).updateTextMemo(index, title, subTitle, content);
    }

    public void deleteTextMemo() {
        MemoRepository.Companion.getInstance(getApplication()).deleteTextMemo(item.getValue().getIndex());
    }

}
