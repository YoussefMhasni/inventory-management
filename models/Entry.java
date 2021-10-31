package models;

import java.time.LocalDate;

public class Entry {
    public int Id;
    public  String REFERENCE;
    public  String DESIGNATION;
    public  String FOURNISSEURID;
    public  float PRIX;
    public  float TOTAL;
    public  int QUANTITE;
    public LocalDate DATE;



    public Entry(int Id,String REFERENCE, String DESIGNATION, String FOURNISSEURID, int QUANTITE, float PRIX,float TOTAL,LocalDate DATE) {
            this.Id = Id;
            this.REFERENCE = REFERENCE;
            this.DESIGNATION = DESIGNATION;
            this.FOURNISSEURID = FOURNISSEURID;
            this.QUANTITE = QUANTITE;
            this.PRIX = PRIX;
            this.TOTAL = TOTAL;
           this.DATE=DATE;
        }
    public LocalDate getDATE() {
        return  DATE;
    }

   /* public void setDATE(LocalDate DATE) {
        this.DATE = DATE;
    }*/

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
    public  String getREFERENCE() {
        return REFERENCE;
        }

    public void setREFERENCE(String REFERENCE) {
            this.REFERENCE = REFERENCE;
        }
    public  String getDESIGNATION() {
        return DESIGNATION;
    }

    public void setDESIGNATION(String DESIGNATION) {
        this.DESIGNATION = DESIGNATION;
    }
    public  String getFOURNISSEURID() {
        return FOURNISSEURID;
    }

    public void setFOURNISSEURID(String FOURNISSEURID) {
        this.FOURNISSEURID = FOURNISSEURID;
    }
    public  float getPRIX() {
        return PRIX;
    }

    public void setPRIX(int PRIX) {
        this.PRIX = PRIX;
    }
    public  float getTOTAL() {
        return TOTAL;
    }

    public void setTOTAL(int TOTAL) {
        this.TOTAL = TOTAL;
    }

    public  int getQUANTITE() {
        return QUANTITE;
    }

    public void setQUANTITE (int QUANTITE) {
        this.QUANTITE = QUANTITE;
    }
}
