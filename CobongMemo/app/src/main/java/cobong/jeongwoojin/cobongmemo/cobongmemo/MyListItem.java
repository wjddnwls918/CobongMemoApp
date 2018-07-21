package cobong.jeongwoojin.cobongmemo.cobongmemo;

public class MyListItem {

    int index;
    String title,subTitle;
    String memo_type;
    String input_time;
    String content;
    String voiceId;
    String handwriteId;

    public MyListItem(int index, String title, String subTitle,String content, String memo_type, String input_time,String voiceId, String handwriteId){
        this.index = index;
        this.title = title;
        this.subTitle = subTitle;
        this.content = content;
        this.memo_type = memo_type;
        this. input_time = input_time;
        this.voiceId = voiceId;
        this.handwriteId = handwriteId;
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

    public String getMemo_type() {
        return memo_type;
    }

    public void setMemo_type(String memo_type) {
        this.memo_type = memo_type;
    }

    public String getInput_time() {
        return input_time;
    }

    public void setInput_time(String input_time) {
        this.input_time = input_time;
    }
}
