package cobong.jeongwoojin.cobongmemo.cobongmemo.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MemoListItem implements Parcelable {

    int index;
    String title, subTitle;
    String memoType;
    String inputTime;
    String content;
    String voiceId;
    String handwriteId;

    public MemoListItem(int index, String title, String subTitle, String memoType, String inputTime, String content, String voiceId, String handwriteId) {
        this.index = index;
        this.title = title;
        this.subTitle = subTitle;
        this.memoType = memoType;
        this.inputTime = inputTime;
        this.content = content;
        this.voiceId = voiceId;
        this.handwriteId = handwriteId;
    }

    protected MemoListItem(Parcel in) {
        index = in.readInt();
        title = in.readString();
        subTitle = in.readString();
        memoType = in.readString();
        inputTime = in.readString();
        content = in.readString();
        voiceId = in.readString();
        handwriteId = in.readString();
    }

    public static final Creator<MemoListItem> CREATOR = new Creator<MemoListItem>() {
        @Override
        public MemoListItem createFromParcel(Parcel in) {
            return new MemoListItem(in);
        }

        @Override
        public MemoListItem[] newArray(int size) {
            return new MemoListItem[size];
        }
    };

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getMemoType() {
        return memoType;
    }

    public void setMemoType(String memoType) {
        this.memoType = memoType;
    }

    public String getInputTime() {
        return inputTime;
    }

    public void setInputTime(String inputTime) {
        this.inputTime = inputTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getVoiceId() {
        return voiceId;
    }

    public void setVoiceId(String voiceId) {
        this.voiceId = voiceId;
    }

    public String getHandwriteId() {
        return handwriteId;
    }

    public void setHandwriteId(String handwriteId) {
        this.handwriteId = handwriteId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(index);
        dest.writeString(title);
        dest.writeString(subTitle);
        dest.writeString(memoType);
        dest.writeString(inputTime);
        dest.writeString(content);
        dest.writeString(voiceId);
        dest.writeString(handwriteId);
    }
}
