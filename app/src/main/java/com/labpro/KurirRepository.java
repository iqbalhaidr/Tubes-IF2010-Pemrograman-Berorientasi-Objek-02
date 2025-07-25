package com.labpro;

import java.time.LocalDate;
import java.util.List;

public class KurirRepository extends Repository<Kurir>{

    public KurirRepository(List<Kurir> kurirs) {
        super(kurirs);
    }
    public Kurir create(String nama, JenisKelamin jenisKelamin, String pathFoto, LocalDate tanggalLahir) {
        System.out.println("Masuk13");//belum ada validasi
        assert nama != null && nama.trim().length() >= 3 : "Nama kurir minimal 3 karakter";
        assert jenisKelamin != null : "Jenis Kelamin tidak boleh kosong";
        assert pathFoto != null && !pathFoto.trim().isEmpty() : "Path foto tidak boleh kosong";
        assert tanggalLahir != null : "Tanggal lahir tidak boleh kosong";

        int newID = listOfEntity.stream()
                .mapToInt(Kurir::getID)
                .max()
                .orElse(0) + 1;
        System.out.println("Masuk14");//belum ada validasi
        Kurir newKurir = new Kurir(newID, nama, jenisKelamin, pathFoto, tanggalLahir);

        listOfEntity.add(newKurir);
        return newKurir;
    }
    public void update(int ID, String nama, JenisKelamin jenisKelamin, String pathFoto, LocalDate tanggalLahir) {
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
    }

    public void delete(int ID) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Kurir kurir = findById(ID);
        kurir.setDeleteStatus(true);
    }

}
