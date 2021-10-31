package sample;

import helpers.DbConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FXMLDocumentController implements Initializable {
    Connection connection = DbConnection.getConnect();
    @FXML
    public TextField username;
    @FXML
    public PasswordField password;
    String usernamefid;
    String passwordfid;
    @FXML
    private Button logout;
    @FXML
    private Button login;
    @FXML
    private Button dashboard;

    @FXML
    private Button stock;

    @FXML
    private Button Article;

    @FXML
    private Button entry;

    @FXML
    private Button sortie;
    @FXML
    private Button close;

    @FXML
    private Button fournisseur;

    @FXML
    private Button client;

    @FXML
    private Button parametre;

    @FXML
    private Button historique;

    @FXML
    private BorderPane mainpane;
    @FXML
    public void stock(ActionEvent event){
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("stock");
        mainpane.setCenter(view);
    }

    @FXML
    void userLogIn(ActionEvent event) throws IOException {
        Alert alert= new Alert(Alert.AlertType.ERROR);
        if (username.getText().equals(usernamefid) && (password.getText()).equals(passwordfid)) {
            login.getScene().getWindow().hide();
            Parent root = FXMLLoader.load(getClass().getResource("testdesign.fxml"));
            Stage primaryStage = new Stage();
            primaryStage.setTitle("CERDELEC");
            Image logo = new Image("cerdeleclogoapp.png");
            primaryStage.getIcons().add(logo);
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } else if (username.getText().isEmpty() && password.getText().isEmpty()) {
            alert.setHeaderText(null);
            alert.setContentText("Remplissez les informations de connexion");
            alert.showAndWait();
        } else {
            alert.setHeaderText(null);
            alert.setContentText("Données erronées! Ressayez.");
            alert.showAndWait();
        }

    }
    @FXML
    void bureau() throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("dashboard");
        mainpane.setCenter(view);
    }
    @FXML
    void entry() throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("entry");
        mainpane.setCenter(view);
    }
    @FXML
    void sortie() throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("sortie");
        mainpane.setCenter(view);
        SortieController s = new SortieController();
        s.initialize();
        s.addsortie.setOnMouseClicked((MouseEvent event) -> {
            try {
                s.addsortie();
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        });
    }

    @FXML
    void fournisseur(ActionEvent event) throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("fournisseur");
        mainpane.setCenter(view);
    }
    @FXML
    void client(ActionEvent event) throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("client");
        mainpane.setCenter(view);
    }
    @FXML
    void historique(ActionEvent event) throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("historique");
        mainpane.setCenter(view);
    }
    @FXML
    void parametre(ActionEvent event) throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("parametre");
        mainpane.setCenter(view);
    }
    @FXML
    void Article(ActionEvent event) throws IOException {
        System.out.println("You clicked me");
        FxmlLoader object = new FxmlLoader();
        Pane view = object.getPage("article");
        mainpane.setCenter(view);
    }
    @FXML
    void userLogout(ActionEvent event) throws IOException {
        Stage primaryStage = new Stage();
        logout.getScene().getWindow().hide();
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }
    @FXML
    void closeprogram(ActionEvent event) throws IOException {
        close.getScene().getWindow().hide();
    }
    public void initialize(URL url , ResourceBundle rb) {
        String sql = "SELECT USERNAME,MDP FROM ADMIN";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                usernamefid=resultSet.getString("USERNAME");
                passwordfid=resultSet.getString("MDP");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
