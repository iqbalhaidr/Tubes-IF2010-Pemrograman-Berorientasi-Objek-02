package com.labpro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Parsel {
    private int ID;
    private int[] dimensi = new int[3];
    private double berat;
    private String tipe;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int[] getDimensi() {
        return dimensi;
    }

    public void setDimensi(int[] newDimensi) {
        if (newDimensi.length == 3) {
            this.dimensi = newDimensi;
        } else {
            throw new IllegalArgumentException("Dimensi harus memiliki tepat 3 elemen.");
        }
    }

    public double getBerat() {
        return berat;
    }

    public void setBerat(double berat) {
        if (berat >= 0) {
            this.berat = berat;
        } else {
            throw new IllegalArgumentException("Berat tidak boleh negatif.");
        }
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }
}