package com.labpro;

import java.util.Date;
import java.util.List;

public class PengirimanRepository extends Repository<Pengiriman>{
    public PengirimanRepository() { super(); }
    public void create(String noResi, String tujuan, String namaPengirim, String noTelp, String namaPenerima, List<Integer> listIdParsel, Integer kurirId, ParselRepository repo) {
        assert noResi != null && !noResi.trim().isEmpty();
        assert tujuan != null && !tujuan.trim().isEmpty();
        assert namaPengirim != null && !namaPengirim.trim().isEmpty();
        assert noTelp != null && !noTelp.trim().isEmpty();
        assert namaPenerima != null && !namaPenerima.trim().isEmpty();
        assert !listIdParsel.isEmpty();
        Date tanggalPembuatan = new Date();
        Date tanggalPembaruan = new Date();

        int newID;
        if (listOfEntity.isEmpty()) {
            newID = 0;
        } else {
            newID = findById(listOfEntity.size() - 1).getIdPengiriman() + 1; // id terakhir + 1
        }

        StatusPengiriman statusPengiriman = StatusPengiriman.valueOf("MENUGGU_KONFIRMASI");
        Pengiriman newPengiriman = new Pengiriman(newID, noResi, tujuan, statusPengiriman,
                tanggalPembuatan, tanggalPembaruan, namaPengirim, noTelp,
                namaPenerima, noTelp, listIdParsel, kurirId);

        newPengiriman.generateParselList(repo);
    }

    public void update(int ID, String tujuan, StatusPengiriman statusPengiriman, String namaPengirim, String noTelp, String namaPenerima, List<Integer> listIdParsel, Integer kurirId, ParselRepository repo) {
        assert ID >= 0 : "ID tidak boleh bernilai negatif";

        Pengiriman pengiriman = findById(ID);

        if (namaPengirim != null && !namaPengirim.trim().isEmpty()) {
            pengiriman.setNamaPengirim(namaPengirim);
        }
        if (namaPenerima != null && !namaPenerima.trim().isEmpty()) {
            pengiriman.setNamaPenerima(namaPenerima);
        }
        if (tujuan != null && !tujuan.trim().isEmpty()) {
            pengiriman.setTujuan(tujuan);
        }
        if (statusPengiriman.ordinal() < pengiriman.getStatusPengiriman().ordinal()) {
            System.out.println("Status pengiriman tidak boleh mundur!");
        } else {
            pengiriman.setStatusPengiriman(statusPengiriman);
        }
        if (noTelp != null && !noTelp.isEmpty()) {
            pengiriman.setNoTelp(noTelp);
        }
        if (!listIdParsel.isEmpty()) {
            pengiriman.setListIdParsel(listIdParsel);
        }
        if (repo != null) {
            pengiriman.generateParselList(repo);
        }
        pengiriman.setTanggalPembaruan(new Date());
    }
}
