/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sp_dz2;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author MB
 */

public class TableRelocations {
    final static int NUM_OF_NAMES = 10;

    public class Zapis {
        private int loc, seg, ref;
        private String type;
        private String name;
        private String op;
        private int locOrLen;

        public Zapis(String name, int loc, int seg, int ref, String type, String op) {
            this.name = name;
            this.loc = loc;
            this.seg = seg;
            this.ref = ref;
            this.op = op;
            this.type = type;
            locOrLen = -1;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
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

        public void setLoc(int loc) {
            this.loc = loc;
        }

        public void setRef(int ref) {
            this.ref = ref;
        }

        public void setSeg(int seg) {
            this.seg = seg;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOp() {
            return op;
        }

        public void setOp(String op) {
            this.op = op;
        }

        public int getLocOrLen() {
            return locOrLen;
        }

        public void setLocOrLen(int locOrLen) {
            this.locOrLen = locOrLen;
        }

    }
    private static List<Zapis> listZapis;

    public TableRelocations() {
        listZapis = new LinkedList<TableRelocations.Zapis>();
    }

    public static List<Zapis> getListZapis() {
        return listZapis;
    }

    public Zapis getZapis(int index){
        return listZapis.get(index);
    }

    public void add(String name, int loc, int seg, int ref, String type, String op, int locOrLen){
        listZapis.add(new Zapis(name, loc, seg, ref, type, op));
        if(locOrLen != -1) listZapis.get(listZapis.size() - 1).setLocOrLen(locOrLen);
    }

    public boolean isLabeled (int loc){
        for(int i = 0; i<listZapis.size(); i++){
            if(listZapis.get(i).getLoc() == loc && !listZapis.get(i).getType().contains("S")){
                if(listZapis.get(i).getSeg() == listZapis.get(i).getRef()) return true;
            }
        }
        return false;
    }

    public String getLabelName(int loc){
        String name = "";
        for(int i = 0; i<listZapis.size(); i++){
            if(listZapis.get(i).getLoc() == loc && !listZapis.get(i).getType().contains("S")){
                if(listZapis.get(i).getSeg() == listZapis.get(i).getRef()) name = listZapis.get(i).getName();
            }
        }
        return name;
    }
    
    public String[] getNamesForLoc(int loc){
        String [] names = new String[NUM_OF_NAMES];
        int pos = 0;
        for(int i = 0; i<listZapis.size(); i++){
            if(listZapis.get(i).getLoc() == loc){
                names[pos++]
                        = listZapis.get(i).getName();
            }
        }
        return names;
    }

    public String[] getOperationsForLoc(int loc){
        String [] op = new String[NUM_OF_NAMES];
        int pos = 0;
        for(int i = 0; i<listZapis.size(); i++){
            if(listZapis.get(i).getLoc() == loc){
                op[pos++] = listZapis.get(i).getOp();
            }
        }
        return op;
    }

    public String getAdrField(int loc){
        String adr = "";
        String names[] = getNamesForLoc(loc),
                op[] = getOperationsForLoc(loc);
        if (names == null || op == null) {
            return adr;
        }
        if (names.length > 1 && names[1] != null) {
            for (int i = 0; i < names.length && names[i] != null; i++) {
                if(i == 0 && op[i].equalsIgnoreCase("-")) adr+="-";
                adr += names[i] + " ";
                if (i != names.length - 1) {
                    adr += op[i + 1] + " ";
                }
            }
        } else {
            adr = names[0];
        }
        
        return adr;
    }

    public boolean isJumpOnLabel(String s){
        for(int i = 0; i< listZapis.size(); i++){
            if(listZapis.get(i).getRef() == listZapis.get(i).getSeg()
                    && listZapis.get(i).getName().equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    @Override
    public String toString() {
        String res = "TableRelocations:\n";
        for(int i = 0; i<listZapis.size(); i++)
            res+= (listZapis.get(i).getLoc() + "\t" + listZapis.get(i).getSeg() + "\t"
                    + listZapis.get(i).getRef() + "\n");
        return res;
    }

    public String getZapisString(int index) {
        return ("\t\t" + listZapis.get(index).getLoc() + "\t" + listZapis.get(index).getSeg() + "\t"
                    + listZapis.get(index).getRef() + "\t" + listZapis.get(index).getName() + "\n");
    }
    
}
