package com.labpro;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class LogStatus {

    private LocalDateTime datetime;
    private StatusPengiriman status;
    private String pesan;

    public LogStatus(LocalDateTime datetimeStr, StatusPengiriman status) {
        this.datetime = datetimeStr;
        this.status = status;
        this.pesan = "Pesanan sedang "+ status.toString() + "Kurir";
    }

    // Getter dan Setter
    public LocalDateTime getDatetime() {
        return datetime;
    }

    public void setDatetime(LocalDateTime datetime) {
        this.datetime = datetime;
    }

    public StatusPengiriman getStatus() {
        return status;
    }

    public void setStatus(StatusPengiriman status) {
        this.status = status;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    @Override
    public String toString() {
        return "\nLogStatus{" +
                "datetime=" + datetime +
                ", status=" + status +
                ", pesan='" + pesan + '\'' +
                '}';
    }
}