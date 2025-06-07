package com.labpro;

import java.util.List;

public class RepoParselController extends Observable {
    private final ParselRepository parselRepository;

    public RepoParselController(ParselRepository parselRepository) {
        if (parselRepository == null) {
            throw new IllegalArgumentException("parselRepository tidak boleh null.");
        }
        this.parselRepository = parselRepository;
    }

    public void createParsel(Integer panjang, Integer lebar, Integer tinggi, Double berat, String jenisBarang) {
        if (panjang == null || panjang <= 0) {
            throw new IllegalArgumentException("Panjang parsel harus positif.");
        }
        if (lebar == null || lebar <= 0) {
            throw new IllegalArgumentException("Lebar parsel harus positif.");
        }
        if (tinggi == null || tinggi <= 0) {
            throw new IllegalArgumentException("Tinggi parsel harus positif.");
        }
        if (berat == null || berat <= 0) {
            throw new IllegalArgumentException("Berat parsel harus positif.");
        }

        if (jenisBarang == null || jenisBarang.trim().isEmpty()) {
            throw new IllegalArgumentException("Jenis barang tidak boleh kosong.");
        }
        if (jenisBarang.trim().length() < 3) {
            throw new IllegalArgumentException("Jenis barang minimal 3 karakter.");
        }

        int[] dimensi = {panjang, lebar, tinggi};
        Parsel newParsel = null;
        try {
            newParsel = parselRepository.create(dimensi, berat, jenisBarang);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        ObservableEventType createParselEvent;
        createParselEvent = ObservableEventType.valueOf("CreateParsel");
        notifyListeners(newParsel,createParselEvent);
    }

    public void updateParsel(int ID, Integer panjang, Integer lebar, Integer tinggi, Double berat, String jenisBarang) {
        if (ID < 0) {
            throw new IllegalArgumentException("ID Parsel tidak boleh negatif");
        }

        Parsel parselToUpdate = parselRepository.findById(ID);
        if (parselToUpdate == null) {
            throw new IllegalArgumentException("Parsel dengan ID " + ID + " tidak ditemukan.");
        }
        if (parselToUpdate.getStatus() == ParselStatus.REGISTERED) {
            throw new IllegalArgumentException("Parsel tidak bisa di-update");
        }
        int[] currentDimensi = parselToUpdate.getDimensi();
        int[] updatedDimensi = new int[3];

        if (panjang != null && panjang <= 0) {
            throw new IllegalArgumentException("Panjang parsel harus positif.");
        }
        if (lebar != null && lebar <= 0) {
            throw new IllegalArgumentException("Lebar parsel harus positif.");
        }
        if (tinggi != null && tinggi <= 0) {
            throw new IllegalArgumentException("Tinggi parsel harus positif.");
        }
        updatedDimensi[0] = (panjang != currentDimensi[0]) ? panjang : currentDimensi[0];
        updatedDimensi[1] = (panjang != currentDimensi[1]) ? lebar : currentDimensi[1];
        updatedDimensi[2] = (panjang != currentDimensi[2]) ? tinggi : currentDimensi[2];

        if (berat != null && berat <= 0) {
            throw new IllegalArgumentException("Berat parsel harus positif.");
        }

        if (jenisBarang != null && !jenisBarang.trim().isEmpty()) {
            if (jenisBarang.trim().length() < 2) {
                throw new IllegalArgumentException("Jenis barang minimal 2 karakter");
            }
        }
        try {
            parselRepository.update(ID, updatedDimensi, berat, jenisBarang);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        ObservableEventType updateParselEvent;
        updateParselEvent = ObservableEventType.valueOf("UpdateParsel");
        notifyListeners(parselToUpdate,updateParselEvent);

    }

    public void deleteParsel(int ID) {
        if (ID < 0) {
            throw new IllegalArgumentException("ID Parsel tidak boleh negatif");
        }
        Parsel parselToDelete = parselRepository.findById(ID);
        if (parselToDelete == null) {
            throw new IllegalArgumentException("parsel dengan ID " + ID + " tidak ditemukan.");
        }
        parselRepository.delete(ID);
        ObservableEventType deleteParselEvent;
        deleteParselEvent = ObservableEventType.valueOf("DeleteParsel");
        notifyListeners(parselToDelete,deleteParselEvent);
    }

    public List<Parsel> getAllParsel() {
        return parselRepository.findAll();
    }


}
