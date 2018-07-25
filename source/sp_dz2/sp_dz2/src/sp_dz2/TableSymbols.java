/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz2;

import java.util.LinkedList;

/**
 *
 * @author MB
 */
public class TableSymbols { //tabela simbola

    private int numGlobUse, numGlobDef, numEqu;

    public TableSymbols() {
        listsym = new LinkedList<ST_entries>();
        numEqu = numGlobDef = numGlobUse = 0;
    }

    public int getSize(){
        return listsym.size();
    }

    public int getNumEqu() {
        return numEqu;
    }

    public int getNumGlobDef() {
        return numGlobDef;
    }

    public int getNumGlobUse() {
        return numGlobUse;
    }

    public LinkedList<ST_entries> getListsym() {
        return listsym;
    }
    
    // Adds name to table with known value
    public void enter(String name, int value, int seg, boolean globuse, boolean globdef) {
        int index = findEntry(name);
        listsym.get(index).setName(name);
        listsym.get(index).setValue(value);
        listsym.get(index).setSeg(seg);
        if(globdef || globuse){
            listsym.get(index).setGlobuse(globuse);
            listsym.get(index).setGlobdef(globdef);
            if(globuse && !globdef) numGlobUse++;
            else if(!globuse && globdef) numGlobDef++;
            else if(globdef && globuse) numEqu++;
        }
    }

    // Returns value of required name
    // location is the current value of the instruction location counter
 /*   public int valueOfSymbol(String name, int location) {
        int index = findEntry(name);
        if (listsym.get(index).isDefined()) {
            return listsym.get(index).getValue();
        } else if (listsym.get(index).isGlobuse()){
            return 0;
        } else{
            return -1;
        }
    }
*/
    //returns the index of the element in list if the
    //element is found, else adds new entry
    public int findEntry(String name) {
        boolean found = false;
        int index = 0;
        while (!found && index != listsym.size()) {
            if (name.equals(listsym.get(index).getName())) {
                found = true;
            } else {
                index++;
            }
        }
        if (found) {
            return index;
        }

        ST_entries symentryE = new ST_entries(name, 0, true, 0);
        listsym.add(symentryE);
        return index;
    }

    public boolean isGlobusedSymb(String s){
        for(int i=0; i<listsym.size(); i++)
            if(s.equalsIgnoreCase(listsym.get(i).getName()) && listsym.get(i).isGlobuse()) return true;
        return false;
    }

    private LinkedList<ST_entries> listsym;
}
