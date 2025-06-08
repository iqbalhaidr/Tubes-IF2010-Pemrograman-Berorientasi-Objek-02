package com.labpro;

import java.util.List;

public class ParselRepository extends Repository<Parsel> {
    public ParselRepository(List<Parsel> parsels) {
        super(parsels);
    }

    public List<Parsel> findAll() { return super.findAll();}
    public Parsel create(int[] dimensi, double berat, String jenisBarang) {
        ParselStatus status = ParselStatus.valueOf("UNREGISTERED");

        assert dimensi[0] > 0 : "Panjang dimensi parsel harus positif";
        assert dimensi[1] > 0 : "Lebar dimensi parsel harus positif";
        assert dimensi[2] > 0 : "Tinggi dimensi parsel harus positif";
        assert berat > 0 : "Berat parsel harus positif";

        assert jenisBarang != null && !jenisBarang.trim().isEmpty() : "Jenis Barang tidak boleh kosong";

        int newID = listOfEntity.stream()
                .mapToInt(Parsel::getID)
                .max()
                .orElse(0) + 1;
        Parsel newParsel = new Parsel(newID, status, dimensi, berat, jenisBarang);

        listOfEntity.add(newParsel);

        return newParsel;
    }

    public Parsel update(int ID, int[] dimensi, Double berat, String jenisBarang) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Parsel parsel = findById(ID);
        
        int[] currentDimensi = parsel.getDimensi();
        int[] updatedDimensi = dimensi;

        updatedDimensi[0] = (updatedDimensi[0] != currentDimensi[0]) ? updatedDimensi[0] : currentDimensi[0];
        updatedDimensi[1] = (updatedDimensi[1] != currentDimensi[1]) ? updatedDimensi[1] : currentDimensi[1];
        updatedDimensi[2] = (updatedDimensi[2] != currentDimensi[2]) ? updatedDimensi[2] : currentDimensi[2];

        parsel.setDimensi(updatedDimensi);
        if (berat != parsel.getBerat()) {
            parsel.setBerat(berat);
        }

        if (!jenisBarang.isEmpty()) {
            parsel.setJenisBarang(jenisBarang);
        }

    return parsel;
    }

    public void delete(int ID) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Parsel parsel = findById(ID);
        parsel.setDeleteStatus(true);
    }
}
