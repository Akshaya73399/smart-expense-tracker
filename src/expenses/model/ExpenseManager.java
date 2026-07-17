package expenses.model;
import java.util.*;
import java.io.*;

public class ExpenseManager {
    private ArrayList<Expense> expenses = new ArrayList<>();
    private File dataFile = new File("expenses.dat");

    public ExpenseManager(){
        loadExpenses();
    }

    public void addExpense(Expense e){
        expenses.add(e);
        saveExpenses();
    }

    public ArrayList<Expense> getExpenses(){
        return expenses;
    }

    public double getTotal(){
        double total = 0;
        for(Expense e : expenses) total += e.getAmount();
        return total;
    }

    public void saveExpenses(){
        try (ObjectOutputStream o = new ObjectOutputStream(new FileOutputStream(dataFile))){
            o.writeObject(expenses);
        } catch(Exception e){ System.out.println("Error saving data."); }
    }

    public void loadExpenses(){
        try (ObjectInputStream o = new ObjectInputStream(new FileInputStream(dataFile))){
            expenses = (ArrayList<Expense>) o.readObject();
        } catch(Exception e){ expenses = new ArrayList<>(); }
    }
}
