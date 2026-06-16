import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TerminalRunner {
    private static final String MEAL_FILE = "meal_data.txt";
    private static final String SLEEP_FILE = "sleep_data.txt";
    private static User currentUser;
    private static Scanner scanner;

    public static void main(String[] args) {
        AuthSystem.loadData();
        scanner = new Scanner(System.in);

        while (true) {
            showMainMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    handleRegister();
                    break;
                case "2":
                    handleLogin();
                    break;
                case "3":
                    System.out.println("Goodbye.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("================================");
        System.out.println("    HEALTH TRACKER - SYSTEM");
        System.out.println("================================");
        System.out.println("1. Register");
        System.out.println("2. Login");
        System.out.println("3. Exit");
        System.out.print("Choice: ");
    }

    private static void handleRegister() {
        System.out.println("\n--- Register ---");
        System.out.print("Full name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        System.out.print("Email: ");
        String email = scanner.nextLine().trim();
        System.out.print("Phone: ");
        String phone = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        String result = AuthSystem.register(name, username, email, phone, password);
        if ("OK".equals(result)) {
            System.out.println("Registration successful. You can now log in.\n");
        } else {
            System.out.println("Registration failed: " + result + "\n");
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        System.out.print("Username / Email / Phone: ");
        String id = scanner.nextLine().trim();
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();

        User user = AuthSystem.login(id, password);
        if (user == null) {
            System.out.println("Login failed: invalid credentials.\n");
            return;
        }

        currentUser = user;
        System.out.println("\nWelcome, " + user.getName() + "!\n");
        showDashboardMenu();
    }

    private static void showDashboardMenu() {
        while (currentUser != null) {
            System.out.println("================================");
            System.out.println("      TERMINAL DASHBOARD");
            System.out.println("================================");
            System.out.println("1. Calculate BMI/BMR");
            System.out.println("2. Calories Calculator");
            System.out.println("3. History");
            System.out.println("4. Diet Plan");
            System.out.println("5. Meals");
            System.out.println("6. Sleep Tracker");
            System.out.println("7. Workouts");
            System.out.println("8. Set Goal");
            System.out.println("9. Logout");
            System.out.println("10. Exit");
            System.out.print("Choice: ");

            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1":
                    calculateBmiBmr();
                    break;
                case "2":
                    showCalorieCalculator();
                    break;
                case "3":
                    showHistory();
                    break;
                case "4":
                    showDietPlan();
                    break;
                case "5":
                    showMealsMenu();
                    break;
                case "6":
                    showSleepMenu();
                    break;
                case "7":
                    showWorkoutMenu();
                    break;
                case "8":
                    setGoal();
                    break;
                case "9":
                    currentUser = null;
                    System.out.println("Logged out.\n");
                    return;
                case "10":
                    System.out.println("Goodbye.");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void calculateBmiBmr() {
        System.out.println("\n--- Calculate BMI/BMR ---");
        currentUser.setAge(readInt("Age: ", 1, 120));
        currentUser.setHeight(readDouble("Height (cm): ", 50, 300));
        currentUser.setWeight(readDouble("Weight (kg): ", 10, 500));
        currentUser.setGender(readGender());
        currentUser.setActivity(readActivity());

        HealthCalculators.run(currentUser);
        saveUsers();

        double bmi = HealthCalculators.calculateBmi(currentUser);
        double bmr = HealthCalculators.calculateBmr(currentUser);
        double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(currentUser.getActivity()));

        System.out.println("\n--- Results ---");
        printKeyValue("Height (cm)", String.format("%.0f", currentUser.getHeight()));
        printKeyValue("Weight (kg)", String.format("%.0f", currentUser.getWeight()));
        printKeyValue("Age", String.valueOf(currentUser.getAge()));
        printKeyValue("Gender", currentUser.getGender());
        printKeyValue("Activity", HealthCalculators.activityLabel(currentUser.getActivity()));
        printKeyValue("BMI", String.format("%.2f", bmi));
        printKeyValue("BMI Category", HealthCalculators.category(bmi));
        printKeyValue("BMR", String.format("%.0f kcal/day", bmr));
        printKeyValue("TDEE", String.format("%.0f kcal/day", tdee));
        System.out.println();
    }

    private static void showHistory() {
        System.out.println("\n--- History ---");
        List<User.HistoryEntry> bmiHistory = currentUser.getBmiHistory();
        List<User.HistoryEntry> bmrHistory = currentUser.getBmrHistory();

        if (bmiHistory.isEmpty() && bmrHistory.isEmpty()) {
            System.out.println("No history available. Calculate BMI/BMR first.\n");
            return;
        }

        if (!bmiHistory.isEmpty()) {
            System.out.println("BMI History");
            System.out.println(String.format("%-12s  %-10s  %-30s", "Date", "Value", "Category"));
            System.out.println("--------------------------------------------------------------");
            for (User.HistoryEntry entry : bmiHistory) {
                System.out.println(String.format("%-12s  %-10s  %-30s",
                    entry.date, String.format("%.2f", entry.value), entry.note));
            }
            System.out.println();
        }

        if (!bmrHistory.isEmpty()) {
            System.out.println("BMR History");
            System.out.println(String.format("%-12s  %-12s  %-30s", "Date", "Value", "Details"));
            System.out.println("--------------------------------------------------------------");
            for (User.HistoryEntry entry : bmrHistory) {
                System.out.println(String.format("%-12s  %-12s  %-30s",
                    entry.date, String.format("%.0f", entry.value), entry.note));
            }
            System.out.println();
        }
    }

    private static void showCalorieCalculator() {
        System.out.println("\n--- Calories Calculator ---");
        if (currentUser.getHeight() <= 0 || currentUser.getWeight() <= 0 || currentUser.getAge() <= 0 || currentUser.getGender().isEmpty()) {
            System.out.println("Please complete a BMI/BMR calculation first.\n");
            return;
        }

        double bmi = HealthCalculators.calculateBmi(currentUser);
        double bmr = HealthCalculators.calculateBmr(currentUser);
        double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(currentUser.getActivity()));
        String goal = currentUser.getGoal().isEmpty() ? "Maintain Weight" : currentUser.getGoal();
        double target = HealthCalculators.calorieTarget(tdee, goal);

        printKeyValue("Goal", goal);
        printKeyValue("BMI", String.format("%.2f", bmi));
        printKeyValue("BMR", String.format("%.0f kcal/day", bmr));
        printKeyValue("TDEE", String.format("%.0f kcal/day", tdee));
        printKeyValue("Calorie Target", String.format("%.0f kcal/day", target));
        System.out.println();
    }

    private static void showDietPlan() {
        System.out.println("\n--- Diet Plan ---");

        double bmi = HealthCalculators.calculateBmi(currentUser);
        double bmr = HealthCalculators.calculateBmr(currentUser);
        double tdee = HealthCalculators.calculateTdee(bmr, HealthCalculators.multiplier(currentUser.getActivity()));
        DietSuggestion.DietPlan plan = DietSuggestion.generate(bmi, tdee, currentUser.getActivity());

        printKeyValue("Goal", currentUser.getGoal().isEmpty() ? plan.goal : currentUser.getGoal());
        printKeyValue("Daily Calories", plan.calories + " kcal");
        System.out.println("\nDaily Meal Plan:");
        for (String meal : plan.meals) {
            System.out.println(" - " + meal);
        }
        System.out.println("\nEat More:");
        for (String food : plan.foods) {
            System.out.println(" - " + food);
        }
        System.out.println("\nAvoid:");
        for (String avoid : plan.avoid) {
            System.out.println(" - " + avoid);
        }
        System.out.println("\nTip: " + plan.tip + "\n");
    }

    private static void setGoal() {
        System.out.println("\n--- Set Goal ---");
        String[] options = {"Lose Weight", "Build Muscle", "Maintain Weight", "Improve Fitness", "Custom"};
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int choice = readInt("Choice: ", 1, options.length);
        String goal;
        if (choice == options.length) {
            System.out.print("Enter custom goal: ");
            goal = scanner.nextLine().trim();
        } else {
            goal = options[choice - 1];
        }
        currentUser.setGoal(goal);
        saveUsers();
        System.out.println("Goal saved: " + goal + "\n");
    }

    private static void showMealsMenu() {
        while (true) {
            System.out.println("\n--- Meals ---");
            System.out.println("1. Add Meal");
            System.out.println("2. Nutrition Analysis");
            System.out.println("3. View Meals");
            System.out.println("4. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addMeal();
                    break;
                case "2":
                    showMealAnalysis();
                    break;
                case "3":
                    listMeals();
                    break;
                case "4":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void addMeal() {
        System.out.println("\n--- Add Meal ---");
        System.out.print("Meal name: ");
        String name = scanner.nextLine().trim();
        String category = readOption("Category", new String[]{"Breakfast", "Lunch", "Dinner", "Snack"});
        int calories = readInt("Calories: ", 0, 10000);
        int protein = readInt("Protein (g): ", 0, 1000);
        int carbs = readInt("Carbs (g): ", 0, 1000);
        int fat = readInt("Fat (g): ", 0, 1000);

        MealEntry meal = new MealEntry(currentUser.getName(), name, category, calories, protein, carbs, fat);
        appendMeal(meal);
        System.out.println("Meal saved.\n");
    }

    private static void showMealAnalysis() {
        List<MealEntry> meals = loadMeals();
        if (meals.isEmpty()) {
            System.out.println("No meals recorded yet.\n");
            return;
        }
        int calories = 0, protein = 0, carbs = 0, fat = 0;
        for (MealEntry meal : meals) {
            calories += meal.calories;
            protein += meal.protein;
            carbs += meal.carbs;
            fat += meal.fat;
        }
        System.out.println("\n--- Nutrition Analysis ---");
        printKeyValue("Total Calories", calories + " kcal");
        printKeyValue("Total Protein", protein + " g");
        printKeyValue("Total Carbs", carbs + " g");
        printKeyValue("Total Fat", fat + " g");
        System.out.println();
    }

    private static void listMeals() {
        List<MealEntry> meals = loadMeals();
        if (meals.isEmpty()) {
            System.out.println("No meals recorded yet.\n");
            return;
        }
        System.out.println("\n--- Meal Records ---");
        System.out.println(String.format("%-20s  %-10s  %-8s  %-8s  %-8s  %-8s", "Name", "Category", "Cal", "Protein", "Carbs", "Fat"));
        System.out.println("---------------------------------------------------------------------------");
        for (MealEntry meal : meals) {
            System.out.println(String.format("%-20s  %-10s  %-8d  %-8d  %-8d  %-8d",
                meal.mealName, meal.category, meal.calories, meal.protein, meal.carbs, meal.fat));
        }
        System.out.println();
    }

    private static List<MealEntry> loadMeals() {
        List<MealEntry> meals = new ArrayList<>();
        File file = new File(MEAL_FILE);
        if (!file.exists()) return meals;
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 7) continue;
                if (!parts[0].equals(currentUser.getName())) continue;
                meals.add(new MealEntry(parts[0], parts[1], parts[2],
                    Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), Integer.parseInt(parts[5]), Integer.parseInt(parts[6])));
            }
        } catch (Exception ignored) {
        }
        return meals;
    }

    private static void appendMeal(MealEntry meal) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEAL_FILE, true))) {
            bw.write(meal.toFileString());
            bw.newLine();
        } catch (IOException ignored) {
        }
    }

    private static void showSleepMenu() {
        while (true) {
            System.out.println("\n--- Sleep Tracker ---");
            System.out.println("1. Log Sleep");
            System.out.println("2. View Sleep Records");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    logSleep();
                    break;
                case "2":
                    listSleepRecords();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void logSleep() {
        System.out.println("\n--- Log Sleep ---");
        System.out.print("Date (YYYY-MM-DD, blank for today): ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()) date = LocalDate.now().toString();
        double hours = readDouble("Hours slept: ", 0, 24);
        SleepEntry entry = new SleepEntry(currentUser.getName(), date, hours);
        appendSleep(entry);
        System.out.println("Sleep record saved.\n");
    }

    private static void listSleepRecords() {
        List<SleepEntry> records = loadSleepRecords();
        if (records.isEmpty()) {
            System.out.println("No sleep records found.\n");
            return;
        }
        System.out.println("\n--- Sleep Records ---");
        System.out.println(String.format("%-12s  %-8s", "Date", "Hours"));
        System.out.println("---------------------");
        for (SleepEntry record : records) {
            System.out.println(String.format("%-12s  %-8.2f", record.date, record.hours));
        }
        System.out.println();
    }

    private static List<SleepEntry> loadSleepRecords() {
        List<SleepEntry> records = new ArrayList<>();
        File file = new File(SLEEP_FILE);
        if (!file.exists()) return records;
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",", -1);
                if (parts.length < 3) continue;
                if (!parts[0].equals(currentUser.getName())) continue;
                records.add(new SleepEntry(parts[0], parts[1], Double.parseDouble(parts[2])));
            }
        } catch (Exception ignored) {
        }
        return records;
    }

    private static void appendSleep(SleepEntry entry) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(SLEEP_FILE, true))) {
            bw.write(entry.toFileString());
            bw.newLine();
        } catch (IOException ignored) {
        }
    }

    private static void showWorkoutMenu() {
        while (true) {
            System.out.println("\n--- Workouts ---");
            System.out.println("1. Log Workout");
            System.out.println("2. View Workouts");
            System.out.println("3. Back");
            System.out.print("Choice: ");
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    logWorkout();
                    break;
                case "2":
                    listWorkouts();
                    break;
                case "3":
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.\n");
            }
        }
    }

    private static void logWorkout() {
        System.out.println("\n--- Log Workout ---");
        System.out.print("Date (YYYY-MM-DD, blank for today): ");
        String date = scanner.nextLine().trim();
        if (date.isEmpty()) date = LocalDate.now().toString();
        String type = readOption("Workout type", new String[]{"Running", "Cycling", "Gym", "Yoga", "Swimming", "Sports", "Walking", "Other"});
        int duration = readInt("Duration (minutes): ", 0, 1440);
        int calories = readInt("Calories burned: ", 0, 20000);
        System.out.print("Notes: ");
        String notes = scanner.nextLine().trim();

        User.WorkoutEntry workout = new User.WorkoutEntry(date, type, duration, calories, notes);
        currentUser.addWorkout(workout);
        saveUsers();
        System.out.println("Workout saved.\n");
    }

    private static void listWorkouts() {
        List<User.WorkoutEntry> workouts = currentUser.getWorkouts();
        if (workouts.isEmpty()) {
            System.out.println("No workouts recorded yet.\n");
            return;
        }
        System.out.println("\n--- Workout Records ---");
        System.out.println(String.format("%-12s  %-10s  %-8s  %-9s  %-20s", "Date", "Type", "Duration", "Calories", "Notes"));
        System.out.println("-------------------------------------------------------------------------------");
        for (User.WorkoutEntry workout : workouts) {
            System.out.println(String.format("%-12s  %-10s  %-8d  %-9d  %-20s",
                workout.date, workout.type, workout.durationMinutes, workout.caloriesBurned, workout.notes));
        }
        System.out.println();
    }

    private static int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value < min || value > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }

    private static double readDouble(String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            try {
                double value = Double.parseDouble(scanner.nextLine().trim());
                if (value < min || value > max) {
                    System.out.println("Value must be between " + min + " and " + max + ".");
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }
    }

    private static String readGender() {
        while (true) {
            System.out.print("Gender (M/F): ");
            String gender = scanner.nextLine().trim().toUpperCase();
            if (gender.equals("M") || gender.equals("F")) {
                return gender;
            }
            System.out.println("Enter M or F.");
        }
    }

    private static String readActivity() {
        String[] labels = {"Sedentary", "Light", "Moderate", "Active", "Very Active"};
        String[] codes = {"SEDENTARY", "LIGHT", "MODERATE", "ACTIVE", "VERY_ACTIVE"};
        System.out.println("Select activity level:");
        for (int i = 0; i < labels.length; i++) {
            System.out.println((i + 1) + ". " + labels[i] + " " + HealthCalculators.activityLabel(codes[i]));
        }
        while (true) {
            int index = readInt("Choice: ", 1, labels.length) - 1;
            return codes[index];
        }
    }

    private static String readOption(String label, String[] options) {
        System.out.println(label + ":");
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        int index = readInt("Choice: ", 1, options.length) - 1;
        return options[index];
    }

    private static void printKeyValue(String key, String value) {
        System.out.println(String.format("%-18s : %s", key, value));
    }

    private static void saveUsers() {
        try {
            FileHandler.saveUsers(AuthSystem.getUsers());
        } catch (Exception e) {
            System.out.println("[!] Could not save user data: " + e.getMessage());
        }
    }

    private static class MealEntry {
        final String username;
        final String mealName;
        final String category;
        final int calories;
        final int protein;
        final int carbs;
        final int fat;

        MealEntry(String username, String mealName, String category, int calories, int protein, int carbs, int fat) {
            this.username = username;
            this.mealName = mealName;
            this.category = category;
            this.calories = calories;
            this.protein = protein;
            this.carbs = carbs;
            this.fat = fat;
        }

        String toFileString() {
            return String.join(",", username, mealName, category,
                String.valueOf(calories), String.valueOf(protein), String.valueOf(carbs), String.valueOf(fat));
        }
    }

    private static class SleepEntry {
        final String username;
        final String date;
        final double hours;

        SleepEntry(String username, String date, double hours) {
            this.username = username;
            this.date = date;
            this.hours = hours;
        }

        String toFileString() {
            return String.join(",", username, date, String.valueOf(hours));
        }
    }
}
