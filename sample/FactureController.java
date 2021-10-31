package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import models.Facture;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FactureController implements Initializable {

    @FXML
    public Button downloadfacture;
    @FXML
    public TableView<Facture> FactureTable;
    @FXML
    public TableColumn<Facture, String> referencecol;
    @FXML
    public TableColumn<Facture, String> bccol;
    @FXML
    public TableColumn<Facture, String> blcol;
    @FXML
    public TableColumn<Facture, String> designationcol;

    @FXML
    public TableColumn<Facture, String> clientcol;

    @FXML
    public TableColumn<Facture, String> quantitecol;

    @FXML
    public TableColumn<Facture, String> prixcol;
    @FXML

    public TableColumn<Facture, String> prixtotalcol;
    @FXML
    public TableColumn<Facture, String> datecol;
    @FXML
    public TableColumn<Facture, String> editCol;
    @FXML
    private ChoiceBox<Integer> comboboxcom;
    int i,x;
    Connection connection = DbConnection.getConnect();
    ObservableList<Integer> Listcom = FXCollections.observableArrayList();
    public int selecteditem;

    int num;
    int jour;
    public ObservableList<Facture> FactureListe = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle bdl) {
        fillcombobox();
    }


    void fillcombobox() {
        String sql = "SELECT DISTINCT BL FROM SORTIE where BL>0";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(sql);
            while (resultSet1.next()) {
                Listcom.add(resultSet1.getInt("BL"));
            }
            comboboxcom.setItems(Listcom);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    @FXML
    public void add() {
        try {
            Statement statement1 = connection.createStatement();
            selecteditem = comboboxcom.getValue();
            ResultSet resultSet = statement1.executeQuery("Select BC,REFERENCE,DESIGNATION,CLIENT,QUANTITE,PRIX,DATE,BL from sortie where BL= " + selecteditem);
            while (resultSet.next()) {
                FactureListe.add(new Facture(
                        resultSet.getInt("BC"),
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("CLIENT"),
                        resultSet.getInt("QUANTITE"),
                        resultSet.getFloat("PRIX"),
                        resultSet.getFloat("PRIX")*resultSet.getInt("QUANTITE"),
                        resultSet.getDate("DATE").toLocalDate(),
                        resultSet.getString("BL")));
            }
            FactureTable.setItems(FactureListe);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        bccol.setCellValueFactory(new PropertyValueFactory<>("BC"));
        referencecol.setCellValueFactory(new PropertyValueFactory<>("REFERENCE"));
        designationcol.setCellValueFactory(new PropertyValueFactory<>("DESIGNATION"));
        clientcol.setCellValueFactory(new PropertyValueFactory<>("CLIENT"));
        quantitecol.setCellValueFactory(new PropertyValueFactory<>("QUANTITE"));
        prixcol.setCellValueFactory(new PropertyValueFactory<>("PRIX"));
        prixtotalcol.setCellValueFactory(new PropertyValueFactory<>("TOTAL"));
        datecol.setCellValueFactory(new PropertyValueFactory<>("DATE"));
        blcol.setCellValueFactory(new PropertyValueFactory<>("BL"));

        Callback<TableColumn<Facture, String>, TableCell<Facture, String>> cellFoctory = (TableColumn<Facture, String> param) ->
        {
            // make cell containing buttons
            TableCell<Facture, String> cell = new TableCell<Facture, String>() {
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
                        deleteIcon.setOnMouseClicked((MouseEvent event) -> {
                            Facture en = FactureTable.getSelectionModel().getSelectedItem();
                            FactureTable.getItems().remove(en);
                        });
                        HBox managebtn = new HBox(deleteIcon);
                        managebtn.setStyle("-fx-alignment:center");
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        editCol.setCellFactory((cellFoctory));
        FactureTable.setItems(FactureListe);
    }



    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font) {

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(0);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setHorizontalAlignment(align);
        cell.setMinimumHeight(25);
        //in case there is no text and you wan to create an empty row
        if (text.trim().equalsIgnoreCase("")) {
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);
    }
    @FXML
    public void download() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, BadElementException, IOException, SQLException {
        LocalDateTime now = LocalDateTime.now();
        selecteditem = comboboxcom.getValue();
        Statement statement1 = connection.createStatement();
        ResultSet resultSet = statement1.executeQuery("Select max(NUM),max(jour) from facture");
        while(resultSet.next()){
            num=resultSet.getInt(1);
            jour=resultSet.getInt(2);
        }
        num++;
        if(now.getDayOfMonth()==1&&(jour==31||jour==30||jour==29||jour==28)) {
                try {
                    String sql = "DELETE FROM FACTURE";
                    PreparedStatement Statement = connection.prepareStatement(sql);
                    Statement.executeUpdate();
                } catch (SQLException ex) {
                    Logger.getLogger(SortieController.class.getName()).log(Level.SEVERE, null, ex);
                }
                num=1;
            }
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            double orderTotal = 0;
            double total = 0;
            Facture en = FactureTable.getItems().get(0);
            DecimalFormat df = new DecimalFormat("0.00");
            Document document = new Document();
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            FileChooser dialog = new FileChooser();
            dialog.setInitialFileName(en.getCLIENT() + "Facture" + en.getBC());
            dialog.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("pdf", "*.pdf"));
            File file = dialog.showSaveDialog(null);
            dialog.setInitialDirectory(file.getParentFile());
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
                Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);

                //specify column widths
                float[] columnWidths0 = {10f};
                float[] columnWidths = {2f, 5f, 2f, 2f, 2f};
                float[] columnWidths1 = {11f, 2f};
                //create PDF table with the given widths
                PdfPTable table00 = new PdfPTable(columnWidths0);
                PdfPTable table = new PdfPTable(columnWidths);
                PdfPTable table1 = new PdfPTable(columnWidths1);
                PdfPTable tablenfacture = new PdfPTable(1);

                // set table width a percentage of the page width
                table.setWidthPercentage(90f);
                table1.setWidthPercentage(90f);

                table00.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table00.setWidthPercentage(27.6923077f);


                table.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.setHorizontalAlignment(Element.ALIGN_RIGHT);

                tablenfacture.setHorizontalAlignment(Element.ALIGN_CENTER);
                tablenfacture.setWidthPercentage(40f);
                insertCell(tablenfacture, "FACTURE N°:" + num + "/" + now.getMonthValue() + "/" + now.getYear(), Element.ALIGN_CENTER, 1, bfBold12);

                //insert column headings
                insertCell(table, "Ref", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Désignation", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Qté", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "P.U/H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table00, en.getCLIENT(), Element.ALIGN_CENTER, 1, bfBold12);

                table.getDefaultCell().setUseVariableBorders(true);

                Facture enn = FactureTable.getItems().get(0);
                insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "BL N° :" + enn.getBL() + " du " + enn.getDATE() + "\n" + "BC N° :" + enn.getBC() + " du " + enn.getDATE(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                if(FactureListe.size() - 1==0){
                    Facture e = FactureTable.getItems().get(0);
                    insertCell(table, e.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, e.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE() * e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    orderTotal = orderTotal + e.getQUANTITE() * e.getPRIX();
                }else {
                    for (i = 0; i < FactureListe.size() - 1; i++) {
                        Facture e = FactureTable.getItems().get(i);
                        insertCell(table, e.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                        insertCell(table, e.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                        insertCell(table, String.valueOf(e.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                        insertCell(table, String.valueOf(e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                        insertCell(table, String.valueOf(e.getQUANTITE() * e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                        orderTotal = orderTotal + e.getQUANTITE() * e.getPRIX();
                        if (FactureTable.getItems().get(i).getBC() != FactureTable.getItems().get(i + 1).getBC() && i < FactureListe.size() - 2) {
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "BL N° :" + e.getBL() + " du " + e.getDATE() + "\n" + "BC N° :" + e.getBC() + " du " + e.getDATE(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                        }
                        if (i == FactureListe.size() - 2 && FactureTable.getItems().get(i).getBC() != FactureTable.getItems().get(i + 1).getBC()) {
                            i++;
                            Facture ex = FactureTable.getItems().get(FactureListe.size() - 1);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "BL N° :" + ex.getBL() + " du " + ex.getDATE() + "\n" + "BC N° :" + ex.getBC() + " du " + ex.getDATE(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, "", Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, ex.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, ex.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getQUANTITE() * ex.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                            orderTotal = orderTotal + ex.getQUANTITE() * ex.getPRIX();
                        } else if (i == FactureListe.size() - 2 && FactureTable.getItems().get(i).getBC() == FactureTable.getItems().get(i + 1).getBC()) {
                            i++;
                            Facture ex = FactureTable.getItems().get(FactureListe.size() - 1);
                            insertCell(table, ex.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, ex.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                            insertCell(table, String.valueOf(ex.getQUANTITE() * ex.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                            orderTotal = orderTotal + ex.getQUANTITE() * ex.getPRIX();
                        }
                    }
                }
                insertCell(table1, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table1, "TVA 20%", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal * 0.2), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table1, "TOTAL TTC", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal * 1.2), Element.ALIGN_RIGHT, 1, bf12);
// FET TODAY'S DATE


                Paragraph paragrapheimage = new Paragraph();
                Paragraph paragraphe0 = new Paragraph();
                Paragraph paragraphe2 = new Paragraph();
                Paragraph paragraphe00 = new Paragraph();
                Paragraph paragraphe = new Paragraph();
                Paragraph paragraphe1 = new Paragraph();
                paragraphe0.add("FES LE :" + java.time.LocalDate.now());
                paragraphe00.add(table00);
                paragraphe2.add(tablenfacture);
                paragraphe.add(table);
                paragraphe1.add(table1);
                paragrapheimage.add(com.itextpdf.text.Image.getInstance("C:\\Users\\youss\\IdeaProjects\\test\\src\\img\\cerdeleclogo.png"));
                paragrapheimage.setAlignment(Element.ALIGN_RIGHT);
                // add the paragraph to the document
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragrapheimage);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe0);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe00);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe2);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe);
                document.add(paragraphe1);
                document.close();
                downloadfacture.getScene().getWindow().hide();
            } catch (DocumentException de) {
                de.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        String query = "insert into  FACTURE (NUM,jour) VALUES (?,?)";
        try {
            PreparedStatement p = connection.prepareStatement(query);
            p.setInt(1,num);
            p.setInt(2,now.getDayOfMonth());
            p.execute();
            System.out.println("last num is "+num);
        } catch (SQLException ex) {
            Logger.getLogger(AddsortieController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    }
