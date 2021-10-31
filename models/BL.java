package models;

import java.time.LocalDate;

public class BL {
    public int BC;
    public String REFERENCE;
    public String DESIGNATION;
    public String CLIENT;
    public float PRIX;
    public float TOTAL;
    public int QUANTITE;
    public LocalDate DATE;


    public BL(int BC, String REFERENCE, String DESIGNATION, String CLIENT, int QUANTITE, float PRIX,float TOTAL, LocalDate DATE) {
        this.BC = BC;
        this.REFERENCE = REFERENCE;
        this.DESIGNATION = DESIGNATION;
        this.CLIENT = CLIENT;
        this.QUANTITE = QUANTITE;
        this.PRIX = PRIX;
        this.TOTAL = TOTAL;

        this.DATE = DATE;
    }

    public int getBC() {
        return BC;
    }

    public void setBC(int BC) {
        BC = BC;
    }

    public LocalDate getDATE() {
        return DATE;
    }

   /* public void setDATE(LocalDate DATE) {
        this.DATE = DATE;
    }*/

    public String getREFERENCE() {
        return REFERENCE;
    }

    public void setREFERENCE(String REFERENCE) {
        this.REFERENCE = REFERENCE;
    }

    public String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }

    public String getCLIENT() {
        return CLIENT;
    }

    public void setFOURNISSEURID(String FOURNISSEURID) {
        this.CLIENT = CLIENT;
    }

    public float getPRIX() {
        return PRIX;
    }

    public void setPRIX(float PRIX) {
        this.PRIX = PRIX;
    }


    public int getQUANTITE() {
        return QUANTITE;
    }

    public void setQUANTITE(int QUANTITE) {
        this.QUANTITE = QUANTITE;
    }
    public  float getTOTAL() {
        return TOTAL;
    }
    public void setTOTAL(float TOTAL) {
        this.TOTAL = TOTAL;
    }
}
