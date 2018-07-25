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
public class TableDirective {//tabela direktiva

    class Zapis {

        private String nameSeg;
        private int base, len;
        private String desc;

        public Zapis(String nameSeg, int base, int len, String desc) {
            this.nameSeg = nameSeg;
            this.base = base;
            this.len = len;
            this.desc = desc;
        }

        public void setBase(int base) {
            this.base = base;
        }

        public void setLen(int len) {
            this.len = len;
        }

        public int getBase() {
            return base;
        }

        public String getDesc() {
            return desc;
        }

        public int getLen() {
            return len;
        }

        public String getNameSeg() {
            return nameSeg;
        }
    }
    private List<Zapis> listZapis;

    public TableDirective() {
        listZapis = new LinkedList<Zapis>();
        addItems();
    }

    private void addItems() {
        listZapis.add(new Zapis(".text", 0, 0, "RP"));
        listZapis.add(new Zapis(".bss", 0, 0, "RW"));
        listZapis.add(new Zapis(".data", 0, 0, "RWP"));
    }

    public List<Zapis> getListZapis() {
        return listZapis;
    }

    @Override
    public String toString() {
        String res = "TableDirective:\n";
        for (int i = 0; i < listZapis.size(); i++) {
            if (listZapis.get(i).getBase() != listZapis.get(i).getLen()) {
                res += (listZapis.get(i).getNameSeg() + "\t" + listZapis.get(i).getBase() + "\t"
                        + listZapis.get(i).getLen() + "\t" + listZapis.get(i).getDesc() + "\n");
            }
        }
        return res;
    }

    public String getZapisString(int index){
        return ("\t\t" + listZapis.get(index).getNameSeg() + "\t" + listZapis.get(index).getBase() + "\t"
                        + listZapis.get(index).getLen() + "\t" + listZapis.get(index).getDesc() + "\n");
    }

    public boolean isDefined(int index){
        return listZapis.get(index).getBase() != listZapis.get(index).getLen();
    }


}
