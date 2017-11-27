package com.cstu.json.objects;

import java.util.List;

public class ScheduleItem {

    private String date;
    private String dayName;
    private List<ScheduleData> scheduleData;

    public ScheduleItem(String date, String dayName, List<ScheduleData> scheduleData) {
        this.date = date;
        this.dayName = dayName;
        this.scheduleData = scheduleData;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDayName() {
        return dayName;
    }

    public void setDayName(String dayName) {
        this.dayName = dayName;
    }

    public List<ScheduleData> getScheduleData() {
        return scheduleData;
    }

    public void setScheduleData(List<ScheduleData> scheduleData) {
        this.scheduleData = scheduleData;
    }
}
