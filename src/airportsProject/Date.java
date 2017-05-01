package airportsProject;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class Date implements Comparable<Date> {

  private int day;
  private int month;
  private int year;
  private int hour;
  private int minute;
  private int second;

  public Date(int day, int month, int year, int hour, int minute, int second) {
    this.day = day;
    this.month = month;
    this.year = year;
    this.hour = hour;
    this.minute = minute;
    this.second = second;
  }

  public Date() {
    Calendar gregCalendar = new GregorianCalendar();
    this.second = gregCalendar.get(Calendar.SECOND);
    this.minute = gregCalendar.get(Calendar.MINUTE);
    this.hour = gregCalendar.get(Calendar.HOUR_OF_DAY);
    this.day = gregCalendar.get(Calendar.DAY_OF_MONTH);
    this.month = gregCalendar.get(Calendar.MONTH) +1;
    this.year = gregCalendar.get(Calendar.YEAR);
  }

  public boolean isValid(){
      Calendar gregCaelndar = new GregorianCalendar();
      boolean dayIsValid;
      if(this.year >= 0 && this.year <= gregCaelndar.get(Calendar.YEAR)){
          if(this.month >= 1 && this.month <= 12){
              dayIsValid = (isLeapYear(this.year) ? (this.day >= 1 && this.day <= 29) : (this.day >= 1 && this.day <= 28));
              if(dayIsValid){
                  if(this.hour >= 0 && this.hour <= 24 && this.minute >= 0 && this.minute <= 60 && this.second >= 0 && this.second <= 60){
                      return true;
                  }
              }
          }
      }
      return false;
  }

  public int diferenceYears(Date d) {
    int difference = this.year - d.year;
    return Math.abs(difference);
  }

  public int diferenceMoths(Date d) {
    int difference = this.month - d.month+(this.year-d.year) *12;
    return Math.abs(difference);
  }

    public int diferenceDays(Date d) {
        int difference = this.day - d.day;
        return Math.abs(difference);
    }

  public static boolean isLeapYear(int year){
   return ((year%4 == 0) && ((year%100 !=0) || (year%400 == 0)));
  }

  public static int daysMonth (int month, int year){
    switch (month){
      case 4:
      case 6:
      case 9:
      case 11:
        return 30;
      case 2:
        if(isLeapYear(year))
          return 29;
        return 28;
      default:
        return 31;
    }
  }

  public void incrementDate(){
    if(this.month == 12 && this.day == daysMonth(this.month, this.year)){
        this.day = 1;
        this.month = 1;
        this.year++;
    }
    else if(this.day < daysMonth(this.month, this.year)){
      this.day++;
    }else{
      this.day = 1;
      this.month++;
    }
  }

  public boolean beforeDate(Date d) {
    if(this.getYear() < d.year){
      return true;
    }
    if(this.getYear() == d.year && this.getMonth() < d.month){
      return true;
    }
    if(this.getYear() == d.year && this.getMonth() == d.month && this.getDay() < d.day){
      return true;
    }
    if(this.getYear() == d.year && this.getMonth() == d.month && this.getDay() == d.day && this.getHour() < d.hour){
      return true;
    }
    if(this.getYear() == d.year && this.getMonth() == d.month && this.getDay() == d.day && this.getHour() == d.hour && this.getMinute() < d.minute){
      return true;
    }
    if(this.getYear() == d.year && this.getMonth() == d.month && this.getDay() == d.day && this.getHour() == d.hour && this.getMinute() == d.minute && this.getSecond() < d.second){
      return true;
    }
    return false;
  }

  private String monthName(int month){
      switch (month){
          case 1:
              return "January";
          case 2:
              return "February";
          case 3:
              return "March";
          case 4:
              return "April";
          case 5:
              return "May";
          case 6:
              return "June";
          case 7:
              return "July";
          case 8:
              return "August";
          case 9:
              return "September";
          case 10:
              return "October";
          case 11:
              return "November";
          case 12:
              return "December";
          default: return "";
      }
  }

  public boolean equals(Date d) {
    if(this.getYear() == d.year && this.getMonth() == d.month && this.getDay() == d.day && this.getHour() == d.hour && this.getMinute() == d.minute && this.getSecond() == d.second){
      return true;
    }
    return false;
  }

  public String getDuration(){
      return (this.hour > 0 ? this.hour + " hours " : "") +
              (this.minute > 0 ? this.minute + " minutes " : "") +
              (this.second > 0 ? this.second + " seconds " : "");
  }

  public String getDurationLess(){
      return (this.hour > 0 ? this.hour + "h" : "") +
              (this.minute > 0 ? this.minute + "m " : "");
  }

  public String getSlashes(){
      return  this.day + "/" +
              this.month + "/" +
              this.year + "/" +
              this.hour + "/" +
              this.minute + "/" +
              this.second;
  }

  public String getDateLess(){
      return this.day + " " + monthName(this.month) + " " + this.year;
  }

  @Override
  public String toString() {
    return  this.day +
            "/" + this.month +
            "/" + this.year +
            " (" + this.hour +
            "h " + this.minute +
            "m " + this.second +
            "s)";
  }

  public int getDay() {
    return day;
  }

  public void setDay(int day) {
    this.day = day;
  }

  public int getMonth() {
    return month;
  }

  public void setMonth(int month) {
    this.month = month;
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    this.year = year;
  }

  public int getHour() {
    return hour;
  }

  public int getMinute() {
    return minute;
  }

  public int getSecond() {
    return second;
  }

  /**
   *
   * @param o date to compare to
   * @return 0 (if equals) / -1 (this date before another date) / 1 (this date after another date)
   */
  @Override
  public int compareTo(Date o) {
    if(equals(o)){
      return 0;
    }else if( beforeDate(o) ){
      return -1;
    }
    return 1;
  }

}