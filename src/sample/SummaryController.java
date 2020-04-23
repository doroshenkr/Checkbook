package sample;

import com.mysql.cj.result.SqlDateValueFactory;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

public class SummaryController {

    @FXML
    TableView<Account> accountTableView;

    @FXML
    TableView<Transaction> transactionTableView;

    @FXML
    GridPane controlGridPane;

    Date selectedDate = getTodaysDate();

    public void initialize(){
        System.out.println("WELCOME!!!");

        accountTableView.setRowFactory(tv -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                   populateTransactions(selectedDate);

                }
            });
            return row ;
        });

        setupControls();

        createAccountTVColumns();
        createTransactionTVColumns();

        populateAccounts();
        accountTableView.getSelectionModel().select(0);
        populateTransactions(selectedDate);
        //populateTransactions();

        //transactionTableView.getItems().get(0).setDescription("This is the new description!");
    }

    private Date getTodaysDate(){

        long millis=System.currentTimeMillis();
        java.sql.Date date=new java.sql.Date(millis);

        return (Date)date;


    }

    @FXML
    private void setupControls(){

        Button button = new Button();
        button.setId("previous");
        button.setText("Previous");
        button.setOnMouseClicked(e -> {

            Calendar calendar = Calendar
//            Alert alert = new Alert(Alert.AlertType.WARNING);
//            alert.setTitle("Previous");
//            alert.show();

        });
        controlGridPane.add(button,0, 0);

        button = new Button();
        button.setId("next");
        button.setText("Next");
        controlGridPane.add(new Button("Next"),1, 0);

    }

    @FXML
    private void populateTransactions(Date selectedDate){
        System.out.println(selectedDate);

            List<Transaction> transactionList = new ArrayList();
            StringBuilder query = new StringBuilder();
            Account currentAccount;

            if(accountTableView.getSelectionModel().getSelectedItem() != null){
                currentAccount = accountTableView.getSelectionModel().getSelectedItem();
                query.append("SELECT * FROM fintransaction WHERE " +
                        "fintransaction_id = ");
                query.append(currentAccount.getAccountID());
                query.append(" AND MONTH(fintransaction_date) = MONTH('" + selectedDate + "') " +
                        "AND YEAR(fintransaction_date) = YEAR('" + selectedDate + "')");
            } else{
                return;
            }

            try{
                Connection connection = establishConnection();
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query.toString());

                while(resultSet.next()){
                    int id = resultSet.getInt("fintransaction_id");
                    java.sql.Date date = resultSet.getDate("fintransaction_date");
                    String description = resultSet.getString("fintransaction_description");
                    int accountID = resultSet.getInt("finaccount_id");
                    int categoryID = resultSet.getInt("category_id");
                    BigDecimal amount = resultSet.getBigDecimal("amount");
                   transactionList.add(new Transaction(id, date, description, accountID, categoryID, amount));
                }

                transactionTableView.getItems().setAll(transactionList);
                connection.close();
            } catch (SQLException e){
                System.out.println(e);
            }

    }

    private void createTransactionTVColumns(){

        List<TableColumn> columns = new ArrayList<>();

        TableColumn<Transaction, java.sql.Date> transactionDateTableColumn = new TableColumn<>("Date");
        transactionDateTableColumn.setCellValueFactory(new PropertyValueFactory("transactionDate"));
        columns.add(transactionDateTableColumn);

        TableColumn<Transaction, java.sql.Date> transactionDescriptionTableColumn = new TableColumn<>("Description");
        transactionDescriptionTableColumn.setCellValueFactory(new PropertyValueFactory("description"));

        TableColumn<Transaction, java.sql.Date> transactionAmountTableColumn = new TableColumn<>("Amount");
        transactionAmountTableColumn.setCellValueFactory(new PropertyValueFactory("amount"));

        transactionTableView.getColumns().setAll(transactionDateTableColumn, transactionDescriptionTableColumn, transactionAmountTableColumn);
    }



    @FXML
    private void populateAccounts(){

        List<Account> accountList = new ArrayList<>();
        StringBuilder query = new StringBuilder("SELECT * FROM finaccount");

        try{
            Connection connection = establishConnection();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(query.toString());

            while(resultSet.next()){
                accountList.add(new Account(resultSet.getInt("finaccount_id"),resultSet.getString("finaccount_name"), resultSet.getBigDecimal("balance")));
            }

            accountTableView.getItems().setAll(accountList);

            connection.close();
        } catch (SQLException e){
            System.out.println(e);
        }

    }

    @FXML
    private void createAccountTVColumns(){
        TableColumn<Account, String> accountNameColumn = new TableColumn<>("Account Name");
        accountNameColumn.setCellValueFactory(new PropertyValueFactory("name"));

        TableColumn<Account, BigDecimal> accountBalanceColumn = new TableColumn<>("Balance");
        accountBalanceColumn.setCellValueFactory(new PropertyValueFactory("balance"));

        accountTableView.getColumns().setAll(accountNameColumn, accountBalanceColumn);
    }

    private Connection establishConnection(){
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

}
