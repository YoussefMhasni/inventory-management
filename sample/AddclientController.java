package sample;

import helpers.DbConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddclientController {

        @FXML
        private TextField nomfid;

        @FXML
        private TextField telfid;

        @FXML
        private TextField faxfid;

         @FXML
         private TextField emailfid;


        @FXML
        private Button close;

        String query = null;
        public boolean update;
        public int idf;


        @FXML
        void close(MouseEvent event) {
            close.getScene().getWindow().hide();
        }
        @FXML
        public void save(MouseEvent event) {
                insert();
                clean();
            }
        @FXML
        public void clean() {
            nomfid.setText(null);
            telfid.setText(null);
            faxfid.setText(null);
            emailfid.setText(null);
        }
        public void getQuery()  {
            if (update == false) {
                query = "INSERT INTO CLIENT (nom,tel,fax,email) VALUES (?,?,?,?)";
            } else  {
                query = "UPDATE CLIENT SET NOM=?, TEL = ?, FAX = ?, EMAIL=? WHERE ID =?";
            }
        }
        @FXML
        public void insert()  {
            Connection conn = DbConnection.getConnect();
            getQuery();
            switch (query){

                case "INSERT INTO CLIENT (nom,tel,fax,email) VALUES (?,?,?,?)":
                    System.out.println(query);

                    try {
                        PreparedStatement preparedStatement;
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nomfid.getText());
                        preparedStatement.setString(2, telfid.getText());
                        preparedStatement.setString(3, faxfid.getText());
                        preparedStatement.setString(4, emailfid.getText());
                        preparedStatement.executeUpdate();

                    } catch (SQLException ex) {
                        Logger.getLogger(AddclientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case  "UPDATE CLIENT SET NOM=?, TEL = ?, FAX = ?, EMAIL=? WHERE ID =?":
                    System.out.println(query);
                    try {
                        PreparedStatement preparedStatement;
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nomfid.getText());
                        preparedStatement.setString(2, telfid.getText());
                        preparedStatement.setString(3, faxfid.getText());
                        preparedStatement.setString(4, emailfid.getText());
                        preparedStatement.setInt(5, idf);
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(AddclientController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }
        }
        @FXML
        public  void setTextField(int ID , String NOM, String TEL, String FAX,String EMAIL) {
            idf=ID;
            nomfid.setText(NOM);
            telfid.setText(TEL);
            faxfid.setText(FAX);
            emailfid.setText(EMAIL);
        }
        @FXML

        void setUpdate(boolean b) {
            this.update = b;
        }
    }


