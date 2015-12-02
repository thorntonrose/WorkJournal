package org.workjournal;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;
import org.javacogs.*;

/**
 * <code>CellRenderer</code> is used to render JTable cells.
 */
public class CellRenderer extends DefaultTableCellRenderer {
   private Color oddColor;
   private Color evenColor;
   private Font  cellFont = new Font("DialogInput", Font.PLAIN, 12);
   private int   alignment = JLabel.LEFT;

   public CellRenderer() {
   }

   public CellRenderer(Color oddColor, Color evenColor) {
      setOddColor(oddColor);
      setEvenColor(evenColor);
   }

   public CellRenderer(Color oddColor, Color evenColor,
         int alignment) {
      setOddColor(oddColor);
      setEvenColor(evenColor);
      setAlignment(alignment);
   }

   public void setOddColor(Color oddColor) {
      this.oddColor = oddColor;
   }

   public void setEvenColor(Color evenColor) {
      this.evenColor = evenColor;
   }

   public void setAlignment(int alignment) {
      this.alignment = alignment;
   }
   
   /**
    * Return the cell renderer component.
    *
    * Note: The first row of the table is 0.
    */
   public Component getTableCellRendererComponent(
         JTable table, Object value, boolean isSelected,
         boolean hasFocus, int row, int col) {
      // Set cell value.
      setValue(value);

      // Set cell font.
      setFont(table.getFont());

      // Set alignment.
      setHorizontalAlignment(alignment);

      // Set cell background color. If selected, use selection background
      // color. Else, use evenColor for the background of even rows and
      // oddColor for the background of odd rows. If either color is null,
      // the table background color is used instead.
      if (isSelected) {
         setBackground(table.getSelectionBackground());
      } else {
         if ((row % 2) == 0) {
            setBackground(evenColor == null ? table.getBackground() :
               evenColor);
         } else {
            setBackground(oddColor == null ? table.getBackground() :
               oddColor);
         }
      }

      // Return the renderer.
      return this;
   }
}
