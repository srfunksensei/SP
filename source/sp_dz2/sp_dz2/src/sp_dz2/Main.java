/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sp_dz2;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MB
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // TODO code application logic here
            ProcessInputFile pif = new ProcessInputFile("ulaz.txt");
            pif.work();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
