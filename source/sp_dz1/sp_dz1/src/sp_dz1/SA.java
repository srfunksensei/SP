/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz1;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import sp_dz1.LA.LA_sym;

/**
 *
 * @author MB
 */
public class SA { //sintaksna analiza

    private LinkedList<LA.LA_sym> lista;
    private LA lex;
    private BufferedReader src;
    private String fileName;

    public SA(String file) {
        lista = new LinkedList<LA_sym>();
        lex = new LA(file);
        fileName = file;
        try {
            src = new BufferedReader(new FileReader(file));
        } catch (FileNotFoundException ex) {
            System.out.print("Nije pronadjen fajl:" + file + "\n");
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public LinkedList<LA_sym> getLista() {
        return lista;
    }

    public String getFileName() {
        return fileName;
    }

    public void SA_analysis() {
        String line = null;
        while (true) {
            try {
                line = src.readLine();

            } catch (IOException ex) {
                System.out.println("Neuspesno citanje linije!");
                Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (line != null) {
                lista.addLast(lex.LA_analysis(line));
            } else break;
        }
        try {
            src.close();
        } catch (IOException ex) {
            System.out.println("Neuspesno zatvaranje ulaznog fajla!");
            Logger.getLogger(SA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ispis(){
        for(int i = 0; i<lista.size(); i++){
            System.out.println(lista.get(i).getLabel() + " " + lista.get(i).getMnem() + " " +
                    lista.get(i).getAdr() + " " + lista.get(i).getComment() + "\n");
        }
    }
}
