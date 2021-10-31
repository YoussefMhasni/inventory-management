package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    private static Stage stg;
    public Stage primaryStage;
    @Override
    public void start(Stage primaryStage) throws Exception{
        stg = primaryStage;
        primaryStage.setResizable(false);
       // Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Connexion");
       // primaryStage.setScene(new Scene(root, 700, 500));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        primaryStage.sizeToScene();
        Image logo = new Image("cerdeleclogoapp.png");
        primaryStage.getIcons().add(logo);
    }
    public void close(Stage primaryStage){
        primaryStage.close();
    }

    public static void main(String[] args) {
        launch(args);
    }
}