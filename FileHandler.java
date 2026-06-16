import java.io.*;
import java.util.*;


public class FileHandler {

    private static final String FILE_PATH = "users.txt";

    public static void saveUsers(Map<String, User> users) throws Exception {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User u : users.values()) {
                bw.write(u.toFileString());
                bw.newLine();
            }
        } catch (IOException e) {
            throw new Exception("Failed to save users to file.", e);
        }
    }

    public static Map<String, User> loadUsers() throws Exception {
        Map<String, User> users = new HashMap<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return users;

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            int lineNo = 0;
            while ((line = br.readLine()) != null) {
                lineNo++;
                line = line.trim();
                if (line.isEmpty()) continue;
                try {
                    User u = User.fromFileString(line);
                    users.put(u.getUsername(), u);
                } catch (Exception e) {
                    System.out.println("[!] Skipping invalid record at line " + lineNo + ": " + e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new Exception("Failed to load users from file.", e);
        }
        return users;
    }
}
