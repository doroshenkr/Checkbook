package sample;


import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.util.List;

import static sample.data.DBHandler.*;

public class TransactionController {

    @FXML
    Label testLabel;
    @FXML
    DatePicker transactionDatePicker;
    @FXML
    ComboBox<Account> accountCB;
    @FXML
    ComboBox<Category> categoryCB;

    protected void setLabel(String labelText){
        testLabel.setText(labelText);
    }

    protected  void setDate(LocalDate date){
        transactionDatePicker.setValue(date);
    }

    protected void setAccountCB(List<Account> accountList){
        accountCB.getItems().setAll(accountList);
        if(accountCB.getItems().size() > 0){
            accountCB.getSelectionModel().select(0);
        }
    }

    protected void setCategoryCB(List<Category> categoryList){
        categoryCB.getItems().setAll(categoryList);
    }

    private void addTransaction(){

    }

}
