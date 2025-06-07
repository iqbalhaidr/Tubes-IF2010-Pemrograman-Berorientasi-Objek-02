package com.labpro;

import java.time.LocalDate;

public class KurirRepository extends Repository<Kurir>{
    public KurirRepository() {
        super();
    }
    public Kurir create(String nama, JenisKelamin jenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        assert nama != null && nama.trim().length() >= 3 : "Nama kurir minimal 3 karakter";
        assert jenisKelamin != null : "Jenis Kelamin tidak boleh kosong";
        assert pathFoto != null && !pathFoto.trim().isEmpty() : "Path foto tidak boleh kosong";
        assert tanggalLahir != null : "Tanggal lahir tidak boleh kosong";

        int newID;
        if (listOfEntity.isEmpty()) {
            newID = 0;
        } else {
            newID = findById(listOfEntity.size() - 1).getID() + 1; // id terakhir + 1
        }
        Kurir newKurir = new Kurir(newID, nama, jenisKelamin, pathFoto, tanggalLahir);

        listOfEntity.add(newKurir);
        return newKurir;
    }
    public Kurir update(int ID, String nama, JenisKelamin jenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Kurir kurir = findById(ID);
        if (!nama.isEmpty()) {
            kurir.setName(nama);
        }
        if (jenisKelamin != null) {
            kurir.setJenisKelamin(jenisKelamin);
        }
        if (!pathFoto.isEmpty()) {
            kurir.setPathFoto(pathFoto);
        }
        if (tanggalLahir != null) {
            kurir.setTanggalLahir(tanggalLahir);
        }

        return kurir;
    }

}
