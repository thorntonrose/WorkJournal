package org.workjournal;

import java.io.*;
import java.text.*;
import java.util.*;
import org.javacogs.*;

/**
 * This class represents a journal entry, which is simply a record about
 * something that has been done, such as a period of contract work, and
 * includes things like work date, start time, end time, etc.
 *
 * @author Thornton Rose
 *
 * @see Journal
 * @see Formats
 */
public class JournalEntry {
   private long    id;
   private Date    workDate = new Date();
   private String  startTime = "";
   private float   startHours;
   private String  endTime = "";
   private float   endHours;
   private String  mealTime = "";
   private float   mealHours;
   private String  notes = "";

   //------------------------------------------------------------------------------------

   /**
    * Construct a new JournalEntry.
    */
   public JournalEntry() {
   }

   /**
    * Construct a new JournalEntry with the given ID.
    */
   public JournalEntry(long id) {
      setId(id);
   }

   //------------------------------------------------------------------------------------

   /**
    * Get entry ID.
    *
    * @return Entry ID.
    */
   public long getId() {
      return id;
   }

   /**
    * Set entry ID.
    *
    * @param id New ID.
    */
   public void setId(long id) {
      this.id = id;
   }

   /**
    * Get the work date. Note that the time part of the work date will always
    * be midnight (00:00:00.00).
    *
    * @return Work date.
    */
   public Date getWorkDate() {
      return workDate;
   }

   /**
    * Get the work date formatted as Formats.DATE_FORMAT.
    *
    * @return Formatted work date.
    */
   public String getWorkDateFormatted() {
      return Formats.DATE_FORMAT.format(getWorkDate());
   }

   /**
    * Get the work date formatted as Formats.DOW_DATE_FORMAT.
    *
    * @return Formatted work date.
    */
   public String getWorkDateDowFormatted() {
      return Formats.DOW_DATE_FORMAT.format(getWorkDate());
   }

   /**
    * Get the work date formatted as Formats.DB_DATE_FORMAT.
    *
    * @return Formatted work date.
    */
   public String getWorkDateDbFormatted() {
      return Formats.DB_DATE_FORMAT.format(getWorkDate());
   }

   /**
    * Get work day of week.
    *
    * @return Work day of week.
    */
   public String getWorkDow() {
      return Formats.DOW_FORMAT.format(getWorkDate());
   }

   /**
    * Set work date to given date. Note that only the time part of the given
    * date will be ignored.
    *
    * @param workDate New work date.
    */
   public void setWorkDate(java.util.Date workDate) {
      this.workDate = workDate;
   }

   /**
    * Set work date from the given string, which is assumed to be formatted
    * according to Formats.DATE_FORMAT.
    *
    * @param workDate New work date.
    */
   public void setWorkDate(String workDate) throws ParseException {
      this.workDate = Formats.DATE_FORMAT.parse(workDate);
   }

   /**
    * Get start time.
    *
    * @return Start time.
    */
   public String getStartTime() {
      return startTime;
   }

   /**
    * Get the start time as 24 hour time (HOURS_TIME_FORMAT).
    *
    * @return Start time in 24 hour format.
    */
   public String getStartTime24Hour() {
      return Formats.HOURS_TIME_FORMAT.format(DateUtil.time(getStartHours()));
   }

   /**
    * Get the start time as hours.
    *
    * @return Start time hours.
    */
   public float getStartHours() {
      return DateUtil.hours(getStartTime(), Formats.TIME_FORMAT);
   }

   /**
    * Get the start time hours formatted as HOURS_FORMAT.
    *
    * @return Formatted start time hours.
    */
   public String getStartHoursFormatted() {
      return Formats.HOURS_FORMAT.format(getStartHours());
   }

   /**
    * Set start time to given time.
    *
    * @param startTime New start time.
    */
   public void setStartTime(String startTime) {
      this.startTime = startTime;
   }

   /**
    * Get end time.
    *
    * @return End time.
    */
   public String getEndTime() {
      return endTime;
   }

   /**
    * Get the end time as 24 hour time (HOURS_TIME_FORMAT).
    *
    * @return End time in 24 hour format.
    */
   public String getEndTime24Hour() {
      return Formats.HOURS_TIME_FORMAT.format(DateUtil.time(getEndHours()));
   }

   /**
    * Get the end time as hours.
    *
    * @return End time hours.
    */
   public float getEndHours() {
      return DateUtil.hours(getEndTime(), Formats.TIME_FORMAT);
   }

   /**
    * Get the end time hours formatted as Formats.HOURS_FORMAT.
    *
    * @return Formatted end time hours.
    */
   public String getEndHoursFormatted() {
      return Formats.HOURS_FORMAT.format(getEndHours());
   }

   /**
    * Set end time to given time.
    *
    * @param endTime New end time.
    */
   public void setEndTime(String endTime) {
      this.endTime = endTime;
   }

   /**
    * Get meal time.
    *
    * @return Meal time.
    */
   public String getMealTime() {
      return mealTime;
   }

   /**
    * Get the meal time as hours.
    *
    * @return Meal time hours.
    */
   public float getMeals() {
      return DateUtil.hours(getMealTime(), Formats.HOURS_TIME_FORMAT);
   }

   /**
    * Get the meal time hours formatted as Formats.HOURS_FORMAT.
    *
    * @return Formatted meal time hours.
    */
   public String getMealsFormatted() {
      return Formats.HOURS_FORMAT.format(getMeals());
   }

   /**
    * Set meal time to given time.
    *
    * @param mealTime New meal time.
    */
   public void setMealTime(String mealTime) {
      this.mealTime = mealTime;
   }

   /**
    * Calculate and return the work hours (start hours - end hours - meal
    * hours)
    *
    * @return Work hours.
    */
   public float getHours() {
      float eh = getEndHours();

      // If end hours is midnight (0h), then use 24h instead.
      if (eh == 0.0f) {
         eh = 24.0f;
      }

      // Calculate hours.
      float h = eh - getStartHours() - getMeals();

      return h;
   }

   /**
    * Get work hours formatted as Formats.HOURS_FORMAT.
    *
    * @return Formatted hours.
    */
   public String getHoursFormatted() {
      return Formats.HOURS_FORMAT.format(getHours());
   }

   /**
    * Get the work notes.
    *
    * @return Work notes.
    */
   public String getNotes() {
      return notes;
   }

   /**
    * Set the work notes to the given notes.
    *
    * @param notes New notes.
    */
   public void setNotes(String notes) {
      this.notes = notes;
   }

   //------------------------------------------------------------------------------------

   /**
    * Determine if this entry is equal to the given object.
    */
   public boolean equals(Object obj) {
      if (obj instanceof JournalEntry) {
         JournalEntry entry = (JournalEntry) obj;
         return (entry.getId() == getId())
            && entry.getWorkDate().equals(getWorkDate())
            && entry.getStartTime().equals(getStartTime())
            && entry.getEndTime().equals(getEndTime())
            && entry.getMealTime().equals(getMealTime())
            && entry.getNotes().equals(getNotes());
      }

      return false;
   }

   /**
    * Clone this entry.
    */
   public Object clone() {
      JournalEntry entry = new JournalEntry();
      entry.setId(getId());
      entry.setWorkDate(getWorkDate());
      entry.setStartTime(getStartTime());
      entry.setEndTime(getEndTime());
      entry.setMealTime(getMealTime());
      entry.setNotes(getNotes());

      return (Object) entry;
   }

   /**
    * Make this entry a copy of the given entry.
    */
   public void copy(JournalEntry entry) {
      setId(entry.getId());
      setWorkDate(entry.getWorkDate());
      setStartTime(entry.getStartTime());
      setEndTime(entry.getEndTime());
      setMealTime(entry.getMealTime());
      setNotes(entry.getNotes());
   }
}
