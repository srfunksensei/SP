/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz1;

import java.util.LinkedList;

/**
 *
 * @author MB
 */
public class TableSymbols { //tabela simbola

    class ST_forwardrefs {   // forward references for undefined labels

        int patch;          // to be patched
        String action;      //action to perform

        public ST_forwardrefs(int patch, String action) {
            this.patch = patch;
            this.action = action;
        }

        public void setPatch(int patch) {
            this.patch = patch;
        }

        public int getPatch() {
            return patch;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

    };

    public TableSymbols() {
        listsym = new LinkedList<ST_entries>();
    }

    public int getSize(){
        return listsym.size();
    }

    public LinkedList<ST_entries> getListsym() {
        return listsym;
    }
    
    //prints symbol table on std.out
    public void printsymboltable() {

        System.out.println("\nSymbol Table\n------------\n");
        int index = 0;
        while (index != listsym.size()) {
            System.out.print(listsym.get(index).getName() + "   ");
            if (!listsym.get(index).isDefined()) {
                System.out.print("--- undefined");
            } else {
                System.out.print(listsym.get(index).getValue() + "   ");
            }
            System.out.print("\tFLINK:");
            int indx = 0;
            if (listsym.get(index).getFlink() != null) {
                while (indx != listsym.get(index).getFlink().size()) {
                    System.out.print(listsym.get(index).getFlink().get(indx).getPatch() +
                            listsym.get(index).getFlink().get(indx).getAction() + "  ");
                    indx++;
                }
            }
            index++;
            System.out.println();
        }
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
            listsym.get(index).setDefined(false);
        }else{
            listsym.get(index).setDefined(true);
        }
    }

    // Returns value of required name
    // location is the current value of the instruction location counter
    public int valueOfSymbol(String name, int location, String action) {
        int index = findEntry(name);
        if (listsym.get(index).isDefined()) {
            AS.insertNewOp(action);
            return listsym.get(index).getValue();
        } else{
            ST_forwardrefs forwardentry = new ST_forwardrefs(location, action);
            listsym.get(index).getFlink().addFirst(forwardentry);
            return listsym.get(index).isGlobuse()?0:listsym.get(index).getValue(); //-1
        }
    }

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

        ST_entries symentryE = new ST_entries(name, 0, false, new LinkedList<ST_forwardrefs>(), 0);
        listsym.addFirst(symentryE);
        return 0;
    }

    public void backpatch(int[] mem, TableRelocations tr) {
        for (int i = 0; i < listsym.size(); i++) {
            ST_entries entry = listsym.get(i);
            if (entry.getFlink() != null) {
                int indx = 0;
                if (entry.getFlink().size() != 0) {
                    while (entry.getFlink().size() != indx) {
                        if(entry.getFlink().get(indx).getAction().equalsIgnoreCase("+")){
                            mem[entry.getFlink().get(indx).getPatch()] += entry.getValue();
                        } else if(entry.getFlink().get(indx).getAction().equalsIgnoreCase("-")){
                            mem[entry.getFlink().get(indx).getPatch()] -= entry.getValue();
                        }
                        indx++;
                    }
                }
            }
            if(!this.isGlobusedSymb(entry.getName())) {
                    tr.fix(entry.getName(), entry.getSeg(), "");
            }
            if(this.isGlobusedSymb(entry.getName()) && this.isGlobudefSymb(entry.getName())){
                tr.fix(entry.getName(), this.numGlobusedSymbInTable(entry.getName()), "");
            }

        }
    }

    public String getEntryString(int i){
        return ("\t\t" + listsym.get(i).getName() + "\t" + listsym.get(i).getValue() +
                "\t" + listsym.get(i).getSeg() + "\t" +
                (listsym.get(i).isGlobdef() && !listsym.get(i).isGlobuse()?"D":"") +
                (listsym.get(i).isGlobuse() && !listsym.get(i).isGlobdef()?"U":"") +
                (listsym.get(i).isGlobuse() && listsym.get(i).isGlobdef()?"E":""));
    }

    public boolean isDefinedSymb(String s){
        for(int i=0; i<listsym.size(); i++)
            if(s.equalsIgnoreCase(listsym.get(i).getName()) && listsym.get(i).isDefined()) return true;
        return false;
    }

    public boolean isGlobusedSymb(String s){
        for(int i=0; i<listsym.size(); i++)
            if(s.equalsIgnoreCase(listsym.get(i).getName()) && listsym.get(i).isGlobuse()) return true;
        return false;
    }

    public boolean isGlobudefSymb(String s){
        for(int i=0; i<listsym.size(); i++)
            if(s.equalsIgnoreCase(listsym.get(i).getName()) && listsym.get(i).isGlobdef()) return true;
        return false;
    }

    public int numGlobusedSymbInTable(String s){
        int num = 1;
        for(int i =0; i<listsym.size();i++)
            if(listsym.get(i).isGlobuse()){
                if(s.equalsIgnoreCase(listsym.get(i).getName())) return num;
                else num++;
            }
        return -1;
    }

    public ST_entries getSymbol(String s){
        for(int i = 0; i<listsym.size(); i++)
            if(listsym.get(i).getName().equalsIgnoreCase(s)) return listsym.get(i);
        return null;
    }

    public String getOpType(String s, int loc){
        for (int i = 0; i < listsym.size(); i++) {
            if (listsym.get(i).getName().equalsIgnoreCase(s)) {
                for (int j = 0; j < listsym.get(i).getFlink().size(); j++) {
                    if (listsym.get(i).getFlink().get(j).getPatch() == loc) {
                        return listsym.get(i).getFlink().get(j).getAction();
                    }
                }
            }
        }
        return "";
    }
    private LinkedList<ST_entries> listsym;
}
