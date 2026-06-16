import java.util.*;


public class User {

    
    private String name, username, password, email, phone;
    private int    age;
    private double height, weight;
    private String gender   = "";
    private String activity = "";
    private String goal     = "";

    
    private List<HistoryEntry> bmiHistory = new ArrayList<>();
    private List<HistoryEntry> bmrHistory = new ArrayList<>();
    
    
    private List<WorkoutEntry> workouts = new ArrayList<>();

    public static class WorkoutEntry {
        public final String date;
        public final String type;        
        public final int durationMinutes;
        public final int caloriesBurned;
        public final String notes;

        public WorkoutEntry(String date, String type, int duration, int calories, String notes) {
            this.date = date;
            this.type = type;
            this.durationMinutes = duration;
            this.caloriesBurned = calories;
            this.notes = notes;
        }

        public String toFileString() {
            return date + "~" + type + "~" + durationMinutes + "~" + caloriesBurned + "~" + notes;
        }

        public static WorkoutEntry fromFileString(String s) {
            String[] p = s.split("~", -1);
            return new WorkoutEntry(p[0], p[1], Integer.parseInt(p[2]), Integer.parseInt(p[3]), p.length > 4 ? p[4] : "");
        }
    }
    
    public static class HistoryEntry {
        public final String date;
        public final String label;
        public final double value;
        public final String note;

        public HistoryEntry(String date, String label, double value, String note) {
            this.date  = date;
            this.label = label;
            this.value = value;
            this.note  = note;
        }

        public String toFileString() {
            return date + "~" + label + "~" + value + "~" + note;
        }

        public static HistoryEntry fromFileString(String s) {
            String[] p = s.split("~", -1);
            return new HistoryEntry(p[0], p[1], Double.parseDouble(p[2]), p[3]);
        }
    }

    
    public User(String name, String username, String password,
                String email, String phone) {
        this.name     = name;
        this.username = username;
        this.password = password;
        this.email    = email;
        this.phone    = phone;
    }

    
    public String getName()     { return name; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public int    getAge()      { return age; }
    public double getHeight()   { return height; }
    public double getWeight()   { return weight; }
    public String getGender()   { return gender; }
    public String getActivity() { return activity; }
    public String getGoal()     { return goal; }
    public List<HistoryEntry> getBmiHistory() { return Collections.unmodifiableList(bmiHistory); }
    public List<HistoryEntry> getBmrHistory() { return Collections.unmodifiableList(bmrHistory); }
    public List<WorkoutEntry> getWorkouts()   { return Collections.unmodifiableList(workouts); }

   
    public void setAge(int age) {
        if (age < 1 || age > 120) throw new IllegalArgumentException("Age out of range.");
        this.age = age;
    }
    public void setHeight(double height) {
        if (height < 50 || height > 300) throw new IllegalArgumentException("Height out of range.");
        this.height = height;
    }
    public void setWeight(double weight) {
        if (weight < 10 || weight > 500) throw new IllegalArgumentException("Weight out of range.");
        this.weight = weight;
    }
    public void setGender(String gender)     { this.gender   = gender; }
    public void setActivity(String activity) { this.activity = activity; }
    public void setGoal(String goal)         { this.goal = goal; }

    public void addBmiHistory(HistoryEntry e) { bmiHistory.add(e); }
    public void addBmrHistory(HistoryEntry e) { bmrHistory.add(e); }

    public void setBmiHistory(List<HistoryEntry> list) { bmiHistory = new ArrayList<>(list); }
    public void setBmrHistory(List<HistoryEntry> list) { bmrHistory = new ArrayList<>(list); }

    public void addWorkout(WorkoutEntry w) { workouts.add(w); }
    public void setWorkouts(List<WorkoutEntry> list) { workouts = new ArrayList<>(list); }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.join("|", name, username, password, email, phone,
                String.valueOf(age), String.valueOf(height),
                String.valueOf(weight), gender, activity, goal));

        sb.append("|BMIHIST:");
        List<String> bh = new ArrayList<>();
        for (HistoryEntry e : bmiHistory) bh.add(e.toFileString());
        sb.append(String.join(";", bh));

        sb.append("|BMRHIST:");
        List<String> rh = new ArrayList<>();
        for (HistoryEntry e : bmrHistory) rh.add(e.toFileString());
        sb.append(String.join(";", rh));

        sb.append("|WORKOUTS:");
        List<String> wh = new ArrayList<>();
        for (WorkoutEntry w : workouts) wh.add(w.toFileString());
        sb.append(String.join(";", wh));

        return sb.toString();
    }

    public static User fromFileString(String line) throws Exception {
        try {
            String[] p = line.split("\\|", -1);
            User u = new User(p[0], p[1], p[2], p[3], p[4]);
            if (!p[5].equals("0")) u.setAge(Integer.parseInt(p[5]));
            u.height   = Double.parseDouble(p[6]);
            u.weight   = Double.parseDouble(p[7]);
            u.gender   = p[8];
            u.activity = p[9];
            int historyStart = 10;
            if (p.length > 10 && !p[10].startsWith("BMIHIST:") && !p[10].startsWith("BMRHIST:")
                    && !p[10].startsWith("BMRHIST:") && !p[10].startsWith("WORKOUTS:")) {
                u.goal = p[10];
                historyStart = 11;
            }

            // Find BMIHIST, BMRHIST and WORKOUTS
            for (int i = historyStart; i < p.length; i++) {
                if (p[i].startsWith("BMIHIST:")) {
                    String raw = p[i].substring(8);
                    if (!raw.isEmpty()) {
                        for (String s : raw.split(";")) {
                            try { u.bmiHistory.add(HistoryEntry.fromFileString(s)); }
                            catch (Exception ignored) {}
                        }
                    }
                } else if (p[i].startsWith("BMRHIST:")) {
                    String raw = p[i].substring(8);
                    if (!raw.isEmpty()) {
                        for (String s : raw.split(";")) {
                            try { u.bmrHistory.add(HistoryEntry.fromFileString(s)); }
                            catch (Exception ignored) {}
                        }
                    }
                } else if (p[i].startsWith("WORKOUTS:")) {
                    String raw = p[i].substring(9);
                    if (!raw.isEmpty()) {
                        for (String s : raw.split(";")) {
                            try { u.workouts.add(WorkoutEntry.fromFileString(s)); }
                            catch (Exception ignored) {}
                        }
                    }
                }
            }

            return u;
        } catch (Exception e) {
            throw new Exception("Corrupted user record: " + line, e);
        }
    }

    @Override
    public String toString() {
        return "User[" + username + " | " + email + "]";
    }
}
