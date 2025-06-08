package com.labpro;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class PengirimanDomestikFactory extends PengirimanFactory {
    @Override
    public Pengiriman buatPengiriman(
            Integer idPengiriman, String noResi, String tujuan,
            StatusPengiriman statusPengiriman, LocalDate tanggalPembuatan,
            String namaPengirim, String noTelp, String namaPenerima,
            String noTelpPenerima, List<Parsel> listOfParsel,
            Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir
    ) {
        return new PengirimanDomestik(
                idPengiriman, noResi, tujuan, statusPengiriman,
                tanggalPembuatan, namaPengirim, noTelp,
                namaPenerima, noTelpPenerima, listOfParsel,
                kurirId, pdfFilePath, kodePajak, kurir
        );
    }
}
