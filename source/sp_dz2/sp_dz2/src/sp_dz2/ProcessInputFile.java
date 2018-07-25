/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz2;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
public class ProcessInputFile {

    private BufferedReader src;
    private BufferedWriter izlaz;
    private int nsegs, nsyms, nrels;
    private TableSymbols table;
    private TableOperation tableOP;
    private TableRelocations tableRel;
    private int txtLen, bssLen, datLen;
    private int txtBase, bssBase, datBase;
    private static int[] mem;
    private static boolean isINI, isOTI;
    private static boolean hasUSE, hasDEF, hasEQU;
    private List<Block> listBlocks;

    private static String[] names;
    private static int[] locs;

    private List<String> listLocalSymbols;

    public ProcessInputFile(String fileName) throws IOException {
        nsegs = nsyms = nrels = 0;
        bssLen = txtLen = datLen = 0;
        bssBase = txtBase = datBase = 0;
        isINI = isOTI = false;
        hasDEF = hasEQU = hasUSE = false;
        mem = new int[256];
        table = new TableSymbols();
        tableOP = new TableOperation();
        tableRel = new TableRelocations();
        listBlocks = new LinkedList<Block>();

        listLocalSymbols = new LinkedList<String>();
        
        try {
            src = new BufferedReader(new FileReader(fileName));
            izlaz = new BufferedWriter(new FileWriter("izlaz.txt"));
        } catch (FileNotFoundException ex) {
            System.out.println("Neuspesno otvaranje fajla ili kreiranje izlaznog fajla!");
            Logger.getLogger(ProcessInputFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int getNrels() {
        return nrels;
    }

    public void setNrels(int nrels) {
        this.nrels = nrels;
    }

    public int getNsegs() {
        return nsegs;
    }

    public void setNsegs(int nsegs) {
        this.nsegs = nsegs;
    }

    public int getNsyms() {
        return nsyms;
    }

    public void setNsyms(int nsyms) {
        this.nsyms = nsyms;
    }

    public static boolean isIsINI() {
        return isINI;
    }

    public static boolean isIsOTI() {
        return isOTI;
    }

    public static boolean isHasDEF() {
        return hasDEF;
    }

    public static void setHasDEF(boolean hasDEF) {
        ProcessInputFile.hasDEF = hasDEF;
    }

    public static boolean isHasEQU() {
        return hasEQU;
    }

    public static void setHasEQU(boolean hasEQU) {
        ProcessInputFile.hasEQU = hasEQU;
    }

    public static boolean isHasUSE() {
        return hasUSE;
    }

    public static void setHasUSE(boolean hasUSE) {
        ProcessInputFile.hasUSE = hasUSE;
    }

    private void processLinesInFile() {
        String line_for_work = null;
        int numTaraba = 0;
        boolean isLink = false, firstPass = true;
        try {
            line_for_work = src.readLine();
            while (line_for_work != null) {
                if(line_for_work.equalsIgnoreCase("")
                    || line_for_work.equalsIgnoreCase(" ")){
                    line_for_work = src.readLine();
                    continue;
                }
                if (line_for_work.contains("LINK")) {
                    isLink = true;
                    line_for_work = src.readLine();
                    continue;
                }
                if (line_for_work.contains("#")) {
                    numTaraba++;
                    line_for_work = src.readLine();
                    continue;
                }
                if (!line_for_work.contains("LINK") || !line_for_work.contains("#")) {
                    if (isLink) {
                        String[] splitNum = line_for_work.split("[\\s]+");
                        nsegs = Integer.parseInt(splitNum[0]);
                        nsyms = Integer.parseInt(splitNum[1]);
                        nrels = Integer.parseInt(splitNum[2]);
                        isLink = false;
                        names = new String[nrels];
                        locs = new int[nrels];
                    }
                    if (numTaraba == 1) {
                        processSegmentLine(line_for_work);
                    } else if (numTaraba == 2) {
                        processSymbolLine(line_for_work);
                    } else if (numTaraba == 3) {
                        processRelocLine(line_for_work);
                    } else if (numTaraba == 4) {
                        processDataLine(line_for_work, firstPass);
                        firstPass = false;
                    }
                }
                line_for_work = src.readLine();
            }
        } catch (IOException ex) {
            System.out.println("Neuspesno citanje linije iz ulaznog fajla!");
            Logger.getLogger(ProcessInputFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void processSegmentLine(String line) {
        String[] segs = line.split("[\\s]+");
        if (segs[1].equalsIgnoreCase(".bss")) {
            bssBase = Integer.parseInt(segs[2]);
            bssLen = Integer.parseInt(segs[3]);
        }
        if (segs[1].equalsIgnoreCase(".text")) {
            txtBase = Integer.parseInt(segs[2]);
            txtLen = Integer.parseInt(segs[3]);
        }
        if (segs[1].equalsIgnoreCase(".data")) {
            datBase = Integer.parseInt(segs[2]);
            datLen = Integer.parseInt(segs[3]);
        }
    }

    private void processSymbolLine(String line) {
        String[] syms = line.split("[\\s]+");
        if (syms[4].equalsIgnoreCase("U")) {
            table.enter(syms[1], Integer.parseInt(syms[2]), Integer.parseInt(syms[3]), true, false);
            if (!hasUSE) {
                hasUSE = true;
            }
        }
        if (syms[4].equalsIgnoreCase("D")) {
            table.enter(syms[1], Integer.parseInt(syms[2]), Integer.parseInt(syms[3]), false, true);
            if (!hasDEF) {
                hasDEF = true;
            }
        }
        if (syms[4].equalsIgnoreCase("E")) {
            table.enter(syms[1], Integer.parseInt(syms[2]), Integer.parseInt(syms[3]), true, true);
            if (!hasEQU) {
                hasEQU = true;
            }
        }
    }

    private void processRelocLine(String line) {
        String[] rels = line.split("[\\s]+");
        tableRel.add(rels[5], Integer.parseInt(rels[1]), Integer.parseInt(rels[2]), Integer.parseInt(rels[3]), rels[4], rels[6], rels.length == 8 ? Integer.parseInt(rels[7]) : -1);
    }

    private void processDataLine(String line, boolean firstPass) {
        String[] data = line.split("[\\s]+");
        int place = 0;
        if (firstPass) {
            place = txtBase;
        } else {
            place = datBase;
        }
        for (int i = 0; i < data.length; i++) {
            mem[place++] = Integer.parseInt(data[i]);
            
        }
        if (firstPass) {
            place = bssBase;
            for (int i = 0; i < bssLen; i++) {
                mem[place++] = 0;
            }
        }
        for (int i = txtBase; i < txtLen; i++) {
                if (tableOP.oneByteInstr(mem[i])) {
                    if (mem[i] == 10) isINI = true;
                    if (mem[i] == 14)isOTI = true;
                    continue;
                }
                i++;
            }
    }

    private void refactorCode() {
        Block b = new Block();
        getLabels(locs, names);
        for (int i = txtBase; i < txtBase + txtLen; ) {
            Instruction instr = new Instruction();
            instr.setOpCode(mem[i]);
            boolean optim = tableOP.needOptimisation(mem[i]);
            boolean isADD = false;

            if (optim) {
                b.setNeedOptimisation(true);
                b.setCriticLine(b.getSize());
            }

            if (tableOP.oneByteInstr(mem[i])) {
                instr.setReplacement(repleceOneByteInstr(mem[i]));
                if (tableOP.isInstructionThatSetsC(mem[i])) {
                    instr.setSetC(true);
                    b.setSetC(true);
                }
                if (tableOP.isInstructionThatSetsPZ(mem[i])) {
                    instr.setSetPZ(true);
                    b.setSetPZ(true);
                }
                //adds label to the line if there is any
                for (int j = 0; j < locs.length && names[j]!= null; j++) {
                    if (locs[j] == i) {
                        instr.setHasLabel(true);
                        instr.setLabel("_" + names[j] + ":");

                        //add previous block to graph if not first instruction
                        if (i != txtBase && b.getSize()!= 0) {
                            listBlocks.add(b);

                            //make new block and add instruction
                            b = new Block();
                            b.addInstr(instr);
                            listBlocks.get(listBlocks.size() - 1).setNext1(b);
                        } else {
                            b.addInstr(instr);
                        }
                        isADD = true;
                        break;
                    }
                }
                if(!isADD){
                    for (int j = 0; j < table.getSize(); j++) {
                        if(table.getListsym().get(j).isGlobdef() && !table.getListsym().get(j).isGlobuse()
                                && table.getListsym().get(j).getValue() == i){
                            instr.setHasLabel(true);
                            instr.setLabel(table.getListsym().get(j).getName());
                            break;
                        }
                    }
                    if (i != txtBase && instr.isHasLabel()) {
                        listBlocks.add(b);

                        //make new block and add instruction
                        b = new Block();
                        b.addInstr(instr);
                        listBlocks.get(listBlocks.size() - 1).setNext1(b);
                    } else {
                        b.addInstr(instr);
                    }
                }
                i++;
                continue;
            }

            if (tableOP.twoBytesInstr(mem[i])) {
                String splitedL[];// = tableRel.getAdrField(i + 1).split("[\\s]+");
                String label = "";
                
                if(tableRel.getAdrField(i + 1) == null) {
                    splitedL = new String[0];
                    label = Integer.toString(mem[i + 1]);
                } else {
                    splitedL = makeAdrPart(i + 1).split("[\\s]+");//tableRel.getAdrField(i + 1).split("[\\s]+");
                }

                for (int j = 0; j < splitedL.length && splitedL[j] != null; j++) {
                    if(splitedL[j].equalsIgnoreCase("+") || splitedL[j].equalsIgnoreCase("-")){
                        label += splitedL[j];
                        continue;
                    }
                    boolean added = false;
                    for (int k = 0; k < names.length && names[k] != null; k++) {
                        if(splitedL[j].equalsIgnoreCase(names[k])){
                            String nameLab = "_" + names[k];
                            if(!label.equalsIgnoreCase("")) label += nameLab;
                            else label +=nameLab;
                            added = true;
                        }
                    }
                    if (!added) {
                            if(!label.equalsIgnoreCase("")) label += splitedL[j];
                            else label += splitedL[j];
                        }
                    }

                instr.setReplacement(replaceTwoBytesInstr(mem[i], label));
                instr.setJumpOnLabel(label);

                if (tableOP.jumpInstrC(mem[i])) {
                    instr.setNeedC(true);
                    b.setNeedC(true);
                }
                if (tableOP.jumpInstrPZ(mem[i])) {
                    instr.setNeedP(true);
                    instr.setNeedZ(true);
                    b.setNeedP(true);
                    b.setNeedZ(true);
                }

                for (int j = 0; j < locs.length && names[j]!= null; j++) {
                    if (locs[j] == i) {
                        instr.setHasLabel(true);
                        instr.setLabel("_" + names[j] + ":");
                        break;
                    }
                }

                if(instr.isHasLabel() && tableOP.isJumpInstr(mem[i])){
                    listBlocks.add(b);
                    b = new Block();
                    b.addInstr(instr);
                    listBlocks.get(listBlocks.size() - 1).setNext1(b);
                    listBlocks.add(b);
                    b = new Block();
                    listBlocks.get(listBlocks.size() - 1).setNext1(b);
                }else {
                    b.addInstr(instr);
                    if (tableOP.isJumpInstr(mem[i])) {
                        listBlocks.add(b);
                        b = new Block();
                        listBlocks.get(listBlocks.size() - 1).setNext1(b);
                    }
                }
                i+=2;
            }
        }
        listBlocks.add(b);
        setBlock2();
        for (int i = 0; i < listBlocks.size(); i++) {
            optimise(listBlocks.get(i));
            writeCodeFromBlock(listBlocks.get(i));
        }
    }

    private void getLabels( int[] locs, String[] names){
        int pos = 0;
        for (int i = 0; i < tableRel.getListZapis().size(); i++) {
            if (tableRel.getZapis(i).getRef() == tableRel.getZapis(i).getSeg()
                    && tableRel.getZapis(i).getRef() == 1
                    && !tableRel.getZapis(i).getType().contains("S")) {
                boolean inList = false;
                for (int j = 0; j < names.length && names[j] != null; j++) {
                    if (tableRel.getZapis(i).getName().equalsIgnoreCase(names[j])) {
                        inList = true;
                    }
                }
                if (!inList) {
                    locs[pos] = tableRel.getZapis(i).getLocOrLen() == -1 ? mem[tableRel.getZapis(i).getLoc()] : tableRel.getZapis(i).getLocOrLen();
                    names[pos++] = tableRel.getZapis(i).getName();
                }
            }
        }
    }

    //sets all second blocks for jump instruction for all blocks
    private void setBlock2() {
        for (int i = 0; i < listBlocks.size(); i++) {
            Block block = listBlocks.get(i);
            if(tableOP.isJumpInstr(block.getLastInsrtInBlock().getOpCode()) &&
                    !block.getLastInsrtInBlock().getJumpOnLabel().equalsIgnoreCase("") &&
                    !block.getLastInsrtInBlock().getJumpOnLabel().equalsIgnoreCase(" ") ){
                for (int j = 0; j < listBlocks.size(); j++) {
                    Block block1 = listBlocks.get(j);
                    if(block1.getInstruction(0).isHasLabel() &&
                            block1.getInstruction(0).getLabel().equalsIgnoreCase(block.getLastInsrtInBlock().getJumpOnLabel() + ":")){
                        block.setNext2(block1);
                    }
                }
            }
        }
    }

    private static boolean optimise(Block block) {
        if (!block.isNeedOptimisation()) {
            return false;
        }
        block.wasInBlock = true;
        if (TableOperation.jumpInstrPZ(block.getInstruction(block.getSize() - 1).getOpCode())) {
            for (int i = block.getCriticLine(); i < block.getSize() - 1 - 1; i++) {
                if (TableOperation.isInstructionThatSetsPZ(block.getInstruction(i).getOpCode())) {
                    return false;
                }
            }
            Instruction i = new Instruction();
            i.setReplacement("test AL, AL");
            block.addOnPlace(i, block.getCriticLine() + 1);
            return true;
        } else if (block.getNext1() != null && tryDeeper(block.getNext1())) {
            Instruction i = new Instruction();
            i.setReplacement("dec AL");
            block.addOnPlace(i, block.getCriticLine() + 1);
            i = new Instruction();
            i.setReplacement("inc AL");
            block.addOnPlace(i, block.getCriticLine() + 1);
        } else if (block.getNext2() != null && tryDeeper(block.getNext2())) {
            Instruction i = new Instruction();
            i.setReplacement("dec AL");
            block.addOnPlace(i, block.getCriticLine() + 1);
            i = new Instruction();
            i.setReplacement("inc AL");
            block.addOnPlace(i, block.getCriticLine() + 1);
        }

        return false;
    }

        // try on that branch
    private static boolean tryDeeper(Block next) {
	if(next.wasInBlock)return false;
	next.wasInBlock=true;
	for(int i=0; i<next.getSize(); i++)
		if(TableOperation.isInstructionThatSetsPZ(next.getInstruction(i).getOpCode()))return false;
	if(TableOperation.jumpInstrPZ(next.getInstruction(next.getSize() - 1).getOpCode()))return true;
	if(next.getNext1()!=null && tryDeeper(next.getNext1())) return true;
	if(next.getNext2()!=null && tryDeeper(next.getNext2())) return true;
	return false;
    }

    private void writeCodeFromBlock(Block block){
        for (int i = 0; i < listBlocks.size(); i++) {
            listBlocks.get(i).wasInBlock = false;
        }
        for (int i = 0; i < block.getSize(); i++) {
            try {
                if(block.getInstruction(i).getReplacement() != null){
                    if(block.getInstruction(i).isHasLabel()){
                        izlaz.write(block.getInstruction(i).getLabel() + "\t");
                    }else {
                        izlaz.write("         ");
                    }
                    izlaz.write(block.getInstruction(i).getReplacement());
                    izlaz.newLine();
                }
            } catch (IOException ex) {
                System.out.println("Neuspesan upis u fajl!");
                Logger.getLogger(ProcessInputFile.class.getName()).log(Level.SEVERE, null, ex);
            }
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

    private String repleceOneByteInstr(int num) {
        String rep = "";

        if (num == 2) {
            rep = "clc";
        }
        if (num == 3) {
            rep = "xor CL, CL";
        }
        if (num == 4) {
            rep = "cmc";
        }
        if (num == 5) {
            rep = "inc AL";
        }
        if (num == 6) {
            rep = "dec AL";
        }
        if (num == 7) {
            rep = "inc CL";
        }
        if (num == 8) {
            rep = "dec CL";
        }
        if (num == 9) {
            rep = "mov CL, AL";
        }
        if (num == 10) {
            rep = "call readdec"; 
        }
        if (num == 14) {
            rep = "call writedec";
        }
        if (num == 19) {
            rep = "push AX";
        }
        if (num == 20) {
            rep = "pop AX";
        }
        if (num == 21) {
            rep = "shl AL, 1";
        }
        if (num == 22) {
            rep = "shr AL, 1";
        }
        if (num == 23) {
            rep = "pop EIP";
        }
        if (num == 24) {
            rep = "invoke ExitProcess, 0";
        }

        return rep;
    }

    private String replaceTwoBytesInstr(int num, String adr) {
        String rep = "";

        if (num == 25) {
            if(isNumber(adr)){
                rep = "mov AL, BYTE PTR [" + adr + "]";
            } else {
                rep = "mov AL, BYTE PTR " + adr;
            }
        }
        if (num == 26) {
            rep = "mov AL, BYTE PTR  [" + adr + " + ECX]";
        }
        if (num == 27) {
            if(isNumber(adr)){
                rep = "mov AL, " + adr;
            } else {
                rep = "mov AL, BYTE PTR " + adr;
            }
        }
        if (num == 30) {
            if(isNumber(adr)){
                rep = "mov [" + adr + "], AL";
            } else {
                rep = "mov " + adr + ", AL";
            }
        }
        if (num == 31) {
            rep = "mov [" + adr + " + ECX]" + ", AL";
        }
        if (num == 32) {
            if(isNumber(adr)){
                rep = "add AL, [" + adr + "]";
            } else {
                rep = "add AL, BYTE PTR " + adr;
            }
        }
        if (num == 33) {
            rep = "add AL, BYTE PTR [" + adr + " + ECX]";
        }
        if (num == 38) {
            if(isNumber(adr)){
                rep = "sub AL, BYTE PTR [" + adr + "]";
            } else {
                rep = "sub AL, BYTE PTR " + adr;
            }
        }
        if (num == 39) {
            rep = "sub AL, BYTE PTR [" + adr + " + ECX]";
        }
        if (num == 44) {
            if(isNumber(adr)){
                rep = "cmp AL, BYTE PTR [" + adr + "]";
            } else {
                rep = "cmp AL, BYTE PTR " + adr;
            }
        }
        if (num == 47) {
            if(isNumber(adr)){
                rep = "and AL, BYTE PTR [" + adr + "]";
            } else {
                rep = "and AL, BYTE PTR " + adr;
            }
        }
        if (num == 50) {
            if (isNumber(adr)) {
                rep = "or AL, BYTE PTR [" + adr + "]";
            } else {
                rep = "or AL, BYTE PTR " + adr;
            }
        }
        if (num == 53) {
            rep = "jmp " + adr;
        }
        if (num == 54) {
            rep = "jz " + adr;
        }
        if (num == 55) {
            rep = "jnz " + adr;
        }
        if (num == 56) {
            rep = "jg " + adr;
        }
        if (num == 57) {
            rep = "jng " + adr;
        }
        if (num == 58) {
            rep = "jnc " + adr;
        }
        if (num == 59) {
            rep = "jc " + adr;
        }
        if (num == 60) {
            rep = "call " + adr;
        }

        return rep;
    }

    //insert symbols in .data sections
    private void insertData() {
        String inserted[] = new String[nrels];
        int loc = 0;
        try {
            boolean insertedZ = false;
            //insert reserved location if any

            //insert DS
            if (bssLen > 0) {
                int bss_len = 0;
                for (int i = 0; i < tableRel.getListZapis().size(); i++) {
                    if (tableRel.getZapis(i).getRef() == 2 && !tableRel.getZapis(i).getType().contains("S")) {
                        for (int j = 0; j < inserted.length && inserted[j] != null; j++) {
                            if (inserted[j].equalsIgnoreCase(tableRel.getZapis(i).getName())) {
                                insertedZ = true;
                                break;
                            }
                        }
                        if(!insertedZ){
                            bss_len += tableRel.getZapis(i).getLocOrLen();
                            inserted[loc++] = tableRel.getZapis(i).getName();
                        }
                        insertedZ = false;
                    }
                }
                bss_len = bssLen - bss_len;

                if (bss_len > 0) {
                    /* we can not have something like this
                     *  .bss
                     *  A DS 3
                     *    DS 12
                    */
                    boolean hasD = false;
                    for(int i = 0; i<table.getSize(); i++) {
                        if(table.getListsym().get(i).getSeg() == 2 &&
                                table.getListsym().get(i).isGlobdef() &&
                                !table.getListsym().get(i).isGlobuse())
                            hasD = true;
                    }
                    if(!hasD){
                        izlaz.write("\tDB\t" + bssLen + "\tDUP(0)");
                        izlaz.newLine();
                    }
                }
            }

            //insert DC
            inserted = new String[nrels];
            insertedZ = false;
            loc = 0;
            if (datLen > 0) {
                int dat_len = 0;
                int [] locs = new int[nrels];
                for (int i = 0; i < tableRel.getListZapis().size(); i++) {
                    if (tableRel.getZapis(i).getRef() == 3 && !tableRel.getZapis(i).getType().contains("S")) {
                        for (int j = 0; j < inserted.length && inserted[j] != null; j++) {
                            if (inserted[j].equalsIgnoreCase(tableRel.getZapis(i).getName())) {
                                insertedZ = true;
                                break;
                            }
                        }
                        if(!insertedZ){
                            locs[loc] = tableRel.getZapis(i).getLocOrLen();
                            inserted[loc++] = tableRel.getZapis(i).getName();
                            dat_len ++;
                        }
                        insertedZ = false;
                    }
                }
                dat_len = datLen - dat_len;

                if (dat_len > 0) {
                    for (int i = datBase; i < datBase + datLen; i++) {
                        for (int j = 0; j < locs.length; j++) {
                            if( locs[j] == i){
                                insertedZ = true;
                                break;
                            }
                        }
                        if(!insertedZ){
                            izlaz.write("\tDB\t" + mem[i]);
                            izlaz.newLine();
                        }
                        insertedZ = false;
                    }
                }
            }

            //insert globdef symbols
            insertedZ = false;
            inserted = new String[nrels];
            loc = 0;
            for (int i = 0; i < table.getSize(); i++) {
                if(table.getListsym().get(i).isGlobdef() && !table.getListsym().get(i).isGlobuse() &&
                        table.getListsym().get(i).getSeg() != 1){
                    // to do
                    // if more than one DS Def
                    izlaz.write(table.getListsym().get(i).getName());
                    izlaz.write("\tDB\t");
                    if(table.getListsym().get(i).getSeg() == 2){
                        izlaz.write(Integer.toString(bssLen));
                        izlaz.write("\tDUP(0)");
                    } else if(table.getListsym().get(i).getSeg() == 3){
                        izlaz.write(Integer.toString(mem[table.getListsym().get(i).getValue()]));
                    }
                    izlaz.newLine();
                }
            }

            //insert other symbols
            for (int i = 0; i < nrels; i++) {
                if (tableRel.getZapis(i).getRef() != 1 && !tableRel.getZapis(i).getType().contains("S")) {
                    //tableRel.getZapis(i).getLocOrLen() != -1 && 
                    for (int j = 0; j < inserted.length && inserted[j] != null; j++) {
                        if (inserted[j].equalsIgnoreCase(tableRel.getZapis(i).getName())) {
                            insertedZ = true;
                            break;
                        }
                    }
                    if (!insertedZ) {

                        izlaz.write(tableRel.getZapis(i).getName());
                        izlaz.write("\tDB\t");
                        if (tableRel.getZapis(i).getRef() == 2) {
                            izlaz.write(Integer.toString(tableRel.getZapis(i).getLocOrLen()));
                            izlaz.write("\tDUP(0)");
                        } else if (tableRel.getZapis(i).getRef() == 3) {
                            izlaz.write(makeAdrPart(tableRel.getZapis(i).getLocOrLen()));
                        } else {
                            /* if programer forgot to put some
                             * symbols in .data or .bss segment
                             * it will be put
                             * symbolName DB ?
                             */
                            izlaz.write("?");
                        }
                        izlaz.newLine();
                        inserted[loc++] = tableRel.getZapis(i).getName();
                        insertedZ = false;

                        //insert in list for replacement
                        listLocalSymbols.add(tableRel.getZapis(i).getName());
                    }
                }
            }
            
            //insert for INI and OTI
            if (isINI) {
                izlaz.write("stdin\tDD\t0");
                izlaz.newLine();
            }
            if (isOTI) {
                izlaz.write("stdout\tDD\t0");
                izlaz.newLine();
            }
        } catch (IOException ex) {
            System.out.println("Neuspesan upis u izlazni fajl!");
            Logger.getLogger(ProcessInputFile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //make adres part for DC
    private String makeAdrPart(int loc){
        int value = mem[loc], valueRes = 0, numOp = 0;
        String val = "";
        boolean first = false;
        for (int i = 0; i < tableRel.getListZapis().size(); i++) {
            if(tableRel.getListZapis().get(i).getLoc() == loc){
                numOp++;
                if(!first){
                    if(tableRel.getListZapis().get(i).getOp().equalsIgnoreCase("+")){
                        valueRes += symbolValue(tableRel.getListZapis().get(i).getName());

                    } else if(tableRel.getListZapis().get(i).getOp().equalsIgnoreCase("-")){
                        valueRes -= symbolValue(tableRel.getListZapis().get(i).getName());
                        val += "- ";
                    }
                    first = true;
                    val+= makeSymbol(tableRel.getZapis(i).getName()); // fixup
                } else {
                    val += " " + tableRel.getListZapis().get(i).getOp() + " " + tableRel.getListZapis().get(i).getName();
                    if(tableRel.getListZapis().get(i).getOp().equalsIgnoreCase("+")){
                        valueRes += symbolValue(tableRel.getListZapis().get(i).getName());

                    } else if(tableRel.getListZapis().get(i).getOp().equalsIgnoreCase("-")){
                        valueRes -= symbolValue(tableRel.getListZapis().get(i).getName());
                    }
                }
            }
        }
        if(numOp > 0 && !isNumber(val)){
            if (Math.abs(valueRes) - Math.abs(value) == 0) return val;
            else {
                int num = 0;
                if (value >= 0) {
                    num = value - valueRes;
                } else {
                    if(Math.abs(value) > Math.abs(valueRes)){

                        num = value - valueRes;
                    } else{
                        num = Math.abs(valueRes) - Math.abs(value);
                    }
                }

                if(val.charAt(0) == '-'){
                    val = Integer.toString(num) + " " + val;
                } else {
                    if(num > 0){
                        val = val + " + " + Integer.toString(num);
                    } else {
                        val += " " + Integer.toString(num);
                    }
                }
            }
        } else {
            return Integer.toString(value);
        }

        return val;
    }

    private int symbolValue(String name){
        int val = 0;
        for (int i = 0; i < tableRel.getListZapis().size(); i++) {
            if(tableRel.getZapis(i).getName().equalsIgnoreCase(name) &&
                    !tableRel.getZapis(i).getType().contains("S")){
                if(tableRel.getZapis(i).getRef() == 3){
                    val = tableRel.getZapis(i).getLocOrLen(); //tableRel.getZapis(i).getLoc()
                    break;
                } else if(tableRel.getZapis(i).getRef() == 2){
                    //to do
                    //if more DS
                    val = bssBase + bssLen - tableRel.getZapis(i).getLocOrLen();
/*                    String inserted[]  =new String[nrels];
                    int loc = 0;

                    for (int j = 0; j < tableRel.getListZapis().size(); j++) {
                        boolean added = false;
                        if(tableRel.getListZapis().get(j).getName().equalsIgnoreCase(name) ||
                                tableRel.getListZapis().get(j).getRef() != 2 ||
                                tableRel.getListZapis().get(j).getType().contains("S")) continue;
                        for (int k = 0; k < inserted.length && inserted[k] != null; k++) {
                            if(tableRel.getListZapis().get(j).getName().equalsIgnoreCase(name) ||
                                    tableRel.getListZapis().get(j).getRef() != 2 ||
                                    tableRel.getListZapis().get(j).getType().contains("S")) continue;
                            if(tableRel.getListZapis().get(j).getName().equalsIgnoreCase(inserted[k])){
                                added = true;
                                break;
                            }
                        }
                        if(!added){
                            inserted[loc++] = tableRel.getListZapis().get(j).getName();
                            val -= tableRel.getZapis(i).getLocOrLen();
                        }
                    }
                    break;
 */
                } else if(tableRel.getZapis(i).getRef() == 1){
                    for (int j = 0; j < names.length; j++) {
                        if(name.equalsIgnoreCase(names[j])){
                            val = locs[j];
                            break;
                        }
                    }
                }
                return val;
            }
        }
        for (int i = 0; i < table.getSize(); i++) {
            if(table.getListsym().get(i).getName().equalsIgnoreCase(name)){
                return table.getListsym().get(i).getValue();
            }
        }
        return val;
    }

    // makes symbol like in HYPO machine 4B -> 1B
    private String makeSymbol(String symb) {
        for (int i = 0; i < listLocalSymbols.size(); i++) {
            if (symb.equalsIgnoreCase(listLocalSymbols.get(i))) {
                symb = "dSTART + " + (i + 1);
                break;
            }
        }
        return symb;
    }

    // make hex to int
    private int makeIntFromHex(String num){
        int inum = Integer.valueOf(num, 16);
        if(inum > 127) inum-=256;
        return inum;
    }
    
    public void work() {
        System.out.println("Emulating...");
        processLinesInFile();
        try {
            ReadWrite.header(izlaz, table);

            insertData();

            izlaz.newLine();
            izlaz.write(".code");
            izlaz.newLine();

            if (isINI) {
                ReadWrite.read(izlaz);
                izlaz.newLine();
            }
            if (isOTI) {
                ReadWrite.write(izlaz);
            }

            izlaz.newLine(); izlaz.newLine();
            izlaz.write("main:");
            izlaz.newLine();
            izlaz.write("         mov ECX, 0");
            izlaz.newLine();

            refactorCode();

            izlaz.write("end main");
            izlaz.newLine();

            src.close();
            izlaz.close();

        } catch (IOException ex) {
            Logger.getLogger(ProcessInputFile.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Emulation completed!");
    }
}
