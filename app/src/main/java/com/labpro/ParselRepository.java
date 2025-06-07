package com.labpro;

import java.util.List;

public class ParselRepository extends Repository<Parsel> {
    public ParselRepository(List<Parsel> parsels) {
        super(parsels);
    }

    public Parsel create(int panjang, int lebar, int tinggi, double berat, String jenisBarang) {
        ParselStatus status = ParselStatus.valueOf("UNREGISTERED");

        assert panjang > 0 : "Panjang dimensi parsel harus positif";
        assert lebar > 0 : "Lebar dimensi parsel harus positif";
        assert tinggi > 0 : "Tinggi dimensi parsel harus positif";
        assert berat > 0 : "Berat parsel harus positif";
        int[]dimensi = {panjang, lebar, tinggi};

        assert jenisBarang != null && !jenisBarang.trim().isEmpty() : "Jenis Barang tidak boleh kosong";

        int newID;
        if (listOfEntity.isEmpty()) {
            newID = 0;
        } else {
            newID = findById(listOfEntity.size() - 1).getID() + 1; // id terakhir + 1
        }
        Parsel newParsel = new Parsel(newID, status, dimensi, berat, jenisBarang);

        listOfEntity.add(newParsel);

        return newParsel;
    }

    public Parsel update(int ID, String strStatus, Integer panjang, Integer lebar, Integer tinggi, Double berat, String jenisBarang) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Parsel parsel = findById(ID);

        if (!strStatus.isEmpty()) {
            ParselStatus status;
            assert strStatus.equals("LAKI_LAKI") || strStatus.equals("PEREMPUAN") :
                    "Jenis kelamin tidak valid. Harus 'LAKI_LAKI' atau 'PEREMPUAN'.";
            status = ParselStatus.valueOf(strStatus.toUpperCase());
            parsel.setStatus(status);
        }



    return parsel;
    }
}
