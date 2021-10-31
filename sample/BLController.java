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
import models.BL;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.text.DecimalFormat;
import java.util.ResourceBundle;

public class BLController extends JPanel implements Initializable {
    @FXML
    private Button downloadfBL;
    @FXML
    public TextField blfid;
    @FXML
    public TableView<BL> BLTable;
    @FXML
    public TableColumn<BL, String> referencecol;
    @FXML
    public TableColumn<BL, String> bccol;
    @FXML
    public TableColumn<BL, String> designationcol;

    @FXML
    public TableColumn<BL, String> clientcol;

    @FXML
    public TableColumn<BL, String> quantitecol;

    @FXML
    public TableColumn<BL, String> prixcol;
    @FXML
    public TableColumn<BL, String> prixtotalcol;
    @FXML
    public TableColumn<BL, String> datecol;
    @FXML
    public TableColumn<BL, String> editCol;
    @FXML
    private ChoiceBox<Integer> comboboxcom;
    int i, x,BL=0,commande=0;
    int nbl,pbl;

    Connection connection = DbConnection.getConnect();
    ObservableList<Integer> Listcom = FXCollections.observableArrayList();
    public int selecteditem;


    public ObservableList<BL> BLliste = FXCollections.observableArrayList();
    public void initialize(URL url, ResourceBundle bdl) {
        fillcombobox();
    }


    void fillcombobox() {
        String sql = "SELECT DISTINCT BC FROM SORTIE";

        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet1 = statement.executeQuery(sql);
            while (resultSet1.next()) {
                Listcom.add(resultSet1.getInt("BC"));
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
            ResultSet resultSet = statement1.executeQuery("Select BC,REFERENCE,DESIGNATION,CLIENT,QUANTITE,PRIX,DATE from sortie where BC= " + selecteditem);
            while (resultSet.next()) {
                BLliste.add(new BL(
                        resultSet.getInt("BC"),
                        resultSet.getString("REFERENCE"),
                        resultSet.getString("DESIGNATION"),
                        resultSet.getString("CLIENT"),
                        resultSet.getInt("QUANTITE"),
                        resultSet.getFloat("PRIX"),
                        resultSet.getFloat("PRIX")*resultSet.getInt("QUANTITE"),
                        resultSet.getDate("DATE").toLocalDate()));
            }
            BLTable.setItems(BLliste);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // blfild

        try {
            Statement statement1 = connection.createStatement();
            Statement statement2 = connection.createStatement();
            selecteditem = comboboxcom.getValue();
            ResultSet resultSet = statement1.executeQuery("Select max(BL) from sortie");
            ResultSet resultSet1 = statement2.executeQuery("Select BL from sortie where BC ="+selecteditem);
                while (resultSet.next()) {
                    nbl = resultSet.getInt(1);
                }
                while (resultSet1.next()) {
                    pbl=resultSet1.getInt(1);
                }
            if(pbl==0) {
                blfid.setText((Integer.toString(nbl+1)));
            }
            else{
                blfid.setText((Integer.toString(pbl)));

                }
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
        Callback<TableColumn<BL, String>, TableCell<BL, String>> cellFoctory = (TableColumn<BL, String> param) ->
        {
            // make cell containing buttons
            TableCell<BL, String> cell = new TableCell<BL, String>() {
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
                            BL en = BLTable.getSelectionModel().getSelectedItem();
                            BLTable.getItems().remove(en);
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
        BLTable.setItems(BLliste);
    }
    @FXML
    public void download() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, BadElementException, IOException, InterruptedException {
         Document document = new Document();

        for (x = 0; x < BLliste.size() - 1; x++) {
            System.out.println(BLliste.get(x).getBC());
            if (BLliste.get(x).getBC() == BLliste.get(x + 1).getBC()) {
            } else
                break;
        }
        if (BLliste.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setHeaderText(null);
            alert.setContentText("Veuillez séléctionner le N° du BC");
            alert.showAndWait();
        } else if (x != BLliste.size() - 1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Impossible d'avoir plus d'un BC dans votre BL");
            alert.showAndWait();

        } else {
            double orderTotal = 0;
            BL en = BLTable.getItems().get(0);
            DecimalFormat df = new DecimalFormat("0.00");
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
            FileChooser dialog = new FileChooser();
            dialog.setInitialFileName(en.getCLIENT() + "BC" + en.getBC());
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
                float[] columnWidths1 = {2f, 2f};
                //create PDF table with the given widths
                PdfPTable table0 = new PdfPTable(columnWidths0);
                PdfPTable table00 = new PdfPTable(columnWidths0);
                PdfPTable table = new PdfPTable(columnWidths);
                PdfPTable table1 = new PdfPTable(columnWidths1);
                // set table width a percentage of the page width
                table.setWidthPercentage(90f);
                table1.setWidthPercentage(27.6923077f);

                table0.setHorizontalAlignment(Element.ALIGN_LEFT);
                table0.setWidthPercentage(27.6923077f);


                table00.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table00.setWidthPercentage(27.6923077f);
                table.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                //insert column headings
                insertCell(table, "Ref", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Désignation", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Qté", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "P.U/H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table00, en.getCLIENT(), Element.ALIGN_RIGHT, 1, bfBold12);
                table.getDefaultCell().setUseVariableBorders(true);
                for (i = 0; i < BLliste.size(); i++) {
                    BL e = BLTable.getItems().get(i);
                    insertCell(table, e.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, e.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE() * e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    orderTotal = orderTotal + e.getQUANTITE() * e.getPRIX();
                }
                insertCell(table0, "BON DE LIVRAISON N°" + blfid.getText(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table0, "DATE:" + en.getDATE(), Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table0, "BON DE COMMANDE N°" + en.getBC(), Element.ALIGN_CENTER, 1, bfBold12);
                table.setHeaderRows(1);
                String sql = "UPDATE SORTIE SET BL = ? WHERE BC = ?";
                try {
                    PreparedStatement preparedStatement;
                    preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.setString(1, blfid.getText());
                    preparedStatement.setInt(2, BLliste.get(x).getBC());
                    preparedStatement.executeUpdate();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                insertCell(table1, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table1, "TVA 20%", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal * 0.2), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(table1, "NET TTC", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal * 1.2), Element.ALIGN_RIGHT, 1, bf12);
                Paragraph paragrapheimage = new Paragraph();
                Paragraph paragraphe0 = new Paragraph();
                Paragraph paragraphe00 = new Paragraph();
                Paragraph paragraphe = new Paragraph();
                Paragraph paragraphe1 = new Paragraph();
                paragraphe0.add(table0);
                paragraphe00.add(table00);
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
                document.add(Chunk.NEWLINE);
                document.add(paragraphe00);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe);
                document.add(paragraphe1);
                document.close();
                downloadfBL.getScene().getWindow().hide();
            } catch (DocumentException de) {
                de.printStackTrace();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
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
}
