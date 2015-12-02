package org.workjournal;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;
import javax.swing.*;
import org.javacogs.*;

/**
 * <code>ReportViewer</code> is used to run and view reports.
 */
public class ReportViewer extends JFrame {
   private Report    report;
   private Journal   journal;
   private Hashtable criteria = new Hashtable();

   // UI components [JBuilder]
   private BorderLayout mainBorderLayout = new BorderLayout();
   private JPanel contentPanel = new JPanel();
   private CardLayout contentLayout = new CardLayout();
   private JScrollPane contentScrollPane = new JScrollPane();
   private JTextArea textArea = new JTextArea();
   private JPanel criteriaPanel = new JPanel();
   private JPanel criteriaComponentPanel = new JPanel();
   private JPanel buttonPanel = new JPanel();
   private JTextField endDateField = new JTextField();
   private GridLayout buttonLayout = new GridLayout();
   private JPanel fieldsPanel = new JPanel();
   private JButton runButton = new JButton();
   private JLabel startDateLabel = new JLabel();
   private JLabel endDateLabel = new JLabel();
   private JTextField startDateField = new JTextField();
   private BorderLayout criteriaComponentLayout = new BorderLayout();
   private CardLayout criteriaLayout = new CardLayout();

   //------------------------------------------------------------------------------------

   /**
    * Create a new report form.
    */
   public ReportViewer() {
      // Init UI components.
      jbInit();
      addListeners();

      // Center on screen.
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(
         (screenSize.width - getWidth()) / 2,
         (screenSize.height - getHeight()) / 2);

      // Set date fields.
      startDateField.setText(Formats.DATE_FORMAT.format(new Date()));
      endDateField.setText(startDateField.getText());
   }

   /**
    * Create a new report form.
    */
   public ReportViewer(Journal journal, Report report) {
      this();
      this.journal = journal;
      this.report = report;
      setTitle(report.getTitle());
   }

   //------------------------------------------------------------------------------------

   /**
    * Initialize UI components. [JBuilder]
    */
   public void jbInit() {
      setTitle("Report");
      setSize(new Dimension(595, 580));

      startDateLabel.setBounds(new Rectangle(2, 4, 33, 17));
      startDateLabel.setText("Start:");

      startDateField.setBounds(new Rectangle(36, 2, 70, 21));

      endDateLabel.setBounds(new Rectangle(115, 4, 27, 17));
      endDateLabel.setText("End:");

      endDateField.setBounds(new Rectangle(142, 2, 70, 21));

      fieldsPanel.setLayout(null);
      fieldsPanel.setPreferredSize(new Dimension(10, 28));
      fieldsPanel.add(endDateField, null);
      fieldsPanel.add(startDateLabel, null);
      fieldsPanel.add(startDateField, null);
      fieldsPanel.add(endDateLabel, null);

      runButton.setPreferredSize(new Dimension(29, 22));
      runButton.setMargin(new Insets(0, 0, 0, 0));
      runButton.setText("Run");

      buttonPanel.setLayout(buttonLayout);
      buttonPanel.setMinimumSize(new Dimension(68, 24));
      buttonPanel.setPreferredSize(new Dimension(68, 24));
      buttonPanel.add(runButton, null);

      criteriaComponentPanel.setLayout(criteriaComponentLayout);
      criteriaComponentPanel.setPreferredSize(new Dimension(55, 24));
      criteriaComponentPanel.add(fieldsPanel, BorderLayout.CENTER);
      criteriaComponentPanel.add(buttonPanel, BorderLayout.EAST);

      criteriaLayout.setVgap(2);

      criteriaPanel.setLayout(criteriaLayout);
      criteriaPanel.setPreferredSize(new Dimension(55, 28));
      criteriaPanel.add(criteriaComponentPanel, "jPanel5");

      textArea.setEditable(false);
      textArea.setFont(new Font("Courier", Font.PLAIN, 12));

      contentScrollPane.setBorder(BorderFactory.createEtchedBorder());
      contentScrollPane.setVerticalScrollBarPolicy(
         JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
      contentScrollPane.getViewport().add(textArea, null);

      contentPanel.setLayout(contentLayout);
      contentPanel.add(contentScrollPane, "contentScrollPane");

      this.getContentPane().setLayout(mainBorderLayout);
      this.getContentPane().add(contentPanel, BorderLayout.CENTER);
      this.getContentPane().add(criteriaPanel, BorderLayout.NORTH);
   }

   /**
    * Add listeners to UI components.
    */
   private void addListeners() {
      // Add listener to window.
      addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent event) {
            dispose();
         }
      });

      // Add listener to Run button.
      runButton.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent event) {
            runReport();
         }
      });
   }

   //------------------------------------------------------------------------------------

   /**
    * Run the report. The output is displayed in the text area.
    */
   public void runReport() {
      Date startDate = null;
      Date endDate = null;

      try {
         // Parse the start date.
         try {
            startDate = Formats.DATE_FORMAT.parse(startDateField.getText());
            startDateField.setText(Formats.DATE_FORMAT.format(startDate));
         } catch(ParseException ex) {
            MessageDialog.showInfo(this, "Start must have the form "
               + Formats.DATE_FORMAT.toPattern() + ".");
            return;
         }

         // Parse the end date.
         try {
            endDate = Formats.DATE_FORMAT.parse(endDateField.getText());
            endDateField.setText(Formats.DATE_FORMAT.format(endDate));
         } catch(ParseException ex) {
            MessageDialog.showInfo(this, "End must have the form "
               + Formats.DATE_FORMAT.toPattern() + ".");
            return;
         }

         // Create an output writer.
         StringWriter sw = new StringWriter();
         PrintWriter out = new PrintWriter(sw);

         try {
            // Run the report.
            criteria.put("startDate", startDate);
            criteria.put("endDate", endDate);
            report.run(journal, criteria, out);

            // Show the output.
            textArea.setText(sw.toString());
         } finally {
            // Close the output writer.
            out.close();
         }
      } catch(Exception ex) {
         MessageDialog.showError(this, "ReportViewer.runReport", ex);
      }
   }
 }