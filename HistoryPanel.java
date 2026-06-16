import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.util.List;


@SuppressWarnings("unused")
public class HistoryPanel extends JPanel implements Refreshable {

    private final DefaultTableModel bmiModel;
    private final DefaultTableModel bmrModel;

    HistoryPanel(AppFrame app) {
        setLayout(new BorderLayout());
        setBackground(AppStyles.BG);
        setBorder(AppStyles.pageBorder());

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppStyles.BG);
        header.setBorder(BorderFactory.createEmptyBorder(0, 0, 14, 0));

        JLabel title = AppStyles.sectionLabel("History");
        title.setFont(AppStyles.FONT_TITLE);

        JButton backBtn = AppStyles.linkButton("← Dashboard");
        backBtn.addActionListener(e -> app.navigate("DASHBOARD"));

        header.add(title,   BorderLayout.WEST);
        header.add(backBtn, BorderLayout.EAST);

        bmiModel = new DefaultTableModel(
            new String[]{"Date", "BMI Value", "Category"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bmiTable = styledTable(bmiModel);
        bmrModel = new DefaultTableModel(
            new String[]{"Date", "BMR (kcal)", "Details"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable bmrTable = styledTable(bmrModel);

        JPanel bmiPanel = tableSection("BMI History", bmiTable);
        JPanel bmrPanel = tableSection("BMR History", bmrTable);

        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, bmiPanel, bmrPanel);
        split.setResizeWeight(0.5);
        split.setBorder(null);
        split.setBackground(AppStyles.BG);

        add(header, BorderLayout.NORTH);
        add(split,  BorderLayout.CENTER);
    }

    public void refresh() {
        User u = AppFrame.currentUser;
        if (u == null) return;

        bmiModel.setRowCount(0);
        for (User.HistoryEntry e : u.getBmiHistory())
            bmiModel.addRow(new Object[]{e.date, String.format("%.2f", e.value), e.note});

        bmrModel.setRowCount(0);
        for (User.HistoryEntry e : u.getBmrHistory())
            bmrModel.addRow(new Object[]{e.date, String.format("%.0f", e.value), e.note});
    }

    
    private static JTable styledTable(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setFont(AppStyles.FONT_BODY);
        t.getTableHeader().setFont(AppStyles.FONT_LBL);
        t.setRowHeight(26);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 0));
        t.setBackground(AppStyles.CARD_BG);
        AppStyles.configureTableAppearance(t);
        return t;
    }

    private static JPanel tableSection(String heading, JTable table) {
        JPanel p = new JPanel(new BorderLayout(0, 8));
        p.setBackground(AppStyles.BG);
        p.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 6));

        JLabel lbl = AppStyles.sectionLabel(heading);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(BorderFactory.createLineBorder(AppStyles.BORDER_C));

        p.add(lbl,    BorderLayout.NORTH);
        p.add(scroll, BorderLayout.CENTER);
        return p;
    }
}
