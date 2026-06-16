import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;


public class AppStyles {

    

    
    public static final Color BG        = new Color(0x0F172A); 
    public static final Color CARD_BG   = new Color(0x1E293B); 

    
    public static final Color PRIMARY   = new Color(0x3B82F6); 
    public static final Color ACCENT    = new Color(0x10B981); 
    public static final Color DANGER    = new Color(0xEF4444); 

    
    public static final Color TEXT      = new Color(0xF8FAFC); 
    public static final Color TEXT_DIM  = new Color(0x94A3B8); 

    
    public static final Color BORDER_C  = new Color(0x334155);

    
    public static final Font FONT_TITLE = new Font("SansSerif", Font.BOLD, 20);
    public static final Font FONT_HEAD  = new Font("SansSerif", Font.BOLD, 14);
    public static final Font FONT_BODY  = new Font("SansSerif", Font.PLAIN, 13);
    public static final Font FONT_SMALL = new Font("SansSerif", Font.PLAIN, 11);
    public static final Font FONT_LBL   = new Font("SansSerif", Font.BOLD, 12);
    
    public static JButton primaryButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_LBL); b.setForeground(Color.WHITE); b.setBackground(PRIMARY);
        b.setOpaque(true); b.setBorderPainted(false); b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(170, 40));
        return b;
    }
    public static JButton linkButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_BODY);
        b.setForeground(PRIMARY);
        b.setContentAreaFilled(false);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    public static JTextField styledField() {
        JTextField f = new JTextField();
        f.setFont(FONT_BODY); f.setBackground(new Color(0x0F172A)); f.setForeground(TEXT); f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C), BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }
    public static JPasswordField styledPassword() {
        JPasswordField f = new JPasswordField();
        f.setFont(FONT_BODY); f.setBackground(new Color(0x0F172A)); f.setForeground(TEXT); f.setCaretColor(Color.WHITE);
        f.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C), BorderFactory.createEmptyBorder(6, 10, 6, 10)));
        return f;
    }
    public static JComboBox<String> styledCombo(String... items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(FONT_BODY);
        c.setBackground(CARD_BG);
        c.setForeground(TEXT);
        return c;
    }

    public static JPanel cardPanel() {
        JPanel p = new JPanel();
        p.setBackground(CARD_BG);
        p.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createLineBorder(BORDER_C, 1), BorderFactory.createEmptyBorder(15, 15, 15, 15)));
        return p;
    }
    public static JButton accentButton(String text) {
        JButton b = new JButton(text);
        b.setFont(FONT_LBL);
        b.setForeground(Color.WHITE);
        b.setBackground(ACCENT);

        b.setOpaque(true);
        b.setBorderPainted(false);
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setPreferredSize(new Dimension(170, 40));
        return b;
    }

    public static Border pageBorder() {
        return BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(BORDER_C, 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        );
    }

    public static JLabel sectionLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_HEAD);
        l.setForeground(TEXT);
        return l;
    }

    public static JLabel styledLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_BODY);
        l.setForeground(TEXT);
        return l;
    }

    public static void configureTableAppearance(javax.swing.JTable table) {
        table.setForeground(TEXT);
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setForeground(TEXT);
        table.getTableHeader().setBackground(new Color(0x334155));
        table.setSelectionBackground(new Color(0x1E3A5F));
        table.setGridColor(new Color(0x475569));
    }
}


