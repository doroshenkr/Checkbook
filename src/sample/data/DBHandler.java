package sample.data;

import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import sample.Account;
import sample.Category;
import sample.Transaction;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class DBHandler {

    public static Connection establishConnection(){
        Connection connection = null;
        Properties connectionProperties = new Properties();
        connectionProperties.put("user", "test");
        connectionProperties.put("password", "test");

        try{
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/checkbook", connectionProperties);
            return connection;
        } catch(SQLException e){
            System.out.println(e);
        }

        return null;
    }

    public static List<Category> getCategories(){

        String query = "Select * from category";
        ArrayList<Category> categoryList = new ArrayList<>();

        try{
            Connection connection = establishConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query);

            while(resultSet.next()){
                categoryList.add(new Category(resultSet.getInt("category_id"), resultSet.getString("name"), resultSet.getString("type")));
            }

            connection.close();

        } catch (SQLException e){
            System.out.println(e);
        }
        return categoryList;
    }


    public static void addTransaction(Transaction transaction){



    }

    public static List<Account> getAccounts(){
        List<Account> accountList = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM account");

        try{
            Connection connection = establishConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());

            while(resultSet.next()){
                accountList.add(new Account(resultSet.getInt("account_id"),resultSet.getString("name"), resultSet.getBigDecimal("balance")));
            }

            accountList.add(new Account(99,"Test Account", new BigDecimal(100.00)));

            connection.close();

        } catch (SQLException e){
            System.out.println(e);
        }
        return accountList;

    }

    public static List<Transaction> getTransactions(LocalDate date, Account account){

        ArrayList<Transaction> transactionList = new ArrayList<>();
        StringBuilder query = new StringBuilder();

        query.append("SELECT transaction.*, category.name AS category_name ");
        query.append("FROM transaction INNER JOIN category ON transaction.category_id = category.category_id ");

        query.append("WHERE account_id = ");
        query.append(account.getAccountID());
        query.append(" AND MONTH(transaction_date) = MONTH('" + date + "') " +
                "AND YEAR(transaction_date) = YEAR('" + date + "')");

        try{
            Connection connection = establishConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());

            while(resultSet.next()){
                int id = resultSet.getInt("transaction_id");
                java.sql.Date ftdate = resultSet.getDate("transaction_date");
                String description = resultSet.getString("description");
                int accountID = resultSet.getInt("account_id");
                String categoryName = resultSet.getString("category_name");
                BigDecimal income = resultSet.getBigDecimal("income");
                BigDecimal expense = resultSet.getBigDecimal("expense");
                transactionList.add(new Transaction(id, ftdate, description, accountID, categoryName, income, expense));
            }
            connection.close();
        } catch (SQLException e){
            System.out.println(e);
        }

        return transactionList;

    }

    public static void addAccount(String name, BigDecimal balance){

        StringBuilder query = new StringBuilder();

        query.append("INSERT INTO account (name, balance) VALUES ");
        query.append("('" + name + "', " + balance + ")");
        System.out.println(query);

        try{
            Connection connection = establishConnection();
            Statement statement = connection.createStatement();
            statement.execute(query.toString());

            connection.close();

        } catch(SQLException e){
            System.out.println(e);
        }


    }

    public static void deleteAccount(Account account){

        try{

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Account Deletion");
            alert.setHeaderText("Delete Account: " + account.getName());
            alert.setContentText("Are you sure you want to delete this account?\n" +
                    "All associated transactions will also be deleted.\n\t\t\t\t");

            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){


                Connection connection = establishConnection();
                Statement statement = connection.createStatement();

                String query = "DELETE FROM transaction WHERE account_id = " + account.getAccountID();
                statement.execute(query);

                query = "DELETE FROM account WHERE account_id = " + account.getAccountID();
                statement.execute(query);

                connection.close();

            }



        } catch(SQLException e){
            System.out.println(e);
        }

    }

}
