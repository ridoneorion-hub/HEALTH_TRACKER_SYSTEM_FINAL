import javax.swing.*;
import java.awt.*;

public class CaloriesCalculatorPanel extends JPanel {
    private final JTextField heightF, weightF, ageF;
    private final JComboBox<String> genderC, activityC, goalC;
    private final JLabel errorLabel;
    private final JPanel resultPanel;

    CaloriesCalculatorPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JPanel card = AppStyles.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(480, 999));

        JLabel title = AppStyles.sectionLabel("Calories Calculator");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Calculate daily calorie needs based on your profile and goal.");
        sub.setFont(AppStyles.FONT_BODY);
        sub.setForeground(AppStyles.TEXT_DIM);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        heightF = AppStyles.styledField();
        weightF = AppStyles.styledField();
        ageF    = AppStyles.styledField();
        genderC = AppStyles.styledCombo("Male", "Female");
        activityC = AppStyles.styledCombo("Sedentary", "Light", "Moderate", "Active", "Very Active");
        goalC = AppStyles.styledCombo("Maintain Weight", "Lose Weight", "Build Muscle", "Improve Fitness");

        JPanel form = new JPanel(new GridLayout(6, 2, 10, 10));
        form.setBackground(AppStyles.CARD_BG);
        form.setAlignmentX(LEFT_ALIGNMENT);
        addRow(form, "Height (cm)", heightF);
        addRow(form, "Weight (kg)", weightF);
        addRow(form, "Age", ageF);
        addRow(form, "Gender", genderC);
        addRow(form, "Activity Level", activityC);
        addRow(form, "Goal", goalC);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppStyles.FONT_SMALL);
        errorLabel.setForeground(AppStyles.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        JButton calcBtn = AppStyles.primaryButton("Calculate Calories");
        JButton backBtn = AppStyles.linkButton("← Dashboard");
        calcBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);

        calcBtn.addActionListener(e -> doCalculate());
        backBtn.addActionListener(e -> app.navigate("DASHBOARD"));

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(16));
        card.add(form);
        card.add(Box.createVerticalStrut(8));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(14));
        card.add(calcBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(backBtn);

        resultPanel = AppStyles.cardPanel();
        resultPanel.setLayout(new BoxLayout(resultPanel, BoxLayout.Y_AXIS));
        resultPanel.setBackground(AppStyles.CARD_BG);
        resultPanel.setAlignmentX(LEFT_ALIGNMENT);
        resultPanel.setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));

        JPanel wrap = new JPanel();
        wrap.setLayout(new BoxLayout(wrap, BoxLayout.Y_AXIS));
        wrap.setBackground(AppStyles.BG);
        wrap.add(card);
        wrap.add(Box.createVerticalStrut(18));
        wrap.add(resultPanel);

        JScrollPane scroll = new JScrollPane(wrap);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);
        add(scroll, BorderLayout.CENTER);
    }

    private static void addRow(JPanel grid, String labelText, JComponent field) {
        JLabel l = new JLabel(labelText);
        l.setFont(AppStyles.FONT_LBL);
        l.setForeground(AppStyles.TEXT);
        grid.add(l);
        grid.add(field);
    }

    private void doCalculate() {
        errorLabel.setText(" ");
        try {
            double height = Double.parseDouble(heightF.getText().trim());
            double weight = Double.parseDouble(weightF.getText().trim());
            int age = Integer.parseInt(ageF.getText().trim());
            String gender = genderC.getSelectedIndex() == 0 ? "M" : "F";
            String activity = activityKey((String) activityC.getSelectedItem());
            String goal = (String) goalC.getSelectedItem();

            User u = AppFrame.currentUser;
            if (u == null) {
                errorLabel.setText("Please login before using this calculator.");
                return;
            }

            u.setHeight(height);
            u.setWeight(weight);
            u.setAge(age);
            u.setGender(gender);
            u.setActivity(activity);
            u.setGoal(goal);

            HealthCalculators.run(u);

            double bmi = HealthCalculators.calculateBmi(u);
            double bmr = HealthCalculators.calculateBmr(u);
            double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(activity));
            double target = HealthCalculators.calorieTarget(tdee, goal);
            DietSuggestion.DietPlan plan = DietSuggestion.generate(bmi, tdee, activity);
            int water = DietSuggestion.waterIntake(weight);

            showResult(goal, bmi, bmr, tdee, target, plan, water);
        } catch (NumberFormatException ex) {
            errorLabel.setText("Please enter valid numeric values.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private void showResult(String goal, double bmi, double bmr, double tdee,
                            double target, DietSuggestion.DietPlan plan, int water) {
        resultPanel.removeAll();

        resultPanel.add(AppStyles.sectionLabel("Calculator Results"));
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(labelLine("Goal", goal));
        resultPanel.add(labelLine("BMI", String.format("%.2f", bmi)));
        resultPanel.add(labelLine("BMR", String.format("%.0f kcal/day", bmr)));
        resultPanel.add(labelLine("TDEE", String.format("%.0f kcal/day", tdee)));
        resultPanel.add(labelLine("Calories Target", String.format("%.0f kcal/day", target)));
        resultPanel.add(labelLine("Daily Calorie Plan", plan.goal));
        resultPanel.add(labelLine("Water", water + " ml/day"));
        resultPanel.add(Box.createVerticalStrut(12));
        resultPanel.add(AppStyles.sectionLabel("Recommended Meal Focus"));
        resultPanel.add(Box.createVerticalStrut(6));
        for (String food : plan.foods) {
            resultPanel.add(textLabel("• " + food));
        }
        resultPanel.add(Box.createVerticalStrut(10));
        resultPanel.add(AppStyles.sectionLabel("Avoid"));
        resultPanel.add(Box.createVerticalStrut(6));
        for (String avoid : plan.avoid) {
            resultPanel.add(textLabel("• " + avoid));
        }

        revalidate();
        repaint();
    }

    private static String activityKey(String label) {
        if (label == null) return "SEDENTARY";
        switch (label) {
            case "Light": return "LIGHT";
            case "Moderate": return "MODERATE";
            case "Active": return "ACTIVE";
            case "Very Active": return "VERY_ACTIVE";
            default: return "SEDENTARY";
        }
    }

    private static JLabel labelLine(String name, String value) {
        JLabel label = new JLabel(String.format("%s: %s", name, value));
        label.setFont(AppStyles.FONT_BODY);
        label.setForeground(AppStyles.TEXT);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }

    private static JLabel textLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(AppStyles.FONT_BODY);
        label.setForeground(AppStyles.TEXT);
        label.setAlignmentX(LEFT_ALIGNMENT);
        return label;
    }
}
