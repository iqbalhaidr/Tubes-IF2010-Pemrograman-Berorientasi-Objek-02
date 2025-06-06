package com.labpro;


import java.util.Date;
import java.util.List;

public class PengirimanDomestik extends Pengiriman {

    public PengirimanDomestik(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                              Date tanggalPembuatan, String namaPengirim, String noTelp,
                              String namaPenerima, String noTelpPenerima, List<Integer> listIdParsel,
                              Integer kurirId , String pdfFilePath, String kodePajak, Kurir kurir) {

        super( idPengiriman,  noResi,  tujuan,  statusPengiriman,
                tanggalPembuatan,  namaPengirim,  noTelp,
                namaPenerima,  noTelpPenerima, listIdParsel,
                kurirId, pdfFilePath, kodePajak, kurir);
    }

    public String getType(){
        return "Domestik";
    }
}