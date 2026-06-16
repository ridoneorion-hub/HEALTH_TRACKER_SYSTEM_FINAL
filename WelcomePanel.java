import javax.swing.*;
import java.awt.*;


public class WelcomePanel extends JPanel {

    WelcomePanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);


        JPanel centre = new JPanel();
        centre.setLayout(new BoxLayout(centre, BoxLayout.Y_AXIS));
        centre.setBackground(AppStyles.BG);
        centre.setBorder(AppStyles.pageBorder());

        centre.add(Box.createVerticalGlue());

        
        JLabel icon  = new JLabel("❤  Health Tracker", SwingConstants.CENTER);
        icon.setFont(new Font("SansSerif", Font.BOLD, 26));
        icon.setForeground(AppStyles.PRIMARY);
        icon.setAlignmentX(CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Track your BMI, BMR, and diet — all in one place", SwingConstants.CENTER);
        sub.setFont(AppStyles.FONT_BODY);
        sub.setForeground(AppStyles.TEXT_DIM);
        sub.setAlignmentX(CENTER_ALIGNMENT);

        centre.add(icon);
        centre.add(Box.createVerticalStrut(10));
        centre.add(sub);
        centre.add(Box.createVerticalStrut(40));

       
        JButton loginBtn    = AppStyles.primaryButton("Login");
        JButton registerBtn = AppStyles.accentButton("Create Account");
        
        loginBtn.setAlignmentX(CENTER_ALIGNMENT);
        registerBtn.setAlignmentX(CENTER_ALIGNMENT);

        loginBtn.addActionListener(e -> app.navigate("LOGIN"));
        registerBtn.addActionListener(e -> app.navigate("REGISTER"));

        centre.add(loginBtn);
        centre.add(Box.createVerticalStrut(12));
        centre.add(registerBtn);
        centre.add(Box.createVerticalStrut(12));
        
        
      
        centre.add(Box.createVerticalGlue());

        add(centre, BorderLayout.CENTER);
    }
}
