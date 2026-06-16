import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;


public class WorkoutTrackerPanel extends JPanel implements Refreshable {

    private JTextField dateField;
    private JComboBox<String> typeCombo;
    private JTextField durationField;
    private JTextField caloriesField;
    private JTextField notesField;
    private JTextArea displayArea;

    public WorkoutTrackerPanel() {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JLabel title = new JLabel("💪 Workout Tracker", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 22));
        title.setForeground(AppStyles.TEXT);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(AppStyles.BG);

        JButton backButton = AppStyles.linkButton("← DASHBOARD");
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) getParent().getLayout();
            cl.show(getParent(), "DASHBOARD");
        });

        topPanel.add(backButton, BorderLayout.WEST);
        topPanel.add(title, BorderLayout.CENTER);
        add(topPanel, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBackground(AppStyles.CARD_BG);

        JLabel dateLabel = AppStyles.styledLabel("Date (YYYY-MM-DD):");
        form.add(dateLabel);
        dateField = AppStyles.styledField();
        form.add(dateField);

        
        JLabel typeLabel = AppStyles.styledLabel("Workout Type:");
        form.add(typeLabel);
        typeCombo = AppStyles.styledCombo("Running", "Cycling", "Gym", "Yoga", "Swimming", "Sports", "Walking", "Other");
        form.add(typeCombo);

      
        JLabel durationLabel = AppStyles.styledLabel("Duration (minutes):");
        form.add(durationLabel);
        durationField = AppStyles.styledField();
        form.add(durationField);

       
        JLabel caloriesLabel = AppStyles.styledLabel("Calories Burned:");
        form.add(caloriesLabel);
        caloriesField = AppStyles.styledField();
        form.add(caloriesField);

        
        JLabel notesLabel = AppStyles.styledLabel("Notes:");
        form.add(notesLabel);
        notesField = AppStyles.styledField();
        form.add(notesField);

        add(form, BorderLayout.CENTER);

        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(AppStyles.BG);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(AppStyles.CARD_BG);
        displayArea.setForeground(AppStyles.TEXT);
        displayArea.setFont(AppStyles.FONT_BODY);

        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setPreferredSize(new Dimension(400, 200));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        buttonPanel.setBackground(AppStyles.BG);

        JButton addButton = AppStyles.primaryButton("➕ Add Workout");
        JButton statsButton = AppStyles.accentButton("📈 View Stats");

        addButton.addActionListener(e -> addWorkout());
        statsButton.addActionListener(e -> showWorkoutStats());

        buttonPanel.add(addButton);
        buttonPanel.add(statsButton);

        bottomPanel.add(buttonPanel, BorderLayout.NORTH);
        bottomPanel.add(scroll, BorderLayout.CENTER);

        add(bottomPanel, BorderLayout.SOUTH);

        loadWorkouts();
    }

    private void addWorkout() {
        try {
            User u = AppFrame.currentUser;
            if (u == null) return;

            String date = dateField.getText().trim();
            String type = (String) typeCombo.getSelectedItem();
            int duration = Integer.parseInt(durationField.getText().trim());
            int calories = Integer.parseInt(caloriesField.getText().trim());
            String notes = notesField.getText().trim();

            if (date.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter a date (YYYY-MM-DD format)");
                return;
            }

            User.WorkoutEntry workout = new User.WorkoutEntry(date, type, duration, calories, notes);
            u.addWorkout(workout);

            displayArea.append(String.format("[%s] %s - %d min | %d kcal | %s\n", date, type, duration, calories, notes));

            clearFields();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers for duration and calories");
        }
    }

    private void showWorkoutStats() {
        User u = AppFrame.currentUser;
        if (u == null) return;

        java.util.List<User.WorkoutEntry> workouts = new ArrayList<>(u.getWorkouts());
        if (workouts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No workouts recorded yet!");
            return;
        }

        int totalWorkouts = workouts.size();
        int totalMinutes = workouts.stream().mapToInt(w -> w.durationMinutes).sum();
        int totalCalories = workouts.stream().mapToInt(w -> w.caloriesBurned).sum();
        double avgDuration = totalMinutes / (double) totalWorkouts;
        double avgCalories = totalCalories / (double) totalWorkouts;

        String stats = String.format(
            "📊 Workout Statistics\n\n" +
            "Total Workouts: %d\n" +
            "Total Time: %d minutes\n" +
            "Total Calories: %d\n" +
            "Avg Duration: %.1f min\n" +
            "Avg Calories: %.0f",
            totalWorkouts, totalMinutes, totalCalories, avgDuration, avgCalories
        );

        JOptionPane.showMessageDialog(this, stats, "Workout Stats", JOptionPane.INFORMATION_MESSAGE);
    }

    private void loadWorkouts() {
        User u = AppFrame.currentUser;
        if (u == null) return;

        for (User.WorkoutEntry w : u.getWorkouts()) {
            displayArea.append(String.format("[%s] %s - %d min | %d kcal | %s\n",
                w.date, w.type, w.durationMinutes, w.caloriesBurned, w.notes));
        }
    }

    private void clearFields() {
        dateField.setText("");
        durationField.setText("");
        caloriesField.setText("");
        notesField.setText("");
        typeCombo.setSelectedIndex(0);
    }

    public void refresh() {
        displayArea.setText("");
        loadWorkouts();
    }
}
