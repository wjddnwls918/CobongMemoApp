package cobong.jeongwoojin.cobongmemo.cobongmemo;

public class AlarmListItem {

    int index;
    String hour, minute, recurOption, recurRule;

    public AlarmListItem(int index, String hour, String minute, String recurOption, String recurRule) {
        this.index = index;
        this.hour = hour;
        this.minute = minute;
        this.recurOption = recurOption;
        this.recurRule = recurRule;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getRecurOption() {
        return recurOption;
    }

    public void setRecurOption(String recurOption) {
        this.recurOption = recurOption;
    }

    public String getRecurRule() {
        return recurRule;
    }

    public void setRecurRule(String recurRule) {
        this.recurRule = recurRule;
    }
}
