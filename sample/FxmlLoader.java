package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;

import java.net.URL;

public class FxmlLoader {
    public Pane view;
@FXML
    public Pane getPage(String fileName){
        try{
            URL fileUrl = Main.class.getResource("/sample/" + fileName +".fxml");
            if(fileUrl==null) {
                throw new java.io.FileNotFoundException("Fxml file can't be found");
            }
            view = new FXMLLoader().load(fileUrl);
            }catch(Exception e) {
            System.out.println("No page " + fileName + "please check FxmlLoader ");
        }
        return view;
        }

    }
