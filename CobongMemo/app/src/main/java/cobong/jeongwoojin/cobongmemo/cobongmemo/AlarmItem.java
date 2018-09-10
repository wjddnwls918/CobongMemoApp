package cobong.jeongwoojin.cobongmemo.cobongmemo;

public class AlarmItem {

        int hour;
        int minute;
        boolean dayCheck[];

        public AlarmItem(int hour, int minute, boolean dayCheck[]){
                this.hour = hour;
                this.minute = minute;
                this.dayCheck = dayCheck;
        }

        public int getHour() {
                return hour;
        }

        public void setHour(int hour) {
                this.hour = hour;
        }

        public int getMinute() {
                return minute;
        }

        public void setMinute(int minute) {
                this.minute = minute;
        }

        public boolean[] getDayCheck() {
                return dayCheck;
        }

        public void setDayCheck(boolean[] dayCheck) {
                this.dayCheck = dayCheck;
        }
}
