package sample;

import helpers.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ModifyadminController {

    @FXML
    private Button close;
    @FXML
    private TextField nommodif;
    @FXML
    private TextField usernamemodif;
    @FXML
    private TextField mdpmodif;

    Connection connection = DbConnection.getConnect();

    @FXML
    void close() {
        close.getScene().getWindow().hide();
    }
    @FXML
    void save() {
            String query = "UPDATE ADMIN SET NOM=?, USERNAME = ?, MDP = ?";
            try {
                PreparedStatement preparedStatement;
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, nommodif.getText());
                preparedStatement.setString(2, usernamemodif.getText());
                preparedStatement.setString(3, mdpmodif.getText());
                preparedStatement.executeUpdate();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setHeaderText(null);
                alert.setContentText("Mise a jour avec succès!");
                alert.showAndWait();
                AdminController m = new AdminController();
                m.initialize();
            } catch (SQLException ex) {
                Logger.getLogger(sample.ModifyadminController.class.getName()).log(Level.SEVERE, null, ex);
            }

    }
    @FXML
    public  void setTextField(String NOM , String USERNAME, String MDP) {
        nommodif.setText(NOM);
        usernamemodif.setText(USERNAME);
        mdpmodif.setText(MDP);
    }
}
