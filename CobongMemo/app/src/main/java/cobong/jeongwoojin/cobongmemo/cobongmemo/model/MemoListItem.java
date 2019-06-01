package cobong.jeongwoojin.cobongmemo.cobongmemo;

public class MyListItem {

    int index;
    String title, subTitle;
    String memoType;
    String inputTime;
    String content;
    String voiceId;
    String handwriteId;

    public MyListItem(int index, String title, String subTitle, String memoType, String inputTime, String content, String voiceId, String handwriteId) {
        this.index = index;
        this.title = title;
        this.subTitle = subTitle;
        this.memoType = memoType;
        this.inputTime = inputTime;
        this.content = content;
        this.voiceId = voiceId;
        this.handwriteId = handwriteId;
    }

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
}
