package sample;

import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import models.Entry;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddentryController {
    public String selecteditem;
    @FXML
    private ChoiceBox<String> combobox;
    @FXML
    private ChoiceBox<String> comboboxfournisseur;
    @FXML
    public Button save;
    @FXML
    public TextField designationfid;
    @FXML
    public TextField fournisseuridfid;
    @FXML
    public TextField quantitefid;
    @FXML
    public TextField prixfid;
    @FXML
    public DatePicker datefid;
    @FXML
    public Button close;

    ObservableList<String> Listref = FXCollections.observableArrayList();
    ObservableList<String> Listfour = FXCollections.observableArrayList();
    String query = null;
    Connection connection = DbConnection.getConnect();
    public boolean update;
    public int ref,ide;
    public int quantiteprecedente ;
    public void initialize() {
        fillcombobox();
    }
public  ObservableList<Entry> data;
    //combobox
void fillcombobox() {
    String sql = "SELECT REFERENCE FROM ARTICLE";
    String sql1 = "SELECT NOM FROM FOURNISSEUR";
    try {
        Statement statement = connection.createStatement();
        Statement statement1 = connection.createStatement();
        ResultSet resultSet1 = statement.executeQuery(sql);
        ResultSet resultSet2 = statement1.executeQuery(sql1);
        while (resultSet1.next()) {
            Listref.add(resultSet1.getString("REFERENCE"));
        }
        while (resultSet2.next()) {
            Listfour.add(resultSet2.getString("NOM"));
        }
        combobox.setItems(Listref);
        comboboxfournisseur.setItems(Listfour);
    } catch (SQLException throwables) {
        throwables.printStackTrace();
    }
    combobox.setOnAction(event -> {
        try {
            Statement statement1 = connection.createStatement();
            selecteditem = combobox.getSelectionModel().getSelectedItem().toString();
            System.out.println(selecteditem);
            ResultSet resultSet = statement1.executeQuery("Select DESIGNATION , PRIXACHAT from ARTICLE where REFERENCE= '"+selecteditem+"'");
            while (resultSet.next()) {
                designationfid.setText(resultSet.getString("DESIGNATION"));
                prixfid.setText(resultSet.getString("PRIXACHAT"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    });
}
    @FXML
    public void close(){
            close.getScene().getWindow().hide();
    }
    @FXML
    public void save() throws IOException {

        String designation = designationfid.getText();
        String quantite = quantitefid.getText();
        String prix = prixfid.getText();
        if (designation.isEmpty() || quantite.isEmpty() || prix.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();

        } else {
            getQuery();
            insert();

        }
    }
    @FXML
    public void clean() {
        designationfid.setText(null);
        quantitefid.setText(null);
        prixfid.setText(null);
        datefid.setValue(null);

    }
    public void getQuery()   {
        if (!update) {
            query = "INSERT INTO ENTRY (REFERENCE,DESIGNATION,FOURNISSEURID,QUANTITE,PRIX,DATE) VALUES (?,?,?,?,?,?)";
        } else  {
            query = "UPDATE ENTRY SET REFERENCE = ?, DESIGNATION=?, FOURNISSEURID = ?, QUANTITE = ?, PRIX= ?,  DATE=? WHERE Id =?";
        }
    }
@FXML
    public void insert()  {
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);

    getQuery();
    switch (query){

        case "INSERT INTO ENTRY (REFERENCE,DESIGNATION,FOURNISSEURID,QUANTITE,PRIX,DATE) VALUES (?,?,?,?,?,?)":
            try {
                PreparedStatement preparedStatement;
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, combobox.getValue());
                preparedStatement.setString(2, designationfid.getText());
                preparedStatement.setString(3, comboboxfournisseur.getValue());
                preparedStatement.setString(4, quantitefid.getText());
                preparedStatement.setString(5, prixfid.getText());
                preparedStatement.setString(6,String.valueOf(datefid.getValue()));
                preparedStatement.executeUpdate();
                alert.setHeaderText(null);
                alert.setContentText("Ajout avec succès!");
                alert.showAndWait();
                    System.out.println("Reçu");
                    String sql = "UPDATE Article set QUANTITE =QUANTITE+? WHERE REFERENCE = ? ";
                    PreparedStatement P = connection.prepareStatement(sql);
                    P.setString(1, quantitefid.getText());
                    P.setString(2, combobox.getValue());
                    P.executeUpdate();
                    String s = "SELECT max(ID) FROM ENTRY";
                    Statement sr = connection.createStatement();
                    ResultSet resultSet = sr.executeQuery(s);
                    while (resultSet.next()) {
                        ide =resultSet.getInt(1);
                    }
                    String in = " INSERT INTO HISTORIQUE (EID,EREFERENCE,EDESIGNATION,EQUANTITE,EPRIX,EDATE) VALUES (?,?,?,?,?,?)";
                    System.out.println(in);
                    PreparedStatement p= connection.prepareStatement(in);
                    p.setInt(1, ide);
                    p.setString(2, combobox.getValue());
                    p.setString(3, designationfid.getText());
                    p.setString(4, quantitefid.getText());
                    p.setString(5, prixfid.getText());
                    p.setString(6, String.valueOf(datefid.getValue()));
                    p.executeUpdate();

                clean();
            } catch (SQLException ex) {
                Logger.getLogger(AddentryController.class.getName()).log(Level.SEVERE, null, ex);
            }
    break;
        case "UPDATE ENTRY SET REFERENCE = ?, DESIGNATION=?, FOURNISSEURID = ?, QUANTITE = ?, PRIX= ?,  DATE=? WHERE Id =?":
            try {
            PreparedStatement preparedStatement;
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, combobox.getValue());
            preparedStatement.setString(2, designationfid.getText());
            preparedStatement.setString(3, comboboxfournisseur.getValue());
            preparedStatement.setString(4, quantitefid.getText());
            preparedStatement.setString(5, prixfid.getText());
            preparedStatement.setString(6,String.valueOf(datefid.getValue()));
            preparedStatement.setInt(7, ref);
            preparedStatement.executeUpdate();
            alert.setHeaderText(null);
            alert.setContentText("Modification avec succés");
            alert.showAndWait();
                    quantiteprecedente=0;
                    System.out.println("Reçu");
                    String sql = "UPDATE Article set QUANTITE =(QUANTITE -? +? )WHERE REFERENCE = ? ";
                    PreparedStatement P = connection.prepareStatement(sql);
                    P.setInt(1, quantiteprecedente);
                    P.setInt(2, Integer.parseInt(quantitefid.getText()));
                    P.setString(3, combobox.getValue());
                    P.executeUpdate();
                String i = " UPDATE HISTORIQUE set EREFERENCE =?,EDESIGNATION=?,EQUANTITE=?,EPRIX=?,EDATE=? WHERE EID=?";
                PreparedStatement p;
                p = connection.prepareStatement(i);
                p.setString(1, combobox.getValue());
                p.setString(2, designationfid.getText());
                p.setString(3, quantitefid.getText());
                p.setString(4, prixfid.getText());
                p.setString(5, String.valueOf(datefid.getValue()));
                p.setInt(6,ref);
                p.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(AddentryController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
    @FXML
    public  void setTextField(int Id,String REFERENCE , String DESIGNATION, String FOURNISSEURID, int QUANTITE, float PRIX, LocalDate date ) {
        ref=Id;
        combobox.getSelectionModel().select(REFERENCE);
        designationfid.setText(DESIGNATION);
        comboboxfournisseur.getSelectionModel().select(FOURNISSEURID);
        quantitefid.setText(Integer.toString(QUANTITE));
        quantiteprecedente = QUANTITE;
        prixfid.setText(Float.toString(PRIX));
        datefid.setValue(date);
    }
    @FXML
    void setUpdate(boolean b) {
        this.update = b;
    }
}
