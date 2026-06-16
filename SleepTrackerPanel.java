import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SleepTrackerPanel extends JPanel implements Refreshable {
    private final List<SleepEntry> records = new ArrayList<>();
    private final JTextField dateField, hoursField;
    private final JTextArea displayArea;
    private static final String FILE_NAME = "sleep_data.txt";

    public SleepTrackerPanel() {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JLabel title = AppStyles.sectionLabel("Sleep Tracker");
        title.setFont(AppStyles.FONT_TITLE);
        title.setForeground(AppStyles.TEXT);
        title.setHorizontalAlignment(SwingConstants.CENTER);

        JButton backButton = AppStyles.linkButton("← DASHBOARD");
        backButton.addActionListener(e -> ((CardLayout) getParent().getLayout()).show(getParent(), "DASHBOARD"));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppStyles.BG);
        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        inputPanel.setBackground(AppStyles.CARD_BG);
        inputPanel.add(AppStyles.styledLabel("Date:"));
        dateField = AppStyles.styledField(); inputPanel.add(dateField);
        inputPanel.add(AppStyles.styledLabel("Sleep Hours:"));
        hoursField = AppStyles.styledField(); inputPanel.add(hoursField);
        JButton addButton = AppStyles.primaryButton("Add Sleep Record");
        JButton averageButton = AppStyles.accentButton("Show Average");
        inputPanel.add(addButton); inputPanel.add(averageButton);
        add(inputPanel, BorderLayout.CENTER);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(AppStyles.CARD_BG);
        displayArea.setForeground(AppStyles.TEXT);
        displayArea.setFont(AppStyles.FONT_BODY);
        JScrollPane scrollPane = new JScrollPane(displayArea);
        scrollPane.setPreferredSize(new Dimension(400, 250));
        add(scrollPane, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addSleepRecord());
        averageButton.addActionListener(e -> showAverageSleep());
        refresh();
    }

    public void refresh() {
        records.clear();
        displayArea.setText("");
        loadRecords();
    }

    private void addSleepRecord() {
        if (AppFrame.currentUser == null) return;
        try {
            double hours = Double.parseDouble(hoursField.getText().trim());
            SleepEntry entry = new SleepEntry(AppFrame.currentUser.getName(), dateField.getText().trim(), hours);
            records.add(entry);
            appendRecord(entry);
            displayArea.append(entry + "\n");
            dateField.setText(""); hoursField.setText("");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid sleep hours");
        }
    }

    private void appendRecord(SleepEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(entry.toFileString());
            bw.newLine();
        } catch (IOException ignored) {}
    }

    private void loadRecords() {
        if (AppFrame.currentUser == null) return;
        String currentUser = AppFrame.currentUser.getName();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 3 || !parts[0].equals(currentUser)) continue;
                SleepEntry entry = new SleepEntry(parts[0], parts[1], Double.parseDouble(parts[2]));
                records.add(entry);
                displayArea.append(entry + "\n");
            }
        } catch (IOException ignored) {}
    }

    private void showAverageSleep() {
        if (AppFrame.currentUser == null) return;
        double total = records.stream().mapToDouble(r -> r.hours).sum();
        double avg = records.isEmpty() ? 0 : total / records.size();
        String message = avg < 6 ? "You are not sleeping enough." : avg <= 8 ? "Your sleep schedule is healthy." : "You may be oversleeping.";
        JOptionPane.showMessageDialog(this, "Average Sleep: " + String.format("%.2f", avg) + " hours\n" + message);
    }

    private static class SleepEntry {
        final String username, date;
        final double hours;
        SleepEntry(String username, String date, double hours) { this.username = username; this.date = date; this.hours = hours; }
        @Override public String toString() { return date + " - " + hours + " hours"; }
        String toFileString() { return String.join(",", username, date, String.valueOf(hours)); }
    }
}
