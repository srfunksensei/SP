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
public class TableOperation {//tabela operacionih kodova
    class Polje{
        String mnemonic;
        String valueHex;
        int valueDec;

        public Polje(String mnemonic, String valueHex, int value) {
            this.mnemonic = mnemonic;
            this.valueHex = valueHex;
            this.valueDec = value;
        }

    };

    private static List<Polje> listMnem;

    public TableOperation() {
        listMnem = new LinkedList<Polje>();
        addItems();
    }

    private void addItems(){
        //jednobajtne
        listMnem.add(new Polje("CLC", "02h", 2));
        listMnem.add(new Polje("CLX", "03h", 3));
        listMnem.add(new Polje("CMC", "04h", 4));
        listMnem.add(new Polje("INC", "05h", 5));
        listMnem.add(new Polje("DEC", "06h", 6));
        listMnem.add(new Polje("INX", "07h", 7));
        listMnem.add(new Polje("DEX", "08h", 8));
        listMnem.add(new Polje("TAX", "09h", 9));
        listMnem.add(new Polje("INI", "0Ah", 10));
        listMnem.add(new Polje("OTI", "0Eh", 14));
        listMnem.add(new Polje("PSH", "13h", 19));
        listMnem.add(new Polje("POP", "14h", 20));
        listMnem.add(new Polje("SHL", "15h", 21));
        listMnem.add(new Polje("SHR", "16h", 22));
        listMnem.add(new Polje("RET", "17h", 23));
        listMnem.add(new Polje("HLT", "18h", 24));

        //dvobajtne
        listMnem.add(new Polje("LDA", "19h", 25));
        listMnem.add(new Polje("LDX", "1Ah", 26));
        listMnem.add(new Polje("LDI", "1Bh", 27));
        listMnem.add(new Polje("STA", "1Eh", 30));
        listMnem.add(new Polje("STX", "1Fh", 31));
        listMnem.add(new Polje("ADD", "20h", 32));
        listMnem.add(new Polje("ADX", "21h", 33));
        listMnem.add(new Polje("SUB", "26h", 38));
        listMnem.add(new Polje("SBX", "27h", 39));
        listMnem.add(new Polje("CMP", "2Ch", 44));
        listMnem.add(new Polje("ANA", "2Fh", 47));
        listMnem.add(new Polje("ORA", "32h", 50));
        listMnem.add(new Polje("BRN", "35h", 53));
        listMnem.add(new Polje("BZE", "36h", 54));
        listMnem.add(new Polje("BNZ", "37h", 55));
        listMnem.add(new Polje("BPZ", "38h", 56));
        listMnem.add(new Polje("BNG", "39h", 57));
        listMnem.add(new Polje("BCC", "3Ah", 58));
        listMnem.add(new Polje("BCS", "3Bh", 59));
        listMnem.add(new Polje("JSR", "3Ch", 60));

        //ostalo
/*        listMnem.add(new Polje("DC", "0h", 0));
        listMnem.add(new Polje("DS", "0h", 0));
        listMnem.add(new Polje("BEG", "FFh", -1));
        listMnem.add(new Polje("END", "FFh", -1));
        listMnem.add(new Polje("TXT", "FFh", -1));
        listMnem.add(new Polje("BSS", "FFh", -1));
        listMnem.add(new Polje("DAT", "FFh", -1));
*/    }

    public static boolean exists(String mnem){
        for(int i = 0; i<listMnem.size(); i++){
            if((listMnem.get(i).mnemonic).equalsIgnoreCase(mnem) ) return true;
        }
        return false;
    }

    public static boolean oneByteInstr(String s){
        if("CLC".equalsIgnoreCase(s)) return true;
        if("CLX".equalsIgnoreCase(s)) return true;
        if("CMC".equalsIgnoreCase(s)) return true;
        if("INC".equalsIgnoreCase(s)) return true;
        if("DEC".equalsIgnoreCase(s)) return true;
        if("INX".equalsIgnoreCase(s)) return true;
        if("DEX".equalsIgnoreCase(s)) return true;
        if("TAX".equalsIgnoreCase(s)) return true;
        if("INI".equalsIgnoreCase(s)) return true;
        if("OTI".equalsIgnoreCase(s)) return true;
        if("PSH".equalsIgnoreCase(s)) return true;
        if("POP".equalsIgnoreCase(s)) return true;
        if("SHL".equalsIgnoreCase(s)) return true;
        if("SHR".equalsIgnoreCase(s)) return true;
        if("RET".equalsIgnoreCase(s)) return true;
        if("HLT".equalsIgnoreCase(s)) return true;

        if("BEG".equalsIgnoreCase(s)) return true;
        if("END".equalsIgnoreCase(s)) return true;
        if("TXT".equalsIgnoreCase(s)) return true;
        if("BSS".equalsIgnoreCase(s)) return true;
        if("DAT".equalsIgnoreCase(s)) return true;

        return false;
    }
    
    public static boolean twoBytesInstr(String s){
        if("LDA".equalsIgnoreCase(s)) return true;
        if("LDX".equalsIgnoreCase(s)) return true;
        if("LDI".equalsIgnoreCase(s)) return true;
        if("STA".equalsIgnoreCase(s)) return true;
        if("STX".equalsIgnoreCase(s)) return true;
        if("ADD".equalsIgnoreCase(s)) return true;
        if("ADX".equalsIgnoreCase(s)) return true;
        if("SUB".equalsIgnoreCase(s)) return true;
        if("SBX".equalsIgnoreCase(s)) return true;
        if("CMP".equalsIgnoreCase(s)) return true;
        if("ANA".equalsIgnoreCase(s)) return true;
        if("ORA".equalsIgnoreCase(s)) return true;
        if("BRN".equalsIgnoreCase(s)) return true;
        if("BZE".equalsIgnoreCase(s)) return true;
        if("BNZ".equalsIgnoreCase(s)) return true;
        if("BPZ".equalsIgnoreCase(s)) return true;
        if("BNG".equalsIgnoreCase(s)) return true;
        if("BCC".equalsIgnoreCase(s)) return true;
        if("BCS".equalsIgnoreCase(s)) return true;
        if("JSR".equalsIgnoreCase(s)) return true;

        if("DC".equalsIgnoreCase(s)) return true;
        if("DS".equalsIgnoreCase(s)) return true;
        if("ORG".equalsIgnoreCase(s)) return true;
        if("EQU".equalsIgnoreCase(s)) return true;
        if("DEF".equalsIgnoreCase(s)) return true;
        if("USE".equalsIgnoreCase(s)) return true;
        return false;
    }

    public static boolean isDirective(String dir){
        dir = dir.trim();
        if("BEG".equalsIgnoreCase(dir)) return true;
        if("END".equalsIgnoreCase(dir)) return true;
        if("ORG".equalsIgnoreCase(dir)) return true;
        if("DS".equalsIgnoreCase(dir)) return true;
//        if("DC".equalsIgnoreCase(dir)) return true;
        if("EQU".equalsIgnoreCase(dir)) return true;
        if("TXT".equalsIgnoreCase(dir)) return true;
        if("BSS".equalsIgnoreCase(dir)) return true;
        if("DAT".equalsIgnoreCase(dir)) return true;
        if("DEF".equalsIgnoreCase(dir)) return true;
        if("USE".equalsIgnoreCase(dir)) return true;
        return false;
    }

    public static boolean isJumpInstr(String s){
        if("BRN".equalsIgnoreCase(s)) return true;
        if("BZE".equalsIgnoreCase(s)) return true;
        if("BNZ".equalsIgnoreCase(s)) return true;
        if("BPZ".equalsIgnoreCase(s)) return true;
        if("BNG".equalsIgnoreCase(s)) return true;
        if("BCC".equalsIgnoreCase(s)) return true;
        if("BCS".equalsIgnoreCase(s)) return true;
        if("JSR".equalsIgnoreCase(s)) return true;
        return false;
    }
    
    public static int getValueDec(String mnem){
        for(int i = 0; i<listMnem.size(); i++){
            if((listMnem.get(i).mnemonic).equals(mnem) ) return listMnem.get(i).valueDec;
        }
        return 0;
    }

    public static String getValueHex(String mnem){
        int indx = 0;
        for(int i = 0; i<listMnem.size(); i++){
            if((listMnem.get(indx).mnemonic).equals(mnem) ) return listMnem.get(indx).valueHex;
        }
        return "0";
    }

    public static List<Polje> getListMnem() {
        return listMnem;
    }


}
