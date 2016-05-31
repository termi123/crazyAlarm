package dungpt.crazyalarm.vietitpro.crazyalarm.obj;

/**
 * Created by DungPT17 on 2016/05/17.
 */
public class AlarmItem {

    private String time;
    private String date;
    private int type;
    private boolean isEnable;

    public AlarmItem(String time, String date, int type, boolean isEnable) {
        this.time = time;
        this.date = date;
        this.type = type;
        this.isEnable = isEnable;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }
}
