package org.workjournal;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import org.javacogs.*;

/**
 * This class is an extension of JTable used for displaying a Journal in a table. It
 * encapsulates the table attributes, such as table column headers, cell renderers,
 * etc. To access a Journal, JournalTable uses a JournalTableModel.
 *
 * @author Thornton Rose
 *
 * @see JournalTableModel
 * @see Journal
 */
public class JournalTable extends JTable {
   /**
    * Create a new journal table.
    */
   public JournalTable() {
      // Turn off border; turn on grid lines.
      setBorder(null);
      setShowHorizontalLines(true);
      setShowVerticalLines(true);

      // Set default header alignment to left.
      JTableHeader th = getTableHeader();
      JLabel hrc = (JLabel) th.getDefaultRenderer();
      hrc.setHorizontalAlignment(JLabel.LEFT);
      hrc.setIcon(new ImageIcon("images/pixel.gif"));
      hrc.setIconTextGap(1);

      // Set resize mode to last column only.
      // (This doesn't work like I think it should. Unless the min. and/or
      // max. width of a column is set, it gets resized anyway.)
      setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);

      // Create cell renderers.
      CellRenderer bgr = new CellRenderer(
         new Color(0xEE, 0xEE, 0xFF), null);
      CellRenderer rightBgr = new CellRenderer(
         new Color(0xEE, 0xEE, 0xFF), null, JLabel.RIGHT);

      // Add columns to table.
      setAutoCreateColumnsFromModel(false);
      addColumn(new SimpleTableColumn(0,  30, "Day",        bgr));
      addColumn(new SimpleTableColumn(1,  70, "Date",       bgr));
      addColumn(new SimpleTableColumn(2,  65, "Start Time", bgr));
      addColumn(new SimpleTableColumn(3,  60, "End Time",   bgr));
      addColumn(new SimpleTableColumn(4,  65, "Meal Time",  bgr));
      addColumn(new SimpleTableColumn(5,  40, "Meals",      rightBgr));
      addColumn(new SimpleTableColumn(6,  40, "Hours",      rightBgr));
      addColumn(new SimpleTableColumn(7, 200, "Notes",      bgr));
   }

   public void refresh() {
      ((AbstractTableModel) getModel()).fireTableDataChanged();
   }
}
