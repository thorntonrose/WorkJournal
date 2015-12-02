package org.workjournal;

import java.io.*;
import java.util.*;

/**
 * <code>Report</code> is the base class for reports.
 */
public abstract class Report {
   private String title = "";

   //------------------------------------------------------------------------------------

   /**
    * Create a new report.
    */
   protected Report(String title) {
      setTitle(title);
   }

   //------------------------------------------------------------------------------------

   /**
    * Get title.
    */
   public String getTitle() {
      return this.title;
   }
   
   /**
    * Set title.
    */
   protected void setTitle(String title) {
      this.title = title;
   }
   
   /**
    * Run report.
    */
   public abstract void run(Journal journal, Hashtable criteria, 
         PrintWriter writer) throws IOException;
}