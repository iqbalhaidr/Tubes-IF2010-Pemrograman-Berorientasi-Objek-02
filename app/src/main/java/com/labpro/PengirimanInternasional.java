package com.labpro;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import java.util.Date;

public class PengirimanInternasional extends Pengiriman{

    public PengirimanInternasional(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                                   LocalDate tanggalPembuatan, String namaPengirim, String noTelp,
                                   String namaPenerima, String noTelpPenerima, List<Parsel> listOfParsel,
                                   Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir) {

        super( idPengiriman,  noResi,  tujuan,  statusPengiriman,
                tanggalPembuatan,  namaPengirim,  noTelp,
                namaPenerima,  noTelpPenerima, listOfParsel,
                kurirId, pdfFilePath, kodePajak, kurir);
        this.kodePajak = kodePajak;
        this.pdfFilePath = pdfFilePath;

    }

    public Map<String,String> getDetails(){
        Map<String, String> details = super.getDetails();
        details.put("kodePajak", this.kodePajak);
        return  details;
    }

    @Override
    public String getType(){
        return "Internasional";
    }
}
