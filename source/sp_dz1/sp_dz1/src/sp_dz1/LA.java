/*
 * klasa za leksicku analizu
 */
package sp_dz1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MB
 */
public class LA {

    public class LA_sym {

        private boolean isLabeled;
        private String label;
        private String mnem;
        private String adr;
        private String comment;
        private List<String> listSymbInAdrField;
        private List<String> listOpInAdrField;

        public LA_sym() {
            label = mnem = adr = comment = "";
            isLabeled = false;
            listOpInAdrField = new LinkedList<String>();
            listSymbInAdrField = new LinkedList<String>();
        }

        public String getAdr() {
            return adr;
        }

        public String getComment() {
            return comment;
        }

        public boolean isIsLabeled() {
            return isLabeled;
        }

        public String getLabel() {
            return label;
        }

        public String getMnem() {
            return mnem;
        }

        public void setAdr(String adr) {
            this.adr = adr;
        }

        public void setComment(String comment) {
            this.comment = comment;
        }

        public void setIsLabeled(boolean isLabeled) {
            this.isLabeled = isLabeled;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public void setMnem(String mnem) {
            this.mnem = mnem;
        }

        public int getSizeListAdrSym(){
            return listSymbInAdrField.size();
        }

        public int getSizeListAdrOp(){
            return listOpInAdrField.size();
        }

        public List<String> getListOpInAdrField() {
            return listOpInAdrField;
        }

        public List<String> getListSymbInAdrField() {
            return listSymbInAdrField;
        }

    }

    public boolean analiseAdrField(String s, LA_sym la) {
        if (s.contains("+") || s.contains("-")) {
            while (true) {
                int plus = s.indexOf("+"), minus = s.indexOf("-");
                if (plus > minus) {
                    if (minus > 0) {
                        la.getListSymbInAdrField().add(s.substring(0, s.indexOf("-")));
                        la.getListOpInAdrField().add("-");
                        s = s.substring(s.indexOf("-") + 1, s.length());
                    } else {
                        la.getListSymbInAdrField().add(s.substring(0, s.indexOf("+")));
                        la.getListOpInAdrField().add("+");
                        s = s.substring(s.indexOf("+") + 1, s.length());
                    }
                } else if (minus > plus) {
                    if (plus > 0) {
                        la.getListSymbInAdrField().add(s.substring(0, s.indexOf("+")));
                        la.getListOpInAdrField().add("+");
                        s = s.substring(s.indexOf("+") + 1, s.length());
                    } else {
                        la.getListSymbInAdrField().add(s.substring(0, s.indexOf("-")));
                        la.getListOpInAdrField().add("-");
                        s = s.substring(s.indexOf("-") + 1, s.length());
                    }
                }
                    if (!s.contains("+") && !s.contains("-")) {
                        s.trim();
                        if (!s.equalsIgnoreCase("") && !s.equalsIgnoreCase(" ")) {
                            la.getListSymbInAdrField().add(s);
                        }
                        return true;
                    }
                
            }
        }
        if (s.contains(",")) {
            while (true) {
                la.getListSymbInAdrField().add(s.substring(0, s.indexOf(",")));
                s = s.substring(s.indexOf(",") + 1, s.length());
                if (!s.contains(",")) {
                    s.trim();
                    if (!s.equalsIgnoreCase("") && !s.equalsIgnoreCase(" ")) {
                        la.getListSymbInAdrField().add(s);
                    }
                    return true;
                }
            }
        }

        return false;

    }

    //analysis the line
    public LA_sym LA_analysis(String linija) {
        String line_to_read = linija;

        line = line_to_read;
        linelength = line.length();

        LA_sym la_sym_line = new LA_sym();
        /*
        Leksička analiza odnosi se grupisanje tokena na tekućoj liniji, u identifikatore/mnemonike, konstante, komentare itd.
        Sintaksna analiza odnosi se na izdvajanje polja labele, polja direktive/opkoda i adresnog polja sa linije (upisuju se odvojeno u radni fajl da drugi prolaz ne bi opet morao da sprovodi leksičku analizu).
        Semantička analiza odnosi se na popunjavanje tabele simbola, obradu direktiva i proveru korektnosti izvornog programa i prijavu grešaka.

         */
        String komentar = "";
        int comment = line_to_read.indexOf(";");
            if (comment != -1) {
                komentar = line_to_read.substring(comment, line_to_read.length());
                la_sym_line.setComment(komentar);

                line_to_read = line_to_read.substring(0, comment);
            }

        String[] list = line_to_read.split("[\\s]+");

        for(int i = 0; i<list.length; i++) {

            if(TableOperation.oneByteInstr(list[i]) || TableOperation.twoBytesInstr(list[i])){
                la_sym_line.setMnem(list[i]);
            }
            else {
                if(i == 0 && !list[i].equalsIgnoreCase("") && !list[i].equalsIgnoreCase(" ")){
                    la_sym_line.setLabel(list[i]);
                    la_sym_line.setIsLabeled(true);
                } else{
                    if(!list[i].equalsIgnoreCase("") && !list[i].equalsIgnoreCase(" ")){
                        if(!analiseAdrField(list[i], la_sym_line))
                            la_sym_line.setAdr(list[i]);
                        else la_sym_line.setAdr(list[i]);
                    }
                }
            }
        }
        
        return la_sym_line;
    }

    public LA(String file) {
        try {
            src = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.print("Nije pronadjen fajl: " + file + " \n");
            Logger.getLogger(LA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    BufferedReader src;     // source file
    int linelength;         // line length
    String line;            // last line read
}
