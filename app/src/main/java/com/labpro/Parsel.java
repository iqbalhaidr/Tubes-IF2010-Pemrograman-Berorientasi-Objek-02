package com.labpro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Parsel implements HasID, Data {
    private int ID;
    private ParselStatus status;
    private int[] dimensi = new int[3];
    private double berat;
    private String jenisBarang;
    private boolean deleteStatus;

    public Parsel(int ID, ParselStatus status, int[] dimensi, double berat, String jenisBarang) {
        this.ID = ID;
        this.status = status;
        this.dimensi = dimensi;
        this.berat = berat;
        this.jenisBarang = jenisBarang;
        this.deleteStatus = false;

    }
    public Integer getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }


    public ParselStatus getStatus() { return status;}

    public void setStatus(ParselStatus status) { this.status = status;}

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

    public String getJenisBarang() {
        return jenisBarang;
    }

    public void setJenisBarang(String jenisBarang) {

        this.jenisBarang = jenisBarang;
    }

    public boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof Parsel) {
            Parsel p = (Parsel) o;
            return ID == p.ID;
        }
        return false;
    }

    public String toString() {
        return "Parsel{" +
                "ID=" + ID +
                ", status=" + status +
                ", dimensi=" + Arrays.toString(dimensi) + // Gunakan Arrays.toString() untuk array
                ", berat=" + berat +
                ", jenisBarang='" + jenisBarang + '\'' +
                ", deleteStatus=" + deleteStatus +
                '}';
    }
}