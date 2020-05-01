package sample;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {

    private final int transactionId;
    private java.sql.Date transactionDate;
    private String description;
    private int accountId;
    private String categoryName;
    private BigDecimal income;
    private BigDecimal expense;

    public Transaction(int transactionId, java.sql.Date transactionDate, String description, int accountId, String categoryName, BigDecimal income, BigDecimal expense) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.description = description;
        this.accountId = accountId;
        this.categoryName = categoryName;
        this.income = income;
        this.expense = expense;
    }


    public java.sql.Date getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public BigDecimal getIncome() {
        return income;
    }


    public BigDecimal getExpense(){
        return expense;
    }
}
