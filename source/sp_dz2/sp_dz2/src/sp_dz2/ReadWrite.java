/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz2;

import java.io.BufferedWriter;
import java.io.IOException;

/**
 *
 * @author MB
 */
public class ReadWrite {

    static public void header(BufferedWriter izlaz, TableSymbols table) throws IOException {
        izlaz.write(".386");izlaz.newLine();
        izlaz.write(".model flat, stdcall");
        izlaz.newLine(); izlaz.newLine();

        if (ProcessInputFile.isHasDEF()) {
            izlaz.write("PUBLIC ");
            int num = table.getNumGlobDef();
            for (int i = 0; i < table.getSize(); i++) {
                if (table.getListsym().get(i).isGlobdef() && !table.getListsym().get(i).isGlobuse()) {
                    izlaz.write(table.getListsym().get(i).getName());
                    num--;
                    if(num > 0) izlaz.write(",");
                }
            }
            izlaz.newLine();
        }
        if (ProcessInputFile.isHasUSE()) {
            izlaz.write("EXTRN ");
            int num = table.getNumGlobUse();
            for (int i = 0; i < table.getSize(); i++) {
                if (!table.getListsym().get(i).isGlobdef() && table.getListsym().get(i).isGlobuse()) {
                    izlaz.write(table.getListsym().get(i).getName());
                    num--;
                    if(num > 0) izlaz.write(",");
                }
            }
            izlaz.newLine();
        }
        if (ProcessInputFile.isHasEQU()) {
            for (int i = 0; i < table.getSize(); i++) {
                if (table.getListsym().get(i).isGlobdef() && table.getListsym().get(i).isGlobuse()) {
                    izlaz.write(table.getListsym().get(i).getName());
                    izlaz.write(" equ ");
                    boolean addedName = false;
                    for (int j = 0; j < TableRelocations.getListZapis().size(); j++) {
                        if(TableRelocations.getListZapis().get(i).getLoc() == 0 &&
                                TableRelocations.getListZapis().get(i).getSeg() == 0){
                            izlaz.write(TableRelocations.getListZapis().get(i).getName());
                            addedName = true;
                            break;
                        }
                    }
                    if(!addedName){
                        izlaz.write(Integer.toString(table.getListsym().get(i).getValue()));
                    } else {
                        if(table.getListsym().get(i).getValue() >= 0){
                            izlaz.write("+");
                            izlaz.write(Integer.toString(table.getListsym().get(i).getValue()));
                        } else {
                            //izlaz.write("-");
                            izlaz.write(Integer.toString(table.getListsym().get(i).getValue()));
                        }
                    }
                }
            }
            izlaz.newLine();
        }
        if (ProcessInputFile.isIsINI()) {//INI
            izlaz.write("STD_INPUT_HANDLE equ -10");
            izlaz.newLine();
        }
        if (ProcessInputFile.isIsOTI()) {//OTI
            izlaz.write("STD_OUTPUT_HANDLE equ -11");
            izlaz.newLine();
        }

        izlaz.write("PUBLIC main");izlaz.newLine();izlaz.newLine();
        izlaz.write("INCLUDELIB kernel32.lib");izlaz.newLine();izlaz.newLine();

        izlaz.write("ExitProcess proto:dword");izlaz.newLine();

        if (ProcessInputFile.isIsINI()) {//INI
            izlaz.write("ReadConsoleA proto :dword, :dword, :dword, :dword, :dword  ");
            izlaz.newLine();
        }
        if (ProcessInputFile.isIsOTI()) {//OTI
            izlaz.write("WriteConsoleA proto :dword, :dword, :dword, :dword, :dword");
            izlaz.newLine();
        }
        izlaz.newLine();

        if (ProcessInputFile.isIsINI() || ProcessInputFile.isIsOTI()) {//INI || OTI
            izlaz.write("GetStdHandle proto :dword");
            izlaz.newLine();izlaz.newLine();
        }
        //DATA sekcija
        izlaz.write(".data");izlaz.newLine();
        izlaz.write("dSTART\tDB\t0");izlaz.newLine();
        
    }

    static public void read(BufferedWriter izlaz) throws IOException {
        String[] s = {"readc	PROC",
            "         push EBP",
            "         mov EBP, ESP",
            "         push EAX",
            "         push EBX",
            "         push EDX",
            "         cmp stdin, 0",
            "         jne dalje_readc",
            "         invoke GetStdHandle, STD_INPUT_HANDLE",
            "         mov stdin, EAX",
            "dalje_readc:",
            "         sub ESP, 4",
            "         mov EAX, ESP",
            "         sub ESP, 4",
            "         mov EBX, ESP",
            "         invoke ReadConsoleA, stdin, EAX, 1, EBX, 0",
            "         add ESP,4",
            "         pop EAX",
            "         mov [EBP+8], AL",
            "         pop EDX",
            "         pop EBX",
            "         pop EAX",
            "         pop EBP",
            "         ret",
            "readc   ENDP",
            "readdec	PROC",
            "         push EBP",
            "         mov EBP, ESP",
            "         push EBX",
            "         push ECX",
            "         push EDX",
            "         mov ECX, 10",
            "         mov EBX, 0",
            "         mov EDX,0",
            "??read:	push ECX",
            "         sub ESP, 4",
            "         call readc",
            "         pop EAX",
            "         pop ECX",
            "         cmp AL,'-'",
            "         je jesteminus",
            "         jmp daljeminus",
            "         jesteminus: ",
            "         mov EDX,1",
            "         push ECX",
            "         sub ESP, 4",
            "         call readc",
            "         pop EAX",
            "         pop ECX",
            "daljeminus:",
            "         cmp AL, '0'",
            "         jb kraj_rddec",
            "         cmp AL, '9'",
            "         ja kraj_rddec",
            "         sub AL, '0'",
            "         push EAX",
            "         mov EAX, EBX",
            "         push EDX",
            "         mul ECX",
            "         pop EDX",
            "         mov EBX, EAX",
            "         pop EAX",
            "         and EAX, 0000000fh",
            "         add EBX, EAX",
            "         jmp ??read",
            "kraj_rddec:mov EAX, EBX",
            "         cmp EDX,1",
            "         je  negiraj",
            "         jmp daljenegiraj",
            "         negiraj: neg AL",
            "daljenegiraj:",
            "         pop EDX",
            "         pop ECX",
            "         pop EBX",
            "         pop EBP",
            "         ret",
            "readdec	ENDP"};

        for (int i = 0; i < s.length; i++) {
            izlaz.write(s[i]);
            izlaz.newLine();
            if (s[i].equals("readc   ENDP")) {
                izlaz.newLine();
            }
        }

    }

    static public void write(BufferedWriter izlaz) throws IOException {
        String[] s = {"printc	PROC par1:dword, par2:dword",
            "         ; push EBP",
            "         ; mov EBP, ESP",
            "         push EAX",
            "         push EBX",
            "         push ECX",
            "         cmp stdout, 0",
            "         jne dalje_printc",
            "         invoke GetStdHandle, STD_OUTPUT_HANDLE",
            "         mov stdout, EAX",
            "dalje_printc:",
            "         mov ECX, [par1]",
            "         mov EBX, EBP ",
            "         add EBX, 8",
            "         mov EAX, [par2]",
            "         invoke WriteConsoleA, stdout, EAX, ECX, EBX, 0",
            "         pop ECX	",
            "         pop EBX",
            "         pop EAX",
            "         ; pop EBP",
            "         ret",
            "printc	ENDP",
            "writedec PROC",
            "         push EAX",
            "         push EBX",
            "         push ECX",
            "          push EDX",
            "         mov AH, 0",
            "         mov CL, 10",
            "          mov EBX, 0",
            "         mov EDX, 0",
            "         mov DL, AL",
            "         and DL, 80h",
            "         jnz obrni",
            "         jmp ??write",
            "obrni: neg AL",
            "??write:div CL",
            "         xchg AH, AL",
            "         add AL,'0'",
            "         push EAX",
            "         inc EBX",
            "         xchg AH, AL   ",
            "         cmp AL, 0",
            "         je minusprovera",
            "         mov AH, 0",
            "         jmp ??write",
            "minusprovera:",
            "         cmp DL, 80h",
            "         je dodajminus",
            "         jmp kraj_wrdec",
            "dodajminus:",
            "         push '-'",
            "         push ESP",
            "         push 1",
            "         call printc",
            "         add ESP, 4",
            "kraj_wrdec:",
            "         cmp EBX, 0",
            "         je kraj_skroz",
            "         push ESP",
            "         push 1",
            "         call printc",
            "         add ESP,4",
            "         dec EBX",
            "         jmp kraj_wrdec",
            "kraj_skroz:",
            "         pop EDX",
            "         pop ECX",
            "         pop EBX",
            "         pop EAX",
            "         ret",
            "writedec ENDP",};

        for (int i = 0; i < s.length; i++) {
            izlaz.write(s[i]);
            izlaz.newLine();
            if (s[i].equals("printc	ENDP")) {
                izlaz.newLine();
            }
        }

    }
}
