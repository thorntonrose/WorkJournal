package org.workjournal;

import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import org.javacogs.*;

/**
 * <code>JournalEntryForm</code> is used to enter or edit a
 * <code>JournalEntry.</code>
 *
 * @author Thornton Rose
 *
 * @see JournalEntry
 */
public class JournalEntryForm extends JFrame {
   public static final int CANCEL = AWTEvent.RESERVED_ID_MAX + 1;
   public static final int SAVE   = AWTEvent.RESERVED_ID_MAX + 2;

   private Vector       listeners = new Vector();
   private Journal      journal;
   private JournalEntry entry;
   private JournalEntry origEntry;
   private boolean      isNew;

   /*
    * UI components [JBuilder]
    */
   private BorderLayout frameLayout = new BorderLayout();
   private BorderLayout mainLayout = new BorderLayout();
   private JPanel mainPanel = new JPanel();
   private JPanel fieldsPanel = new JPanel();
   private JLabel workDateLabel = new JLabel();
   private JTextField workDateField = new JTextField();
   private JLabel startTimeLabel = new JLabel();
   private JTextField startTimeField = new JTextField();
   private JLabel endTimeLabel = new JLabel();
   private JTextField endTimeField = new JTextField();
   private JLabel mealTimeLabel = new JLabel();
   private JTextField mealTimeField = new JTextField();
   private JTextArea notesTextArea = new JTextArea();
   private JScrollPane notesScrollPane = new JScrollPane();
   private GridLayout buttonLayout = new GridLayout();
   private JPanel buttonPanel = new JPanel();
   private BorderLayout controlLayout = new BorderLayout();
   private JButton saveButton = new JButton();
   private JPanel controlPanel = new JPanel();
   private JButton cancelButton = new JButton();

   //------------------------------------------------------------------------------------

   /**
    * Construct a new form.
    */
   public JournalEntryForm() {
      // Init UI components.
      jbInit();
      addListeners();

      // Center on screen.
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(
         (screenSize.width - getWidth()) / 2,
         (screenSize.height - getHeight()) / 2);
   }

   /**
    * Construct a new form with the given parent.
    */
   public JournalEntryForm(Journal journal, JournalEntry entry) {
      this();
      this.journal = journal;
      this.entry = entry;
      this.origEntry = (JournalEntry) entry.clone();

      // Put "(new)" in the title if the entry is new.
      if (entry.getId() == 0) {
         this.isNew = true;
         setTitle(getTitle() + " (new)");
      }

      // Show the journal entry fields.
      workDateField.setText(entry.getWorkDateFormatted());
      startTimeField.setText(entry.getStartTime());
      endTimeField.setText(entry.getEndTime());
      mealTimeField.setText(entry.getMealTime());
      notesTextArea.setText(entry.getNotes());
   }

   //--------------------------------------------------------------------------

   /**
    * Get journal entry.
    */
   public JournalEntry getEntry() {
      return this.entry;
   }

   /*
    * Initialize UI components. [JBuilder]
    */
   private void jbInit() {
      this.setTitle("Journal Entry");
      this.setSize(new Dimension(360, 280));

      workDateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      workDateLabel.setBounds(new Rectangle(0, 4, 65, 17));
      workDateLabel.setText("Work Date:");

      workDateField.setBounds(new Rectangle(69, 2, 103, 21));

      startTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      startTimeLabel.setBounds(new Rectangle(0, 29, 65, 17));
      startTimeLabel.setText("Start Time:");

      startTimeField.setBounds(new Rectangle(69, 27, 103, 21));

      endTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      endTimeLabel.setBounds(new Rectangle(0, 54, 65, 17));
      endTimeLabel.setText("End Time:");

      endTimeField.setBounds(new Rectangle(69, 52, 103, 21));

      mealTimeLabel.setHorizontalAlignment(SwingConstants.RIGHT);
      mealTimeLabel.setBounds(new Rectangle(0, 79, 65, 17));
      mealTimeLabel.setText("Meal Time:");

      mealTimeField.setBounds(new Rectangle(69, 77, 103, 21));

      fieldsPanel.setLayout(null);
      fieldsPanel.setPreferredSize(new Dimension(300, 98));
      fieldsPanel.add(workDateLabel, null);
      fieldsPanel.add(workDateField, null);
      fieldsPanel.add(startTimeLabel, null);
      fieldsPanel.add(endTimeLabel, null);
      fieldsPanel.add(mealTimeLabel, null);
      fieldsPanel.add(startTimeField, null);
      fieldsPanel.add(endTimeField, null);
      fieldsPanel.add(mealTimeField, null);

      saveButton.setMargin(new Insets(0, 0, 0, 0));
      saveButton.setText("Save");

      cancelButton.setMargin(new Insets(0, 0, 0, 0));
      cancelButton.setText("Cancel");

      buttonLayout.setColumns(2);
      buttonLayout.setHgap(5);

      buttonPanel.add(saveButton, null);
      buttonPanel.add(cancelButton, null);

      buttonPanel.setLayout(buttonLayout);
      buttonPanel.setMinimumSize(new Dimension(138, 24));
      buttonPanel.setPreferredSize(new Dimension(138, 24));

      controlPanel.setLayout(controlLayout);
      controlPanel.setMinimumSize(new Dimension(94, 24));
      controlPanel.setPreferredSize(new Dimension(145, 24));

      controlPanel.add(buttonPanel, BorderLayout.EAST);

      notesTextArea.setBorder(null);
      notesTextArea.setLineWrap(true);
      notesTextArea.setWrapStyleWord(true);

      notesScrollPane.getViewport().add(notesTextArea, null);

      mainLayout.setVgap(5);

      mainPanel.setLayout(mainLayout);
      mainPanel.add(fieldsPanel, BorderLayout.NORTH);
      mainPanel.add(notesScrollPane, BorderLayout.CENTER);
      mainPanel.add(controlPanel, BorderLayout.SOUTH);

      this.getContentPane().setLayout(frameLayout);
      this.getContentPane().add(mainPanel, BorderLayout.CENTER);
   }

   /**
    * Add listeners to form components.
    */
   private void addListeners() {
      // Add listener to window.
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            cancel();
         }
      });

      // Add listener to Save button.
      saveButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            save();
         }
      });

      // Add listener to Cancel button.
      cancelButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            cancel();
         }
      });
   }

   /**
    * Update the entry from the fields and save it.
    */
   public void save() {
      Date   workDate;
      String startTime;
      String endTime;
      String mealTime;

      // Parse work date. If parse exception, tell user and return.
      try {
         workDate = Formats.DATE_FORMAT.parse(workDateField.getText().trim());
      } catch(ParseException ex) {
         MessageDialog.showInfo(this, "Work Date format is "
            + Formats.DATE_FORMAT.toPattern() + ".");
         return;
      }

      // Parse start time. If parse exception, tell user and return.
      startTime = startTimeField.getText().trim().toUpperCase();

      if (startTime.length() > 0) {
         startTime = StrUtil.padLeft(startTime, 8, '0');

         try {
            Formats.TIME_FORMAT.parse(startTime);
         } catch(ParseException ex) {
            MessageDialog.showInfo(this, "Start Time format is "
               + Formats.TIME_FORMAT.toPattern() + ".");
            return;
         }
      } else {
         startTime = "12:00 AM";
      }

      // Parse end time. If parse exception, tell user and return.
      endTime = endTimeField.getText().trim().toUpperCase();

      if (endTime.length() > 0) {
         // Check format.
         endTime = StrUtil.padLeft(endTime, 8, '0');

         try {
            Formats.TIME_FORMAT.parse(endTime);
         } catch(ParseException ex) {
            MessageDialog.showInfo(this, "End Time format is "
               + Formats.TIME_FORMAT.toPattern() + ".");
            return;
         }

         // Check range.
         float eh = DateUtil.hours(endTime, Formats.TIME_FORMAT);
         float sh = DateUtil.hours(startTime, Formats.TIME_FORMAT);

         if (eh == 0.0f) {
            eh = 24.0f;
         }

         if (sh > eh) {
            MessageDialog.showInfo(this,
               "End Time must be later than Start Time.");
            return;
         }
      } else {
         endTime = "12:00 AM";
      }

      // Parse meal time. If parse exception, tell user and return.
      mealTime = mealTimeField.getText().trim().toUpperCase();

      if (mealTime.length() > 0) {
         mealTime = StrUtil.padLeft(mealTime, 5, '0');

         try {
            Formats.HOURS_TIME_FORMAT.parse(mealTime);
         } catch(ParseException ex) {
            MessageDialog.showInfo(this, "Meal Time format is "
               + Formats.HOURS_TIME_FORMAT.toPattern() + ".");
            return;
         }
      } else {
         mealTime = "0:00";
      }

      try {
         // Save fields to journal entry.
         entry.setWorkDate(workDate);
         entry.setStartTime(startTime);
         entry.setEndTime(endTime);
         entry.setMealTime(mealTime);
         entry.setNotes(notesTextArea.getText().trim());

         // Store the entry.
         if (isNew) {
            entry.setId(journal.newId());
            journal.add(entry);
         } else {
            journal.update(entry);
         }

         // Notify listeners that the entry was saved and close the window.
         fireActionEvent(SAVE);
         dispose();
      } catch(Exception ex) {
         // Show the error.
         MessageDialog.showError(this, "JournalEntryForm.save", ex);

         // Restore the entry to its original state.
         entry.copy(origEntry);
      }
   }

   /**
    * Close the form and notify listeners that the data was not saved.
    */
   public void cancel() {
      fireActionEvent(CANCEL);
      dispose();
   }

   //--------------------------------------------------------------------------

   /**
    * Add a listener.
    */
   public void addListener(ActionListener listener) {
      listeners.add(listener);
   }

   /**
    * Remove a listener.
    */
   public void removeListener(ActionListener listener) {
      listeners.remove(listener);
   }

   /**
    * Notify the listeners.
    */
   private void fireActionEvent(int eventID) {
      // Create event.
      ActionEvent e = new ActionEvent(this, eventID, null);

      // Get listeners.
      Iterator i = listeners.iterator();

      // Iterate through listeners.
      while (i.hasNext()) {
         // Get listener and dispatch event.
         ActionListener l = (ActionListener) i.next();
         l.actionPerformed(e);
      }
   }
}