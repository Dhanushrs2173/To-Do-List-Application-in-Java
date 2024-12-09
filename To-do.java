import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

public class TodoApp extends JFrame implements ActionListener {
    // Components
    private JTextField taskField;
    private JButton addButton, deleteButton, clearButton;
    private JList<String> taskList;
    private DefaultListModel<String> listModel;
    private File file = new File("tasks.txt");

    public TodoApp() {
        // Setting up the frame
        setTitle("To-Do List");
        setSize(300, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Task input field
        taskField = new JTextField(20);
        add(taskField, BorderLayout.NORTH);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        addButton = new JButton("Add");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        addButton.addActionListener(this);
        deleteButton.addActionListener(this);
        clearButton.addActionListener(this);

        buttonPanel.add(addButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);
        add(buttonPanel, BorderLayout.SOUTH);

        // List to display tasks
        listModel = new DefaultListModel<>();
        taskList = new JList<>(listModel);
        JScrollPane listScroll = new JScrollPane(taskList);
        add(listScroll, BorderLayout.CENTER);

        // Load tasks from file
        loadTasks();
    }

    // Load tasks from file
    private void loadTasks() {
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    listModel.addElement(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Save tasks to file
    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < listModel.size(); i++) {
                writer.write(listModel.get(i));
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String command = e.getActionCommand();

        // Add task
        if (command.equals("Add")) {
            String task = taskField.getText().trim();
            if (!task.isEmpty()) {
                listModel.addElement(task);
                saveTasks();
                taskField.setText("");
            }
        }
        // Delete selected task
        else if (command.equals("Delete")) {
            int selectedIndex = taskList.getSelectedIndex();
            if (selectedIndex != -1) {
                listModel.remove(selectedIndex);
                saveTasks();
            }
        }
        // Clear all tasks
        else if (command.equals("Clear")) {
            listModel.clear();
            saveTasks();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TodoApp app = new TodoApp();
            app.setVisible(true);
        });
    }
}
