package sample;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import helpers.DbConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.stage.FileChooser;
import models.Historique;
import models.SHistorique;

import javax.swing.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HistoriqueController {
    @FXML
    private DatePicker datepickerdebut;
    @FXML
    private DatePicker datepickerfin;
    Connection connection = DbConnection.getConnect();
    ObservableList<Historique> EntryList = FXCollections.observableArrayList();
    ObservableList<SHistorique> SortieListe = FXCollections.observableArrayList();

    public void initialize() throws SQLException {


    }
    @FXML
    public void refresh() {
        if (datepickerdebut.getValue().isAfter( datepickerfin.getValue())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setContentText("Saisir une date correcte");
            alert.showAndWait();
        } else {
            EntryList.clear();
            try {
                String sql1 = "SELECT * from historique where EDATE between '" + datepickerdebut.getValue().toString() + "' and '" + datepickerfin.getValue().toString() + "'";
                String sql2 = "SELECT * from shistorique where SDATE between '" + datepickerdebut.getValue().toString() + "' and '" + datepickerfin.getValue().toString() + "'";
                System.out.println(sql1);
                Statement statement1 = connection.createStatement();
                Statement statement2 = connection.createStatement();
                ResultSet resultSet1 = statement1.executeQuery(sql1);
                ResultSet resultSet2 = statement2.executeQuery(sql2);
                while (resultSet1.next()) {
                    EntryList.add(new Historique(
                            resultSet1.getDate("EDATE"),
                            resultSet1.getString("EREFERENCE"),
                            resultSet1.getString("EDESIGNATION"),
                            resultSet1.getInt("EQUANTITE"),
                            resultSet1.getInt("EPRIX")));
                }
                while (resultSet2.next()) {
                    SortieListe.add(new SHistorique(
                            resultSet2.getDate("SDATE"),
                            resultSet2.getString("SREFERENCE"),
                            resultSet2.getString("SDESIGNATION"),
                            resultSet2.getInt("SQUANTITE"),
                            resultSet2.getInt("SPRIX")));
                }
                System.out.println(SortieListe);
            } catch (SQLException ex) {
                Logger.getLogger(DbConnection.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    @FXML
    void imprimer() throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        refresh();
        Historique s=EntryList.get(0);
        double orderTotal = 0;
        double sorderTotal = 0;
        DecimalFormat df = new DecimalFormat("0.00");
        Document document = new Document();
        FileChooser dialog = new FileChooser();
        dialog.setInitialFileName("Rapport "+datepickerdebut.getValue().getMonth().getValue()+"-"+datepickerdebut.getValue().getYear());
        dialog.getExtensionFilters().addAll( new FileChooser.ExtensionFilter("pdf","*.pdf"));
        File file = dialog.showSaveDialog(null);
        dialog.setInitialDirectory(file.getParentFile());
            try {
                PdfWriter.getInstance(document, new FileOutputStream(file));
                document.open();
                Font bfBold12 = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD, new BaseColor(0, 0, 0));
                Font bf12 = new Font(Font.FontFamily.TIMES_ROMAN, 12);
                //specify column widths
                float[] columnWidths = {2f, 5f, 2f, 2f, 2f};
                //create PDF table with the given widths
                PdfPTable table = new PdfPTable(columnWidths);
                PdfPTable table1 = new PdfPTable(2);
                PdfPTable stable = new PdfPTable(columnWidths);
                PdfPTable stable1 = new PdfPTable(2);
                // set table width a percentage of the page width
                table.setWidthPercentage(90f);
                table1.setWidthPercentage(27.6923077f);
                table.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table1.setHorizontalAlignment(Element.ALIGN_RIGHT);
                stable.setWidthPercentage(90f);
                stable1.setWidthPercentage(27.6923077f);
                stable.setHorizontalAlignment(Element.ALIGN_RIGHT);
                stable1.setHorizontalAlignment(Element.ALIGN_RIGHT);

                //insert column headings
                insertCell(table, "Date Entrée", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Reference", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Désignation", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Qté", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable, "Date Sortie", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable, "Reference", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable, "Désignation", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable, "Qté", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable, "Total H.T", Element.ALIGN_CENTER, 1, bfBold12);
                for (int i = 0; i < EntryList.size(); i++) {
                    Historique e = EntryList.get(i);
                    insertCell(table, String.valueOf(e.getDATE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, e.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, e.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(table, String.valueOf(e.getQUANTITE() * e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    orderTotal = orderTotal + e.getQUANTITE() * e.getPRIX();
                }
                table.setHeaderRows(1);
                for (int i = 0; i < SortieListe.size(); i++) {
                    SHistorique e = SortieListe.get(i);
                    insertCell(stable, String.valueOf(e.getDATE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(stable, e.getREFERENCE(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(stable, e.getDESIGNATION(), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(stable, String.valueOf(e.getQUANTITE()), Element.ALIGN_CENTER, 1, bfBold12);
                    insertCell(stable, String.valueOf(e.getQUANTITE() * e.getPRIX()), Element.ALIGN_CENTER, 1, bfBold12);
                    sorderTotal = sorderTotal + e.getQUANTITE() * e.getPRIX();
                }
                stable.setHeaderRows(1);


                insertCell(table1, "NET TTC", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(table1, df.format(orderTotal * 1.2), Element.ALIGN_RIGHT, 1, bf12);
                insertCell(stable1, "NET TTC", Element.ALIGN_CENTER, 1, bfBold12);
                insertCell(stable1, df.format(sorderTotal * 1.2), Element.ALIGN_RIGHT, 1, bf12);


                Paragraph paragraphe1 = new Paragraph();
                Paragraph paragraphe = new Paragraph();
                Paragraph paragraphe2 = new Paragraph();

                Paragraph sparagraphe1 = new Paragraph();
                Paragraph sparagraphe = new Paragraph();
                Paragraph sparagraphe2 = new Paragraph();
                Paragraph paragrapheimage = new Paragraph();
                paragraphe1.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN,20, Font.BOLD, BaseColor.BLACK));
                paragraphe1.add("Rapport du ");
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(datepickerdebut.getValue().format( DateTimeFormatter.ofPattern("dd/MM/YYYY"))+" au "+datepickerfin.getValue().format( DateTimeFormatter.ofPattern("dd/MM/YYYY")));
                paragraphe1.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN,15, Font.BOLD, BaseColor.BLACK));
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add(Chunk.NEWLINE);
                paragraphe1.add("Entrées");
                paragraphe1.setAlignment(Element.ALIGN_CENTER);
                paragraphe.add(table);
                paragraphe2.add(table1);
                paragrapheimage.add(com.itextpdf.text.Image.getInstance("C:\\Users\\youss\\IdeaProjects\\test\\src\\img\\cerdeleclogo.png"));
                paragrapheimage.setAlignment(Element.ALIGN_RIGHT);
                sparagraphe1.setFont(FontFactory.getFont(FontFactory.TIMES_ROMAN,15, Font.BOLD, BaseColor.BLACK));
                sparagraphe1.add("Sorties");
                sparagraphe1.setAlignment(Element.ALIGN_CENTER);
                sparagraphe1.setAlignment(Element.TITLE);
                sparagraphe.add(stable);
                sparagraphe2.add(stable1);
                // add the paragraph to the document
                document.add(paragrapheimage);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe1);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(paragraphe);
                document.add(paragraphe2);
                document.add(Chunk.NEXTPAGE);

                document.add(sparagraphe1);
                document.add(Chunk.NEWLINE);
                document.add(Chunk.NEWLINE);
                document.add(sparagraphe);
                document.add(sparagraphe2);
                document.close();
            } catch(DocumentException de){
                    de.printStackTrace();
                } catch(IOException ioe){
                    ioe.printStackTrace();
                }
    }
   public void clear() throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Suppresion de l'historique");
        alert.setHeaderText("Voulez vous supprimer toute l'historique?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            Connection connection = DbConnection.getConnect();
            String sql = "DELETE FROM shistorique ";
            String sql1 = "DELETE FROM historique ";
            PreparedStatement Statement = connection.prepareStatement(sql);
            PreparedStatement Statement2 = connection.prepareStatement(sql1);
            Statement.executeUpdate();
            Statement2.executeUpdate();
        } else {
            alert.hide();
        }

    }
    private void insertCell(PdfPTable table, String text, int align, int colspan, Font font){

        //create a new cell with the specified Text and Font
        PdfPCell cell = new PdfPCell(new Phrase(text.trim(), font));
        //set the cell alignment
        cell.setHorizontalAlignment(0);
        //set the cell column span in case you want to merge two or more cells
        cell.setColspan(colspan);
        cell.setMinimumHeight(25);
        //in case there is no text and you wan to create an empty row
        if(text.trim().equalsIgnoreCase("")){
            cell.setMinimumHeight(10f);
        }
        //add the call to the table
        table.addCell(cell);
    }
}

