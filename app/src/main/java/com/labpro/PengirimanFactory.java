package com.labpro;

import java.util.Date;
import java.util.List;


public abstract class PengirimanFactory {
    public Pengiriman createPengiriman(
            Integer idPengiriman, String noResi, String tujuan,
            StatusPengiriman statusPengiriman, Date tanggalPembuatan,
            String namaPengirim, String noTelp, String namaPenerima,
            String noTelpPenerima, List<Integer> listIdParsel,
            Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir
    ) {

        return buatPengiriman(
                idPengiriman, noResi, tujuan, statusPengiriman,
                tanggalPembuatan, namaPengirim, noTelp,
                namaPenerima, noTelpPenerima, listIdParsel,
                kurirId, pdfFilePath, kodePajak, kurir
        );
    }

    public abstract Pengiriman buatPengiriman(
            Integer idPengiriman, String noResi, String tujuan,
            StatusPengiriman statusPengiriman, Date tanggalPembuatan,
            String namaPengirim, String noTelp, String namaPenerima,
            String noTelpPenerima, List<Integer> listIdParsel,
            Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir
    );
}

