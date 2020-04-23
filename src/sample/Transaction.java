package sample;

import java.math.BigDecimal;
import java.sql.Date;

public class Transaction {

    private final int transactionId;
    private java.sql.Date transactionDate;
    private String description;
    private int accountId;
    private int categoryId;
    private BigDecimal amount;

    public Transaction(int transactionId, java.sql.Date transactionDate, String description, int accountId, int categoryId, BigDecimal amount) {
        this.transactionId = transactionId;
        this.transactionDate = transactionDate;
        this.description = description;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.amount = amount;
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

    public BigDecimal getAmount() {
        return amount;
    }
}
