package models;

public class Client {
        public int ID;
        public String NOM;
        public String TEL;
        public String FAX;
        public String EMAIL;

        public Client(int ID, String NOM, String TEL, String FAX,String EMAIL) {
            this.ID = ID;
            this.NOM = NOM;
            this.TEL = TEL;
            this.FAX = FAX;
            this.EMAIL=EMAIL;
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
        public String getEMAIL() {
            return EMAIL;
        }

        public void setEMAIL(String EMAIL) {
            this.EMAIL = EMAIL;
        }


    }

