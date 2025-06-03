package com.labpro;

import com.sun.tools.javac.util.List;

import java.util.Date;


public class Pengiriman {
    protected Integer idPengiriman;
    protected String noResi;
    protected String tujuan;
    protected StatusPengiriman statusPengiriman;
    protected Date tanngalPembuatan;
    protected String namaPengiriman;
    protected String noTelp;
    protected String namaPenerima;
    protected String noTelpPenerima;
    protected List<String> listIdParsel;
    protected List<Parsel> listOfParsel;
    protected Integer kurirId;

    public Pengiriman(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                      Date tanggalPembuatan, String namaPengirim, String noTelp,
                      String namaPenerima, String noTelpPenerima, List<String> listIdParsel,
                      Integer kurirId) {

        this.idPengiriman = idPengiriman;
        this.noResi = noResi;
        this.tujuan = tujuan;
        this.statusPengiriman = statusPengiriman;
        this.tanngalPembuatan = tanggalPembuatan;
        this.namaPengiriman = namaPengirim;
        this.noTelp = noTelp;
        this.namaPenerima = namaPenerima;
        this.noTelpPenerima = noTelpPenerima;
        this.listIdParsel = listIdParsel;
        this.kurirId = kurirId;
    }

    public void generateParselList(ParselRepository repo) {
        List<Parsel> parselList = new ArrayList<>();
        for (String idParsel : this.listIdParsel) {
            Parsel parsel = repo.findById(idParsel);
            parselList.add(parsel);
        }
        this.listOfParsel = parselList;
    }

    public Integer getIdPengiriman() {
        return idPengiriman;
    }

    public void setIdPengiriman(Integer idPengiriman) {
        this.idPengiriman = idPengiriman;
    }

    public String getNoResi() {
        return noResi;
    }

    public void setNoResi(String noResi) {
        this.noResi = noResi;
    }

    public String getTujuan() {
        return tujuan;
    }

    public void setTujuan(String tujuan) {
        this.tujuan = tujuan;
    }

    public StatusPengiriman getStatusPengiriman() {
        return statusPengiriman;
    }

    public void setStatusPengiriman(StatusPengiriman statusPengiriman) {
        this.statusPengiriman = statusPengiriman;
    }

    public Date getTanngalPembuatan() {
        return tanngalPembuatan;
    }

    public void setTanngalPembuatan(Date tanngalPembuatan) {
        this.tanngalPembuatan = tanngalPembuatan;
    }

    public String getNamaPengiriman() {
        return namaPengiriman;
    }

    public void setNamaPengiriman(String namaPengiriman) {
        this.namaPengiriman = namaPengiriman;
    }

    public String getNoTelp() {
        return noTelp;
    }

    public void setNoTelp(String noTelp) {
        this.noTelp = noTelp;
    }

    public String getNamaPenerima() {
        return namaPenerima;
    }

    public void setNamaPenerima(String namaPenerima) {
        this.namaPenerima = namaPenerima;
    }

    public String getNoTelpPenerima() {
        return noTelpPenerima;
    }

    public void setNoTelpPenerima(String noTelpPenerima) {
        this.noTelpPenerima = noTelpPenerima;
    }

    public List<String> getListIdParsel() {
        return listIdParsel;
    }

    public void setListIdParsel(List<String> listIdParsel) {
        this.listIdParsel = listIdParsel;
    }

    public List<Parsel> getListOfParsel() {
        return listOfParsel;
    }

    public void setListOfParsel(List<Parsel> listOfParsel) {
        this.listOfParsel = listOfParsel;
    }

    public Integer getKurirId() {
        return kurirId;
    }

    public void setKurirId(Integer kurirId) {
        this.kurirId = kurirId;
    }

    public void addIdParsel(String idParsel) {
        if (this.listIdParsel == null) {
            this.listIdParsel = new ArrayList<Parsel>();
        }
        this.listIdParsel.add(idParsel);
    }

    public void removeIdParsel(String idParsel) {
        if (this.listIdParsel != null) {
            this.listIdParsel.remove(idParsel);
        }
    }


}
