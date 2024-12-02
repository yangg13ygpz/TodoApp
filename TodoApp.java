import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
public class TodoApp extends JFrame {
    private File taskFile;
    private File reflectionFile;
    private AccountManager accountManager;
    public TodoApp() {
        setTitle("To-Do List App");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        accountManager = new AccountManager();
        taskFile = new File("tasks.txt");
        reflectionFile = new File("reflections.txt");
        showIntroScreen();
    }
    private void showIntroScreen() {
        getContentPane().removeAll();
        repaint();
        JPanel introPanel = new JPanel(new BorderLayout());
        JLabel introLabel = new JLabel("Welcome to the To-Do List App!", JLabel.CENTER);
        introLabel.setFont(new Font("Arial", Font.BOLD, 20));
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton signinButton = new JButton("Sign up");
        signinButton.setFont(new Font("Arial", Font.PLAIN, 16));
        signinButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter username:");
                String password = JOptionPane.showInputDialog("Enter password:");
                if (username != null && password != null) {
                    if (accountManager.createAccount(username, password)) {
                        JOptionPane.showMessageDialog(null, "Account created successfully!");
                    } else {
                        JOptionPane.showMessageDialog(null, "Account already exists!");
                    }
                }
            }
        });
        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.PLAIN, 16));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = JOptionPane.showInputDialog("Enter username:");
                String password = JOptionPane.showInputDialog("Enter password:");
                if (username != null && password != null && accountManager.login(username, password)) {
                    JOptionPane.showMessageDialog(null, "Login successful!");
                    showMainAppScreen();
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials!");
                }
            }
        });
        buttonPanel.add(signinButton);
        buttonPanel.add(loginButton);
        introPanel.add(introLabel, BorderLayout.NORTH);
        introPanel.add(buttonPanel, BorderLayout.CENTER);
        add(introPanel);
        setVisible(true);
    }
    private void showMainAppScreen() {
        getContentPane().removeAll();
        repaint();
        // GridLayout, 5 rows for 5 buttons (change if need to add more)
        JPanel mainPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        // Nightly Reflection button
        JButton nightlyReflectionButton = new JButton("Nightly Reflection");
        nightlyReflectionButton.setFont(new Font("Arial", Font.PLAIN, 16));
        nightlyReflectionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showNightlyReflectionScreen();
            }
        });
        // To-Do List button
        JButton toDoListButton = new JButton("To-Do List");
        toDoListButton.setFont(new Font("Arial", Font.PLAIN, 16));
        toDoListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showToDoListScreen();
            }
        });
        // Settings button
        JButton settingsButton = new JButton("Settings");
        settingsButton.setFont(new Font("Arial", Font.PLAIN, 16));
        settingsButton.addActionListener(e -> showSettingsScreen());
        // Rewards Store button
        JButton rewardsStoreButton = new JButton("Rewards Store");
        rewardsStoreButton.setFont(new Font("Arial", Font.PLAIN, 16));
        rewardsStoreButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRewardsStore();
            }
        });
        // Logout button
        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.PLAIN, 16));
        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountManager.logout();
                showIntroScreen();
                JOptionPane.showMessageDialog(null, "You have been logged out.");
            }
        });
        // Added buttons to the panel
        mainPanel.add(nightlyReflectionButton);
        mainPanel.add(toDoListButton);
        mainPanel.add(settingsButton);
        mainPanel.add(rewardsStoreButton);
        mainPanel.add(logoutButton);
        // Add the panel to the frame
        add(mainPanel);
        applyThemeColor(mainPanel);
        revalidate();
        repaint();
    }

    private void showSettingsScreen() {
        getContentPane().removeAll();
        repaint();
        JPanel settingsPanel = new JPanel(new BorderLayout());
        JLabel settingsLabel = new JLabel("Settings", JLabel.CENTER);
        settingsPanel.add(settingsLabel, BorderLayout.NORTH);
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        // Change Theme Color
        JButton themeButton = new JButton("Change Theme Color");
        themeButton.addActionListener(e -> {
            Color newColor = JColorChooser.showDialog(this, "Choose a Theme Color", accountManager.getCurrentUserData().getThemeColor());
            if (newColor != null) {
                accountManager.updateThemeColor(newColor);
                applyThemeColor(settingsPanel);  // Apply the new theme to the panel
                revalidate();
                repaint();
            }
        });
        // Clear All Tasks and Reflections
        JButton clearAllButton = new JButton("Clear All Tasks and Reflections");
        clearAllButton.addActionListener(e -> {
            int confirmation = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to clear all tasks and reflections?",
                    "Confirm Clear All",
                    JOptionPane.YES_NO_OPTION);
            if (confirmation == JOptionPane.YES_OPTION) {
                accountManager.clearCurrentUserTasksAndReflections();
                JOptionPane.showMessageDialog(this, "All tasks and reflections have been cleared!");
            }
        });
        buttonPanel.add(themeButton);
        buttonPanel.add(clearAllButton);
        // Back Button
        JButton backButton = new JButton("Back");
        backButton.addActionListener(e -> showMainAppScreen());
        buttonPanel.add(backButton);
        settingsPanel.add(buttonPanel, BorderLayout.CENTER);
        add(settingsPanel);
        settingsLabel.setFont(new Font("Arial", Font.BOLD, 18));
        applyThemeColor(settingsPanel);
        revalidate();
        repaint();
    }
    // adjust size, won't show some buttons at its intended size
    private void showToDoListScreen() {
        getContentPane().removeAll();
        repaint();
        // Fetch the current user's data
        UserData currentUserData = accountManager.getCurrentUserData();
        if (currentUserData == null) {
            JOptionPane.showMessageDialog(null, "Please log in first.");
            return;
        }
        // Use user-specific task list from UserData
        List<String> toDoList = currentUserData.getTasks();
        DefaultListModel<String> toDoListModel = new DefaultListModel<>();
        toDoList.forEach(toDoListModel::addElement);
        JList<String> toDoJList = new JList<>(toDoListModel);
        JScrollPane scrollPane = new JScrollPane(toDoJList);
        JTextField taskInputField = new JTextField(20);
        JButton addButton = new JButton("Add Task");
        JButton removeButton = new JButton("Remove Selected Task");
        JButton arrangeTasksButton = new JButton("Arrange Tasks");
        JButton backButton = new JButton("Back");
        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(150, 30);
        addButton.setPreferredSize(buttonSize);
        removeButton.setPreferredSize(buttonSize);
        arrangeTasksButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        // Add task action
        addButton.addActionListener(e -> {
            String task = taskInputField.getText().trim();
            if (!task.isEmpty()) {
                toDoListModel.addElement(task);
                toDoList.add(task);  // Update user-specific list directly
                taskInputField.setText("");
            }
        });
        // Remove task action
        removeButton.addActionListener(e -> {
            int selectedIndex = toDoJList.getSelectedIndex();
            if (selectedIndex != -1) {
                toDoListModel.remove(selectedIndex);
                toDoList.remove(selectedIndex);  // Update user-specific list directly
                currentUserData.addPoints(10);  // Add 10 points
                JOptionPane.showMessageDialog(null, "Task deleted! You have earned 10 points.");
            } else {
                JOptionPane.showMessageDialog(null, "Please select a task to remove.");
            }
        });
        // Arrange tasks action
        arrangeTasksButton.addActionListener(e -> {
            reorderTasks(toDoListModel);
            toDoList.clear();
            for (int i = 0; i < toDoListModel.size(); i++) {
                toDoList.add(toDoListModel.getElementAt(i));  // Update user-specific list directly
            }
        });
        // Back button action
        backButton.addActionListener(e -> showMainAppScreen());
        // Panel layout
        JPanel inputPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.gridx = 0;
        gbc.gridy = 0;
        inputPanel.add(taskInputField, gbc);
        gbc.gridy = 1;
        inputPanel.add(addButton, gbc);
        gbc.gridy = 2;
        inputPanel.add(removeButton, gbc);
        gbc.gridy = 3;
        inputPanel.add(arrangeTasksButton, gbc);
        gbc.gridy = 4;
        inputPanel.add(backButton, gbc);
        JPanel toDoPanel = new JPanel(new BorderLayout());
        toDoPanel.add(scrollPane, BorderLayout.CENTER);
        toDoPanel.add(inputPanel, BorderLayout.EAST);
        add(toDoPanel);
        applyThemeColor(toDoPanel);
        revalidate();
        repaint();
    }

    private int getRewardCost(String reward) {
        switch (reward) {
            case "Extra Break (100 points)": return 100;
            case "Custom Theme (200 points)": return 200;
            case "Priority Task Slot (300 points)": return 300;
            case "Walmart Gift Card $5 (5000 points)": return 5000;
            case "Apple Store Gift Card $5 (5000 points)": return 5000;
            default: return 0;  // Return 0 if no reward matches
        }
    }


    private void showRewardsStore() {
        getContentPane().removeAll();
        repaint();
        // Retrieve current user's data from AccountManager
        UserData currentUserData = accountManager.getCurrentUserData();
        if (currentUserData == null) {
            JOptionPane.showMessageDialog(null, "Please log in first.");
            return;
        }
        // Rewards available to purchase and the user's current points
        List<String> availableRewards = currentUserData.getAvailableRewards();
        int currentPoints = currentUserData.getPoints();
        JPanel rewardsPanel = new JPanel(new BorderLayout());
        // Display current points
        JLabel pointsLabel = new JLabel("Current Points: " + currentPoints, JLabel.CENTER);
        pointsLabel.setFont(new Font("Arial", Font.BOLD, 16));
        rewardsPanel.add(pointsLabel, BorderLayout.NORTH);
        // List of available rewards
        DefaultListModel<String> rewardsListModel = new DefaultListModel<>();
        for (String reward : availableRewards) {
            rewardsListModel.addElement(reward);
        }
        JList<String> rewardsJList = new JList<>(rewardsListModel);
        JScrollPane rewardsScrollPane = new JScrollPane(rewardsJList);
        // Purchase button
        JButton purchaseButton = new JButton("Purchase Reward");
        purchaseButton.setFont(new Font("Arial", Font.PLAIN, 16));
        purchaseButton.addActionListener(e -> {
            String selectedReward = rewardsJList.getSelectedValue();
            if (selectedReward == null) {
                JOptionPane.showMessageDialog(null, "Please select a reward to purchase.");
                return;
            }
            // Check if the user has enough points to purchase the selected reward
            int rewardCost = getRewardCost(selectedReward);  // This method fetches the cost based on reward
            if (currentPoints >= rewardCost) {
                // User has enough points, proceed with the purchase
                currentUserData.purchaseReward(selectedReward, rewardCost);
                // After purchase, update the points label and available rewards
                updateRewardsUI(currentUserData, pointsLabel, rewardsListModel);
                JOptionPane.showMessageDialog(null, "Reward Purchased: " + selectedReward);
            } else {
                JOptionPane.showMessageDialog(null, "Insufficient points to purchase this reward.");
            }
        });
        // Back button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> showMainAppScreen());
        // Layout for buttons
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(purchaseButton);
        buttonsPanel.add(backButton);
        // Assemble the screen layout
        rewardsPanel.add(rewardsScrollPane, BorderLayout.CENTER);
        rewardsPanel.add(buttonsPanel, BorderLayout.SOUTH);
        add(rewardsPanel);
        applyThemeColor(rewardsPanel);
        revalidate();
        repaint();
    }

    private void updateRewardsUI(UserData currentUserData, JLabel pointsLabel, DefaultListModel<String> rewardsListModel) {
        // Update the points label to reflect the new points balance
        pointsLabel.setText("Current Points: " + currentUserData.getPoints());
        // Update the list of available rewards
        rewardsListModel.clear();  // Clear the current list model
        // Add the updated available rewards to the list
        List<String> updatedRewards = currentUserData.getAvailableRewards();
        for (String reward : updatedRewards) {
            rewardsListModel.addElement(reward);  // Add each reward back to the list
        }
    }

    private void reorderTasks(DefaultListModel<String> user) {
        // Prepare list for drag-and-drop rearrangement
        JList<String> list = new JList<>(user);
        list.setFont(new Font("Arial", Font.PLAIN, 14));
        list.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        list.setLayoutOrientation(JList.VERTICAL);
        list.setVisibleRowCount(-1);
        list.setDragEnabled(true);
        list.setDropMode(DropMode.INSERT);
        list.setTransferHandler(new ListItemTransferHandler());
        // Display drag-and-drop rearrangement popup
        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new Dimension(350, 200));
        JOptionPane.showMessageDialog(null, scrollPane, "Rearrange Tasks", JOptionPane.PLAIN_MESSAGE);
    }
    private void showNightlyReflectionScreen() {
        getContentPane().removeAll();
        repaint();
        // Fetch the current user's data
        UserData currentUserData = accountManager.getCurrentUserData();
        if (currentUserData == null) {
            JOptionPane.showMessageDialog(null, "Please log in first.");
            return;
        }
        // Use user-specific reflection list from UserData
        List<String> reflections = currentUserData.getReflections();

        // Create the text area for writing a new reflection
        JTextArea reflectionTextArea = new JTextArea(10, 30);
        reflectionTextArea.setLineWrap(true);
        reflectionTextArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(reflectionTextArea);
        // Save Button
        JButton saveButton = new JButton("Save Reflection");
        saveButton.setFont(new Font("Arial", Font.PLAIN, 16));
        saveButton.addActionListener(e -> {
            String reflectionText = reflectionTextArea.getText();
            if (!reflectionText.isEmpty()) {
                reflections.add(reflectionText);  // Add new reflection to the user-specific list
                currentUserData.addPoints(5);  // Optionally add points for saving a reflection
                reflectionTextArea.setText("");  // Clear after saving
                JOptionPane.showMessageDialog(null, "Reflection saved!");
            }
        });
        // Back Button
        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Arial", Font.PLAIN, 16));
        backButton.addActionListener(e -> showMainAppScreen());
        // Display Previous Reflections
        JTextArea previousReflectionsArea = new JTextArea();
        previousReflectionsArea.setEditable(false);
        previousReflectionsArea.setText(String.join("\n", reflections));
        JScrollPane previousScrollPane = new JScrollPane(previousReflectionsArea);
        previousScrollPane.setPreferredSize(new Dimension(350, 150));
        // Set preferred size for buttons
        Dimension buttonSize = new Dimension(150, 30);
        saveButton.setPreferredSize(buttonSize);
        backButton.setPreferredSize(buttonSize);
        // Layout of buttons and panels
        JPanel buttonsPanel = new JPanel(new FlowLayout());
        buttonsPanel.add(saveButton);
        buttonsPanel.add(backButton);
        // Assemble the reflection panel
        JPanel reflectionPanel = new JPanel(new BorderLayout());
        reflectionPanel.add(scrollPane, BorderLayout.NORTH);  // New reflection input
        reflectionPanel.add(buttonsPanel, BorderLayout.CENTER);  // Buttons
        reflectionPanel.add(previousScrollPane, BorderLayout.SOUTH);  // Previous reflections
        // Apply the theme color and update the UI
        add(reflectionPanel);
        applyThemeColor(reflectionPanel);
        revalidate();
        repaint();
    }

    private void saveReflection(String username, String reflection) {
        File reflectionFile = new File(username + "_reflections.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reflectionFile, true))) {
            writer.write(reflection);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<String> loadReflections(String username) {
        List<String> reflections = new ArrayList<>();
        File reflectionFile = new File(username + "_reflections.txt");
        if (reflectionFile.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(reflectionFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    reflections.add(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return reflections;  // Return the loaded reflections, or an empty list if none exist
    }

    private void applyThemeColor(Container container) {
        Color userThemeColor = accountManager.getCurrentUserData().getThemeColor();
        container.setBackground(userThemeColor);
        for (Component component : container.getComponents()) {
            if (component instanceof JPanel || component instanceof JButton) {
                component.setBackground(userThemeColor);
            }
        }
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp();
    }

}



































