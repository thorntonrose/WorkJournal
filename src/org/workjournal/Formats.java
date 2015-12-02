package org.workjournal;

import java.text.*;

/**
 * The <code>Formats</code> class contains constants for formatting dates,
 * numbers, etc. into text.
 */
public final class Formats {
   /**
     * Private constructor, so class cannot be instantiated.
     */
   private Formats() {
   }

   //--------------------------------------------------------------------------

   /**
     * Basic date format: MM/dd/yyyy
     */
   public static final SimpleDateFormat DATE_FORMAT =
      new SimpleDateFormat("MM/dd/yyyy");

   /**
     * Day-of-week date format: EEE MM/dd/yyyy
     */
   public static final SimpleDateFormat DOW_DATE_FORMAT =
      new SimpleDateFormat("EEE MM/dd/yyyy");

   /**
     * Day-of-week format: EEE
     */
   public static final SimpleDateFormat DOW_FORMAT =
      new SimpleDateFormat("EEE");

   /**
     * Database date format: yyyy-MM-dd
     */
   public static final SimpleDateFormat DB_DATE_FORMAT =
      new SimpleDateFormat("yyyy-MM-dd");

   /**
    * Basic time format: hh:mm a
    */
   public static final SimpleDateFormat TIME_FORMAT =
      new SimpleDateFormat("hh:mm a");

   /**
    * Hours time format: HH:mm
    */
   public static final SimpleDateFormat HOURS_TIME_FORMAT =
      new SimpleDateFormat("HH:mm");

   /**
    * Hours decimal format: ##0.00
    */
   public static final DecimalFormat HOURS_FORMAT =
      new DecimalFormat("##0.00");
}