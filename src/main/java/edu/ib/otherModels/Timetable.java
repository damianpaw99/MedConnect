package edu.ib.otherModels;

import java.time.LocalDate;
import java.time.LocalTime;

public class Timetable {

    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private Integer timeBetween;
    private boolean monday;
    private boolean tuesday;
    private boolean wednesday;
    private boolean thursday;
    private boolean friday;
    private boolean saturday;
    private boolean sunday;
    private Long doctorPesel;

    public LocalDate getStartDateAsDate(){
        return LocalDate.parse(startDate);
    }
    public LocalDate getEndDateAsDate(){
        return LocalDate.parse(endDate);
    }
    public LocalTime getStartTimeAsTime(){
        return LocalTime.parse(startTime);
    }
    public LocalTime getEndTimeAsTime(){
        return LocalTime.parse(endTime);
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTimeBetween() {
        return timeBetween;
    }

    public void setTimeBetween(Integer timeBetween) {
        this.timeBetween = timeBetween;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public Long getDoctorPesel() {
        return doctorPesel;
    }

    public void setDoctorPesel(Long doctorPesel) {
        this.doctorPesel = doctorPesel;
    }


    public boolean isCorrect(){
        if(startDate.equals("")) return false;
        if(endDate.equals("")) return false;
        if(getEndDateAsDate().isBefore(getStartDateAsDate())) return false;
        if(!(monday||tuesday||wednesday||thursday||friday||saturday||sunday)) return false;
        if(doctorPesel==null) return false;
        if(timeBetween<0) return false;
        if(startTime.equals("")) return false;
        if(endTime.equals("")) return false;
        if(getEndTimeAsTime().isBefore(getStartTimeAsTime())) return false;
        return true;
    }
}
