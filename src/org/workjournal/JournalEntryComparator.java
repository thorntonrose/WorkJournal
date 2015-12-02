package org.workjournal;

import java.util.*;

/**
 * <code>EntryComparator</code> is used to compare journal entries when
 * the journal is sorted.
 */
public final class JournalEntryComparator implements Comparator {

   /**
    * Construct a new journal entry comparator.
    */
   public JournalEntryComparator() {
   }

   //------------------------------------------------------------------------------------

   /**
    * Determine if this comparator equals the given object.
    */
   public boolean equals(Object obj) {
      return this.hashCode() == obj.hashCode();
   }

   /**
    * Compare one journal entry to another.
    *
    * @param o1 1st object to compare.
    * @param o2 2nd object to compare.
    *
    * @return 0 if o1 == o2, negative if o1 &lt; o2, positive if o1 &gt; o2.
    */
   public int compare(Object o1, Object o2) {
      /*
      String k1 = key(o1);
      String k2 = key(o2);
      int r = k1.compareTo(k2);
      System.out.println(k1 + " ~ " + k2 + " = " + r);

      return r;
      */
      return key(o1).compareTo(key(o2));
   }

   /**
    * Return key for given object:
    * {workDateDbFormatted}.{startTime24Hour}.{endTime24Hour}
    */
   private String key(Object o) {
      JournalEntry entry = (JournalEntry) o;
      return entry.getWorkDateDbFormatted()
         + "." + entry.getStartTime24Hour()
         + "." + entry.getEndTime24Hour();
   }
}
