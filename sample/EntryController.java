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
import models.Entry;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EntryController {
    @FXML
    public Button addentry;
    @FXML
    public TextField filterField;

    @FXML
    public Button login;
    @FXML
    public Button logout;
    @FXML
    public Button bureau;
    @FXML
    public Button stock;
    @FXML
    public Button entry;

    @FXML
    public Button Article;
    @FXML
    public Button parametre;
    @FXML
    public Button sortie;
    @FXML
    public Button fournisseur;
    @FXML
    public Button client;
    @FXML
    public Button historique;

    @FXML
    private TableView<Entry> EntryTable;
    @FXML
    private TableColumn<Entry, String> idCol;

    @FXML
    private TableColumn<Entry, String> referencecol;

    @FXML
    private TableColumn<Entry, String> designationcol;

    @FXML
    private TableColumn<Entry, String> fournisseuridcol;

    @FXML
    private TableColumn<Entry, String> quantitecol;

    @FXML

    private TableColumn<Entry, String> prixcol;
    @FXML

    private TableColumn<Entry, String> prixtotalcol;

    @FXML
    private TableColumn<Entry, String> datecol;
    @FXML
    private TableColumn<Entry, String> editCol;


    String query = null;
    Connection connection = DbConnection.getConnect();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    ObservableList<Entry> EntryList = FXCollections.observableArrayList();

    public void initialize() throws IOException {
        loadData();
        EntryTable.refresh();
        FilteredList<Entry> filteredData = new FilteredList<>(EntryList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Entry -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Entry.getREFERENCE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Entry.getDESIGNATION()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(Entry.getFOURNISSEURID()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(Entry.getDATE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(Entry.getPRIX()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                } else if (String.valueOf(Entry.getQUANTITE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });
        SortedList<Entry> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(EntryTable.comparatorProperty());
        EntryTable.setItems(sortedData);
    }

    @FXML
    public void refreshentry() {
        EntryList.clear();
        try {
            String sql = "SELECT * FROM ENTRY";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                EntryList.add(new Entry(
                        resultSet.getInt("Id"),
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("FOURNISSEURID"),
                        resultSet.getInt("QUANTITE"),
                        resultSet.getFloat("PRIX"),
                        resultSet.getFloat("PRIX")*resultSet.getInt("QUANTITE"),
                        resultSet.getDate("DATE").toLocalDate()));
            }
            EntryTable.setItems(EntryList);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void loadData() {
        refreshentry();
        idCol.setCellValueFactory(new PropertyValueFactory<>("Id"));
        referencecol.setCellValueFactory(new PropertyValueFactory<>("REFERENCE"));
        designationcol.setCellValueFactory(new PropertyValueFactory<>("DESIGNATION"));
        fournisseuridcol.setCellValueFactory(new PropertyValueFactory<>("FOURNISSEURID"));
        quantitecol.setCellValueFactory(new PropertyValueFactory<>("QUANTITE"));
        prixcol.setCellValueFactory(new PropertyValueFactory<>("PRIX"));
        prixtotalcol.setCellValueFactory(new PropertyValueFactory<>("TOTAL"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("DATE"));
        Callback<TableColumn<Entry, String>, TableCell<Entry, String>> cellFoctory = (TableColumn<Entry, String> param) ->
        {
            // make cell containing buttons
            TableCell<Entry, String> cell = new TableCell<Entry, String>() {
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
                                Entry en = EntryTable.getSelectionModel().getSelectedItem();
                                String sql = "DELETE FROM ENTRY WHERE Id  = ?";
                                PreparedStatement Statement = connection.prepareStatement(sql);
                                Statement.setInt(1, en.getId());
                                Statement.executeUpdate();
                                refreshentry();
                            } catch (SQLException ex) {
                                Logger.getLogger(EntryController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Entry e = EntryTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("addentry.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(EntryController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            AddentryController addentryController = loader.getController();
                            addentryController.setUpdate(true);
                            addentryController.setTextField(e.getId(), e.getREFERENCE(), e.getDESIGNATION(), e.getFOURNISSEURID(), e.getQUANTITE(), e.getPRIX(), e.getDATE());
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
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol.setCellFactory((cellFoctory));
        EntryTable.setItems(EntryList);
    }
    //ENTRY TABLE CONTROLLER OTHER WINDOWS

    @FXML
    void addentry() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("addentry.fxml"));
        primaryStage.setTitle("Ajout d'entrées");
        Image logo = new Image("cerdeleclogoapp.png");
        primaryStage.getIcons().add(logo);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);

    }

}