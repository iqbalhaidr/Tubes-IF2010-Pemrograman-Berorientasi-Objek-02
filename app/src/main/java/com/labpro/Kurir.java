package com.labpro;

import java.time.LocalDate;


public class Kurir implements Data {
    private int id;
    private String name;
    private JenisKelamin jenisKelamin;
    private String pathFoto;
    private LocalDate tanggalLahir;
    private boolean deleteStatus; // soft delete

    Kurir(int ID, String name, JenisKelamin jenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        this.id = ID;
        this.name = name;
        this.jenisKelamin = jenisKelamin;
        this.pathFoto = pathFoto;
        this.tanggalLahir = tanggalLahir;
        this.deleteStatus = false;

    }

    public Integer getID() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JenisKelamin getJenisKelamin() {
        return jenisKelamin;
    }

    public void setJenisKelamin (JenisKelamin jenisKelamin) {
        this.jenisKelamin = jenisKelamin;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }

    public LocalDate getTanggalLahir() {
        return tanggalLahir;
    }

    public void setTanggalLahir(LocalDate tanggalLahir) {
        this.tanggalLahir = tanggalLahir;
    }

    public boolean getDeleteStatus() {
        return deleteStatus;
    }

    public void setDeleteStatus(boolean deleteStatus) {
        this.deleteStatus = deleteStatus;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Kurir) {
            Kurir other = (Kurir) obj;
            return this.id == other.id;
        }

        return false;
    }

    @Override
    public String toString() {
        return "Kurir " + name + " dengan ID " + id;
    }
}
