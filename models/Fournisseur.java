package models;

public class Fournisseur {
    public int ID;
    public String NOM;
    public String TEL;
    public String FAX;

    public Fournisseur(int ID, String NOM, String TEL, String FAX) {
        this.ID = ID;
        this.NOM = NOM;
        this.TEL = TEL;
        this.FAX = FAX;
    }
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNOM() {
        return NOM;
    }

    public void setNOM(String NOM) {
        this.NOM = NOM;
    }

    public String getTEL() {
        return TEL;
    }

    public void setTEL(String TEL) {
        this.TEL = TEL;
    }

    public String getFAX() {
        return FAX;
    }

    public void setFAX(String FAX) {
        this.FAX = FAX;
    }

}

