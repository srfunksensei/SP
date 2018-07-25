/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz2;

/**
 *
 * @author MB
 */
public class ST_entries {

    String name;          // name
    int value;         // value once defined
    boolean defined;           // true after defining occurrence encountered
    int seg;            // segment in which simbol is defined
    boolean globuse, globdef;

    public ST_entries(String name, int value, boolean defined, int seg) {
        this.name = name;
        this.value = value;
        this.defined = defined;
        this.seg = seg;
        globdef = globuse = false;
    }

    public boolean isDefined() {
        return defined;
    }

    public String getName() {
        return name;
    }

    public int getValue() {
        return value;
    }

    public void setDefined(boolean defined) {
        this.defined = defined;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getSeg() {
        return seg;
    }

    public void setSeg(int seg) {
        this.seg = seg;
    }

    public boolean isGlobdef() {
        return globdef;
    }

    public void setGlobdef(boolean globdef) {
        this.globdef = globdef;
    }

    public boolean isGlobuse() {
        return globuse;
    }

    public void setGlobuse(boolean globuse) {
        this.globuse = globuse;
    }
    
}
