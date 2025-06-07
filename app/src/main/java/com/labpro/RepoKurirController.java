package com.labpro;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors; // Import untuk operasi stream


public class RepoKurirController extends Observable {
    private final KurirRepository kurirRepository;

    public RepoKurirController(KurirRepository kurirRepository) {
        if (kurirRepository == null) {
            throw new IllegalArgumentException("KurirRepository tidak boleh null.");
        }
        this.kurirRepository = kurirRepository;
    }


    public void createKurir(String nama, String strJenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        System.out.println("Masuk5");//belum ada validasi
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama kurir tidak boleh kosong");
        }

        if (nama.trim().length() < 2) {
            throw new IllegalArgumentException("Nama kurir minimal 2 karakter");
        }

        JenisKelamin jenisKelaminEnum;
        if (strJenisKelamin == null || strJenisKelamin.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis Kelamin tidak boleh kosong");
        }
        try {
            jenisKelaminEnum = JenisKelamin.valueOf(strJenisKelamin.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Jenis Kelamin tidak valid");
        }

        if (tanggalLahir == null) {
            throw new IllegalArgumentException("Tanggal lahir tidak boleh kosong");
        }
        if (tanggalLahir.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Tanggal lahir tidak valid.");
        }

        if (pathFoto == null || pathFoto.trim().isEmpty()) {
            throw new IllegalArgumentException("Foto tidak boleh kosong");
        }
        System.out.println("Masuk8");//belum ada validasi

        Kurir newKurir = null;
        try {
            newKurir = kurirRepository.create(nama, jenisKelaminEnum, pathFoto, tanggalLahir);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Masuk12");//belum ada validasi
        ObservableEventType createKurirEvent;
        createKurirEvent = ObservableEventType.valueOf("CreateKurir");
        notifyListeners(newKurir,createKurirEvent);
    }

    public void updateKurir(int ID, String nama, String strJenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        if (ID < 0) {
            throw new IllegalArgumentException("ID Kurir tidak boleh negatif");
        }
        System.out.println("Masuk6");
        Kurir kurirToUpdate = kurirRepository.findById(ID);
        if (kurirToUpdate == null) {
            throw new IllegalArgumentException("Kurir dengan ID " + ID + " tidak ditemukan.");
        }
        if (nama != null && !nama.trim().isEmpty()) {
            if (nama.trim().length() < 2) {
                throw new IllegalArgumentException("Nama kurir minimal 2 karakter");
            }
        }
        JenisKelamin jenisKelaminEnum = null;
        if (strJenisKelamin != null && !strJenisKelamin.isEmpty()) {
            try {
                jenisKelaminEnum = JenisKelamin.valueOf(strJenisKelamin.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Jenis Kelamin tidak valid");
            }
        }
        try {
            kurirRepository.update(ID, nama, jenisKelaminEnum, pathFoto, tanggalLahir);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
        System.out.println("Masuk7");
        System.out.println(kurirToUpdate.getName());
        ObservableEventType updateKurirEvent;
        updateKurirEvent = ObservableEventType.valueOf("UpdateKurir");
        notifyListeners(kurirToUpdate,updateKurirEvent);
    }

    public void deleteKurir(int ID) {
        if (ID < 0) {
            throw new IllegalArgumentException("ID Kurir tidak boleh negatif");
        }
        System.out.println("Masuk7");
        Kurir kurirToDelete = kurirRepository.findById(ID);
        if (kurirToDelete == null) {
            throw new IllegalArgumentException("Kurir dengan ID " + ID + " tidak ditemukan.");
        }
        kurirRepository.delete(ID);
        ObservableEventType deleteKurirEvent;
        deleteKurirEvent = ObservableEventType.valueOf("DeleteKurir");
        notifyListeners(kurirToDelete,deleteKurirEvent);
    }


    public List<Kurir> getAllKurir() {
        return kurirRepository.findAll().stream()
                .filter(entity -> !entity.getDeleteStatus())
                .collect(Collectors.toList());
    }
}
