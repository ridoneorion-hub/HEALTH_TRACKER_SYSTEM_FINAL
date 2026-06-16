import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class MealTrackerPanel extends JPanel implements Refreshable {
    private final List<MealEntry> meals = new ArrayList<>();
    private final JTextField mealField, caloriesField, proteinField, carbsField, fatField;
    private final JComboBox<String> categoryBox;
    private final JTextArea displayArea;
    private static final String FILE_NAME = "meal_data.txt";

    public MealTrackerPanel() {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JLabel title = AppStyles.sectionLabel("Meal Tracker");
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

        JPanel form = new JPanel(new GridLayout(7, 2, 10, 10));
        form.setBackground(AppStyles.CARD_BG);
        form.add(AppStyles.styledLabel("Meal Name:"));
        mealField = AppStyles.styledField(); form.add(mealField);
        form.add(AppStyles.styledLabel("Category:"));
        categoryBox = AppStyles.styledCombo("Breakfast", "Lunch", "Dinner", "Snack");
        form.add(categoryBox);
        form.add(AppStyles.styledLabel("Calories:")); caloriesField = AppStyles.styledField(); form.add(caloriesField);
        form.add(AppStyles.styledLabel("Protein (g):")); proteinField = AppStyles.styledField(); form.add(proteinField);
        form.add(AppStyles.styledLabel("Carbs (g):")); carbsField = AppStyles.styledField(); form.add(carbsField);
        form.add(AppStyles.styledLabel("Fat (g):")); fatField = AppStyles.styledField(); form.add(fatField);

        JButton addButton = AppStyles.primaryButton("Add Meal");
        JButton analysisButton = AppStyles.accentButton("Nutrition Analysis");
        form.add(addButton); form.add(analysisButton);

        add(form, BorderLayout.CENTER);

        displayArea = new JTextArea();
        displayArea.setEditable(false);
        displayArea.setBackground(AppStyles.CARD_BG);
        displayArea.setForeground(AppStyles.TEXT);
        displayArea.setFont(AppStyles.FONT_BODY);

        JScrollPane scroll = new JScrollPane(displayArea);
        scroll.setPreferredSize(new Dimension(400, 220));
        add(scroll, BorderLayout.SOUTH);

        addButton.addActionListener(e -> addMeal());
        analysisButton.addActionListener(e -> showAnalysis());

        refresh();
    }

    public void refresh() {
        meals.clear();
        displayArea.setText("");
        loadMeals();
    }

    private void addMeal() {
        try {
            if (AppFrame.currentUser == null) return;
            MealEntry meal = new MealEntry(
                AppFrame.currentUser.getName(),
                mealField.getText().trim(),
                categoryBox.getSelectedItem().toString(),
                Integer.parseInt(caloriesField.getText().trim()),
                Integer.parseInt(proteinField.getText().trim()),
                Integer.parseInt(carbsField.getText().trim()),
                Integer.parseInt(fatField.getText().trim())
            );
            meals.add(meal);
            appendMeal(meal);
            displayArea.append(meal + "\n");
            clearFields();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Enter valid values");
        }
    }

    private void appendMeal(MealEntry meal) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_NAME, true))) {
            bw.write(meal.toFileString());
            bw.newLine();
        } catch (IOException ignored) {}
    }

    private void loadMeals() {
        if (AppFrame.currentUser == null) return;
        String currentUser = AppFrame.currentUser.getName();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 7 || !parts[0].equals(currentUser)) continue;
                MealEntry meal = new MealEntry(parts[0], parts[1], parts[2],
                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]),
                    Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
                meals.add(meal);
                displayArea.append(meal + "\n");
            }
        } catch (IOException ignored) {}
    }

    private void showAnalysis() {
        if (AppFrame.currentUser == null) return;
        int calories = meals.stream().mapToInt(m -> m.calories).sum();
        int protein = meals.stream().mapToInt(m -> m.protein).sum();
        int carbs = meals.stream().mapToInt(m -> m.carbs).sum();
        int fat = meals.stream().mapToInt(m -> m.fat).sum();
        String suggestion = calories < 1500 ? "You may need more calories." : calories <= 2500 ? "Your calorie intake looks balanced." : "Your calorie intake is high.";
        JOptionPane.showMessageDialog(this,
            "Nutrition Analysis\n\nCalories: " + calories + "\n" +
            "Protein: " + protein + " g\n" +
            "Carbs: " + carbs + " g\n" +
            "Fat: " + fat + " g\n\n" + suggestion);
    }

    private void clearFields() {
        mealField.setText(""); caloriesField.setText(""); proteinField.setText(""); carbsField.setText(""); fatField.setText("");
    }

    private static class MealEntry {
        final String username, mealName, category;
        final int calories, protein, carbs, fat;
        MealEntry(String username, String mealName, String category, int calories, int protein, int carbs, int fat) {
            this.username = username; this.mealName = mealName; this.category = category;
            this.calories = calories; this.protein = protein; this.carbs = carbs; this.fat = fat;
        }
        @Override public String toString() { return mealName + " | " + category + " | Calories: " + calories; }
        String toFileString() { return String.join(",", username, mealName, category, String.valueOf(calories), String.valueOf(protein), String.valueOf(carbs), String.valueOf(fat)); }
    }
}
