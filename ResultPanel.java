import javax.swing.*;
import java.awt.*;


public class ResultPanel extends JPanel implements Refreshable {

    ResultPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);


        JButton backBtn = AppStyles.linkButton("← Dashboard");
        backBtn.addActionListener(e -> app.navigate("DASHBOARD"));

        JButton dietBtn = AppStyles.accentButton("View Diet Plan");
        dietBtn.addActionListener(e -> app.navigate("DIET"));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setBackground(AppStyles.BG);
        btnRow.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btnRow.add(backBtn);
        btnRow.add(Box.createHorizontalStrut(12));
        btnRow.add(dietBtn);

        add(btnRow, BorderLayout.SOUTH);

        JLabel placeholder = new JLabel("Loading...", SwingConstants.CENTER);
        placeholder.setForeground(AppStyles.TEXT);
        add(placeholder, BorderLayout.CENTER);
    }

    public void refresh() {
        
        BorderLayout bl = (BorderLayout) getLayout();
        Component old = bl.getLayoutComponent(BorderLayout.CENTER);
        if (old != null) remove(old);

        User u = AppFrame.currentUser;
        if (u == null) { revalidate(); repaint(); return; }

        double bmi  = HealthCalculators.calculateBmi(u);
        double bmr  = HealthCalculators.calculateBmr(u);
        double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(u.getActivity()));

        
        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppStyles.BG);
        content.setBorder(AppStyles.pageBorder());

        JLabel title = AppStyles.sectionLabel("Your Results");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(16));

        JPanel bmiCard = AppStyles.cardPanel();
        bmiCard.setLayout(new GridLayout(2, 2, 14, 6));
        bmiCard.setAlignmentX(LEFT_ALIGNMENT);

        bmiCard.add(boldLabel("BMI"));
        JLabel bmiValue = new JLabel(String.format("%.2f", bmi));
        bmiValue.setForeground(AppStyles.PRIMARY);
        bmiValue.setFont(new Font("SansSerif", Font.BOLD, 16));
        bmiCard.add(bmiValue);
        bmiCard.add(boldLabel("Category"));
        JLabel bmiCategory = new JLabel(HealthCalculators.category(bmi));
        bmiCategory.setForeground(AppStyles.TEXT);
        bmiCard.add(bmiCategory);

        content.add(bmiCard);
        content.add(Box.createVerticalStrut(12));

        JPanel interpCard = AppStyles.cardPanel();
        interpCard.setLayout(new BoxLayout(interpCard, BoxLayout.Y_AXIS));
        interpCard.setAlignmentX(LEFT_ALIGNMENT);

        JLabel interpTitle = AppStyles.sectionLabel("What this means");
        JLabel interpText  = htmlLabel(HealthCalculators.interpret(bmi), 500);
        JLabel riskText    = htmlLabel("<b>Health risks:</b> " + HealthCalculators.riskDetail(bmi), 500);

        for (JLabel l : new JLabel[]{interpText, riskText}) {
            l.setFont(AppStyles.FONT_BODY);
            l.setForeground(AppStyles.TEXT);
            l.setAlignmentX(LEFT_ALIGNMENT);
        }
        interpTitle.setAlignmentX(LEFT_ALIGNMENT);

        interpCard.add(interpTitle);
        interpCard.add(Box.createVerticalStrut(8));
        interpCard.add(interpText);
        interpCard.add(Box.createVerticalStrut(6));
        interpCard.add(riskText);
        content.add(interpCard);
        content.add(Box.createVerticalStrut(12));

        double calorieTarget = HealthCalculators.calorieTarget(tdee, u.getGoal());

        JPanel bmrCard = AppStyles.cardPanel();
        bmrCard.setLayout(new GridLayout(4, 2, 14, 6));
        bmrCard.setAlignmentX(LEFT_ALIGNMENT);

        bmrCard.add(boldLabel("Goal"));
        JLabel goalLabel = new JLabel(u.getGoal().isEmpty() ? "Maintain Weight" : u.getGoal());
        goalLabel.setForeground(AppStyles.TEXT);
        bmrCard.add(goalLabel);
        bmrCard.add(boldLabel("BMR (Base Metabolic Rate)"));
        JLabel bmrValue = new JLabel(String.format("%.0f kcal/day", bmr));
        bmrValue.setForeground(AppStyles.ACCENT);
        bmrValue.setFont(new Font("SansSerif", Font.BOLD, 14));
        bmrCard.add(bmrValue);
        bmrCard.add(boldLabel("Activity Level"));
        JLabel activityLabel = new JLabel(HealthCalculators.activityLabel(u.getActivity()));
        activityLabel.setForeground(AppStyles.TEXT);
        bmrCard.add(activityLabel);
        bmrCard.add(boldLabel("TDEE (Daily Energy Need)"));
        JLabel tdeeLabel = new JLabel(String.format("%.0f kcal/day", tdee));
        tdeeLabel.setForeground(AppStyles.ACCENT);
        tdeeLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bmrCard.add(tdeeLabel);
        bmrCard.add(boldLabel("Calories Target"));
        JLabel targetLabel = new JLabel(String.format("%.0f kcal/day", calorieTarget));
        targetLabel.setForeground(AppStyles.PRIMARY);
        targetLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        bmrCard.add(targetLabel);

        content.add(AppStyles.sectionLabel("Energy Needs"));
        content.add(Box.createVerticalStrut(8));
        content.add(bmrCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        add(scroll, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private static JLabel boldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppStyles.FONT_LBL);
        l.setForeground(AppStyles.TEXT_DIM);
        return l;
    }

    private static JLabel htmlLabel(String html, int width) {
        JLabel l = new JLabel("<html><body style='width:" + width + "px'>" + html + "</body></html>");
        l.setForeground(AppStyles.TEXT);
        return l;
    }
}
