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
import models.Article;

import java.io.IOException;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ArticleController {
    @FXML
    private TableView<Article> ArticleTable;
    @FXML
    private TableColumn<Article,String> referencecol;

    @FXML
    private TableColumn<Article,String> designationcol;

    @FXML
    private TableColumn<Article,String> marquecol;

    @FXML
    private TableColumn<Article,String> prixventecol;

    @FXML
    private TableColumn<Article,String> prixachatcol;

    @FXML
    private TextField filterField;
    @FXML
    private TableColumn<Article,String> editCol;

    Connection connection = DbConnection.getConnect();
    ResultSet resultSet = null;
    ObservableList<Article> ArticleList = FXCollections.observableArrayList();

    public void initialize() {
        loadData();
        FilteredList<Article> filteredData = new FilteredList<>(ArticleList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Article -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Article.getREFERENCE()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Article.getDESIGNATION()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Article.getMARQUE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Article.getPRIXACHAT()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Article.getPRIXVENTE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });

        SortedList<Article> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(ArticleTable.comparatorProperty());
        ArticleTable.setItems(sortedData);
    }

    @FXML
    public void refresharticle() {
        ArticleList.clear();
        try {
            String sql = "SELECT * FROM ARTICLE";
            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                ArticleList.add(new Article(
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("MARQUE"),
                        resultSet.getFloat("PRIXACHAT"),
                        resultSet.getFloat("PRIXVENTE")));
            }
            ArticleTable.setItems(ArticleList);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    public void loadData() {
        refresharticle();
        referencecol.setCellValueFactory(new PropertyValueFactory<>("REFERENCE"));
        designationcol.setCellValueFactory(new PropertyValueFactory<>("DESIGNATION"));
        marquecol.setCellValueFactory(new PropertyValueFactory<>("MARQUE"));
        prixventecol.setCellValueFactory(new PropertyValueFactory<>("PRIXVENTE"));
        prixachatcol.setCellValueFactory(new PropertyValueFactory<>("PRIXACHAT"));
        Callback<TableColumn<Article, String>, TableCell<Article, String>> cellFoctory = (TableColumn<Article, String> param) ->
        {
            // make cell containing buttons
            TableCell<Article, String> cell = new TableCell<Article, String>() {
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
                                Article en = ArticleTable.getSelectionModel().getSelectedItem();
                                String sql = "DELETE FROM ARTICLE WHERE REFERENCE  = ?";
                                PreparedStatement Statement = connection.prepareStatement(sql);
                                Statement.setString(1, en.getREFERENCE());
                                Statement.executeUpdate();
                                refresharticle();
                            } catch (SQLException ex) {
                                Logger.getLogger(ArticleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        });
                        editIcon.setOnMouseClicked((MouseEvent event) -> {
                            Article e = ArticleTable.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader();
                            loader.setLocation(getClass().getResource("Addarticle.fxml"));
                            try {
                                loader.load();
                            } catch (IOException ex) {
                                Logger.getLogger(ArticleController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            AddarticleController addarticleController = loader.getController();
                            addarticleController.setUpdate(true);
                            addarticleController.setTextField(e.getREFERENCE(), e.getDESIGNATION(), e.getMARQUE(), e.getPRIXACHAT(),e.getPRIXVENTE());
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
        ArticleTable.setItems(ArticleList);
    }

//ADDARTICLE
    @FXML
    void addarticle() throws IOException {
        Stage primaryStage = new Stage();
        Parent root = FXMLLoader.load(getClass().getResource("Addarticle.fxml"));
        primaryStage.setTitle("Ajout Article");
        Image logo = new Image("cerdeleclogoapp.png");
        primaryStage.getIcons().add(logo);
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.setResizable(false);

    }
}
