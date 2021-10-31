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
import models.Client;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClientController {
        @FXML
    public TextField filterField;
        @FXML

        public Button logout;
        @FXML
        public Button bureau;
        @FXML
        public Button stock;
        @FXML
        public Button entry;
        @FXML
        public Button sortie;
        @FXML
        public Button fournisseur;
        @FXML
        public Button client;
        @FXML
        public Button historique;

        @FXML
        private TableView<Client> ClientTable;
        @FXML
        private TableColumn<Client, String> idcol;

        @FXML
        private TableColumn<Client, String> nomcol;

        @FXML
        private TableColumn<Client, String> telcol;

        @FXML
        private TableColumn<Client, String> faxcol;

      @FXML
        private TableColumn<Client, String> emailcol;

        @FXML
        private TableColumn<Client, String> editCol;
    @FXML
    public Button Article;
    @FXML
    public Button parametre;
    Connection connection = DbConnection.getConnect();
    ResultSet resultSet = null;
    ObservableList<Client> ClientList = FXCollections.observableArrayList();
    public void initialize() {

        refreshclient();

        FilteredList<Client> filteredData = new FilteredList<>(ClientList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Client -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Client.getNOM()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Client.getID()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Client.getFAX()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Client.getEMAIL()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Client.getTEL()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });
        SortedList<Client> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ClientTable.comparatorProperty());
        ClientTable.setItems(sortedData);
    }

        @FXML
        public void refresh() {
            ClientList.clear();
            try {
                String sql = "SELECT * FROM CLIENT";
                Statement statement = connection.createStatement();
                resultSet = statement.executeQuery(sql);
                while (resultSet.next()) {
                    ClientList.add(new Client(
                            resultSet.getInt("ID"),
                            resultSet.getString("NOM"),
                            resultSet.getString("TEL"),
                            resultSet.getString("FAX"),
                            resultSet.getString("EMAIL")));
                }
                ClientTable.setItems(ClientList);
            } catch (SQLException ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }


        @FXML
        void refreshclient() {
            refresh();
            idcol.setCellValueFactory(new PropertyValueFactory<>("ID"));
            nomcol.setCellValueFactory(new PropertyValueFactory<>("NOM"));
            telcol.setCellValueFactory(new PropertyValueFactory<>("TEL"));
            faxcol.setCellValueFactory(new PropertyValueFactory<>("FAX"));
            emailcol.setCellValueFactory(new PropertyValueFactory<>("EMAIL"));
            Callback<TableColumn<Client, String>, TableCell<Client, String>> cellFoctory = (TableColumn<Client, String> param) ->
            {
                // make cell containing buttons
                TableCell<Client, String> cell = new TableCell<Client, String>() {
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
                                    Client f = ClientTable.getSelectionModel().getSelectedItem();
                                    String sql = "DELETE FROM Client WHERE ID  = ?";
                                    PreparedStatement Statement = connection.prepareStatement(sql);
                                    Statement.setInt(1, f.getID());
                                    Statement.executeUpdate();
                                    refresh();
                                } catch (SQLException ex) {
                                    Logger.getLogger(sample.EntryController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            });
                            editIcon.setOnMouseClicked((MouseEvent event) -> {
                                Client e = ClientTable.getSelectionModel().getSelectedItem();
                                FXMLLoader loader = new FXMLLoader();
                                loader.setLocation(getClass().getResource("Ajoutclient.fxml"));
                                try {
                                    loader.load();
                                } catch (IOException ex) {
                                    Logger.getLogger(ClientController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                                AddclientController addClientController = loader.getController();
                                addClientController.setUpdate(true);
                                addClientController.setTextField(e.getID(), e.getNOM(), e.getTEL(), e.getFAX(),e.getEMAIL());
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
            ClientTable.setItems(ClientList);
        }



        //ENTRY TABLE CONTROLLER OTHER WINDOWS
        @FXML
        void addclient(MouseEvent event)  throws IOException {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("Ajoutclient.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage primaryStage = new Stage();
            primaryStage.setTitle("Ajout Client");
            Image logo = new Image("cerdeleclogoapp.png");
            primaryStage.getIcons().add(logo);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

        }
}

