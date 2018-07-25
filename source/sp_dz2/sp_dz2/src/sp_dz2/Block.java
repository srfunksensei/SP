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
public class Block {
    private List<Instruction> listInstr;
    private boolean needZ, needC, needP, setC, setPZ;
    private boolean needOptimisation;
    private int criticLine;
    private Block next1, next2;

    public boolean wasInBlock;

    public Block() {
        listInstr = new LinkedList<Instruction>();
        needOptimisation = false;
        criticLine = -1;
        next1= next2 = null;
    }

    public void addInstr(Instruction i){
        listInstr.add(i);
    }

    public void addOnPlace(Instruction i, int place){
        listInstr.add(place, i);
    }

    public Instruction getLastInsrtInBlock(){
        return listInstr.get(listInstr.size() - 1);
    }

    public Instruction getInstruction(int indx){
        return listInstr.get(indx);
    }
    
    public int getSize(){
        return listInstr.size();
    }

    public String getJumpMnem(int i){
        String s[] = listInstr.get(i).getReplacement().split("[\\s]+");
        if(listInstr.get(i).isHasLabel()){
            return s[1];
        } else {
            return s[0];
        }
    }

    public Block getNext1() {
        return next1;
    }

    public void setNext1(Block next1) {
        this.next1 = next1;
    }

    public Block getNext2() {
        return next2;
    }

    public void setNext2(Block next2) {
        this.next2 = next2;
    }

    public int getCriticLine() {
        return criticLine;
    }

    public boolean isNeedOptimisation() {
        return needOptimisation;
    }

    public void setCriticLine(int criticLine) {
        this.criticLine = criticLine;
    }

    public void setNeedOptimisation(boolean needOptimisation) {
        this.needOptimisation = needOptimisation;
    }

    public boolean isNeedC() {
        return needC;
    }

    public void setNeedC(boolean needC) {
        this.needC = needC;
    }

    public boolean isNeedP() {
        return needP;
    }

    public void setNeedP(boolean needP) {
        this.needP = needP;
    }

    public boolean isNeedZ() {
        return needZ;
    }

    public void setNeedZ(boolean needZ) {
        this.needZ = needZ;
    }

    public boolean isSetC() {
        return setC;
    }

    public void setSetC(boolean setC) {
        this.setC = setC;
    }

    public boolean isSetPZ() {
        return setPZ;
    }

    public void setSetPZ(boolean setPZ) {
        this.setPZ = setPZ;
    }

}
