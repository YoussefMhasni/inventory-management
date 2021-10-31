package models;

import java.util.Date;

public class Historique {
    public  Date DATE;
    public  String REFERENCE;
    public  String DESIGNATION;
    public  int PRIX;
    public  int QUANTITE;
    public  Date DATES;
    public  String REFERENCES;
    public  String DESIGNATIONS;
    public  int PRIXS;
    public  int QUANTITES;

    public Date getDATES() {
        return DATES;
    }

    public void setDATES(Date DATES) {
        this.DATES = DATES;
    }

    public String getREFERENCES() {
        return REFERENCES;
    }

    public void setREFERENCES(String REFERENCES) {
        this.REFERENCES = REFERENCES;
    }

    public String getDESIGNATIONS() {
        return DESIGNATIONS;
    }

    public void setDESIGNATIONS(String DESIGNATIONS) {
        this.DESIGNATIONS = DESIGNATIONS;
    }

    public int getPRIXS() {
        return PRIXS;
    }

    public void setPRIXS(int PRIXS) {
        this.PRIXS = PRIXS;
    }

    public int getQUANTITES() {
        return QUANTITES;
    }

    public void setQUANTITES(int QUANTITES) {
        this.QUANTITES = QUANTITES;
    }


    public Date getDATE() {
        return DATE;
    }

    public void setDATE(Date DATE) {
        this.DATE = DATE;
    }
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

    public int getPRIX() {
        return PRIX;
    }

    public void setPRIX(int PRIX) {
        this.PRIX = PRIX;
    }

    public int getQUANTITE() {
        return QUANTITE;
    }

    public void setQUANTITE(int QUANTITE) {
        this.QUANTITE = QUANTITE;
    }

    public Historique(Date DATE,String REFERENCE, String DESIGNATION, int QUANTITE, int PRIX) {
        this.DATE=DATE;
        this.REFERENCE = REFERENCE;
        this.DESIGNATION = DESIGNATION;
        this.QUANTITE = QUANTITE;
        this.PRIX = PRIX;
    }
}
