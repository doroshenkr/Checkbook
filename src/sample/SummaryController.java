package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.data.DBHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Properties;

import static sample.data.DBHandler.*;

public class SummaryController {

    @FXML TableView<Account> accountTableView;
    @FXML TableView<Transaction> transactionTableView;
    @FXML ToolBar dateToolBar;
    @FXML ToolBar transactionToolBar;
    @FXML Label selectedDateLabel;
    @FXML Button previousButton;
    @FXML Button nextButton;
    @FXML Button deleteAccountButton;


    private LocalDate selectedDate = LocalDate.now();

    public void initialize(){
        System.out.println("WELCOME!!!");

        selectedDateLabel.setText(formatDate(selectedDate));

        setAccountTableViewRowFactory();

        populateAccounts();

        if(accountTableView.getItems().size() > 0){
            accountTableView.getSelectionModel().select(0);
            populateTransactions(selectedDate);
        }


    }


    private String formatDate(LocalDate date){

        return date.format(DateTimeFormatter.ofPattern("MM/yyyy"));

    }

    @FXML
    private void setAccountTableViewRowFactory(){
        accountTableView.setRowFactory(tv -> {
            TableRow<Account> row = new TableRow<>();
            row.setOnMouseClicked(e -> {
                if (!row.isEmpty()) {
                    populateTransactions(selectedDate);
                }
            });
            return row ;
        });
    }


    @FXML
    private void decrementDateByMonth(){
        selectedDate = selectedDate.minus(Period.ofMonths(1));
        populateTransactions(selectedDate);
        selectedDateLabel.setText(formatDate(selectedDate));

    }

    @FXML
    private void incrementDateByMonth(){
        selectedDate = selectedDate.plus(Period.ofMonths(1));
        populateTransactions(selectedDate);
        selectedDateLabel.setText(formatDate(selectedDate));

    }


    @FXML
    private void populateTransactions(LocalDate selectedDate){

        Account currentAccount = accountTableView.getSelectionModel().getSelectedItem();

        if(currentAccount != null) {
            transactionTableView.getItems().setAll(getTransactions(selectedDate, currentAccount));
            transactionTableView.getItems().add(new Transaction(5, new Date(2020, 4, 20), "test", 1, "Name", null, new BigDecimal(8.00)));
        }

    }


    @FXML
    private void populateAccounts(){

        accountTableView.getItems().setAll(getAccounts());

    }


    @FXML
    private void openExpenseWindow(){

        try{
//            Parent root = FXMLLoader.load(getClass().getResource("transaction.fxml"));

            FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));

            Parent root = loader.load();

            TransactionController transactionController = loader.getController();
            transactionController.setDate(selectedDate);
            transactionController.setAccountCB(getAccounts());
            transactionController.setCategoryCB(getCategories());


            Stage stage = new Stage();
            stage.setTitle("Add Transaction");
            stage.setScene(new Scene(root, 250, 250));
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

//            FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
//            Parent root = loader.load();
//            TransactionController transactionController = loader.getController();
//            transactionController.setLabel("Look! We've just passed info between stages");

            stage.showAndWait();
            populateTransactions(selectedDate);
        } catch(IOException e){
            System.out.println(e);
        }

    }

    @FXML
    private void openAccountWindow(){


        try{
            Parent root = FXMLLoader.load(getClass().getResource("addAccountWindow.fxml"));

            Stage stage = new Stage();
            stage.setTitle("Add Account");
            stage.setScene(new Scene(root, 250, 250));
            stage.setResizable(false);
            //stage.setAlwaysOnTop(true);
            stage.initModality(Modality.APPLICATION_MODAL);

            stage.showAndWait();
            accountTableView.getItems().setAll(getAccounts());

        } catch (IOException e){
            System.out.println(e);
        }
    }

    @FXML
    private void deleteAccount(){

        if(accountTableView.getSelectionModel().getSelectedItem() != null){
            Account selectedAccount = accountTableView.getSelectionModel().getSelectedItem();
            DBHandler.deleteAccount(selectedAccount);
            populateAccounts();
        }

    }

}
