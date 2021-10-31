package sample;

import helpers.DbConnection;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class BureauController implements Initializable{

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
    private LineChart<String, Number> rapportvente;

    @FXML
    private PieChart statistiquevente;

    @FXML
    private LineChart<String, Number> rapportachat;

    Connection connection = DbConnection.getConnect();
    int sum1,sum2,sum3;
    int sum4,sum5,sum6;
    int sum7,sum8,sum9;
    int sum10,sum11,sum12;
    int s7,s8,s9;
    int s10,s11,s12;
   public String  Mois (int s) {
       String mois=null;
       switch (s) {
           case 1: {
               mois = "Jan";
               break;
           }
           case 2: {
               mois = "Fev";
               break;
           }
           case 3: {
               mois = "Mar";
               break;
           }
           case 4: {
               mois = "Avr";
               break;
           }
           case 5: {
               mois = "Mai";
               break;
           }
           case 6: {
               mois = "Jui";
               break;
           }
           case 7: {
               mois = "Jul";
               break;
           }
           case 8: {
               mois = "Aout";
               break;
           }
           case 9: {
               mois = "Sept";
               break;
           }
           case 10: {
               mois = "Oct";
               break;
           }
           case 11: {
               mois = "Nov";
               break;
           }
           case 12: {
               mois = "Dec";
               break;
           }
       }
       return mois;
   }
String moisactuel(int date){
    int mois;
    String s=null;
    mois = date;
    if(mois<10){
        s="0"+Integer.toString(mois);
    }
    return s;
}
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        LocalDate date =java.time.LocalDate.now();
        int mois;

        //Vente
        XYChart.Series<String,Number> series = new XYChart.Series<String,Number>();

        String sql1 = "Select sum(PRIX*QUANTITE) from Entry E where  Date LIKE '"+date.getYear()+"-"+date.getMonthValue()+"-%%'";
        String sql2 = "Select sum(PRIX*QUANTITE) from Entry E where  Date LIKE '"+date.getYear()+"-"+(moisactuel(date.getMonthValue()-1))+"-%%'";
        String sql3 = "Select sum(PRIX*QUANTITE) from Entry E where  Date LIKE '"+date.getYear()+"-"+(moisactuel(date.getMonthValue()-2))+"-%%'";
        System.out.println(sql1);
        System.out.println(sql2);
        System.out.println(sql3);
        try {
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            Statement statement3 = connection.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(sql1);
            ResultSet resultSet2 = statement2.executeQuery(sql2);
            ResultSet resultSet3 = statement3.executeQuery(sql3);

            while(resultSet1.next()){
                sum7 = +resultSet1.getInt(1);
            }
            while(resultSet2.next()){
                sum8 = +resultSet2.getInt(1);
            }
            while(resultSet3.next()){
                sum9 = +resultSet3.getInt(1);
            }


            series.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()-2),sum9));
            series.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()-1),sum8));
            series.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()),sum7));

            rapportachat.getData().add(series);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        //produits plus vendus
        ArrayList<String> list1 =new ArrayList<>();
        ArrayList<Integer> list2 =new ArrayList<>();

        String chaine = "SELECT REFERENCE, SUM(QUANTITE) AS QUANTITE FROM SORTIE  GROUP BY REFERENCE ORDER BY QUANTITE desc LIMIT 3";

        try {
            Statement s = connection.createStatement();
            ResultSet r = s.executeQuery(chaine);
            while(r.next()) {
                list1.add(r.getString(1));
                list2.add(r.getInt(2));
            }
        }catch (SQLException throwables) {
            throwables.printStackTrace();
        }

                PieChart.Data data[] = new PieChart.Data[list1.size()];


        for (int i = 0; i <list1.size() ; i++) {
                 data[i]=new PieChart.Data(list1.get(i),list2.get(i));
        }
        ObservableList<PieChart.Data> PieCharts = FXCollections.observableArrayList(data[0],data[1],data[2]);




        PieCharts.forEach(dat ->
                dat.nameProperty().bind(
                        Bindings.concat(
                                dat.getName(), " Qté: ",dat.pieValueProperty().getValue().intValue()
                        )
                )
        );

        statistiquevente.getData().addAll(PieCharts);
        statistiquevente.setLabelLineLength(10);
        statistiquevente.setLegendSide(Side.LEFT);
        statistiquevente.setLegendVisible(true);
//ACHAT
        XYChart.Series<String,Number> serie = new XYChart.Series<String,Number>();
        String query1 ="Select sum(PRIX*QUANTITE) from Sortie where  Date LIKE '"+date.getYear()+"-"+date.getMonthValue()+"-%%'";
        String query2 ="Select sum(PRIX*QUANTITE) from Sortie where  Date LIKE '"+date.getYear()+"-"+(moisactuel(date.getMonthValue()-1))+"-%%'";
        String query3 ="Select sum(PRIX*QUANTITE) from Sortie where  Date LIKE '"+date.getYear()+"-"+(moisactuel(date.getMonthValue()-2))+"-%%'";

        try {
            Statement stat1 = connection.createStatement();
            Statement stat2 = connection.createStatement();
            Statement stat3 = connection.createStatement();
            ResultSet resultSet11= stat1.executeQuery(query1);
            ResultSet resultSet22 = stat2.executeQuery(query2);
            ResultSet resultSet33= stat3.executeQuery(query3);
            while(resultSet11.next()){
                s7 = +resultSet11.getInt(1);
            }
            while(resultSet22.next()){
                s8 = +resultSet22.getInt(1);
            }
            while(resultSet33.next()){
                s9 = +resultSet33.getInt(1);
            }
            serie.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()-2),s9));
            serie.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()-1),s8));
            serie.getData().add(new XYChart.Data<String, Number>(Mois(date.getMonthValue()),s7));

            rapportvente.getData().add(serie);

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }


}
