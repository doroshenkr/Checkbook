package sample;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

import java.awt.*;
import java.io.IOException;
import java.math.BigDecimal;
import java.security.InvalidParameterException;
import java.util.ArrayList;

import javafx.stage.Stage;
import sample.data.DBHandler;

import static sample.data.DBHandler.getAccounts;


public class AccountController {

    @FXML
    TextField accountNameTF;
    @FXML
    TextField accountBalanceTF;

    @FXML
    private void addAccount(){

        String name = accountNameTF.getText();
        String balanceString = accountBalanceTF.getText();

        System.out.println(name);
        System.out.println(balanceString);

        try{
            BigDecimal balance = new BigDecimal(balanceString);

            ArrayList<Account> accountList = new ArrayList<>(getAccounts());

            for(int i = 0; i < accountList.size(); i++){
                if(accountList.get(i).getName().equals(name)){
                    Alert errorMessage = new Alert(Alert.AlertType.WARNING);
                    errorMessage.setContentText("Account with name already exists.");
                    errorMessage.show();
                    return;
                }
            }

            DBHandler.addAccount(name, balance);
            Stage stage = (Stage) accountNameTF.getScene().getWindow();
            stage.close();

        } catch(NumberFormatException e){
            Alert errorMessage = new Alert(Alert.AlertType.WARNING);
            errorMessage.setContentText("Invalid Balance: Balance must be a valid decimal number");
            errorMessage.show();
        }

    }

}
