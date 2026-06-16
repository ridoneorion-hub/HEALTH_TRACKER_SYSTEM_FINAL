import java.time.LocalDate;

public class HealthCalculators {

    public static void run(User user) {
        double bmi  = calculateBmi(user);
        double bmr  = calculateBmr(user);
        double tdee = calculateTdee(bmr, multiplier(user.getActivity()));

        user.addBmiHistory(new User.HistoryEntry(
            LocalDate.now().toString(), "BMI", bmi, category(bmi)
        ));
        user.addBmrHistory(new User.HistoryEntry(
            LocalDate.now().toString(), "BMR", bmr,
            String.format("TDEE=%.0f Activity=%s", tdee, user.getActivity())
        ));

        try { FileHandler.saveUsers(AuthSystem.getUsers()); }
        catch (Exception ignored) {}
    }

    public static double calculateBmi(User user) {
        return calculateBmi(user.getWeight(), user.getHeight());
    }

    public static double calculateBmi(double weight, double height) {
        return weight / Math.pow(height / 100.0, 2);
    }

    public static double calculateBmr(User user) {
        return user.getGender().equals("M")
            ? (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) + 5
            : (10 * user.getWeight()) + (6.25 * user.getHeight()) - (5 * user.getAge()) - 161;
    }

    public static double calculateTdee(double bmr, double activityMultiplier) {
        return bmr * activityMultiplier;
    }

    public static double calorieTarget(double tdee, String goal) {
        if (goal == null) return tdee;
        switch (goal.toUpperCase()) {
            case "LOSE WEIGHT":
                return tdee - 500;
            case "BUILD MUSCLE":
                return tdee + 300;
            case "MAINTAIN WEIGHT":
                return tdee;
            case "IMPROVE FITNESS":
                return tdee + 100;
            default:
                return tdee;
        }
    }

    public static double multiplier(String act) {
        switch (act) {
            case "LIGHT":       return 1.375;
            case "MODERATE":    return 1.55;
            case "ACTIVE":      return 1.725;
            case "VERY_ACTIVE": return 1.9;
            default:            return 1.2;
        }
    }

    public static String activityLabel(String act) {
        switch (act) {
            case "LIGHT":       return "Light (1-3 days/week)";
            case "MODERATE":    return "Moderate (3-5 days/week)";
            case "ACTIVE":      return "Active (6-7 days/week)";
            case "VERY_ACTIVE": return "Very Active (hard exercise)";
            default:            return "Sedentary (little exercise)";
        }
    }

    public static String category(double bmi) {
        if (bmi < 18.5) return "Underweight";
        if (bmi < 25.0) return "Normal / Healthy";
        if (bmi < 30.0) return "Overweight";
        return "Obese";
    }

    public static String interpret(double bmi) {
        if (bmi < 18.5)
            return "You are underweight. Your body may lack essential nutrients. "
                 + "Focus on nutrient-dense foods and consult a healthcare provider.";
        if (bmi < 25.0)
            return "Great job! Your weight is in the healthy range. "
                 + "Maintain your current lifestyle with balanced diet and regular exercise.";
        if (bmi < 30.0)
            return "You are slightly overweight. Small changes in diet and increasing "
                 + "physical activity can bring you back to a healthy range.";
        return "Your BMI indicates obesity. This raises the risk of heart disease, "
             + "diabetes, and joint problems. A structured plan with a doctor is recommended.";
    }

    public static String riskDetail(double bmi) {
        if (bmi < 18.5)
            return "Risks: Malnutrition · Bone loss · Immune weakness · Fatigue";
        if (bmi < 25.0)
            return "Risks: Very low. Keep up the good work!";
        if (bmi < 30.0)
            return "Risks: High blood pressure · Type 2 diabetes · Sleep apnea";
        return "Risks: Heart disease · Stroke · Type 2 diabetes · Joint pain · Some cancers";
    }
}
