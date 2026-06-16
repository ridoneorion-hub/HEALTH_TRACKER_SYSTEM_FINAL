
public class DietSuggestion {

    public static class DietPlan {
        public final String goal;
        public final String[] meals;
        public final String[] foods;
        public final String[] avoid;
        public final String   tip;
        public final int      calories;

        DietPlan(String goal, int calories, String[] meals,
                 String[] foods, String[] avoid, String tip) {
            this.goal     = goal;
            this.calories = calories;
            this.meals    = meals;
            this.foods    = foods;
            this.avoid    = avoid;
            this.tip      = tip;
        }
    }

   
    public static DietPlan generate(double bmi, double tdee, String activity) {
        if (bmi < 18.5) {
            int target = (int) (tdee + 400);
            return new DietPlan("Weight Gain – Lean Muscle Building", target,
                new String[]{"Breakfast: Oats with banana, peanut butter & whole milk","Mid-Morning: Handful of mixed nuts + fruit juice","Lunch: Brown rice, lentils, grilled chicken/paneer & salad","Afternoon: Whole grain bread with avocado or egg","Dinner: Whole wheat roti, dal, vegetables & yogurt","Before Bed: Warm milk with a banana"},
                new String[]{"Oats","Bananas","Peanut butter","Brown rice","Lentils","Eggs","Whole milk","Avocado","Chicken breast","Sweet potato"},
                new String[]{"Skipping meals","Excessive caffeine","Junk food instead of calories","Low-fat only products"},
                "Eat every 3-4 hours. Prioritize protein (1.2-1.6 g/kg body weight) to build lean mass, not just fat.");
        }
        if (bmi < 25.0) {
            int target = (int) tdee;
            return new DietPlan("Maintenance – Balanced Healthy Lifestyle", target,
                new String[]{"Breakfast: Greek yogurt with berries + whole grain toast","Mid-Morning: Apple or pear with a handful of almonds","Lunch: Quinoa/brown rice, mixed vegetables & lean protein","Afternoon: Green tea + fruit or rice cake","Dinner: Grilled fish/tofu, steamed veggies & salad","Evening (optional): Herbal tea + small dark chocolate"},
                new String[]{"Greek yogurt","Berries","Quinoa","Salmon","Tofu","Almonds","Leafy greens","Eggs","Olive oil","Legumes"},
                new String[]{"Sugary drinks","Ultra-processed snacks","Excessive alcohol","Late-night heavy meals"},
                "Stay hydrated (8 glasses/day). Focus on food variety – eat the rainbow for micronutrients.");
        }
        if (bmi < 30.0) {
            int target = (int) (tdee - 400);
            return new DietPlan("Weight Loss – Moderate Calorie Deficit", target,
                new String[]{"Breakfast: Boiled eggs (2) + 1 slice whole grain toast + black coffee","Mid-Morning: Cucumber/carrot sticks with hummus","Lunch: Large salad with grilled chicken, chickpeas & lemon dressing","Afternoon: Green tea + small handful of walnuts","Dinner: Stir-fried vegetables with tofu/fish & cauliflower rice","Evening: Herbal tea (chamomile or green)"},
                new String[]{"Eggs","Leafy greens","Chicken breast","Fish","Chickpeas","Cauliflower","Broccoli","Walnuts","Berries","Green tea"},
                new String[]{"White bread & refined carbs","Sugary beverages","Fried foods","Processed meats","High-sugar snacks","Alcohol"},
                "A 400 kcal daily deficit leads to ~0.4 kg loss/week. Never go below 1200 kcal (women) or 1500 kcal (men).");
        }
        int target = (int) (tdee - 600);
        return new DietPlan("Weight Management – Structured Calorie Reduction", target,
            new String[]{"Breakfast: Vegetable omelette (2 eggs) with spinach, no oil/butter","Mid-Morning: 1 small fruit (apple/pear) + water","Lunch: Large vegetable soup + small serving of protein (fish/chicken)","Afternoon: Unsweetened green tea + celery sticks","Dinner: Steamed vegetables with grilled protein, small portion","After Dinner: Warm water with lemon"},
            new String[]{"Spinach","Kale","Broccoli","Chicken breast","Fish","Lentils","Tomatoes","Cucumber","Berries","Herbal tea"},
            new String[]{"All fried foods","Sugary drinks & fruit juices","White rice & bread","Fast food","Processed snacks","Alcohol","High-fat dairy"},
            "Consult a doctor or dietitian before starting. Aim for 0.5-1 kg/week loss maximum. Walk 30 min daily.");
    }

    
    public static int waterIntake(double weightKg) {
        return (int) (weightKg * 35);
    }

    public static String exerciseSuggestion(double bmi, String activity) {
        if (bmi < 18.5)
            return "Focus on strength training (3x/week): squats, push-ups, rows. Add yoga for flexibility. Avoid excessive cardio.";
        if (bmi < 25.0)
            return "Mix of cardio (150 min/week) + strength training (2-3x/week). Try swimming, cycling, or group fitness classes.";
        if (bmi < 30.0)
            return "Start with 30-min brisk walks daily. Add low-impact cardio: swimming, cycling. Aim for 250+ min cardio/week.";
        return "Begin with gentle walking (15-20 min daily), build up slowly. Water aerobics is excellent. Avoid high-impact exercise initially.";
    }
}
