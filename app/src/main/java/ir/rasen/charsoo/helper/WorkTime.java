package ir.rasen.charsoo.helper;

public class WorkTime {

    public int time_open_hour;
    public int time_open_minutes;
    public int time_close_hour;
    public int time_close_minutes;
    public boolean[] workDays = new boolean[7];

    public void setSat(boolean sat) {
        workDays[0] = sat;
    }
    public boolean getSat(){
        return workDays[0];
    }

    public void setSun(boolean sun) {
        workDays[1] = sun;
    }

    public boolean getSun(){
        return workDays[1];
    }
    public void setMon(boolean mon) {
        workDays[2] = mon;
    }
    public boolean getMon(){
        return workDays[2];
    }

    public void setTue(boolean tue) {
        workDays[3] = tue;
    }

    public boolean getTue(){
        return workDays[3];
    }
    public void setWed(boolean wed) {
        workDays[4] = wed;
    }

    public boolean getWed(){
        return workDays[4];
    }
    public void setThr(boolean thr) {
        workDays[5] = thr;
    }
    public boolean getThr(){
        return workDays[5];
    }

    public void setFri(boolean fri) {
        workDays[6] = fri;
    }
    public boolean getFri(){
        return workDays[6];
    }

  /*  public void addWorkDay(int dayOfWeek) throws Exception {
        if (dayOfWeek < 1 || dayOfWeek > 7)
            throw new Exception("Invalid work day");
        workDays[dayOfWeek - 1] = true;
    }*/

    public boolean[] getWorkDays() {
        return this.workDays;
    }

    public String getWorkDaysString() {
        String result = "";
        for (int i = 0; i < 7; i++) {
            if (workDays[i])
                result += String.valueOf(i + 1) + ",";
        }

        return result.substring(0, result.length() - 1);
    }


    public void setWorkDaysFromString(String workDaysString) throws Exception {
        if (workDaysString.contains(",")) {
            String[] workDaysArrays = workDaysString.split(",");
            for (String str : workDaysArrays)
                workDays[Integer.valueOf(str)-1] = true;
        }
    }


    public String getTimeWorkOpenWebservice() {
        String hour;
        String minutes;
        if (time_open_hour < 10)
            hour = "0" + String.valueOf(time_open_hour);
        else
            hour = String.valueOf(time_open_hour);
        if (time_open_minutes < 10)
            minutes = "0" + String.valueOf(time_open_minutes);
        else
            minutes = String.valueOf(time_open_minutes);

        return hour + ":" + minutes;
    }

    public String getTimeWorkCloseWebservice() {
        String hour;
        String minutes;
        if (time_close_hour < 10)
            hour = "0" + String.valueOf(time_close_hour);
        else
            hour = String.valueOf(time_close_hour);
        if (time_close_minutes < 10)
            minutes = "0" + String.valueOf(time_close_minutes);
        else
            minutes = String.valueOf(time_close_minutes);

        return hour + ":" + minutes;
    }


    public void setTimeWorkOpenFromString(String timeOpen) throws Exception {
        if (timeOpen.contains(":")) {
            time_open_hour = Integer.valueOf(timeOpen.substring(0, timeOpen.indexOf(":")));
            time_open_minutes = Integer.valueOf(timeOpen.substring(timeOpen.indexOf(":") + 1, timeOpen.length()));
        } else {
            time_open_hour = 0;
            time_open_minutes = 0;
        }

    }

    public void setTimeWorkCloseFromString(String timeClose) throws Exception {
        if (timeClose.contains(":")) {
            time_close_hour = Integer.valueOf(timeClose.substring(0, timeClose.indexOf(":")));
            time_close_minutes = Integer.valueOf(timeClose.substring(timeClose.indexOf(":") + 1, timeClose.length()));
        } else {
            time_close_hour = 0;
            time_close_minutes = 0;
        }
    }


}
