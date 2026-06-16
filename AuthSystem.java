import java.util.*;

public class AuthSystem {

    private static Map<String, User> users = new HashMap<>();
    protected static Object currentUser;

    public static Map<String, User> getUsers() {
        return users;
    }

    static void loadData() {
        try {
            users = FileHandler.loadUsers();
        } catch (Exception e) {
            System.out.println("[!] Could not load saved accounts: " + e.getMessage());
        }
    }

    static String register(String name, String username, String email,
                           String phone, String password) {
        return register(name, username, email, phone, password, "");
    }

    static String register(String name, String username, String email,
                           String phone, String password, String goal) {
        for (User u : users.values()) {
            if (u.getUsername().equalsIgnoreCase(username))
                return "Username already taken!";
            if (u.getEmail().equalsIgnoreCase(email))
                return "Email already registered!";
            if (u.getPhone().equals(phone))
                return "Phone number already registered!";
        }
        if (name.isEmpty() || username.isEmpty() || email.isEmpty()
                || phone.isEmpty() || password.isEmpty())
            return "All fields are required.";

        User newUser = new User(name, username, password, email, phone);
        newUser.setGoal(goal);
        users.put(username, newUser);
        try {
            FileHandler.saveUsers(users);
        } catch (Exception e) {
            return "Registered in memory but could not save: " + e.getMessage();
        }
        return "OK";
    }

    static User login(String id, String pass) {
        for (User u : users.values()) {
            boolean match = u.getUsername().equalsIgnoreCase(id)
                         || u.getEmail().equalsIgnoreCase(id)
                         || u.getPhone().equals(id);
            if (match && u.getPassword().equals(pass)) return u;
        }
        return null;
    }
}
