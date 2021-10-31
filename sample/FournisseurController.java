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
import models.Fournisseur;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FournisseurController {
        @FXML
        public TextField filterField;

    @FXML
    private TableView<Fournisseur> FournisseurTable;
    @FXML
    private TableColumn<Fournisseur, String> idcol;

    @FXML
    private TableColumn<Fournisseur, String> nomcol;

    @FXML
    private TableColumn<Fournisseur, String> telcol;

    @FXML
    private TableColumn<Fournisseur, String> faxcol;

    @FXML
    private TableColumn<Fournisseur, String> editCol;

    @FXML
    public Button Article;
    @FXML
    public Button parametre;

        Connection connection = DbConnection.getConnect();
        ResultSet resultSet = null;
        ObservableList<Fournisseur> FournisseurList = FXCollections.observableArrayList();



    public void initialize() {
        refreshfournisseur();
        FilteredList<Fournisseur> filteredData = new FilteredList<>(FournisseurList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Fournisseur -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Fournisseur.getNOM()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Fournisseur.getID()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Fournisseur.getFAX()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Fournisseur.getTEL()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });
        SortedList<Fournisseur> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(FournisseurTable.comparatorProperty());
        FournisseurTable.setItems(sortedData);
    }
    @FXML
        public void refresh() {
            FournisseurList.clear();
            try {
                String sql = "SELECT * FROM FOURNISSEUR";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    FournisseurList.add(new Fournisseur(
                            resultSet.getInt("ID"),
                            resultSet.getString("NOM"),
                            resultSet.getString("TEL"),
                            resultSet.getString("FAX")));
                }
                System.out.println(FournisseurList);
                FournisseurTable.setItems(FournisseurList);
            } catch (SQLException ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    @FXML
    void refreshfournisseur() {
            refresh();
            idcol.setCellValueFactory(new PropertyValueFactory<>("ID"));
            nomcol.setCellValueFactory(new PropertyValueFactory<>("NOM"));
            telcol.setCellValueFactory(new PropertyValueFactory<>("TEL"));
            faxcol.setCellValueFactory(new PropertyValueFactory<>("FAX"));
            Callback<TableColumn<Fournisseur, String>, TableCell<Fournisseur, String>> cellFoctory = (TableColumn<Fournisseur, String> param) ->
            {
                // make cell containing buttons
                TableCell<Fournisseur, String> cell = new TableCell<Fournisseur, String>() {
                    public void updateItem(String item, boolean empty) {
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
                                    Fournisseur f = FournisseurTable.getSelectionModel().getSelectedItem();
                                    String sql = "DELETE FROM FOURNISSEUR WHERE ID  = ?";
                                    PreparedStatement Statement = connection.prepareStatement(sql);
                                    Statement.setInt(1, f.getID());
                                    Statement.executeUpdate();
                                    refresh();
                                } catch (SQLException ex) {
                                    Logger.getLogger(sample.EntryController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                Fournisseur e = FournisseurTable.getSelectionModel().getSelectedItem();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("Ajoutfournisseur.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(FournisseurController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                AddfournisseurController addfournisseurController = loader.getController();
                                addfournisseurController.setUpdate(true);
                                addfournisseurController.setTextField(e.getID(), e.getNOM(), e.getTEL(), e.getFAX());
                                Parent parent = loader.getRoot();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(parent));
                                stage.initStyle(StageStyle.UTILITY);
                                stage.show();
                            });
                            HBox managebtn = new HBox(editIcon, deleteIcon);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(deleteIcon, new Insets(2, 2, 0, 3));
                            HBox.setMargin(editIcon, new Insets(2, 3, 0, 2));
                            setGraphic(managebtn);
                        }
                    }
                };

                return cell;
            };
            editCol.setCellFactory((cellFoctory));
            FournisseurTable.setItems(FournisseurList);
        }

        @FXML
        void addfournisseur(MouseEvent event)  throws IOException {
            Stage primaryStage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("Ajoutfournisseur.fxml"));
            primaryStage.setTitle("Ajout Fournisseur");
            Image logo = new Image("cerdeleclogoapp.png");
            primaryStage.getIcons().add(logo);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
            primaryStage.setResizable(false);

        }
    }
