package sample;

import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddsortieController implements Initializable {
    public String selecteditem;
    @FXML
    private ChoiceBox<String> combobox;

    @FXML
    public Button save;
    @FXML
    public Button delete;
    @FXML
    public ChoiceBox<String> comboboxclient;
    @FXML
    public TextField designationfid;
    @FXML
    public TextField bcfid;
    @FXML
    public TextField quantitefid;
    @FXML
    public TextField prixfid;
    @FXML
    public DatePicker datefid;
    @FXML
    private Button close;

    int qtit;
    int var;
    ObservableList<String> Listclient = FXCollections.observableArrayList();
    ObservableList<String> Listref = FXCollections.observableArrayList();
    ObservableList<String> listrefentry = FXCollections.observableArrayList();
    ObservableList<Integer> listqentry = FXCollections.observableArrayList();
    //  public String statut= spinnerstatut.getValue();
    public void initialize(URL url, ResourceBundle bdl) {
        fillcombobox();
    }
    String query = null;
    Connection connection  = DbConnection.getConnect();
    ResultSet resultSet = null;
    public boolean update;
    public int ref,ide;
    public int quantiteprecedente;
    void fillcombobox() {
        String sql = "SELECT * FROM ARTICLE";
        String sql2 = "SELECT * FROM Client";

        try {
            Statement statement = connection.createStatement();
            Statement st = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(sql);
            ResultSet resultSet2 = st.executeQuery(sql2);
            while (resultSet1.next()) {
                Listref.add(resultSet1.getString("REFERENCE"));
            }
            while (resultSet2.next()) {
                Listclient.add(resultSet2.getString("NOM"));
            }
            combobox.setItems(Listref);
            comboboxclient.setItems(Listclient);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        combobox.setOnAction(event -> {
            try {
                Statement statement1 = connection.createStatement();
                selecteditem = combobox.getSelectionModel().getSelectedItem().toString();
                ResultSet resultSet = statement1.executeQuery("Select DESIGNATION , PRIXVENTE from ARTICLE where REFERENCE= '"+ selecteditem+"'");
                while (resultSet.next()) {
                    designationfid.setText(resultSet.getString("DESIGNATION"));
                    prixfid.setText(resultSet.getString("PRIXVENTE"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    @FXML
    public void close() throws IOException {
        close.getScene().getWindow().hide();
    }
    @FXML
    public void save(MouseEvent event) throws SQLException, InterruptedException {

        connection = DbConnection.getConnect();
        String sql ="Select REFERENCE , QUANTITE from article";
        Statement stat = connection.createStatement();
        ResultSet rs = stat.executeQuery(sql);
        while (rs.next()) {
            listrefentry.add(rs.getString(1));
            listqentry.add(rs.getInt(2));
        }
        System.out.println(listrefentry);
        System.out.println(listqentry);
        String designation = designationfid.getText();
        String quantite = quantitefid.getText();
        int quantitevaleur = Integer.parseInt(quantitefid.getText());
        String prix = prixfid.getText();

        Alert alert = new Alert(Alert.AlertType.WARNING);
        if (designation.isEmpty() || quantite.isEmpty() || prix.isEmpty()) {
            alert.setHeaderText(null);
            alert.setContentText("Please Fill All DATA");
            alert.showAndWait();
        }
        Alert alert1= new Alert(Alert.AlertType.WARNING);

        for (int i = 0; i < listrefentry.size(); i++) {
            if (!(combobox.getValue().equals(listrefentry.get(i))) && i == listrefentry.size() - 1) {
                alert.setHeaderText(null);
                alert.setContentText("Cet Article n'est pas encore en stock");
                alert.showAndWait();

                 break;
            } else if (combobox.getValue().equals(listrefentry.get(i)) && quantitevaleur > quantiteprecedente +listqentry.get(i)) {
                System.out.println(quantiteprecedente + " " + quantitevaleur);
                var=quantiteprecedente +listqentry.get(i);
                if (quantiteprecedente < quantitevaleur) {
                    alert1.setContentText("Vous n'avez que " + var + " en stock!");
                    alert1.showAndWait();
                    break;
                }
            }else if(combobox.getValue().equals(listrefentry.get(i))&& quantitevaleur <= quantiteprecedente +listqentry.get(i)){
                    insert();
                    clean();
                    break;
            }
        }
        }
    @FXML
    public void clean() {
        designationfid.setText(null);
        quantitefid.setText(null);
        prixfid.setText(null);
        datefid.setValue(null);
    }

    public void getQuery() throws SQLException {
        if (update == false) {
            query = "INSERT INTO SORTIE (BC,REFERENCE,DESIGNATION,CLIENT,QUANTITE,PRIX,DATE) VALUES (?,?,?,?,?,?,?)";
        } else  {
            query = "UPDATE SORTIE SET BC=?,REFERENCE = ?, DESIGNATION=?, CLIENT = ?, QUANTITE = ?, PRIX= ?, DATE=? WHERE Id =?";
        }
        System.out.println(query+"1");
    }
    @FXML
    public void insert() throws SQLException {
        Connection conn = DbConnection.getConnect();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        getQuery();
        switch (query){
            case "INSERT INTO SORTIE (BC,REFERENCE,DESIGNATION,CLIENT,QUANTITE,PRIX,DATE) VALUES (?,?,?,?,?,?,?)":
                System.out.println(query+"2");
                try {
                    PreparedStatement preparedStatement;
                    preparedStatement = conn.prepareStatement(query);
                    preparedStatement.setString(1, bcfid.getText());
                    preparedStatement.setString(2, combobox.getValue());
                    preparedStatement.setString(3, designationfid.getText());
                    preparedStatement.setString(4, comboboxclient.getValue());
                    preparedStatement.setString(5, quantitefid.getText());
                    preparedStatement.setString(6, prixfid.getText());
                    preparedStatement.setString(7,String.valueOf(datefid.getValue()));
                    preparedStatement.execute();
                    alert.setHeaderText(null);
                    alert.setContentText("Ajout avec succès!");
                    alert.showAndWait();
                    String sql ="UPDATE Article set QUANTITE =QUANTITE-? WHERE REFERENCE = ? ";
                    PreparedStatement P= connection.prepareStatement(sql);
                    P.setString(1, quantitefid.getText());
                    P.setString(2, combobox.getValue());
                    P.executeUpdate();
                        String s = "SELECT max(ID) FROM SORTIE";
                        Statement sr = connection.createStatement();
                        ResultSet resultSet = sr.executeQuery(s);
                        while (resultSet.next()) {
                            ide = resultSet.getInt(1);
                        }
                        String in = " INSERT INTO SHISTORIQUE (SID,SREFERENCE,SDESIGNATION,SQUANTITE,SPRIX,SDATE) VALUES (?,?,?,?,?,?)";
                        System.out.println(in);
                        PreparedStatement p = connection.prepareStatement(in);
                        p.setInt(1, ide);
                        p.setString(2, combobox.getValue());
                        p.setString(3, designationfid.getText());
                        p.setString(4, quantitefid.getText());
                        p.setString(5, prixfid.getText());
                        p.setString(6, String.valueOf(datefid.getValue()));
                        p.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(AddsortieController.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case "UPDATE SORTIE SET BC=?,REFERENCE = ?, DESIGNATION=?, CLIENT = ?, QUANTITE = ?, PRIX= ?, DATE=? WHERE Id =?":
                System.out.println(quantiteprecedente);
                System.out.println(quantitefid.getText());
                try {
                    // Statement preparedstatement = connection.createStatement();
                    PreparedStatement preparedStatement;
                    preparedStatement = conn.prepareStatement(query);

                    preparedStatement.setString(1, bcfid.getText());
                    preparedStatement.setString(2, combobox.getValue());
                    preparedStatement.setString(3, designationfid.getText());
                    preparedStatement.setString(4, comboboxclient.getValue());
                    preparedStatement.setString(5, quantitefid.getText());
                    preparedStatement.setString(6, prixfid.getText());
                    preparedStatement.setString(7,String.valueOf(datefid.getValue()));
                    preparedStatement.setInt(8, ref);
                    preparedStatement.executeUpdate();
                    alert.setHeaderText(null);
                    alert.setContentText("Mise a jour avec succès!");
                    alert.showAndWait();
                    String sql ="UPDATE Article set QUANTITE =(QUANTITE +? -? )WHERE REFERENCE = ? ";
                    PreparedStatement P= connection.prepareStatement(sql);
                    P.setInt(1, quantiteprecedente);
                    P.setString(2, quantitefid.getText());
                    P.setString(3, combobox.getValue());
                    P.executeUpdate();

                        String i = " UPDATE SHISTORIQUE set SREFERENCE =?,SDESIGNATION=?,SQUANTITE=?,SPRIX=?,SDATE=? WHERE SID=?";
                        PreparedStatement p;
                        p = connection.prepareStatement(i);
                        p.setString(1, combobox.getValue());
                        p.setString(2, designationfid.getText());
                        p.setString(3, quantitefid.getText());
                        p.setString(4, prixfid.getText());
                        p.setString(5, String.valueOf(datefid.getValue()));
                        p.setInt(6, ref);
                        p.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(AddsortieController.class.getName()).log(Level.SEVERE, null, ex);
                }
        }

    }
    @FXML
    public  void setTextField(int id, String REFERENCE,int BC,  String DESIGNATION, String CLIENT, int QUANTITE, float PRIX, LocalDate date) {
        ref=id;
        quantiteprecedente=QUANTITE;
        combobox.getSelectionModel().select(REFERENCE);
        bcfid.setText(Integer.toString(BC));
        designationfid.setText(DESIGNATION);
        comboboxclient.getSelectionModel().select(CLIENT);
        quantitefid.setText(Integer.toString(QUANTITE));
        prixfid.setText(Float.toString(PRIX));
        datefid.setValue(date);
    }
    @FXML
    void setUpdate(boolean b) {
        this.update = b;
    }
}

