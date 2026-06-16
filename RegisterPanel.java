import javax.swing.*;
import java.awt.*;


public class RegisterPanel extends JPanel {

    private final JTextField     nameF, userF, emailF, phoneF;
    private final JPasswordField passF;
    private final JComboBox<String> goalF;
    private final JLabel         errorLabel;

    RegisterPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JPanel card = AppStyles.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(420, 999));

        JLabel title = AppStyles.sectionLabel("Create Account");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        nameF  = AppStyles.styledField();
        userF  = AppStyles.styledField();
        emailF = AppStyles.styledField();
        phoneF = AppStyles.styledField();
        passF  = AppStyles.styledPassword();
        goalF  = AppStyles.styledCombo("Lose Weight", "Build Muscle", "Maintain Weight", "Improve Fitness");

        JPanel grid = new JPanel(new GridLayout(6, 2, 10, 10));
        grid.setBackground(AppStyles.CARD_BG);
        grid.setAlignmentX(LEFT_ALIGNMENT);
        addRow(grid, "Full Name",  nameF);
        addRow(grid, "Username",   userF);
        addRow(grid, "Email",      emailF);
        addRow(grid, "Phone",      phoneF);
        addRow(grid, "Goal",       goalF);
        addRow(grid, "Password",   passF);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppStyles.FONT_SMALL);
        errorLabel.setForeground(AppStyles.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        JButton regBtn  = AppStyles.accentButton("Register");
        JButton backBtn = AppStyles.linkButton("← Back");
        regBtn.setAlignmentX(LEFT_ALIGNMENT);
        backBtn.setAlignmentX(LEFT_ALIGNMENT);

        regBtn.addActionListener(e -> doRegister(app));
        backBtn.addActionListener(e -> app.navigate("WELCOME"));

        card.add(title);
        card.add(Box.createVerticalStrut(18));
        card.add(grid);
        card.add(Box.createVerticalStrut(8));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(regBtn);
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

    private void doRegister(AppFrame app) {
        String result = AuthSystem.register(
            nameF.getText().trim(), userF.getText().trim(),
            emailF.getText().trim(), phoneF.getText().trim(),
            new String(passF.getPassword()), goalF.getSelectedItem().toString()
        );
        if (result.equals("OK")) {
            JOptionPane.showMessageDialog(this, "Account created! Please login.");
            clearFields();
            app.navigate("LOGIN");
        } else {
            errorLabel.setText(result);
        }
    }

    private void clearFields() {
        nameF.setText(""); userF.setText(""); emailF.setText("");
        phoneF.setText(""); passF.setText(""); goalF.setSelectedIndex(0); errorLabel.setText(" ");
    }
}
