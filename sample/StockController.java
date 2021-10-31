package sample;

import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.util.Callback;
import models.Stock;

import java.sql.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class StockController {
    @FXML
   private TextField filterField;
    @FXML
    private Button fournisseur;

    @FXML
    private Button client;

    @FXML
    private Button bureau;

    @FXML
    private Button stock;

    @FXML
    private Button entry;

    @FXML
    private Button sortie;

    @FXML
    private Button historique;

    @FXML
    private Button logout;

    @FXML
    private Button parametre;

    @FXML
        private Button Article;

    @FXML
    private TableView<Stock> StockTable;

    @FXML
    private TableColumn<Stock, String> referencecol;

    @FXML
    private TableColumn<Stock, String> designationcol;

    @FXML
    private TableColumn<Stock, String> marquecol;

    @FXML
    private TableColumn<Stock, String> prixachatcol;

    @FXML
    private TableColumn<Stock, String> prixventecol;

    @FXML
    private TableColumn<Stock, String> quantitecol;

    @FXML
    private TableColumn<Stock, String> statutcol;
    int sumentry = 0;
    int i ;
    int qtit,x;
    ArrayList<Integer> listquantite = new ArrayList<>();

    String query = null;
    Connection connection = DbConnection.getConnect();
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;

    ObservableList<Stock> StockList = FXCollections.observableArrayList();
    public void initialize()  {

        refreshstock();
        FilteredList<Stock> filteredData = new FilteredList<>(StockList, b -> true);
        filterField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(Stock -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String lowerCaseFilter = newValue.toLowerCase();
                if (String.valueOf(Stock.getREFERENCE()).toLowerCase().indexOf(lowerCaseFilter) != -1 ) {
                    return true; // Filter matches first name.
                } else if (String.valueOf(Stock.getDESIGNATION()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Stock.getMARQUE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Stock.getPRIXACHAT()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Stock.getPRIXVENTE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else if (String.valueOf(Stock.getQUANTITE()).toLowerCase().indexOf(lowerCaseFilter) != -1) {
                    return true; // Filter matches last name.
                }
                else
                    return false; // Does not match.
            });
        });
        SortedList<Stock> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(StockTable.comparatorProperty());
        StockTable.setItems(sortedData);
    }
    @FXML
    public void refresh() {
        StockList.clear();
        try {
            String sql = "SELECT A.REFERENCE,A.DESIGNATION,MARQUE,PRIXACHAT,PRIXVENTE,A.QUANTITE FROM ARTICLE A ";

            Statement statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                StockList.add(new Stock(
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("MARQUE"),
                        resultSet.getFloat("PRIXACHAT"),
                        resultSet.getFloat("PRIXVENTE"),
                        resultSet.getInt("QUANTITE")));
            }
            StockTable.setItems(StockList);
            System.out.println(StockList);
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }

    }


    @FXML
    void refreshstock() {
        refresh();
        String sql = "SELECT QUANTITE FROM ARTICLE A";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            listquantite.add(0);
            while (resultSet.next()) {
                sumentry = resultSet.getInt(1);
                listquantite.add(sumentry);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        System.out.println(listquantite);
        x = listquantite.size();
        System.out.println("je suis x = "+x);
        i=-1;
        Callback<TableColumn<Stock, String>, TableCell<Stock, String>> cellFoctory = (TableColumn<Stock, String> param) -> {
            i++;
        TableCell<Stock, String> cel =  new TableCell<>();
            System.out.println("je suis i : "+ i);

            if (i<x) {
                qtit = listquantite.get(i);
                TableCell<Stock , String> cell = new TableCell<Stock , String>() {
                public void updateItem(String item, boolean empty) {
                        Label label1 = new Label();
                        super.updateItem(item, empty);
                        //that cell created only on non-empty rows
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            System.out.println("je suis sumentry : " + qtit);
                            if (qtit > 10) {
                                label1.setText("En Stock");
                                label1.setStyle("-fx-background-color: GREEN;-fx-text-fill: #000000;");
                                System.out.println("je suis sumentry  vert : " + qtit);
                            } else if(qtit>5){
                                label1.setText("Stock faible");
                                label1.setStyle("-fx-background-color: ORANGE ;-fx-text-fill: #000000;");
                                System.out.println("je suis sumentry rouge: " + qtit);
                            }
                            else{
                                label1.setText("Manque de stock");
                                label1.setStyle("-fx-background-color: #ff0000 ;-fx-text-fill: #000000;");
                                System.out.println("je suis sumentry rouge: " + qtit);
                            }
                            HBox managebtn = new HBox(label1);
                            managebtn.setStyle("-fx-alignment:center");
                            HBox.setMargin(label1, new Insets(1, 2, 0, 3));
                            setGraphic(managebtn);
                            setText(null);
                        }
                }
                };

        return cell;
        }
            else {
                return cel;
            }
        };

        statutcol.setCellFactory((cellFoctory));
        //refresh();
        referencecol.setCellValueFactory(new PropertyValueFactory<>("REFERENCE"));
        designationcol.setCellValueFactory(new PropertyValueFactory<>("DESIGNATION"));
        marquecol.setCellValueFactory(new PropertyValueFactory<>("MARQUE"));
        prixachatcol.setCellValueFactory(new PropertyValueFactory<>("PRIXACHAT"));
        prixventecol.setCellValueFactory(new PropertyValueFactory<>("PRIXVENTE"));
        quantitecol.setCellValueFactory(new PropertyValueFactory<>("QUANTITE"));
    }
}

