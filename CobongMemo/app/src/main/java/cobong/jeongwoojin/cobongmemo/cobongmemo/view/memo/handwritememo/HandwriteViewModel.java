package cobong.jeongwoojin.cobongmemo.cobongmemo.view.memo.handwritememo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoListItem;
import cobong.jeongwoojin.cobongmemo.cobongmemo.model.MemoRepository;

public class HandwriteViewModel extends AndroidViewModel {

    private MemoListItem item;
    private HandwriteNavigator navigator;

    public MemoListItem getItem() {
        return item;
    }

    public HandwriteViewModel(Application application){
        super(application);
    }

    //뒤로가기
    public void onExitClick() {
        navigator.onExitClick();
    }

    //삭제
    public void onDeleteClick() {
        navigator.onDeleteClick();
    }

    //작성 완료
    public void onWriteClick() {
        navigator.onWriteClick();
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

    //insert handwrite memo
    public void insertHandwriteMemo(String title, String subTitle, String handwriteId){
        MemoRepository.Companion.getInstance(getApplication()).insertHandwriteMemo(title,subTitle,handwriteId);
    }

    //delete handwrite memo
    public void deleteHandwriteMemo(int index){
        MemoRepository.Companion.getInstance(getApplication()).deleteTextMemo(index);
    }
}
