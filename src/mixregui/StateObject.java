/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mixregui;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;

/**
 *
 * @author jixin
 */
public class StateObject implements Serializable {

    private File file;
    private String key;
    private int value_int;
    private double value_double;
    private String value_str;
    private boolean value_bool;
    private DefaultListModel<String> value_stringlist;
    private boolean[][] reg_boxes;
    private ArrayList<String> value_stringArraylist;

    public StateObject(String key, int value_int) {
        this.key = key;
        this.value_int = value_int;
    }

    public StateObject(String key, double value_double) {
        this.key = key;
        this.value_double = value_double;
    }

    public StateObject(String key, String value_str) {
        this.key = key;
        this.value_str = value_str;
    }

    public StateObject(String key, boolean value_bool) {
        this.key = key;
        this.value_bool = value_bool;
    }

    public StateObject(String key, ArrayList<String> value_stringArraylist, int value) {
        this.key = key;
        this.value_stringArraylist = value_stringArraylist;
    }

    public StateObject(String key, DefaultListModel<String> value_stringlist) {
        this.key = key;
        this.value_stringlist = value_stringlist;
    }

    public StateObject(String key, boolean[][] reg_boxes) {
        this.key = key;
        this.reg_boxes = reg_boxes;
    }
    
        public StateObject(String key, File value_file) {
        this.key = key;
        this.file = value_file;
    }

    public int getInt() {
        return value_int;
    }

    public double getDouble() {
        return value_double;
    }

    public String getString() {
        return value_str;
    }

    public boolean getBoolean() {
        return value_bool;
    }

    public DefaultListModel<String> getStringList() {
        return value_stringlist;
    }

    public boolean[][] getBox() {
        return reg_boxes;
    }

    public ArrayList<String> getStringArrayList() {
        return value_stringArraylist;
    }

    public String getKey() {
        return key;
    }

    public File getFile() {
        return file;
    }

}
