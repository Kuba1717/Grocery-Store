package com.example.sklep.model;
import java.time.LocalTime;
import java.util.Date;

public class Schedule {
    private int id;
    private int userId;
    private Date dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;

    public Schedule(int userId, Date dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.userId = userId;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Date getDayOfWeek() {
        return dayOfWeek;
    }



    public void setDayOfWeek(Date dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
}

