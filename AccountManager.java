import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class AccountManager {
    private Map<String, String> accounts = new HashMap<>();
    private Map<String, UserData> userDataMap = new HashMap<>();
    private String currentUser;
    // Create an account and associate it with a new UserData instance
    public boolean createAccount(String username, String password) {
        if (!accounts.containsKey(username)) {
            accounts.put(username, password);
            userDataMap.put(username, new UserData());
            return true;
        }
        return false;
    }
    // Login logic
    public boolean login(String username, String password) {
        if (accounts.containsKey(username) && accounts.get(username).equals(password)) {
            currentUser = username;
            return true;
        }
        return false;
    }
    // Logout logic
    public void logout() {
        currentUser = null;
    }
    // Get currently logged-in user
    public String getCurrentUser() {
        return currentUser;
    }
    // Get data specific to the current user
    public UserData getCurrentUserData() {
        return currentUser != null ? userDataMap.get(currentUser) : null;
    }
    // Theme management
    public Color getUserThemeColor(String username) {
        return userDataMap.containsKey(username) ? userDataMap.get(username).getThemeColor() : null;
    }
    public void setUserThemeColor(String username, Color color) {
        if (userDataMap.containsKey(username)) {
            userDataMap.get(username).setThemeColor(color);
        }
    }

    public void updateThemeColor(Color color) {
        if (currentUser != null) {
            userDataMap.get(currentUser).setThemeColor(color);
        }
    }
    // Task management
    public List<String> getUserTasks(String username) {
        return userDataMap.containsKey(username) ? userDataMap.get(username).getTasks() : null;
    }
    public void saveUserTasks(String username, List<String> tasks) {
        if (userDataMap.containsKey(username)) {
            userDataMap.get(username).setTasks(tasks);
        }
    }
    // Reflection management
    public List<String> getUserReflections(String username) {
        return userDataMap.containsKey(username) ? userDataMap.get(username).getReflections() : null;
    }
    public void saveUserReflections(String username, List<String> reflections) {
        if (userDataMap.containsKey(username)) {
            userDataMap.get(username).setReflections(reflections);
        }
    }

    public void clearCurrentUserTasksAndReflections() {
        if (currentUser != null) {
            userDataMap.get(currentUser).getTasks().clear();
            userDataMap.get(currentUser).getReflections().clear();
        }
    }
}


