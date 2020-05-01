package sample;

import java.math.BigDecimal;

public class Account {

    private final int accountID;
    private String name;
    private BigDecimal balance;

    public Account(int accountID, String name, BigDecimal balance) {
        this.accountID = accountID;
        this.name = name;
        this.balance = balance;
    }

    public int getAccountID() {
        return accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return name;
    }
}
