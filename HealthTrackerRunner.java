import javax.swing.*;
import java.awt.*;


public class HealthTrackerRunner {
    public static void main(String[] args) {
        AuthSystem.loadData();
        SwingUtilities.invokeLater(() -> new AppFrame().setVisible(true));
    }
}

class AppFrame extends JFrame {
    static User currentUser = null;

 
    private final CardLayout cards = new CardLayout();
    private final JPanel     deck  = new JPanel(cards);

   
    private final WelcomePanel    welcomePanel;
    private final LoginPanel      loginPanel;
    private final RegisterPanel   registerPanel;
    private final DashboardPanel  dashboardPanel;
    private final CalculatorPanel calculatorPanel;
    private final CaloriesCalculatorPanel caloriesPanel;
    private final ResultPanel     resultPanel;
    private final HistoryPanel    historyPanel;
    private final DietPanel       dietPanel;
    private final SleepTrackerPanel sleepTrackerPanel;
    private final MealTrackerPanel mealTrackerPanel;
    private final WorkoutTrackerPanel workoutTrackerPanel;

    AppFrame() {
        super("Health Tracker");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 620);
        setMinimumSize(new Dimension(680, 520));
        setLocationRelativeTo(null);

        welcomePanel    = new WelcomePanel(this);
        loginPanel      = new LoginPanel(this);
        registerPanel   = new RegisterPanel(this);
        dashboardPanel  = new DashboardPanel(this);
        calculatorPanel = new CalculatorPanel(this);
        caloriesPanel   = new CaloriesCalculatorPanel(this);
        resultPanel     = new ResultPanel(this);
        historyPanel    = new HistoryPanel(this);
        dietPanel       = new DietPanel(this);
        sleepTrackerPanel = new SleepTrackerPanel();
        mealTrackerPanel = new MealTrackerPanel();
        workoutTrackerPanel = new WorkoutTrackerPanel();

        deck.setBackground(AppStyles.BG);
        deck.add(welcomePanel,    "WELCOME");
        deck.add(loginPanel,      "LOGIN");
        deck.add(registerPanel,   "REGISTER");
        deck.add(dashboardPanel,  "DASHBOARD");
        deck.add(calculatorPanel, "CALC");
        deck.add(caloriesPanel,   "CALORIE");
        deck.add(resultPanel,     "RESULT");
        deck.add(historyPanel,    "HISTORY");
        deck.add(dietPanel,       "DIET");
        deck.add(sleepTrackerPanel, "SLEEP_TRACKER");
        deck.add(mealTrackerPanel, "MEALS");
        deck.add(workoutTrackerPanel, "WORKOUTS");

        add(deck);
        navigate("WELCOME");
    }

    void navigate(String screen) {
        cards.show(deck, screen);
        switch (screen) {
            case "DASHBOARD": if (currentUser != null) dashboardPanel.refresh(); break;
            case "RESULT":    if (currentUser != null) resultPanel.refresh();    break;
            case "HISTORY":   if (currentUser != null) historyPanel.refresh();   break;
            case "DIET":      if (currentUser != null) dietPanel.refresh();      break;
            case "MEALS":     if (currentUser != null) mealTrackerPanel.refresh(); break;
            case "WORKOUTS":  if (currentUser != null) workoutTrackerPanel.refresh(); break;
        }
    }
}
