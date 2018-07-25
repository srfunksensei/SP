/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package sp_dz2;

import java.util.List;

/**
 *
 * @author MB
 */
public class Instruction {

    private int opCode;

    private String label, jumpOnLabel;
    private boolean hasLabel;
    private boolean needZ, needC, needP, setC, setPZ;
    private boolean needOptimisation;
    private String replacement;

    public Instruction() {
        jumpOnLabel = "";
    }

    public int getOpCode() {
        return opCode;
    }

    public void setOpCode(int opCode) {
        this.opCode = opCode;
    }

    public String getReplacement() {
        return replacement;
    }

    public void setReplacement(String replacement) {
        this.replacement = replacement;
    }

    public boolean isHasLabel() {
        return hasLabel;
    }

    public void setHasLabel(boolean hasLabel) {
        this.hasLabel = hasLabel;
    }

    public String getJumpOnLabel() {
        return jumpOnLabel;
    }

    public void setJumpOnLabel(String jumpOnLabel) {
        this.jumpOnLabel = jumpOnLabel;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isNeedC() {
        return needC;
    }

    public void setNeedC(boolean needC) {
        this.needC = needC;
    }

    public boolean isNeedOptimisation() {
        return needOptimisation;
    }

    public void setNeedOptimisation(boolean needOptimisation) {
        this.needOptimisation = needOptimisation;
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
