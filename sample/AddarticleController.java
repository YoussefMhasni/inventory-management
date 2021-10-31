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

public class AddarticleController {
    @FXML
    public Button save;
    @FXML
    public TextField referencefid;
    @FXML
    public TextField designationfid;
    @FXML
    public TextField marquefid;
    @FXML
    public TextField prixachatfid;
    @FXML
    public TextField prixventefid;
    @FXML
    private Button close;
    String query = null;
    Connection connection = DbConnection.getConnect();
    public boolean update;
    public String ref;
    @FXML
    public void close(){
        close.getScene().getWindow().hide();
    }
    @FXML
    public void save() {
        String reference = referencefid.getText();
        String designation = designationfid.getText();
        String marque = marquefid.getText();
        String prixachat = prixachatfid.getText();
        String prixvente = prixventefid.getText();
        if (reference.isEmpty() || designation.isEmpty() || marque.isEmpty() || prixachat.isEmpty() || prixvente.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            getQuery();
            insert();
            clean();
        }
    }
    @FXML
    public void clean() {
        referencefid.setText(null);
        designationfid.setText(null);
        marquefid.setText(null);
        prixachatfid.setText(null);
        prixventefid.setText(null);
    }
    public void getQuery()   {
        if (!update) {
            query = "INSERT INTO ARTICLE  VALUES (?,?,?,?,?,0)";
        } else  {
            query = "UPDATE ARTICLE SET REFERENCE = ?, DESIGNATION=?, MARQUE = ?, PRIXACHAT = ?, PRIXVENTE= ? ,QUANTITE =QUANTITE WHERE REFERENCE =?";
        }
    }
    @FXML
    public void insert()  {
        switch (query){
            case "INSERT INTO ARTICLE  VALUES (?,?,?,?,?,0)":
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

                try {
                    PreparedStatement preparedStatement;
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, referencefid.getText());
                    preparedStatement.setString(2, designationfid.getText());
                    preparedStatement.setString(3, marquefid.getText());
                    preparedStatement.setString(4, prixachatfid.getText());
                    preparedStatement.setString(5, prixventefid.getText());
                    preparedStatement.executeUpdate();
                    alert.setHeaderText(null);
                    alert.setContentText("Ajout avec succès!");
                    alert.showAndWait();
                } catch (SQLException ex) {
                    Logger.getLogger(AddentryController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "UPDATE ARTICLE SET REFERENCE = ?, DESIGNATION=?, MARQUE = ?, PRIXACHAT = ?, PRIXVENTE= ? ,QUANTITE =QUANTITE WHERE REFERENCE =?":
                try {
                    PreparedStatement preparedStatement;
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, referencefid.getText());
                    preparedStatement.setString(2, designationfid.getText());
                    preparedStatement.setString(3, marquefid.getText());
                    preparedStatement.setString(4, prixachatfid.getText());
                    preparedStatement.setString(5, prixventefid.getText());
                    preparedStatement.setString(6, ref);
                    preparedStatement.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(AddentryController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
    }
    @FXML
    public  void setTextField(String REFERENCE , String DESIGNATION, String MARQUE, float PRIXACHAT, float PRIXVENTE ) {
        ref=REFERENCE;
        referencefid.setText(REFERENCE);
        designationfid.setText(DESIGNATION);
        marquefid.setText(MARQUE);
        prixachatfid.setText(Float.toString(PRIXACHAT));
        prixventefid.setText(Float.toString(PRIXVENTE));

    }
    @FXML
    void setUpdate(boolean b) {
        this.update = b;
    }
}

