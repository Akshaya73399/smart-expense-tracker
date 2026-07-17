package expenses.view;

import expenses.controller.ExpenseController;
import expenses.model.Expense;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.List;

public class ExpenseView extends JFrame {
    private final ExpenseController controller;
    private final DefaultTableModel tableModel;

    private final JTextField nameField = new JTextField();
    private final JTextField amountField = new JTextField();
    private final JTextField dateField = new JTextField(LocalDate.now().toString());
    private final JComboBox<String> categoryComboBox = new JComboBox<>(new DefaultComboBoxModel<>(
            new String[] { "Food", "Travel", "Shopping", "Bills", "Other" }));
    private final JTextField monthField = new JTextField(String.valueOf(LocalDate.now().getMonthValue()));
    private final JTextField yearField = new JTextField(String.valueOf(LocalDate.now().getYear()));
    private final JLabel totalLabel = new JLabel("₹0.00", SwingConstants.RIGHT);

    public ExpenseView(ExpenseController controller) {
        super("Smart Expense Tracker - Swing Edition");
        this.controller = controller;
        this.tableModel = new DefaultTableModel(new Object[] { "Date", "Name", "Category", "Amount" }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        configureFrame();
        buildLayout();
    }

    public void showWindow() {
        setVisible(true);
    }

    public void showExpenses(List<Expense> expenses) {
        tableModel.setRowCount(0);
        for (Expense expense : expenses) {
            tableModel.addRow(new Object[] {
                    expense.getDate(),
                    expense.getName(),
                    expense.getCategory(),
                    String.format("₹%.2f", expense.getAmount())
            });
        }
    }

    public void showTotal(double total) {
        totalLabel.setText(String.format("₹%.2f", total));
        showMessage("Total spending is " + String.format("₹%.2f", total));
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Smart Expense Tracker", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private void configureFrame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 650);
        setLocationRelativeTo(null);
        setResizable(false);
    }

    private void buildLayout() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Add Expense"));

        formPanel.add(new JLabel("Expense Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Amount:"));
        formPanel.add(amountField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryComboBox);

        formPanel.add(new JLabel("Date (yyyy-MM-dd):"));
        formPanel.add(dateField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addButton = new JButton("Add Expense");
        JButton viewButton = new JButton("View All");
        JButton reportButton = new JButton("Monthly Report");
        JButton totalButton = new JButton("Total Spending");
        JButton exitButton = new JButton("Exit");

        addButton.addActionListener(e -> {
            try {
                String name = nameField.getText();
                double amount = Double.parseDouble(amountField.getText());
                String category = (String) categoryComboBox.getSelectedItem();
                LocalDate date = LocalDate.parse(dateField.getText());
                controller.addExpense(name, amount, category, date);
            } catch (Exception ex) {
                showError("Please enter valid expense details.");
            }
        });

        viewButton.addActionListener(e -> controller.viewAllExpenses());

        reportButton.addActionListener(e -> {
            try {
                int month = Integer.parseInt(monthField.getText());
                int year = Integer.parseInt(yearField.getText());
                if (month < 1 || month > 12) {
                    showError("Month must be between 1 and 12.");
                    return;
                }
                controller.showMonthlyReport(month, year);
            } catch (Exception ex) {
                showError("Enter valid month and year values.");
            }
        });

        totalButton.addActionListener(e -> controller.showTotalSpending());
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(addButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(totalButton);
        buttonPanel.add(exitButton);

        JPanel reportPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        reportPanel.setBorder(BorderFactory.createTitledBorder("Monthly Report Filter"));
        reportPanel.add(new JLabel("Month:"));
        reportPanel.add(monthField);
        reportPanel.add(new JLabel("Year:"));
        reportPanel.add(yearField);

        JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        totalPanel.add(new JLabel("Total Spend: "));
        totalPanel.add(totalLabel);

        JTable table = new JTable(tableModel);
        table.setFillsViewportHeight(true);
        table.setRowHeight(24);

        JScrollPane tableScrollPane = new JScrollPane(table);
        tableScrollPane.setPreferredSize(new Dimension(900, 350));

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(formPanel, BorderLayout.NORTH);
        topPanel.add(buttonPanel, BorderLayout.CENTER);
        topPanel.add(reportPanel, BorderLayout.SOUTH);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(tableScrollPane, BorderLayout.CENTER);
        mainPanel.add(totalPanel, BorderLayout.SOUTH);

        setContentPane(mainPanel);
    }
}
