package com.labpro;

import java.util.Date;
import java.util.List;

public class PengirimanInternasionalFactory extends PengirimanFactory {
    @Override
    public Pengiriman buatPengiriman(
            Integer idPengiriman, String noResi, String tujuan,
            StatusPengiriman statusPengiriman, Date tanggalPembuatan,
            String namaPengirim, String noTelp, String namaPenerima,
            String noTelpPenerima, List<Integer> listIdParsel,
            Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir
    ) {
        return new PengirimanInternasional(
                idPengiriman, noResi, tujuan, statusPengiriman,
                tanggalPembuatan, namaPengirim, noTelp,
                namaPenerima, noTelpPenerima, listIdParsel,
                kurirId, pdfFilePath, kodePajak, kurir
        );
    }
}
