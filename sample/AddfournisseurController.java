package sample;

import helpers.DbConnection;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddfournisseurController implements Initializable {
    @FXML
    private TextField idfid;

    @FXML
    private TextField nomfid;

    @FXML
    private TextField telfid;

    @FXML
    private TextField faxfid;

    @FXML
    private Button save;

    @FXML
    private Button close;


        public void initialize(URL url, ResourceBundle bdl) {
        }



        String query = null;
        Connection connection = null;
        ResultSet resultSet = null;
        public boolean update;
        public int idf;
    @FXML
    void close(MouseEvent event) {
        close.getScene().getWindow().hide();
    }
        @FXML
        public void save(MouseEvent event) throws SQLException {
                getQuery();
                insert();
                clean();
        }
        @FXML
        public void clean() {
            nomfid.setText(null);
            telfid.setText(null);
            faxfid.setText(null);
        }

        public void getQuery() throws SQLException {
            if (update == false) {
                query = "INSERT INTO FOURNISSEUR (nom, tel, fax) VALUES (?,?,?)";
            } else  {
                query = "UPDATE FOURNISSEUR SET NOM=?, TEL = ?, FAX = ? WHERE ID =?";
            }
        }
        @FXML
        public void insert() throws SQLException {
            Connection conn = DbConnection.getConnect();
            getQuery();
            switch (query){

                case "INSERT INTO FOURNISSEUR (nom, tel, fax) VALUES (?,?,?)":
                    System.out.println(query);
                    try {
                        PreparedStatement preparedStatement;
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nomfid.getText());
                        preparedStatement.setString(2, telfid.getText());
                        preparedStatement.setString(3, faxfid.getText());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(AddfournisseurController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    break;
                case "UPDATE FOURNISSEUR SET NOM=?, TEL = ?, FAX = ? WHERE ID =?":
                    System.out.println(query);
                    try {
                        PreparedStatement preparedStatement;
                        preparedStatement = conn.prepareStatement(query);
                        preparedStatement.setString(1, nomfid.getText());
                        preparedStatement.setString(2, telfid.getText());
                        preparedStatement.setString(3, faxfid.getText());
                        preparedStatement.setInt(4, idf);
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        Logger.getLogger(sample.AddentryController.class.getName()).log(Level.SEVERE, null, ex);
                    }
            }

        }
        @FXML
        public  void setTextField(int ID , String NOM, String TEL, String FAX) {
            idf=ID;
            nomfid.setText(NOM);
            telfid.setText(TEL);
            faxfid.setText(FAX);
        }
        @FXML
        void setUpdate(boolean b) {
            this.update = b;

        }
    }

