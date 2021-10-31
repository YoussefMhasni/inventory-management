package models;

public class Stock {
    String  REFERENCE;
    String DESIGNATION;
    String MARQUE;
    float PRIXACHAT;
    float PRIXVENTE;
    int    QUANTITE;
    public  String getREFERENCE() {
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

    public String getMARQUE() {
        return MARQUE;
    }
    public void setMARQUE(String MARQUE) {
        this.MARQUE = MARQUE;
    }

    public float getPRIXACHAT() {
        return PRIXACHAT;
    }

    public void setPRIXACHAT(int PRIXACHAT) {
        this.PRIXACHAT = PRIXACHAT;
    }

    public float getPRIXVENTE() {
        return PRIXVENTE;
    }

    public void setPRIXVENTE(int PRIXVENTE) {
        this.PRIXVENTE = PRIXVENTE;
    }

    public int getQUANTITE() {
        return QUANTITE;
    }

    public void setQUANTITE(int QUANTITE) {
        this.QUANTITE = QUANTITE;
    }

    public Stock(String REFERENCE, String DESIGNATION,String MARQUE, float PRIXACHAT, float PRIXVENTE, int QUANTITE) {
        this.REFERENCE = REFERENCE;
        this.DESIGNATION = DESIGNATION;
        this.MARQUE = MARQUE;
        this.PRIXACHAT = PRIXACHAT;
        this.PRIXVENTE = PRIXVENTE;
        this.QUANTITE = QUANTITE;
    }


}
