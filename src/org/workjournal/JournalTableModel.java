package org.workjournal;

import java.beans.*;
import java.sql.*;
import javax.swing.event.*;
import javax.swing.table.*;
import org.javacogs.*;

/**
 * <code>JournalTableModel</code> is a table model that is used by
 * <code>JournalTable</code> to access a <code>Journal</code>, which is
 * a collection of <code>JournalEntry</code> objects.
 *
 * @author Thornton Rose
 *
 * @see JournalTable
 * @see JournalEntry
 */
public class JournalTableModel extends AbstractTableModel {
   private Journal   journal;
   private String[]  columns;
   private ObjectProxy entryProxy;

   //------------------------------------------------------------------------------------

   /**
    * Construct a new journal table model.
    */
   public JournalTableModel(Journal journal) throws IntrospectionException {
      // Save the journal reference.
      this.journal = journal;

      // Create column map.
      columns = new String[] {
         "workDow",
         "workDateFormatted",
         "startTime",
         "endTime",
         "mealTime",
         "mealsFormatted",
         "hoursFormatted",
         "notes" };

      // Create a proxy for journal entries.
      entryProxy = new ObjectProxy(JournalEntry.class);
   }

   //------------------------------------------------------------------------------------

   /**
    * Get the number of rows in the journal.
    */
   public int getRowCount() {
      return journal.count();
   }

   /**
    * Get the number of columns to display.
    */
   public int getColumnCount() {
      return columns.length;
   }

   /**
    * Get the class of the given column.
    */
   public Class getColumnClass(int col) {
      return getValueAt(0, col).getClass();
   }

   /**
    * Get the value at the given row and column. The JournalEntry property for
    * the given column is retrieved from the column-property map, and the entry
    * bean proxy is used to fetch the property value for the entry bean at the
    * given row.
    */
   public Object getValueAt(int row, int col) {
      JournalEntry entry;
      Object       value;

      try {
         entry = journal.get(row);
         entryProxy.setTarget(entry);
         value = entryProxy.get(columns[col]);
      } catch(Exception ex) {
         ex.printStackTrace(System.out);
         return "";
      }

      return value;
   }
}
