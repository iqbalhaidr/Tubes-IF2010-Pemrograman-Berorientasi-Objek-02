package com.labpro;

//import com.sun.tools.javac.util.List;

import java.util.*;


public abstract class Pengiriman implements HasID {
    protected Integer idPengiriman;
    protected String noResi;
    protected String tujuan;
    protected StatusPengiriman statusPengiriman;
    protected Date tanggalPembuatan;
    protected String namaPengiriman;
    protected String noTelp;
    protected String namaPenerima;
    protected String noTelpPenerima;
    protected List<Integer> listIdParsel;
    protected List<Parsel> listOfParsel;
    protected Integer kurirId;
    protected String kurirName;
    protected String pdfFilePath;
    protected String kodePajak;
    protected Kurir kurir;

    public Pengiriman(Integer idPengiriman, String noResi, String tujuan, StatusPengiriman statusPengiriman,
                      Date tanggalPembuatan, String namaPengirim, String noTelp,
                      String namaPenerima, String noTelpPenerima, List<Integer> listIdParsel,
                      Integer kurirId, String pdfFilePath, String kodePajak, Kurir kurir) {

        this.idPengiriman = idPengiriman;
        this.noResi = noResi;
        this.tujuan = tujuan;
        this.statusPengiriman = statusPengiriman;
        this.tanggalPembuatan = tanggalPembuatan;
        this.namaPengiriman = namaPengirim;
        this.noTelp = noTelp;
        this.namaPenerima = namaPenerima;
        this.noTelpPenerima = noTelpPenerima;
        this.listIdParsel = listIdParsel;
        this.kurirId = kurirId;
        this.kurir = kurir;
    }

    public Integer getID() {
        return getIdPengiriman();
    }

    public void generateParselList(ParselRepository repo) {
        List<Parsel> parselList = new ArrayList<>();
        for (int idParsel : this.listIdParsel) {
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
        return  this.tanggalPembuatan;
    }

    public void setTanngalPembuatan(Date tanggalPembuatan) {
        this.tanggalPembuatan = tanggalPembuatan;
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

    public List<Integer> getListIdParsel() {
        return listIdParsel;
    }

    public void setListIdParsel(List<Integer> listIdParsel) {
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

    public void addIdParsel(int idParsel) {
        if (this.listIdParsel == null) {
            this.listIdParsel = new ArrayList<>();
        }
        this.listIdParsel.add(idParsel);
    }

    public void removeIdParsel(int idParsel) {
        if (this.listIdParsel != null) {
            this.listIdParsel.remove(idParsel);
        }
    }

    public String getKurirName() {
        return this.kurirName;
    }

    public void setKurirName(String kurirName) {
        this.kurirName = kurirName;
    }

    public void addParsel(Parsel parsel) {
        if (this.listOfParsel == null) {
            this.listOfParsel = new ArrayList<>();
        }
        this.listOfParsel.add(parsel);
        this.addIdParsel(parsel.getID());
    }

    public void removeParsel(Parsel parsel) {
        if (this.listOfParsel != null) {
            this.listOfParsel.remove(parsel);
            this.removeIdParsel(parsel.getID());
        }
    }

    public Map<String, String> getDetails() {
        Map<String, String> details = new LinkedHashMap<>();
        details.put("idPengiriman", String.valueOf(idPengiriman));
        details.put("noResi", noResi);
        details.put("tujuan", tujuan);
        details.put("statusPengiriman", statusPengiriman != null ? statusPengiriman.toString() : null);
        details.put("tanggalPembuatan", this.tanggalPembuatan != null ? this.tanggalPembuatan.toString() : null);
        details.put("namaPengiriman", namaPengiriman);
        details.put("noTelp", noTelp);
        details.put("namaPenerima", namaPenerima);
        details.put("noTelpPenerima", noTelpPenerima);
        details.put("kurirId", String.valueOf(kurirId));
        return details;
    }

    public abstract String getType();

}
