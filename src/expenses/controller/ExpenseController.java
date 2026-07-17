package expenses.controller;

import expenses.model.Expense;
import expenses.model.ExpenseManager;
import expenses.view.ExpenseView;

import javax.swing.SwingUtilities;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseController {
    private final ExpenseManager model;
    private final ExpenseView view;

    public ExpenseController() {
        this.model = new ExpenseManager();
        this.view = new ExpenseView(this);
    }

    public void start() {
        view.showWindow();
        refreshExpenseTable();
    }

    public void addExpense(String name, double amount, String category, LocalDate date) {
        if (name == null || name.trim().isEmpty()) {
            view.showError("Expense name cannot be empty.");
            return;
        }

        if (amount <= 0) {
            view.showError("Amount must be greater than zero.");
            return;
        }

        model.addExpense(new Expense(name.trim(), amount, category, date));
        view.showMessage("Expense added successfully!");
        refreshExpenseTable();
    }

    public void viewAllExpenses() {
        refreshExpenseTable();
    }

    public void showMonthlyReport(int month, int year) {
        List<Expense> filtered = new ArrayList<>();

        for (Expense expense : model.getExpenses()) {
            if (expense.getDate().getMonthValue() == month && expense.getDate().getYear() == year) {
                filtered.add(expense);
            }
        }

        view.showExpenses(filtered);
        view.showMessage("Monthly report loaded for " + month + "/" + year + ".");
    }

    public void showTotalSpending() {
        view.showTotal(model.getTotal());
    }

    public void refreshExpenseTable() {
        view.showExpenses(model.getExpenses());
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ExpenseController().start());
    }
}
