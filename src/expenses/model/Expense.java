package expenses.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Expense implements Serializable {
    private String name;
    private double amount;
    private String category;
    private LocalDate date;

    public Expense(String name, double amount, String category, LocalDate date) {
        this.name = name;
        this.amount = amount;
        this.category = category;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return date + " | " + name + " - ₹" + amount + " | Category: " + category;
    }
}
