package com.labpro;

public class ParselRepository extends Repository<Parsel>{
    public ParselRepository() {
        super();
    }

    public void create(String strStatus, int panjang, int lebar, int tinggi, double berat, String jenisBarang) {
        ParselStatus status;
        assert strStatus != null && !strStatus.trim().isEmpty() : "Status tidak boleh kosong";
        status = ParselStatus.valueOf(strStatus.toUpperCase());

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


    }

    public void update(int ID, String strStatus, Integer panjang, Integer lebar, Integer tinggi, Double berat, String jenisBarang) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Parsel parsel = findById(ID);

        if (!strStatus.isEmpty()) {
            ParselStatus status;
            assert strStatus.equals("LAKI_LAKI") || strStatus.equals("PEREMPUAN") :
                    "Jenis kelamin tidak valid. Harus 'LAKI_LAKI' atau 'PEREMPUAN'.";
            status = ParselStatus.valueOf(strStatus.toUpperCase());
            parsel.setStatus(status);
        }

        if (panjang != null || lebar != null || tinggi != null) {
            int currentPanjang = (panjang != null) ? panjang : parsel.getDimensi()[0];
            int currentLebar = (lebar != null) ? lebar : parsel.getDimensi()[1];
            int currentTinggi = (tinggi != null) ? tinggi : parsel.getDimensi()[2];

            assert currentPanjang > 0 : "Panjang dimensi parsel harus positif.";
            assert currentLebar > 0 : "Lebar dimensi parsel harus positif.";
            assert currentTinggi > 0 : "Tinggi dimensi parsel harus positif.";

            parsel.setDimensi(new int[]{currentPanjang, currentLebar, currentTinggi});
        }

        if (berat != null) {
                assert berat > 0: "Berat harus bernilai positif";
                parsel.setBerat(berat);
        }
        if (jenisBarang != null && !jenisBarang.trim().isEmpty()) {
            assert jenisBarang.trim().length() > 1 : "Jenis barang minimal 2 karakter";
            parsel.setJenisBarang(jenisBarang);
        }




    }
}
