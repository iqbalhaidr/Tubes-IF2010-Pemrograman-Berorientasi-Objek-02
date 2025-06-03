package com.labpro;

public enum StatusPengiriman {
    MENUNGGU_KONFIRMASI("Menunggu Konfirmasi"),
    DIPROSES("Diproses"),
    MENUNGGU_KURIR("Menunggu Kurir"),
    DIKIRIM("Dikirim"),
    TIBA_DI_TUJUAN("Tiba di Tujuan"),
    GAGAL("Gagal");

    private final String deskripsi;

    StatusPengiriman(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    @Override
    public String toString() {
        return deskripsi;
    }
}


