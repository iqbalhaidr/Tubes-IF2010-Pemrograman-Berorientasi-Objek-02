package com.labpro;

import java.util.ArrayList;

public class RepoPengirimanController implements Listener {
    public ArrayList<Kurir>  KurirAktif;
    public ArrayList<Parsel> ParselAktif;

    public RepoPengirimanController(ArrayList<Kurir> KurirAktif, ArrayList<Parsel> ParselAktif) {
        this.KurirAktif = KurirAktif;
        this.ParselAktif = ParselAktif;
    }

    public ArrayList<Kurir> getKurirAktif() {
        return KurirAktif;
    }

    public ArrayList<Parsel> getParselAktif() {
        return ParselAktif;
    }

    public void setKurirAktif(ArrayList<Kurir> kurirAktif) {
        KurirAktif = kurirAktif;
    }

    public void setParselAktif(ArrayList<Parsel> parselAktif) {
        ParselAktif = parselAktif;
    }

    @Override
    public void update(Object data, ObservableEventType eventType) {
        switch (eventType) {
            case CreateKurir:
                Kurir kurir = (Kurir) data;
                KurirAktif.add(kurir);
                break;

            case UpdateKurir:
                Kurir kurirUpdate = (Kurir) data;
                for (int i = 0; i < KurirAktif.size(); i++){
                    if (KurirAktif.get(i) == kurirUpdate){
                        KurirAktif.set(i, kurirUpdate);
                        break;
                    }
                }
                break;
            case DeleteKurir:
                 Kurir kurirDelete = (Kurir) data;
                 KurirAktif.remove(kurirDelete);
                 break;
            case CreateParsel:
                Parsel parsel = (Parsel) data;
                ParselAktif.add(parsel);
                break;
            case UpdateParsel:
                Parsel parselUpdate = (Parsel) data;
                for (int i = 0; i < ParselAktif.size(); i++){
                    if (ParselAktif.get(i) == parselUpdate){
                        ParselAktif.set(i, parselUpdate);
                    }
                }
                break;
            case DeleteParsel:
                Parsel parselDelete = (Parsel) data;
                ParselAktif.remove(parselDelete);
                break;
        }
    }
}
