package sample;

import helpers.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminController {
    @FXML
    private TextField nomfid;
    @FXML
    private TextField usernamefid;
    @FXML
    private PasswordField mdpfid;
    Connection connection = DbConnection.getConnect();
    @FXML
    void modifier() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("modifyadmin.fxml"));
        try {
            loader.load();
        } catch (IOException ex) {
            Logger.getLogger(sample.ClientController.class.getName()).log(Level.SEVERE, null, ex);
        }
        ModifyadminController modifyadminController = loader.getController();
        modifyadminController.setTextField(nomfid.getText(),usernamefid.getText(),mdpfid.getText());
        Parent parent = loader.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(parent));
        stage.setTitle("Modification Admin");
        Image logo = new Image("cerdeleclogoapp.png");
        stage.getIcons().add(logo);
        stage.show();
    }
    public void initialize(){
        String sql = "SELECT * FROM ADMIN";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                nomfid.setText(resultSet.getString("NOM"));
                usernamefid.setText(resultSet.getString("USERNAME"));
                mdpfid.setText(resultSet.getString("MDP"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
