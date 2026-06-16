[README.md](https://github.com/user-attachments/files/29013301/README.md)
# Health Tracker Application
A comprehensive health and fitness tracker built in Java. It provides both a graphical Swing interface and a terminal-based interface offering features for tracking BMI, BMR, calorie intake, diet plans, meals, sleep, and workouts.

---

## Features
- User Management – Register, login, and save user profiles.
- BMI / BMR Calculator – Compute BMI and Basal Metabolic Rate, with history tracking.
- Calorie Target Calculator – Based on your activity level and goal (lose weight, build muscle, maintain, improve fitness).
- Personalised Diet Plan – Suggests meals, foods to eat/avoid, and daily calorie targets.
- Meal Tracker – Log meals with macros (protein, carbs, fat) and view nutrition analysis.
- Sleep Tracker – Record sleep hours and see average sleep trends.
- Workout Tracker – Log workouts (type, duration, calories burned) and view statistics.
- History – View past BMI and BMR records.
- Data Persistence – All data is saved to text files (users, meals, sleep) for reuse across sessions.
- Both GUI and Terminal – Same data storage, use either interface interchangeably.

---

## How to Run

### Prerequisites
- Java Development Kit (JDK) 8 or higher installed.
- A terminal/command prompt.

### Compilation
1. Place all `.java` files in a single directory.
2. Open a terminal in that directory.
3. Compile all files:
   bash
   javac java
   

### 1. Graphical Interface (Swing)
Run the GUI version using:
bash
java HealthTrackerRunner

Alternatively, you can run the "Main" class, which launches the same GUI:
bash
java Main


### 2. Terminal Interface (Console)
Run the terminal-based version using:
bash
java TerminalRunner

Follow the on‑screen prompts to navigate through the application.

---

## Data Storage
All user data is stored in plain text files inside the same directory:


users.txt      User profiles, BMI/BMR history, workouts 
meal_data.txt  Logged meals (per user)         
sleep_data.txt Logged sleep records (per user) 

These files are automatically created when you save data. You can also manually edit them, but be careful to preserve the format.

---

## Usage Tips
- First‑time users Register a new account using the Register option.
- Set your metrics Before accessing the diet plan or calorie calculator, complete the *BMI/BMR calculation* (this stores your height, weight, age, gender, and activity level).
- Goals Your fitness goal (e.g., *Lose Weight*) influences calorie targets and diet suggestions.
- Interchangeable You can switch between the GUI and terminal versions at any time – they share the same data files.
- Minimum screen resolution The GUI is designed for a resolution of at least 800×600 pixels.

---

## Troubleshooting
- **Compilation errors Ensure all java files are present and that you have a compatible JDK version.
- **Missing data files The application will create them automatically; if not, check file permissions.
- **Data corruption If a file gets corrupted, delete it and restart a fresh file will be generated.

---

## License
This project is open source and provided for educational purposes. Feel free to modify and improve it.

---

Enjoy tracking your health!
