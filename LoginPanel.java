import javax.swing.*;
import java.awt.*;


public class LoginPanel extends JPanel {

    private final JTextField     idField;
    private final JPasswordField passField;
    private final JLabel         errorLabel;

    LoginPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);

        JPanel card = AppStyles.cardPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setMaximumSize(new Dimension(380, 999));

        JLabel title = AppStyles.sectionLabel("Login");
        title.setFont(AppStyles.FONT_TITLE);
        title.setAlignmentX(LEFT_ALIGNMENT);

        idField   = AppStyles.styledField();
        passField = AppStyles.styledPassword();

        JPanel grid = new JPanel(new GridLayout(2, 2, 10, 10));
        grid.setBackground(AppStyles.CARD_BG);
        grid.setAlignmentX(LEFT_ALIGNMENT);
        JLabel userLbl = new JLabel("Username / Email / Phone");
        userLbl.setForeground(AppStyles.TEXT);
        grid.add(userLbl);
        grid.add(idField);
        JLabel passLbl = new JLabel("Password");
        passLbl.setForeground(AppStyles.TEXT);
        grid.add(passLbl);
        grid.add(passField);
        for (Component c : grid.getComponents())
            if (c instanceof JLabel) ((JLabel)c).setFont(AppStyles.FONT_LBL);

        errorLabel = new JLabel(" ");
        errorLabel.setFont(AppStyles.FONT_SMALL);
        errorLabel.setForeground(AppStyles.DANGER);
        errorLabel.setAlignmentX(LEFT_ALIGNMENT);

        JButton loginBtn = AppStyles.primaryButton("Login");
        loginBtn.setAlignmentX(LEFT_ALIGNMENT);

        JButton backBtn = AppStyles.linkButton("← Back");
        backBtn.setAlignmentX(LEFT_ALIGNMENT);

        loginBtn.addActionListener(e -> doLogin(app));
        backBtn.addActionListener(e -> app.navigate("WELCOME"));

        passField.addActionListener(e -> doLogin(app));

        card.add(title);
        card.add(Box.createVerticalStrut(20));
        card.add(grid);
        card.add(Box.createVerticalStrut(8));
        card.add(errorLabel);
        card.add(Box.createVerticalStrut(12));
        card.add(loginBtn);
        card.add(Box.createVerticalStrut(8));
        card.add(backBtn);

        JPanel wrap = new JPanel(new GridBagLayout());
        wrap.setBackground(AppStyles.BG);
        wrap.add(card);
        add(wrap, BorderLayout.CENTER);
    }

    private void doLogin(AppFrame app) {
        String id   = idField.getText().trim();
        String pass = new String(passField.getPassword());
        User   u    = AuthSystem.login(id, pass);
        if (u == null) {
            errorLabel.setText("Invalid credentials. Try again.");
            return;
        }
        AppFrame.currentUser = u;
        idField.setText("");
        passField.setText("");
        errorLabel.setText(" ");
        app.navigate("DASHBOARD");
    }
}
