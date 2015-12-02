package org.workjournal;

import java.io.*;
import java.util.*;
import org.javacogs.*;

/**
 * <code>JournalReport</code> is used to run a Journal Report, which looks like
 * this:
 * <pre>
 * Journal Report
 * Mon 01/2/2001 - Tue 01/3/2001
 *
 * Mon 01/02/2001:
 *    07:00 AM - 4:00 PM, Meals: 1.00, Hours: 8.00
 *    Worked my fingers to the bone.
 *
 * Tue 01/03/2001:
 *    07:00 AM - 4:00 PM, Meals: 0.50, Hours: 8.00
 *    Hacked, hacked, and hacked some more. Qwergdeephlak! Hacked, hacked, and
 *    hacked some more. Qwergdeephlak!
 *
 * Totals:
 *    Entries: 2, Meals: 1.50, Hours: 16.00
 * </pre>
 */
public class JournalReport extends Report {
   /**
    * Construct a Journal Report.
    */
   public JournalReport() {
      super("Journal Report");
   }

   //--------------------------------------------------------------------------

   /**
    * Run the report.
    */
   public void run(Journal journal, Hashtable criteria, PrintWriter out) 
         throws IOException {
      // Get criteria fields.
      Date startDate = (Date) criteria.get("startDate");
      Date endDate = (Date) criteria.get("endDate");

      // Init. totals.
      float totalMeals = 0;
      float totalHours = 0;
      int   totalEntries = 0;

      // Print header.
      out.println(getTitle());
      out.println(Formats.DOW_DATE_FORMAT.format(startDate)
          + " - " + Formats.DOW_DATE_FORMAT.format(endDate));

      // Iterate through the journal.
      for (int i = 0; i < journal.count(); i ++) {
         JournalEntry entry = journal.get(i);
         Date workDate = entry.getWorkDate();

         // If the work date of the entry is in the given range, add a line
         // for it.
         if ((workDate.compareTo(startDate) >= 0)
             && (workDate.compareTo(endDate) <= 0)) {
            out.println("");
            
            // Print work date.
            out.println(entry.getWorkDateDowFormatted() + ":");
            
            // Print start, end, meals, hours.
            out.println("   "
                + entry.getStartTime() + " - " + entry.getEndTime()
                + ", Meals: " + entry.getMealsFormatted()
                + ", Hours: " + entry.getHoursFormatted());

            // Print notes, word-wrapped and indented.
            StringBuffer notes = new StringBuffer("   ");
            notes.append(StrUtil.trim(entry.getNotes()));
            StrUtil.wordWrap(notes, 77);
            StrUtil.replace(notes, "\n", "\n   ");
            out.println(notes);

            // Increment totals.
            totalMeals += entry.getMeals();
            totalHours += entry.getHours();
            totalEntries ++;
         }
      }

      // Print the totals.
      out.println("");
      out.println("Totals:");
      out.println("   Entries: " + totalEntries
          + ", Meals: " + Formats.HOURS_FORMAT.format(totalMeals)
          + ", Hours: " + Formats.HOURS_FORMAT.format(totalHours));
   }
}