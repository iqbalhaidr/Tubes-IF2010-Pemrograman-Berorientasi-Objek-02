package com.labpro;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PengirimanRepository extends Repository<Pengiriman>{
    private final PengirimanInternasionalFactory factoryInternasional;
    private final PengirimanDomestikFactory factoryDomestik;

    public PengirimanRepository(List<Pengiriman> allPengiriman) {
        super(allPengiriman);
        this.factoryInternasional = new PengirimanInternasionalFactory();
        this.factoryDomestik = new PengirimanDomestikFactory();

    }

    // create Pengiriman Internasional
    public Pengiriman create(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                             LocalDate tanggalPembuatan, String namaPengirim, String noTelp,
                             String namaPenerima, String noTelpPenerima, List<Parsel> listOfParsel,
                             Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir) {

        assert noResi != null && !noResi.trim().isEmpty();
        assert tujuan != null && !tujuan.trim().isEmpty();
        assert namaPengirim != null && !namaPengirim.trim().isEmpty();
        assert noTelpPenerima != null && !noTelpPenerima.trim().isEmpty();
        assert namaPenerima != null && !namaPenerima.trim().isEmpty();
        assert kodePajak != null;
        assert pdfFilePath != null;

        if (tanggalPembuatan == null) {
            tanggalPembuatan =  LocalDate.now();
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

        Pengiriman newPengiriman = factoryInternasional.createPengiriman(
                idPengiriman, noResi, tujuan,
                statusPengiriman, tanggalPembuatan,
                namaPengirim, noTelp, namaPenerima,
                noTelpPenerima, listOfParsel,
                kurirId, pdfFilePath, kodePajak, kurir
        );

        return (PengirimanInternasional) newPengiriman;
    }

    // create Pengiriman Domestik
    public Pengiriman create(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                             LocalDate tanggalPembuatan, String namaPengirim, String noTelp,
                             String namaPenerima, String noTelpPenerima, List<Parsel> listOfParsel,
                             Integer kurirId, Kurir kurir) {

        assert noResi != null && !noResi.trim().isEmpty();
        assert tujuan != null && !tujuan.trim().isEmpty();
        assert namaPengirim != null && !namaPengirim.trim().isEmpty();
        assert noTelpPenerima != null && !noTelpPenerima.trim().isEmpty();
        assert namaPenerima != null && !namaPenerima.trim().isEmpty();

        if (tanggalPembuatan == null) {
            tanggalPembuatan = LocalDate.now();
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

        Pengiriman newPengiriman = factoryDomestik.createPengiriman(
                idPengiriman, noResi, tujuan,
                statusPengiriman, tanggalPembuatan,
                namaPengirim, noTelp, namaPenerima,
                noTelpPenerima, listOfParsel,
                kurirId, null, null, kurir
        );

        return (PengirimanDomestik) newPengiriman;
    }

    // update PengirimanInternasional
    public Pengiriman update(Integer idPengiriman, String noResi, String tujuan,
                             StatusPengiriman statusPengiriman, LocalDate tanggalPembuatan,
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
                             StatusPengiriman statusPengiriman, LocalDate tanggalPembuatan,
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

    public void delete(int id) {
        listOfEntity.removeIf(entity -> entity.getID() == id);
    }
}
