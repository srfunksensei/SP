/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sp_dz1;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MB
 */
public class TableRelocations {

    class Zapis {
        private String name;
        private int loc, seg, ref;
        private String type;
        private String action;

        public Zapis(String name, int loc, int seg, int ref, String type) {
            this.name = name;
            this.loc = loc;
            this.seg = seg;
            this.ref = ref;
            this.type = type;
        }

        public int getLoc() {
            return loc;
        }

        public int getRef() {
            return ref;
        }

        public int getSeg() {
            return seg;
        }

        public String getType() {
            return type;
        }

        public void setLoc(int loc) {
            this.loc = loc;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }

        public void setSeg(int seg) {
            this.seg = seg;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }
    private static List<Zapis> listZapis;

    public TableRelocations() {
        listZapis = new LinkedList<TableRelocations.Zapis>();
    }

    public static List<Zapis> getListZapis() {
        return listZapis;
    }

    //add if in segment
    public void add(String name, int loc, int seg, boolean jump){
        String type = "A1";
        if(jump) type = "R1";
        listZapis.add(new Zapis(name, loc, seg, 0, type));
    }

    public void add(String name, int loc, int seg,int ref, boolean globused){
        String type = "A1";
        if(globused) type = "AS1";
        listZapis.add(new Zapis(name, loc, seg, ref, type));
    }

    public void fix(String name, int ref, String type){
        for(int i = 0; i<listZapis.size(); i++){
            if(listZapis.get(i).getName().equalsIgnoreCase(name)){
                listZapis.get(i).setRef(ref);
                if(!type.equalsIgnoreCase("")) listZapis.get(i).setType(type);
            }
        }
    }

    public boolean calculateIK(String name, String type){
        for (int i = 0; i < listZapis.size(); i++) {
            if(listZapis.get(i).getName().equalsIgnoreCase(name)){
                if(listZapis.get(i).getSeg() == 0) return false;
                int loc = listZapis.get(i).getLoc() - 1;
                String mnem = "";
                for (int j = 0; j < TableOperation.getListMnem().size(); j++) {
                    if(TableOperation.getListMnem().get(j).valueDec == loc)
                        mnem = TableOperation.getListMnem().get(j).mnemonic;
                }
                if(TableOperation.isJumpInstr(mnem) && !mnem.equalsIgnoreCase("JSR")
                        && !( listZapis.get(i).getRef() == listZapis.get(i).getSeg())){
                    if(listZapis.get(i).getType().contains("S")) type = "RS1";
                    else type = "R1";
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String res = "TableRelocations:\n";
        for(int i = 0; i<listZapis.size(); i++)
            res+= (listZapis.get(i).getLoc() + "\t" + listZapis.get(i).getSeg() + "\t"
                    + listZapis.get(i).getRef() + "\t" + listZapis.get(i).getType() + "\n");
        return res;
    }

        public String getZapisString(int index) {
        return ("\t\t" + listZapis.get(index).getLoc() + "\t" + listZapis.get(index).getSeg() + "\t"
                    + listZapis.get(index).getRef() + "\t" + listZapis.get(index).getType() + "\t"
                    + listZapis.get(index).getName() + "\t"); //dodatna polja
    }
    
}
