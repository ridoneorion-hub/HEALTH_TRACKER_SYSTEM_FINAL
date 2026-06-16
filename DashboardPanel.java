import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel implements Refreshable {

    private final JLabel nameLabel;
    private final JLabel bmiLabel;
    private final JLabel bmiCatLabel;

    DashboardPanel(AppFrame app) {
        setLayout(new BorderLayout(0, 0));
        setBackground(AppStyles.BG);
        setBorder(AppStyles.pageBorder());

        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(AppStyles.BG);
        topBar.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        nameLabel = new JLabel("Welcome!");
        nameLabel.setFont(AppStyles.FONT_TITLE);
        nameLabel.setForeground(AppStyles.TEXT);

        JButton logoutBtn = AppStyles.linkButton("Logout");
        logoutBtn.addActionListener(e -> {
            AppFrame.currentUser = null;
            app.navigate("WELCOME");
        });

        topBar.add(nameLabel,  BorderLayout.WEST);
        topBar.add(logoutBtn,  BorderLayout.EAST);

        bmiLabel    = new JLabel("-");
        bmiLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        bmiLabel.setForeground(AppStyles.PRIMARY);

        bmiCatLabel = new JLabel("-");
        bmiCatLabel.setFont(AppStyles.FONT_BODY);
        bmiCatLabel.setForeground(AppStyles.TEXT_DIM);

        JPanel bmiCard = AppStyles.cardPanel();
        bmiCard.setLayout(new BoxLayout(bmiCard, BoxLayout.Y_AXIS));
        bmiCard.add(AppStyles.sectionLabel("Your BMI"));
        bmiCard.add(Box.createVerticalStrut(4));
        bmiCard.add(bmiLabel);
        bmiCard.add(bmiCatLabel);

        JPanel statsRow = new JPanel(new GridLayout(1, 1, 12, 0));
        statsRow.setBackground(AppStyles.BG);
        statsRow.add(bmiCard);

        JPanel grid = new JPanel(new GridLayout(0, 3, 12, 12));
        grid.setBackground(AppStyles.BG);
        grid.setBorder(BorderFactory.createEmptyBorder(14, 0, 0, 0));

        grid.add(navCard("📊", "Calculate BMI / BMR", () -> app.navigate("CALC")));
        grid.add(navCard("🔥", "Calories Calculator", () -> app.navigate("CALORIE")));
        grid.add(navCard("📜", "History",             () -> app.navigate("HISTORY")));
        grid.add(navCard("🥗", "Diet Plan",           () -> app.navigate("DIET")));
        
        grid.add(navCard("😴", "Sleep Tracker",      () -> app.navigate("SLEEP_TRACKER")));
        grid.add(navCard("🍱", "Meals",              () -> app.navigate("MEALS")));
        grid.add(navCard("💪", "Workouts",           () -> app.navigate("WORKOUTS")));
        grid.add(navCard("ℹ️",  "BMI Info",          () -> showBmiInfo()));

        JPanel centre = new JPanel(new BorderLayout(0, 0));
        centre.setBackground(AppStyles.BG);
        centre.add(statsRow, BorderLayout.NORTH);
        centre.add(grid,     BorderLayout.CENTER);

        add(topBar, BorderLayout.NORTH);
        add(centre, BorderLayout.CENTER);
    }

    public void refresh() {
        User u = AppFrame.currentUser;
        if (u == null) return;
        nameLabel.setText("Welcome, " + u.getName() + "!");
        if (u.getWeight() > 0 && u.getHeight() > 0) {
            double bmi = HealthCalculators.calculateBmi(u);
            bmiLabel.setText(String.format("%.1f", bmi));
            bmiCatLabel.setText(HealthCalculators.category(bmi));
        } else {
            bmiLabel.setText("—");
            bmiCatLabel.setText("Not calculated yet");
        }
    }

    private static JPanel navCard(String icon, String label, Runnable action) {
        JPanel card = AppStyles.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel ic = new JLabel(icon, SwingConstants.CENTER);
        ic.setFont(new Font("SansSerif", Font.PLAIN, 24));
        ic.setAlignmentX(CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(label, SwingConstants.CENTER);
        lbl.setFont(AppStyles.FONT_LBL);
        lbl.setForeground(AppStyles.TEXT);
        lbl.setAlignmentX(CENTER_ALIGNMENT);

        card.add(Box.createVerticalGlue());
        card.add(ic);
        card.add(Box.createVerticalStrut(6));
        card.add(lbl);
        card.add(Box.createVerticalGlue());

        card.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) { action.run(); }
            public void mouseEntered(java.awt.event.MouseEvent e) {
                card.setBackground(new Color(0xEAF1FB));
            }
            public void mouseExited(java.awt.event.MouseEvent e) {
                card.setBackground(AppStyles.CARD_BG);
            }
        });
        return card;
    }

    private void showBmiInfo() {
        String msg = "BMI Categories:\n"
            + "  < 18.5  →  Underweight\n"
            + "  18.5 – 24.9  →  Normal / Healthy\n"
            + "  25.0 – 29.9  →  Overweight\n"
            + "  ≥ 30  →  Obese\n\n"
            + "BMI is a screening tool, not a diagnosis.\n"
            + "Consult a healthcare provider for personalised advice.";
        JOptionPane.showMessageDialog(this, msg, "BMI Information",
                JOptionPane.INFORMATION_MESSAGE);
    }
}
