package org.workjournal;

import java.io.*;
import java.util.*;
import org.javacogs.*;

/**
 * <code>TimeReport</code> is used to run a Time Report, which look liks this:
 * <pre>
 * Time Report
 * Mon 01/01/2001 - Wed 01/05/2001
 *
 * Date            Start Time  End Time  Meals  Hours
 * --------------  ----------  --------  -----  ------
 * Mon 01/01/2001  07:00 AM    04:00 PM   1.00    8.00
 * Tue 01/02/2001  07:00 AM    04:00 PM   1.00    8.00
 * Wed 01/03/2001  07:00 AM    04:00 PM   1.00    8.00
 * Thu 01/04/2001  07:00 AM    04:00 PM   1.00    8.00
 * Fri 01/05/2001  07:00 AM    04:00 PM   1.00    8.00
 *                                       -----  ------
 * Totals:                                5.00   40.00
 * </pre>
 */
public class TimeReport extends Report {
   /**
    * Construct a Time Report.
    */
   public TimeReport() {
      super("Time Report");
   }

   //--------------------------------------------------------------------------

   /**
    * Run the time report.
    */
   public void run(Journal journal, Hashtable criteria, PrintWriter out)
         throws IOException {
      // Get criteria fields.
      Date startDate = (Date) criteria.get("startDate");
      Date endDate = (Date) criteria.get("endDate");

      // Init. totals.
      float totalHours = 0;
      float totalMeals = 0;

      // Print the report header.
      out.println(getTitle());
      out.println(Formats.DOW_DATE_FORMAT.format(startDate)
         + " - " +
         Formats.DOW_DATE_FORMAT.format(endDate));

      out.println("");
      out.println("Date            Start Time  End Time  Meals  Hours");
      out.println("--------------  ----------  --------  -----  ------");
      //           Mon 01/01/2001  07:00 AM    04:00 PM   1.00    8.00
      //                                                 -----  ------
      //                                                 99.99  999.99

      // Iterate through the journal.
      for (int i = 0; i < journal.count(); i ++) {
         JournalEntry entry = journal.get(i);
         Date workDate = entry.getWorkDate();

         // If the work date of the entry is in the given range, add a line
         // for it.
         if ((workDate.compareTo(startDate) >= 0)
             && (workDate.compareTo(endDate) <= 0)) {
            // Print line.
            out.println(entry.getWorkDateDowFormatted() + "  "
               + StrUtil.padRight(entry.getStartTime(), 10) + "  "
               + entry.getEndTime() + "  "
               + StrUtil.padLeft(entry.getMealsFormatted(), 5)
               + "  " + StrUtil.padLeft(entry.getHoursFormatted(), 6));

            // Increment totals.
            totalMeals += entry.getMeals();
            totalHours += entry.getHours();
         }
      }

      // Print the totals.
      //           Work Date       Start Time  End Time  Meals  Hours
      //           --------------  ----------  --------  -----  ------
      //           Mon 01/01/2001  07:00 AM    04:00 PM   1.00    8.00
      out.println("                                      -----  ------");
      //                                                 99.99  999.99
      out.println("Totals:                               "
         + StrUtil.padLeft(Formats.HOURS_FORMAT.format(totalMeals), 5)
         + "  "
         + StrUtil.padLeft(Formats.HOURS_FORMAT.format(totalHours), 6));
   }
}
