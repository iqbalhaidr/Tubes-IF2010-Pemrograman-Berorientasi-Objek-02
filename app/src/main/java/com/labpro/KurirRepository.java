package com.labpro;

import java.time.LocalDate;

public class KurirRepository extends Repository<Kurir>{
    public KurirRepository() {
        super();
    }
    public void create(String nama, String strJenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        assert nama != null && nama.trim().length() >= 3 : "Nama kurir minimal 3 karakter";

        JenisKelamin jenisKelamin;
        assert strJenisKelamin != null && !strJenisKelamin.trim().isEmpty() : "Jenis Kelamin tidak boleh kosong";
        jenisKelamin = JenisKelamin.valueOf(strJenisKelamin.toUpperCase());

        assert pathFoto != null && !pathFoto.trim().isEmpty() : "Path foto tidak boleh kosong";
        assert tanggalLahir != null : "Tanggal lahir tidak boleh kosong";

        int newID;
        if (listOfEntity.isEmpty()) {
            newID = 0;
        } else {
            newID = findById(listOfEntity.size() - 1).getId() + 1; // id terakhir + 1
        }
        Kurir newKurir = new Kurir(newID, nama, jenisKelamin, pathFoto, tanggalLahir);

        listOfEntity.add(newKurir);
    }
    public void update(int ID, String nama, String strJenisKelamin, String pathFoto, LocalDate tanggalLahir, Boolean deleteStatus) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Kurir kurir = findById(ID);
        if (!nama.isEmpty()) {
            kurir.setName(nama);
        }
        if (!strJenisKelamin.isEmpty()) {
            JenisKelamin jenisKelamin;
            assert strJenisKelamin.equals("LAKI_LAKI") || strJenisKelamin.equals("PEREMPUAN") :
                    "Jenis kelamin tidak valid. Harus 'LAKI_LAKI' atau 'PEREMPUAN'.";
            jenisKelamin = JenisKelamin.valueOf(strJenisKelamin.toUpperCase());
            kurir.setJenisKelamin(jenisKelamin);
        }
        if (!pathFoto.isEmpty()) {
            kurir.setPathFoto(pathFoto);
        }
        if (tanggalLahir != null) {
            kurir.setTanggalLahir(tanggalLahir);
        }

        if (deleteStatus != null) {
            kurir.setDeleteStatus(deleteStatus);
        }
    }
}
