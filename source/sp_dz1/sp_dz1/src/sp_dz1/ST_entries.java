/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sp_dz1;

import java.util.LinkedList;
import sp_dz1.TableSymbols.ST_forwardrefs;

/**
 *
 * @author MB
 */
public class ST_entries {

    String name;          // name
    int value;         // value once defined
    boolean defined;           // true after defining occurrence encountered
    LinkedList<ST_forwardrefs> flink;  // to forward references
    int seg;            // segment in which simbol is defined
    boolean globuse, globdef;

    public ST_entries(String name, int value, boolean defined, LinkedList<ST_forwardrefs> flink, int seg) {
        this.name = name;
        this.value = value;
        this.defined = defined;
        this.flink = flink;
        this.seg = seg;
        globdef = globuse = false;
    }

    public boolean isDefined() {
        return defined;
    }

    public LinkedList<ST_forwardrefs> getFlink() {
        return flink;
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

    public void setFlink(LinkedList<ST_forwardrefs> flink) {
        this.flink = flink;
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
