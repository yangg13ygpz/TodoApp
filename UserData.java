import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
public class UserData {
    private Color themeColor;
    private List<String> tasks;
    private List<String> reflections;
    private int points;
    private List<String> availableRewards;

    // Constructor
    public UserData() {
        this.themeColor = Color.WHITE;
        this.tasks = new ArrayList<>();
        this.reflections = new ArrayList<>();
        this.points = 0;
        this.availableRewards = new ArrayList<>();
        // Rewards that the user can purchase initially
        availableRewards.add("Extra Break (100 points)");
        availableRewards.add("Custom Theme (200 points)");
        availableRewards.add("Priority Task Slot (300 points)");
        availableRewards.add("Walmart Gift Card $5 (5000 points)");
        availableRewards.add("Apple Store Gift Card $5 (5000 points)");
    }
    // Theme color
    public Color getThemeColor() {
        return themeColor;
    }
    public void setThemeColor(Color themeColor) {
        this.themeColor = themeColor;
    }
    // Tasks
    public List<String> getTasks() {
        return tasks;
    }
    public void setTasks(List<String> tasks) {
        this.tasks = tasks;
    }
    // Reflections
    public List<String> getReflections() {
        return reflections;
    }
    public void setReflections(List<String> reflections) {
        this.reflections = reflections;
    }

    // Rewards system
    public int getPoints() {
        return points;
    }

    public List<String> getAvailableRewards() {
        return availableRewards;
    }

    public void purchaseReward(String reward, int cost) {
        // Deduct points and add reward to the user's list
        this.points -= cost;
        availableRewards.remove(reward); // Remove the reward from the list of available rewards
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public void addReward(String reward) {
        availableRewards.add(reward);  // Add reward back to the available list if necessary
    }
}
