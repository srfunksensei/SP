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
public class TableOperation {//tabela operacionih kodova
    class Polje{
        String mnemonic;
        String valueHex;
        int valueDec;
        boolean needZ, needC, needP, setC, setPZ;

        public Polje(String mnemonic, String valueHex, int valueDec, boolean needZ, boolean needC, boolean needP, boolean setC, boolean setPZ) {
            this.mnemonic = mnemonic;
            this.valueHex = valueHex;
            this.valueDec = valueDec;
            this.needZ = needZ;
            this.needC = needC;
            this.needP = needP;
            this.setC = setC;
            this.setPZ = setPZ;
        }
    };

    private static List<Polje> listMnem;

    public TableOperation() {
        listMnem = new LinkedList<Polje>();
        addItems();
    }

    private void addItems(){
        //jednobajtne
        listMnem.add(new Polje("CLC", "02h", 2, false, false, false, true, false));
        listMnem.add(new Polje("CLX", "03h", 3, false, false, false, false, false));
        listMnem.add(new Polje("CMC", "04h", 4, false, false, false, true, false));
        listMnem.add(new Polje("INC", "05h", 5, false, false, false, false, true));
        listMnem.add(new Polje("DEC", "06h", 6, false, false, false, false, true));
        listMnem.add(new Polje("INX", "07h", 7, false, false, false, false, true));
        listMnem.add(new Polje("DEX", "08h", 8, false, false, false, false, true));
        listMnem.add(new Polje("TAX", "09h", 9, false, false, false, false, false));
        listMnem.add(new Polje("INI", "0Ah", 10, false, false, false, false, true));
        listMnem.add(new Polje("OTI", "0Eh", 14, false, false, false, false, false));
        listMnem.add(new Polje("PSH", "13h", 19, false, false, false, false, false));
        listMnem.add(new Polje("POP", "14h", 20, false, false, false, false, true));
        listMnem.add(new Polje("SHL", "15h", 21, false, false, false, true, true));
        listMnem.add(new Polje("SHR", "16h", 22, false, false, false, true, true));
        listMnem.add(new Polje("RET", "17h", 23, false, false, false, false, false));
        listMnem.add(new Polje("HLT", "18h", 24, false, false, false, false, false));

        //dvobajtne
        listMnem.add(new Polje("LDA", "19h", 25, false, false, false, false, true));
        listMnem.add(new Polje("LDX", "1Ah", 26, false, false, false, false, true));
        listMnem.add(new Polje("LDI", "1Bh", 27, false, false, false, false, true));
        listMnem.add(new Polje("STA", "1Eh", 30, false, false, false, false, false));
        listMnem.add(new Polje("STX", "1Fh", 31, false, false, false, false, false));
        listMnem.add(new Polje("ADD", "20h", 32, false, false, false, true, true));
        listMnem.add(new Polje("ADX", "21h", 33, false, false, false, true, true));
        listMnem.add(new Polje("SUB", "26h", 38, false, false, false, true, true));
        listMnem.add(new Polje("SBX", "27h", 39, false, false, false, true, true));
        listMnem.add(new Polje("CMP", "2Ch", 44, false, false, false, true, true));
        listMnem.add(new Polje("ANA", "2Fh", 47, false, false, false, true, true));
        listMnem.add(new Polje("ORA", "32h", 50, false, false, false, true, true));
        listMnem.add(new Polje("BRN", "35h", 53, false, false, false, false, false));
        listMnem.add(new Polje("BZE", "36h", 54, true, false, false, false, false));
        listMnem.add(new Polje("BNZ", "37h", 55, true, false, false, false, false));
        listMnem.add(new Polje("BPZ", "38h", 56, false, false, true, false, false));
        listMnem.add(new Polje("BNG", "39h", 57, false, false, true, false, false));
        listMnem.add(new Polje("BCC", "3Ah", 58, false, true, false, false, false));
        listMnem.add(new Polje("BCS", "3Bh", 59, false, true, false, false, false));
        listMnem.add(new Polje("JSR", "3Ch", 60, false, false, false, false, false));
    }

    public static boolean exists(int num){
        for(int i = 0; i<listMnem.size(); i++){
            if(listMnem.get(i).valueDec == num) return true;
        }
        return false;
    }

    public static boolean oneByteInstr(int num){
        return num == 2 || num == 3 || num == 4 || num == 5 || num == 6
                || num == 7 || num == 8 || num == 9 || num == 10 || num == 14
                || num == 19 || num == 20 || num == 21 || num == 22
                || num == 23 || num == 24;
}
    
    public static boolean twoBytesInstr(int num){
        return num == 25 || num == 26 || num == 27 || num ==30
                || num == 31 || num == 32 || num == 33 || num == 38
                || num == 39 || num == 44 || num == 47 || num == 50
                || num == 53 || num == 54 || num == 55 || num == 56
                || num == 57 || num == 58 || num == 59 || num == 60;
    }

    public static boolean isJumpInstr(int  num){ //JSR & BRN not included
        return num == 54 || num == 55 || num == 56
                || num == 57 || num == 58 || num == 59;

    }

    public static boolean isInstructionThatSetsPZ(int num){
        return num == 5 || num == 6 || num == 7 || num == 8|| num == 10
                || num == 20 || num == 21 || num == 22 || num == 25
                || num == 26 || num == 27 || num == 32 || num == 33
                || num == 38 || num == 39 || num == 44 || num == 47
                || num == 50;
    }

    public static boolean isInstructionThatSetsC(int num){
        return num == 2 || num == 4 || num == 21 || num == 22
                || num == 32 || num == 33 || num == 38 || num == 39
                || num == 44 || num == 47 || num == 50;
    }

    public static boolean jumpInstrPZ(int num){
        return num == 54 || num == 55 || num == 56 || num == 57;

    }

    public static boolean jumpInstrC(int num){
        return num == 58 || num == 59;
    }

    //delete this and next
    public static boolean jumpInstrPZ(String s){
        return s.equalsIgnoreCase("jz") || s.equalsIgnoreCase("jnz") ||
                s.equalsIgnoreCase("jg") || s.equalsIgnoreCase("jng");

    }

    public static boolean jumpInstrC(String s){
        return s.equalsIgnoreCase("jnc") || s.equalsIgnoreCase("jc");
    }

    public static boolean needOptimisation(int num){
        return num == 10 || num == 20 || num == 25 || num == 26;
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
