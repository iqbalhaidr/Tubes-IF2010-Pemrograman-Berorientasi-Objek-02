package com.labpro;

import java.util.Date;
import java.util.List;

public class PengirimanRepository extends Repository<Pengiriman>{
    private final PengirimanFactory factory;

    public PengirimanRepository(PengirimanFactory factory) {
        super();
        this.factory = factory;
    }

    // create Pengiriman Internasional
    public Pengiriman create(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                             Date tanggalPembuatan, String namaPengirim, String noTelp,
                             String namaPenerima, String noTelpPenerima, List<Integer> listIdParsel,
                             Integer kurirId, String pdfFilePath, String kodePajak) {

        assert noResi != null && !noResi.trim().isEmpty();
        assert tujuan != null && !tujuan.trim().isEmpty();
        assert namaPengirim != null && !namaPengirim.trim().isEmpty();
        assert noTelpPenerima != null && !noTelpPenerima.trim().isEmpty();
        assert namaPenerima != null && !namaPenerima.trim().isEmpty();
        assert kodePajak != null;
        assert pdfFilePath != null;

        if (tanggalPembuatan == null) {
            tanggalPembuatan = new Date();
        }

        if (idPengiriman == null) {
            if (listOfEntity.isEmpty()) {
                idPengiriman = 0;
            } else {
                idPengiriman = findById(listOfEntity.size() - 1).getIdPengiriman() + 1; // id terakhir + 1
            }
        }

        if (statusPengiriman == null) {
            statusPengiriman = StatusPengiriman.valueOf("MENUGGU_KONFIRMASI");
        }

        Pengiriman newPengiriman = factory.createPengiriman(
                idPengiriman, noResi, tujuan,
                statusPengiriman, tanggalPembuatan,
                namaPengirim, noTelp, namaPenerima,
                noTelpPenerima, listIdParsel,
                kurirId, pdfFilePath, kodePajak
        );

        return (PengirimanInternasional) newPengiriman;
    }

    // create Pengiriman Domestik
    public Pengiriman create(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                             Date tanggalPembuatan, String namaPengirim, String noTelp,
                             String namaPenerima, String noTelpPenerima, List<Integer> listIdParsel,
                             Integer kurirId) {

        assert noResi != null && !noResi.trim().isEmpty();
        assert tujuan != null && !tujuan.trim().isEmpty();
        assert namaPengirim != null && !namaPengirim.trim().isEmpty();
        assert noTelpPenerima != null && !noTelpPenerima.trim().isEmpty();
        assert namaPenerima != null && !namaPenerima.trim().isEmpty();

        if (tanggalPembuatan == null) {
            tanggalPembuatan = new Date();
        }

        if (idPengiriman == null) {
            if (listOfEntity.isEmpty()) {
                idPengiriman = 0;
            } else {
                idPengiriman = findById(listOfEntity.size() - 1).getIdPengiriman() + 1; // id terakhir + 1
            }
        }

        if (statusPengiriman == null) {
            statusPengiriman = StatusPengiriman.valueOf("MENUGGU_KONFIRMASI");
        }

        Pengiriman newPengiriman = factory.createPengiriman(
                idPengiriman, noResi, tujuan,
                statusPengiriman, tanggalPembuatan,
                namaPengirim, noTelp, namaPenerima,
                noTelpPenerima, listIdParsel,
                kurirId, null, null
        );

        return (PengirimanDomestik) newPengiriman;
    }

    // update PengirimanInternasional
    public Pengiriman update(Integer idPengiriman, String noResi, String tujuan,
                             StatusPengiriman statusPengiriman, Date tanggalPembuatan,
                             String namaPengirim, String noTelp, String namaPenerima,
                             String noTelpPenerima, List<Integer> listIdParsel,
                             Integer kurirId, String pdfFilePath, String kodePajak) {

        assert idPengiriman >= 0 : "ID tidak boleh bernilai negatif";

        Pengiriman pengiriman = findById(idPengiriman);

        if (noResi != null && !noResi.trim().isEmpty()) {
            pengiriman.setNoResi(noResi);
        }

        if (tujuan != null && !tujuan.trim().isEmpty()) {
            pengiriman.setTujuan(tujuan);
        }

        if (statusPengiriman.ordinal() < pengiriman.getStatusPengiriman().ordinal()) {
            System.out.println("Status pengiriman tidak boleh mundur!");
        } else {
            pengiriman.setStatusPengiriman(statusPengiriman);
        }

        if(tanggalPembuatan != null) {
            pengiriman.setTanngalPembuatan(tanggalPembuatan);
        }

        if (namaPengirim != null && !namaPengirim.trim().isEmpty()) {
            pengiriman.setNamaPengiriman(namaPengirim);
        }

        if (noTelp != null && !noTelp.isEmpty()) {
            pengiriman.setNoTelp(noTelp);
        }

        if (namaPenerima != null && !namaPenerima.trim().isEmpty()) {
            pengiriman.setNamaPenerima(namaPenerima);
        }

        if (noTelpPenerima != null && !noTelpPenerima.isEmpty()) {
            pengiriman.setNoTelpPenerima(noTelp);
        }

        if (!listIdParsel.isEmpty()) {
            pengiriman.setListIdParsel(listIdParsel);
        }

        if (kurirId != null) {
            pengiriman.setKurirId(kurirId);
        }

        return (PengirimanInternasional) pengiriman;
    }

    // update PengirimanDomestik
    public Pengiriman update(Integer idPengiriman, String noResi, String tujuan,
                             StatusPengiriman statusPengiriman, Date tanggalPembuatan,
                             String namaPengirim, String noTelp, String namaPenerima,
                             String noTelpPenerima, List<Integer> listIdParsel,
                             Integer kurirId) {

        assert idPengiriman >= 0 : "ID tidak boleh bernilai negatif";

        Pengiriman pengiriman = findById(idPengiriman);

        if (noResi != null && !noResi.trim().isEmpty()) {
            pengiriman.setNoResi(noResi);
        }

        if (tujuan != null && !tujuan.trim().isEmpty()) {
            pengiriman.setTujuan(tujuan);
        }

        if (statusPengiriman.ordinal() < pengiriman.getStatusPengiriman().ordinal()) {
            System.out.println("Status pengiriman tidak boleh mundur!");
        } else {
            pengiriman.setStatusPengiriman(statusPengiriman);
        }

        if(tanggalPembuatan != null) {
            pengiriman.setTanngalPembuatan(tanggalPembuatan);
        }

        if (namaPengirim != null && !namaPengirim.trim().isEmpty()) {
            pengiriman.setNamaPengiriman(namaPengirim);
        }

        if (noTelp != null && !noTelp.isEmpty()) {
            pengiriman.setNoTelp(noTelp);
        }

        if (namaPenerima != null && !namaPenerima.trim().isEmpty()) {
            pengiriman.setNamaPenerima(namaPenerima);
        }

        if (noTelpPenerima != null && !noTelpPenerima.isEmpty()) {
            pengiriman.setNoTelpPenerima(noTelp);
        }

        if (!listIdParsel.isEmpty()) {
            pengiriman.setListIdParsel(listIdParsel);
        }

        if (kurirId != null) {
            pengiriman.setKurirId(kurirId);
        }

        return (PengirimanDomestik) pengiriman;
    }
}
