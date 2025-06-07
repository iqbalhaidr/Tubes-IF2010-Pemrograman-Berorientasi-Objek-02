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

    public static StatusPengiriman fromDeskripsi(String text) { // Ganti nama dari fromString menjadi fromDeskripsi agar lebih spesifik
        for (StatusPengiriman s : StatusPengiriman.values()) {
            if (s.deskripsi.equalsIgnoreCase(text)) { // Gunakan deskripsi untuk perbandingan
                return s;
            }
        }
        throw new IllegalArgumentException("Tidak ada StatusPengiriman dengan deskripsi '" + text + "' ditemukan.");
    }
}


