package org.workjournal;

import java.io.*;
import java.sql.*;
import java.text.*;
import java.util.*;
import org.javacogs.*;

/**
 * <code>Journal</code> is a collection of <code>JournalEntry</code> objects.
 * It is persisted in a small database using HypersonicSQL.
 *
 * @author Thornton Rose
 *
 * @see JournalEntry
 */
public class Journal {
   private Connection             conn;
   private Vector                 entries = new Vector();
   private JournalEntryComparator comparator = new JournalEntryComparator();
   
   /**
    * Construct a new Journal.
    */
   public Journal(Connection conn) {
      this.conn = conn;
   }

   //--------------------------------------------------------------------------

   /**
    * Get the journal entry at the given position.
    */
   public JournalEntry get(int row) {
      return (JournalEntry) entries.get(row);
   }

   /**
    * Get the index of the given journal entry.
    */
   public int indexOf(JournalEntry entry) {
      return entries.indexOf(entry);
   }

   /**
    * Get the number of journal entries.
    */
   public int count() {
      return entries.size();
   }

   /**
    * Load the entries from the database.
    */
   public void load() throws SQLException {
      // Clear the entries list.
      entries.clear();

      // Create a statement.
      Statement st = conn.createStatement();

      try {
         // Execute the query.
         ResultSet rs = st.executeQuery("select * from journal");
         
         try {
            // Iterate through the results.
            while (rs.next()) {
               // Create a journal entry.
               JournalEntry entry = new JournalEntry();
               entry.setId(rs.getLong("id"));
               entry.setWorkDate(rs.getDate("workdate"));
               entry.setStartTime(rs.getString("starttime"));
               entry.setEndTime(rs.getString("endtime"));
               entry.setMealTime(rs.getString("mealtime"));
               entry.setNotes(StrUtil.replace(rs.getString("notes"), "''", "'"));

               // Add it to the entries list.
               entries.add(entry);
            }
         } finally {
            // Close the result set.
            rs.close();
         }
      } finally {
         // Close the statement.
         st.close();
      }

      // Sort the entries.
      sort();
   }

   /**
    * Add the given journal entry.
    */
   public void add(JournalEntry entry) throws SQLException {
      // Build insert SQL.      
      StringBuffer sql = new StringBuffer();
      sql.append("insert into journal")
         .append(" (id, workdate, starttime, endtime, mealtime, notes)")
         .append(" values(")
         .append(entry.getId())
         .append(", '").append(entry.getWorkDateDbFormatted())
         .append("', '").append(entry.getStartTime())
         .append("', '").append(entry.getEndTime())
         .append("', '").append(entry.getMealTime())
         .append("', '").append(StrUtil.replace(entry.getNotes(), "'", "''"))
         .append("')");

      // Execute the insert.
      execute(sql.toString());
      
      // Add the entry to the list, then sort.
      entries.add(entry);
      sort();
   }

   /**
    * Update the given journal entry.
    */
   public void update(JournalEntry entry) throws SQLException {
      // Build update SQL.
      StringBuffer sql = new StringBuffer();
      sql.append("update journal set ")
         .append("workDate = '").append(entry.getWorkDateDbFormatted())
         .append("', startTime = '").append(entry.getStartTime())
         .append("', endTime = '").append(entry.getEndTime())
         .append("', mealTime = '").append(entry.getMealTime())
         .append("', notes = '").append(
            StrUtil.replace(entry.getNotes(), "'", "''"))
         .append("' where id = ").append(entry.getId());

      // Execute the update.
      execute(sql.toString());
      
      // Sort the list.
      sort();
   }

   /**
    * Delete the given journal entry.
    */
   public void delete(JournalEntry entry) throws SQLException {
      // Build delete SQL.
      StringBuffer sql = new StringBuffer();
      sql.append("delete from journal where id = ").append(entry.getId());
      
      // Execute the delete.
      execute(sql.toString());

      // Remove the entry from the list.
      entries.remove(entry);
   }
   
   /**
    * Sort the journal entries using a <code>JournalEntryComparator</code>.
    */
   public void sort() {
      Collections.sort(entries, comparator);
   }

   /**
    * Generate a new journal entry ID.
    */
   public synchronized long newId() throws SQLException {
      long seq = -1;

      // Create statement.
      Statement st = conn.createStatement();

      try {
         // Run query to get sequence number.
         ResultSet rs = st.executeQuery("select * from journal_seq");

         try {
            // If found, get seq.
            if (rs.next()) {
               seq = rs.getLong("seq");
            }
         } finally {
            // Close result set.
            rs.close();
         }
      
         // If seq not found, insert seq = 1. Else, increment seq and update
         // record.
         if (seq == -1) {
            seq = 1;
            st.execute("insert into journal_seq (seq) values (" + seq + ")");
         } else {
            seq ++;
            st.execute("update journal_seq set seq = " + seq);
         }
      } finally {
         // Close statement.
         st.close();
      }

      // Return new ID.
      return seq;
   }

   //--------------------------------------------------------------------------
   
   /**
    * Execute the given SQL.
    */
   private void execute(String sql) throws SQLException {
      // Create statement.
      Statement st = conn.createStatement();

      try {
         // Execute SQL.
         st.execute(sql);
      } finally {
         // Close statement.
         st.close();
      }
   }
}
