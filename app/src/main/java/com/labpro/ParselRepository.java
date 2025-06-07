package com.labpro;

public class ParselRepository extends Repository<Parsel> {
    public ParselRepository() {
        super();
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

    public Parsel update(int ID, Integer panjang, Integer lebar, Integer tinggi, Double berat, String jenisBarang) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Parsel parsel = findById(ID);
        
        int[] currentDimensi = parsel.getDimensi();
        int[] updatedDimensi = new int[3];

        updatedDimensi[0] = (panjang != currentDimensi[0]) ? panjang : currentDimensi[0];
        updatedDimensi[1] = (panjang != currentDimensi[1]) ? lebar : currentDimensi[1];
        updatedDimensi[2] = (panjang != currentDimensi[2]) ? tinggi : currentDimensi[2];

        if (berat != parsel.getBerat()) {
            parsel.setBerat(berat);
        }

        if (!jenisBarang.isEmpty()) {
            parsel.setJenisBarang(jenisBarang);
        }

    return parsel;
    }
}
