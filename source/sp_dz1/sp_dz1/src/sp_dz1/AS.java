/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz1;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author MB
 */
public class AS {

    private SA syx;
    private TableSymbols table;
    private TableOperation tableOP;
    private TableDirective tableDir;
    private TableRelocations tableRel;
    private static int location;
    private static int nsegs;
    private static int nsyms;
    private static int nrels;
    private static int prevSeg;
    private boolean assembling;
    private static int mem[];
    private int seg;

    private static String[] opForDefinedSymbols;
    private static int place;

    private static int lenSymbInBss[];
    private static int bssPlace;

    public AS(String fileName) {
        table = new TableSymbols();
        tableOP = new TableOperation();
        tableDir = new TableDirective();
        tableRel = new TableRelocations();
        syx = new SA(fileName);
        mem = new int[256];
        seg = 0;
        nsegs = nsyms = nrels = 0;
        prevSeg = 0;

        opForDefinedSymbols = new String[20];
        place = 0;

        lenSymbInBss = new int[10];
        bssPlace = 0;
    }

    public static void insertNewOp(String op){
        opForDefinedSymbols[place++] = op;
    }

    public void assemble() {
        System.out.println("Assembling...");
        firstPass();
        saveObjCode();
        System.out.println("Assembling complete!");
    }

    public void firstPass() {
        assembling = true;
        location = 0;

        syx.SA_analysis();
        LA.LA_sym symb;
        LinkedList<LA.LA_sym> lista = syx.getLista();
        int indx = 0;
        while (assembling) {
            symb = lista.get(indx);
            assembleLine(symb);
            indx++;
        }
        table.backpatch(mem, tableRel);
        //table.printsymboltable();
    }

    public void assembleLine(LA.LA_sym symb) {
        String op = symb.getMnem();
        if (TableOperation.isDirective(op)) // directives
        {
            if ("BEG".equalsIgnoreCase(op)) {
                location = 0;
            } else if ("END".equalsIgnoreCase(op)) {
                int base = tableDir.getListZapis().get(seg - 1).getBase();
                tableDir.getListZapis().get(seg - 1).setLen(location - base);
                assembling = false;
            } else if ("ORG".equalsIgnoreCase(op)) {
                location = Integer.parseInt(symb.getAdr());
            } else if ("DS".equalsIgnoreCase(op)) {
                if (symb.isIsLabeled()) {
                    table.enter(symb.getLabel(), location, 2, false, false);
                }
                int forAdd = evaluate(symb.getAdr(), symb.getListSymbInAdrField(), symb.getListOpInAdrField());
                lenSymbInBss[bssPlace++] = location;
                lenSymbInBss[bssPlace++] = forAdd;
                location = location + forAdd;
            } else if ("EQU".equalsIgnoreCase(symb.getMnem())) {
                nsyms++;
                if (isNumber(symb.getAdr())) {
                    table.enter(symb.getLabel(), Integer.parseInt(symb.getAdr()), 0, true, true);
                }else {
                    if (hasMoreThanOneOperand(symb)) {
                        table.enter(symb.getLabel(), 0, seg, true, true);//evaluate(symb.getAdr(), symb.getListSymbInAdrField(), symb.getListOpInAdrField())
                        int size = symb.getSizeListAdrSym();
                        for (int i = 0; i < size; i++) {
                            if (!isNumber(symb.getListSymbInAdrField().get(i))) {
                                if (!table.isGlobusedSymb(symb.getListSymbInAdrField().get(i))) {
                                    tableRel.add(symb.getListSymbInAdrField().get(i), location, seg, false);
                                } else {
                                    tableRel.add(symb.getListSymbInAdrField().get(i), location, seg, 0, true); //table.numGlobusedSymbInTable(symb.getListSymbInAdrField().get(i))
                                }
                                evaluate(symb.getAdr(), symb.getListSymbInAdrField(), symb.getListOpInAdrField());
                                nrels++;
                            } else {
                                if(symb.getListOpInAdrField().get(i - 1).equalsIgnoreCase("+")){
                                    table.enter(symb.getLabel(), table.getSymbol(symb.getLabel()).getValue() + Integer.parseInt(symb.getListSymbInAdrField().get(i)), 0, true, true);
                                }else if(symb.getListOpInAdrField().get(i - 1).equalsIgnoreCase("-")){
                                    table.enter(symb.getLabel(), table.getSymbol(symb.getLabel()).getValue() - Integer.parseInt(symb.getListSymbInAdrField().get(i)), 0, true, true);
                                }
                            }
                        }
                    } else {
                        if (!table.isGlobusedSymb(symb.getAdr())) {
                            tableRel.add(symb.getAdr(), location, seg, false);
                            table.enter(symb.getLabel(), evaluate(symb.getAdr(), symb.getListSymbInAdrField(), symb.getListOpInAdrField()), seg, true, true);
                        } else {
                            table.enter(symb.getLabel(), location, seg, true, true);
                            tableRel.add(symb.getAdr(), location, seg, table.numGlobusedSymbInTable(symb.getLabel()),true);
                        }
                        nrels++;
                    }
                }

                } else  if ("TXT".equalsIgnoreCase(symb.getMnem())) {
                seg = 1;
                nsegs++;
                tableDir.getListZapis().get(seg - 1).setBase(location);
                if (prevSeg != 0) {
                    int base = tableDir.getListZapis().get(prevSeg - 1).getBase();
                    tableDir.getListZapis().get(prevSeg - 1).setLen(location - base);
                }
                prevSeg = seg;
            } else if ("BSS".equalsIgnoreCase(symb.getMnem())) {
                seg = 2;
                nsegs++;
                tableDir.getListZapis().get(seg - 1).setBase(location);
                if (prevSeg != 0) {
                    int base = tableDir.getListZapis().get(prevSeg - 1).getBase();
                    tableDir.getListZapis().get(prevSeg - 1).setLen(location - base);
                }
                prevSeg = seg;
            } else if ("DAT".equalsIgnoreCase(symb.getMnem())) {
                seg = 3;
                nsegs++;
                tableDir.getListZapis().get(seg - 1).setBase(location);
                if (prevSeg != 0) {
                    int base = tableDir.getListZapis().get(prevSeg - 1).getBase();
                    tableDir.getListZapis().get(prevSeg - 1).setLen(location - base);
                }
                prevSeg = seg;
            } else if ("USE".equalsIgnoreCase(symb.getMnem())) {
                int size = symb.getSizeListAdrSym();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        table.enter(symb.getListSymbInAdrField().get(i), 0, 0, true, false);
                    }
                    nsyms += size;
                } else {
                    table.enter(symb.getAdr(), 0, 0, true, false);
                    nsyms++;
                }
            } else if ("DEF".equalsIgnoreCase(symb.getMnem())) {
                int size = symb.getSizeListAdrSym();
                if (size > 0) {
                    for (int i = 0; i < size; i++) {
                        table.enter(symb.getListSymbInAdrField().get(i), 0, 0, false, true);
                    }
                    nsyms += size;
                } else {
                    table.enter(symb.getAdr(), 0, 0, false, true);
                    nsyms++;
                }
            }
        } else // machine ops
        {

            if (symb.isIsLabeled()) {
                table.enter(symb.getLabel(), location, seg, false, false);
            }
            mem[location] = TableOperation.getValueDec(symb.getMnem());
            if (TableOperation.twoBytesInstr(symb.getMnem())) // TwoByteOps
            {
                if(!symb.getMnem().equalsIgnoreCase("DC"))location++;
                mem[location] = evaluate(symb.getAdr(), symb.getListSymbInAdrField(), symb.getListOpInAdrField());
                if (!symb.getMnem().equalsIgnoreCase("DS") && !symb.getMnem().equalsIgnoreCase("EQU")
                        && !isNumber(symb.getAdr())) {
                    if (hasMoreThanOneOperand(symb)) {
                        int size = symb.getSizeListAdrSym();
                        for (int i = 0; i < size; i++) {
                            if (!isNumber(symb.getListSymbInAdrField().get(i))) {
                                if (!table.isGlobusedSymb(symb.getListSymbInAdrField().get(i))) {
                                    tableRel.add(symb.getListSymbInAdrField().get(i), location, seg, false);
                                } else {
                                    tableRel.add(symb.getListSymbInAdrField().get(i), location, seg, table.numGlobusedSymbInTable(symb.getListSymbInAdrField().get(i)), true);
                                }
                                nrels++;
                            }
                        }
                    } else {
                        if (!table.isGlobusedSymb(symb.getAdr())) {
                            tableRel.add(symb.getAdr(), location, seg, false);
                        } else {
                            tableRel.add(symb.getAdr(), location, seg, table.numGlobusedSymbInTable(symb.getAdr()), true);
                        }
                        nrels++;
                    }
                }
            }

            if (!symb.getMnem().equalsIgnoreCase("")
                    && !symb.getMnem().equalsIgnoreCase(" ")) {
                location++;
            }
        }
    }

    public void saveObjCode() {
        BufferedWriter izlaz = null;
        try {
            izlaz = new BufferedWriter(new FileWriter("izlaz.txt"));
        } catch (IOException ex) {
            System.out.println("Neuspesno kreiranje izlaznog fajla!");
            Logger.getLogger(AS.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            izlaz.write("LINK");
            izlaz.newLine();
            izlaz.write(nsegs + " " + nsyms + " " + nrels);
            izlaz.newLine();
            izlaz.write("# segments\t(name\tbase\tlen\tdesc)\n");
            izlaz.newLine();
            for (int i = 0; i < 3; i++) {
                if (tableDir.isDefined(i)) {
                    izlaz.write(tableDir.getZapisString(i));
                    izlaz.newLine();
                }
            }
            izlaz.write("# symbols\t(name\tvalue\tseg\ttype)\n");
            izlaz.newLine();
            for(int i = 0; i < table.getSize(); i++) {
                if (table.getListsym().get(i).isGlobdef() && table.getListsym().get(i).isGlobuse()) {
                    izlaz.write(table.getEntryString(i));
                    izlaz.newLine();
                }
            }
            for (int i = 0; i < table.getSize(); i++) {
                if (table.getListsym().get(i).isGlobuse() && !table.getListsym().get(i).isGlobdef()) {
                    izlaz.write(table.getEntryString(i));
                    izlaz.newLine();
                }
            }
            for(int i = 0; i < table.getSize(); i++) {
                if (table.getListsym().get(i).isGlobdef() && !table.getListsym().get(i).isGlobuse()) {
                    izlaz.write(table.getEntryString(i));
                    izlaz.newLine();
                }
            }
            izlaz.write("# relocations\t(loc\tseg\tref\ttype)\t(name\top\tplace/len)");
            izlaz.newLine();
            int defOp = 0;
            for (int i = 0; i < nrels; i++) {
                izlaz.write(tableRel.getZapisString(i));
                if (table.getOpType(tableRel.getListZapis().get(i).getName(), tableRel.getListZapis().get(i).getLoc()).equalsIgnoreCase("+")
                        || table.getOpType(tableRel.getListZapis().get(i).getName(), tableRel.getListZapis().get(i).getLoc()).equalsIgnoreCase("-")) {
                    izlaz.write(table.getOpType(tableRel.getListZapis().get(i).getName(), tableRel.getListZapis().get(i).getLoc()));
                    if (tableRel.getListZapis().get(i).getRef() == 2 && !tableRel.getListZapis().get(i).getType().contains("S")) {
                        for (int j = 0; j < lenSymbInBss.length; j += 2) {
                            if (table.getSymbol(tableRel.getListZapis().get(i).getName()).getValue() == lenSymbInBss[j]) {
                                izlaz.write("\t" + lenSymbInBss[j + 1]);
                            }
                        }
                    }
                    if (tableRel.getListZapis().get(i).getRef() == 3 && !tableRel.getListZapis().get(i).getType().contains("S")) {
                        izlaz.write("\t" + table.getSymbol(tableRel.getListZapis().get(i).getName()).getValue());
                    }

                    if(tableRel.getListZapis().get(i).getRef() == tableRel.getListZapis().get(i).getSeg()){
                        for (int j = 0; j < table.getSize(); j++) {
                            if(tableRel.getListZapis().get(i).getName().equalsIgnoreCase(table.getListsym().get(j).getName())){
                                izlaz.write("\t" + table.getListsym().get(j).getValue());
                                break;
                            }
                        }
                    }
                } else {
                    if (place > 0) {
                        izlaz.write(opForDefinedSymbols[defOp]);
                        defOp++;
                        if (tableRel.getListZapis().get(i).getRef() == tableRel.getListZapis().get(i).getSeg()) {
                            for (int j = 0; j < table.getSize(); j++) {
                                if (tableRel.getListZapis().get(i).getName().equalsIgnoreCase(table.getListsym().get(j).getName())) {
                                    izlaz.write("\t" + table.getListsym().get(j).getValue());
                                    break;
                                }
                            }
                        } else {
                            izlaz.write("+");
                        }
                        
                    }
                }
                izlaz.newLine();
            }
            izlaz.write("# data (one line per segment)");
            izlaz.newLine();

            int base = tableDir.getListZapis().get(0).getBase(),
                    len = base + tableDir.getListZapis().get(0).getLen();
            for (int i = base; i < len; i++) {
                izlaz.write(mem[i] + " ");
            }
            izlaz.newLine();
            base = tableDir.getListZapis().get(2).getBase();
            len = base + tableDir.getListZapis().get(2).getLen();
            for (int i = base; i < len; i++) {
                izlaz.write(mem[i] + " ");
            }
            izlaz.newLine();

        } catch (IOException ex) {
            System.out.println("Neuspesan upis u izlaznu datoteku!");
            Logger.getLogger(AS.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            izlaz.close();
        } catch (IOException ex) {
            System.out.println("Neuspesno zatvaranje izlaznog fajla!");
            Logger.getLogger(AS.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isNumber(String num) {
        char arr[] = num.trim().toCharArray();
        boolean res = true;
        for (int i = 0; i < arr.length; i++) {
            res = res && (arr[i] >= '0' && arr[i] <= '9');
        }
        return res;
    }

    // make int to hex string
    private String makeHexFromInt(int num){
        String hexS = Integer.toString(num, 16).toUpperCase();
        return hexS;
    }

    private boolean hasMoreThanOneOperand(LA.LA_sym symb) {
        return symb.getSizeListAdrSym() > 0;
    }

    private int evaluate(String adr, List<String> listSymb, List<String> listOp) {
        int forRet = 0;
        if (listSymb.size() > 0) {
            int ind = 0;
            while (ind < listSymb.size()) {
                if (isNumber(listSymb.get(ind))) {
                    if (ind == 0) {
                        forRet += Integer.parseInt(listSymb.get(ind));
                    } else {
                        if (listOp.get(ind - 1).equals("+")) {
                            forRet += Integer.parseInt(listSymb.get(ind));
                        } else if (listOp.get(ind - 1).equals("-")) {
                            forRet -= Integer.parseInt(listSymb.get(ind));
                        }
                    }

                } else {
                    if (ind == 0) {
                        forRet += table.valueOfSymbol(listSymb.get(ind), location, "+");
                    } else {
                        if("+".equalsIgnoreCase(listOp.get(ind - 1)))
                            forRet += table.valueOfSymbol(listSymb.get(ind), location, listOp.get(ind - 1));
                        if("-".equalsIgnoreCase(listOp.get(ind - 1)))
                            forRet -= table.valueOfSymbol(listSymb.get(ind), location, listOp.get(ind - 1));
                    }
                }
                ind++;
            }
            return forRet;
        } else{
            if (isNumber(adr)) {
                return Integer.parseInt(adr);
            }
            if (table.isDefinedSymb(adr)) {
                //return table.valueOfSymbol(adr, location, "+");
                insertNewOp("+");
                return table.getListsym().get(table.findEntry(adr)).getValue();
            }
            table.valueOfSymbol(adr, location, "+");
            return 0;
        }
    }
}
