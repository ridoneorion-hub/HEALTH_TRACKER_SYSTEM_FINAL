import javax.swing.*;
import java.awt.*;

public class CalculatorPanel extends JPanel {

    private final JTextField      heightF, weightF, ageF;
    private final JComboBox<String> genderC, activityC;
    private final JLabel          errorLabel;

    CalculatorPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JPanel card = AppStyles.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(440, 999));

        JLabel title = AppStyles.sectionLabel("Health Calculator");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sub = new JLabel("Enter your details to calculate BMI and BMR.");
        sub.setFont(AppStyles.FONT_BODY);
        sub.setForeground(AppStyles.TEXT_DIM);
        sub.setAlignmentX(LEFT_ALIGNMENT);

        heightF   = AppStyles.styledField();
        weightF   = AppStyles.styledField();
        ageF      = AppStyles.styledField();
        genderC   = AppStyles.styledCombo("Male", "Female");
        activityC = AppStyles.styledCombo(
            "Sedentary", "Light", "Moderate", "Active", "Very Active"
        );

        JPanel form = new JPanel(new GridLayout(5, 2, 10, 10));
        form.setBackground(AppStyles.CARD_BG);
        form.setAlignmentX(LEFT_ALIGNMENT);
        addRow(form, "Height (cm)",    heightF);
        addRow(form, "Weight (kg)",    weightF);
        addRow(form, "Age",            ageF);
        addRow(form, "Gender",         genderC);
        addRow(form, "Activity Level", activityC);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppStyles.FONT_SMALL);
        errorLabel.setForeground(AppStyles.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        JButton calcBtn = AppStyles.primaryButton("Calculate");
        JButton backBtn = AppStyles.linkButton("← Dashboard");
        calcBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);

        calcBtn.addActionListener(e -> doCalculate(app));
        backBtn.addActionListener(e -> app.navigate("DASHBOARD"));

        card.add(title);
        card.add(Box.createVerticalStrut(4));
        card.add(sub);
        card.add(Box.createVerticalStrut(18));
        card.add(form);
        card.add(Box.createVerticalStrut(8));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(calcBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(backBtn);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(AppStyles.BG);
        wrap.add(card);
        add(wrap, BorderLayout.CENTER);
    }

    private static void addRow(JPanel grid, String labelText, JComponent field) {
        JLabel l = new JLabel(labelText);
        l.setFont(AppStyles.FONT_LBL);
        l.setForeground(AppStyles.TEXT);
        grid.add(l);
        grid.add(field);
    }

    private void doCalculate(AppFrame app) {
        errorLabel.setText(" ");
        try {
            double height = Double.parseDouble(heightF.getText().trim());
            double weight = Double.parseDouble(weightF.getText().trim());
            int    age    = Integer.parseInt(ageF.getText().trim());
            String gender = genderC.getSelectedIndex() == 0 ? "M" : "F";
            String actKey = activityKey((String) activityC.getSelectedItem());

            User u = AppFrame.currentUser;
            u.setHeight(height);
            u.setWeight(weight);
            u.setAge(age);
            u.setGender(gender);
            u.setActivity(actKey);

            HealthCalculators.run(u);
            app.navigate("RESULT");

        } catch (NumberFormatException ex) {
            errorLabel.setText("Please enter valid numeric values.");
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
        }
    }

    private static String activityKey(String label) {
        if (label == null) return "SEDENTARY";
        switch (label) {
            case "Light":      return "LIGHT";
            case "Moderate":   return "MODERATE";
            case "Active":     return "ACTIVE";
            case "Very Active":return "VERY_ACTIVE";
            default:           return "SEDENTARY";
        }
    }
}
