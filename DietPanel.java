import javax.swing.*;
import java.awt.*;


public class DietPanel extends JPanel implements Refreshable {

    DietPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JButton backBtn = AppStyles.linkButton("← Dashboard");
        backBtn.addActionListener(e -> app.navigate("DASHBOARD"));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        btnRow.setBackground(AppStyles.BG);
        btnRow.add(backBtn);
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
        if (u == null || u.getWeight() <= 0) {
            JLabel errorLbl = new JLabel("Please run the calculator first.", SwingConstants.CENTER);
            errorLbl.setForeground(AppStyles.TEXT);
            add(errorLbl, BorderLayout.CENTER);
            revalidate(); repaint(); return;
        }

        double bmi  = u.getWeight() / Math.pow(u.getHeight() / 100.0, 2);
        double bmr  = HealthCalculators.calculateBmr(u);
        double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(u.getActivity()));
        DietSuggestion.DietPlan plan = DietSuggestion.generate(bmi, tdee, u.getActivity());
        int water = DietSuggestion.waterIntake(u.getWeight());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBackground(AppStyles.BG);
        content.setBorder(AppStyles.pageBorder());

        JLabel title = AppStyles.sectionLabel("Your Diet Plan");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);
        content.add(title);
        content.add(Box.createVerticalStrut(6));

        JPanel goalCard = AppStyles.cardPanel();
        goalCard.setLayout(new GridLayout(2, 2, 10, 4));
        goalCard.setBackground(new Color(0xEAF4EC));
        goalCard.setAlignmentX(LEFT_ALIGNMENT);
        goalCard.add(boldLbl("Goal"));
        String userGoal = u.getGoal().isEmpty() ? plan.goal : u.getGoal();
        JLabel goalLbl = new JLabel(userGoal);
        goalLbl.setForeground(new Color(0x1F2937));
        goalCard.add(goalLbl);
        goalCard.add(boldLbl("Daily Calorie Target"));
        JLabel calorieTargetLbl = new JLabel(plan.calories + " kcal  |  Water: " + water + " ml/day");
        calorieTargetLbl.setForeground(new Color(0x1F2937));
        goalCard.add(calorieTargetLbl);

        content.add(goalCard);
        content.add(Box.createVerticalStrut(14));

        content.add(AppStyles.sectionLabel("Daily Meal Plan"));
        content.add(Box.createVerticalStrut(6));
        JPanel mealCard = AppStyles.cardPanel();
        mealCard.setLayout(new BoxLayout(mealCard, BoxLayout.Y_AXIS));
        mealCard.setAlignmentX(LEFT_ALIGNMENT);
        for (String meal : plan.meals) {
            JLabel ml = new JLabel("•  " + meal);
            ml.setFont(AppStyles.FONT_BODY);
            ml.setForeground(AppStyles.TEXT);
            ml.setBorder(BorderFactory.createEmptyBorder(2, 0, 2, 0));
            mealCard.add(ml);
        }
        content.add(mealCard);
        content.add(Box.createVerticalStrut(14));

        JPanel foodsRow = new JPanel(new GridLayout(1, 2, 12, 0));
        foodsRow.setBackground(AppStyles.BG);
        foodsRow.setAlignmentX(LEFT_ALIGNMENT);

        JPanel eatCard = AppStyles.cardPanel();
        eatCard.setLayout(new BoxLayout(eatCard, BoxLayout.Y_AXIS));
        JLabel eatTitle = AppStyles.sectionLabel("✅  Eat More");
        eatTitle.setForeground(AppStyles.ACCENT);
        eatCard.add(eatTitle);
        eatCard.add(Box.createVerticalStrut(8));
        for (String f : plan.foods) {
            JLabel fl = new JLabel("• " + f);
            fl.setFont(AppStyles.FONT_BODY);
            fl.setForeground(AppStyles.TEXT);
            eatCard.add(fl);
        }

        JPanel avoidCard = AppStyles.cardPanel();
        avoidCard.setLayout(new BoxLayout(avoidCard, BoxLayout.Y_AXIS));
        JLabel avoidTitle = AppStyles.sectionLabel("❌  Avoid");
        avoidTitle.setForeground(AppStyles.DANGER);
        avoidCard.add(avoidTitle);
        avoidCard.add(Box.createVerticalStrut(8));
        for (String f : plan.avoid) {
            JLabel fl = new JLabel("• " + f);
            fl.setFont(AppStyles.FONT_BODY);
            fl.setForeground(AppStyles.TEXT);
            avoidCard.add(fl);
        }

        foodsRow.add(eatCard);
        foodsRow.add(avoidCard);
        content.add(foodsRow);
        content.add(Box.createVerticalStrut(14));

        
        JPanel exCard = AppStyles.cardPanel();
        exCard.setLayout(new BoxLayout(exCard, BoxLayout.Y_AXIS));
        exCard.setAlignmentX(LEFT_ALIGNMENT);
        exCard.add(AppStyles.sectionLabel("🏃  Exercise Pairing"));
        exCard.add(Box.createVerticalStrut(6));
        JLabel exLbl = htmlLbl(DietSuggestion.exerciseSuggestion(bmi, u.getActivity()), 500);
        exCard.add(exLbl);
        content.add(exCard);
        content.add(Box.createVerticalStrut(14));

        JPanel tipCard = AppStyles.cardPanel();
        tipCard.setBackground(new Color(0xFFFBEE));
        tipCard.setLayout(new BoxLayout(tipCard, BoxLayout.Y_AXIS));
        tipCard.setAlignmentX(LEFT_ALIGNMENT);
        JLabel tipLbl = htmlLbl("💡 <b>Tip:</b> " + plan.tip, 500);
        tipLbl.setForeground(new Color(0x6B4C00));
        tipCard.add(tipLbl);
        content.add(tipCard);

        JScrollPane scroll = new JScrollPane(content);
        scroll.setBorder(null);
        scroll.getVerticalScrollBar().setUnitIncrement(14);

        add(scroll, BorderLayout.CENTER);
        revalidate(); repaint();
    }

    private static JLabel boldLbl(String text) {
        JLabel l = new JLabel(text);
        l.setFont(AppStyles.FONT_LBL);
        l.setForeground(new Color(0x1F2937));
        return l;
    }

    private static JLabel htmlLbl(String html, int width) {
        JLabel l = new JLabel("<html><body style='width:" + width + "px'>" + html + "</body></html>");
        l.setFont(AppStyles.FONT_BODY);
        l.setForeground(AppStyles.TEXT);
        return l;
    }
}
