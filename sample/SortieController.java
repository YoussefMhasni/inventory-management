package sample;

import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import models.Sortie;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class SortieController {

    @FXML
    public Button login;
    @FXML
    public Button logout;
    @FXML
    public Button bureau;
    @FXML
    public Button stock;
    @FXML
    public Button sortie;
    @FXML
    public Button addsortie;
    @FXML
    public Button entry;
    @FXML
    public Button fournisseur;
    @FXML
    public Button client;
    @FXML
    public Button historique;
    @FXML
    public Button Article;
    @FXML
    public Button parametre;
    @FXML
    public TableView<Sortie> SortieTable;
    @FXML
    public TableColumn<Sortie, String> referencecol;
    @FXML
    public TableColumn<Sortie, String> idcol;
    @FXML
    public TextField filterField;
    @FXML
    public TableColumn<Sortie, String> designationcol;

    @FXML
    public TableColumn<Sortie, String> clientcol;

    @FXML
    public TableColumn<Sortie, String> quantitecol;

    @FXML
    public TableColumn<Sortie, String> prixcol;
    @FXML

    public TableColumn<Sortie, String> prixcoltotal;
    @FXML
    public TableColumn<Sortie, String> datecol;
    @FXML
    public TableColumn<Sortie, String> editCol;
    @FXML
    public TableColumn<Sortie, String> bccol;


    String query = null;
    Connection connection = DbConnection.getConnect();
    ResultSet resultSet = null;
    public ObservableList<Sortie> SortieList = FXCollections.observableArrayList();

    public void initialize() {
        loadData();
        FilteredList<Sortie> filteredData = new FilteredList<>(SortieList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Sortie -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Sortie.getREFERENCE()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Sortie.getDESIGNATION()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Sortie.getCLIENT()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Sortie.getDATE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Sortie.getPRIX()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Sortie.getQUANTITE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Sortie.getBC()).toLowerCase().indexOf(lowerCaseFilter)!=-1)
                    return true;
                else
                    return false; // Does not match.
            });
        });
        SortedList<Sortie> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(SortieTable.comparatorProperty());
        SortieTable.setItems(sortedData);
    }

    @FXML
    public void refreshsortie() {
        SortieList.clear();
        try {
            String sql = "SELECT * FROM SORTIE ";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                SortieList.add(new Sortie(
                        resultSet.getInt("Id"),
                        resultSet.getInt("BC"),
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("CLIENT"),
                        resultSet.getInt("QUANTITE"),
                        resultSet.getFloat("PRIX"),
                        resultSet.getFloat("PRIX")*resultSet.getInt("QUANTITE"),

                        resultSet.getDate("DATE").toLocalDate()));
            }
            SortieTable.setItems(SortieList);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void loadData() {

        refreshsortie();
        idcol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        bccol.setCellValueFactory(new PropertyValueFactory<>("BC"));
        referencecol.setCellValueFactory(new PropertyValueFactory<>("REFERENCE"));
        designationcol.setCellValueFactory(new PropertyValueFactory<>("DESIGNATION"));
        clientcol.setCellValueFactory(new PropertyValueFactory<>("CLIENT"));
        quantitecol.setCellValueFactory(new PropertyValueFactory<>("QUANTITE"));
        prixcol.setCellValueFactory(new PropertyValueFactory<>("PRIX"));
        prixcoltotal.setCellValueFactory(new PropertyValueFactory<>("TOTAL"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("DATE"));
        Callback<TableColumn<Sortie, String>, TableCell<Sortie, String>> cellFoctory = (TableColumn<Sortie, String> param) ->
        {
            // make cell containing buttons
            TableCell<Sortie, String> cell = new TableCell<Sortie, String>() {
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    //that cell created only on non-empty rows
                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {
                        Button deleteIcon = new Button("Supprimer");
                        deleteIcon.setMinWidth(70);
                        deleteIcon.setStyle("-fx-background-color: #ff0000 ;-fx-text-fill: #ffffff;");
                        Button editIcon = new Button("Modifier");
                        editIcon.setStyle("-fx-background-color :#FF8C00;-fx-text-fill: #ffffff;");
                        editIcon.setMinWidth(50);

                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            try {
                                Sortie en = SortieTable.getSelectionModel().getSelectedItem();
                                String sql = "DELETE FROM SORTIE WHERE Id  = ?";
                                PreparedStatement Statement = connection.prepareStatement(sql);
                                Statement.setInt(1, en.getId());
                                Statement.executeUpdate();
                                refreshsortie();
                            } catch (SQLException ex) {
                                Logger.getLogger(SortieController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Sortie e = SortieTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("Addsortie.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(SortieController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            AddsortieController addsortieController = loader.getController();
                            addsortieController.setUpdate(true);
                            addsortieController.setTextField(e.getId(),e.getREFERENCE(),e.getBC(), e.getDESIGNATION(), e.getCLIENT(), e.getQUANTITE(), e.getPRIX(), e.getDATE());
                            Parent parent = loader.getRoot();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(parent));
                            stage.initStyle(StageStyle.UTILITY);
                            stage.show();
                        });
                      
                        HBox managebtn = new HBox(editIcon, deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(deleteIcon, new Insets(2, 1, 0, 3));
                        HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                        setGraphic(managebtn);
                        setText(null);
                        }
                }
            };

            return cell;
        };
        editCol.setCellFactory((cellFoctory));
        SortieTable.setItems(SortieList);
    }


    //Sortie TABLE CONTROLLER OTHER WINDOWS

    @FXML
    void addsortie() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Addsortie.fxml"));
        primaryStage.setTitle("Ajout de sortie");
        Image logo = new Image("cerdeleclogoapp.png");

        primaryStage.getIcons().add(logo);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);

    }
    @FXML
    void bl() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("bon.fxml"));
        primaryStage.setTitle("Ajout de BON");
        Image logo = new Image("cerdeleclogoapp.png");
        primaryStage.getIcons().add(logo);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    @FXML
    void facture() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("facture.fxml"));

        primaryStage.setTitle("Facture");
        Image logo = new Image("cerdeleclogoapp.png");

        primaryStage.getIcons().add(logo);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


}
