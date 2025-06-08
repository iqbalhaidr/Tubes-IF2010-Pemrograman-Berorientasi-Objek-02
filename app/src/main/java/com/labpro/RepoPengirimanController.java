package com.labpro;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//TODO: IMPLEMENT PROXY
public class RepoPengirimanController implements Listener {
    private Map<Kurir, ArrayList<Pengiriman>> pengirimanMap;
    private PengirimanRepository repo;
    public ArrayList<Kurir>  KurirAktif;
    public ArrayList<Parsel> ParselAktif;

    //ATTENTION if we pass all of ArrayList<Kurir> (included not active) we should uncomment method generatePengirimanMap
    public RepoPengirimanController(ArrayList<Kurir> KurirAktif, ArrayList<Parsel> ParselAktif, PengirimanRepository repo) {
        this.KurirAktif = KurirAktif;
        this.ParselAktif = ParselAktif;
        this.repo = repo;
        this.pengirimanMap = new HashMap<>();
        //TODO generate MapKurir base on this.repo.findAll() it will return List Of raw pengiriman
        // need to map those kurir into a pengiriman Map
        generatePengirimanMap();
    }

    private void generatePengirimanMap() {
        List<Pengiriman> allRawPengiriman = repo.findAll();
        pengirimanMap.clear();

        for (Pengiriman pengiriman : allRawPengiriman) {
            Kurir kurir = pengiriman.getKurir();
            System.out.println("INI DIA KURIRR "+pengiriman.getKurir());
            if (kurir != null) {
                // Jika kurir belum ada di map, buat ArrayList baru untuknya
                pengirimanMap.computeIfAbsent(kurir, k -> new ArrayList<>()).add(pengiriman);
            }
        }
        System.out.println("Pengiriman Map berhasil digenerate.");
        // this.KurirAktif = new ArrayList<>(pengirimanMap.keySet());
        // this.ParselAktif = allRawPengiriman.stream()
        //                      .flatMap(p -> p.getParselList().stream())
        //                      .filter(parsel -> parsel.getStatus() == ParselStatus.REGISTERED) // Contoh filter
        //                      .collect(Collectors.toCollection(ArrayList::new));
    }

    //TODO: implement logic delete kurir such as add all parsel
    // that assign to deleted kurir to active parsel
    public void notifiedKurirDeleted(Kurir kurir) {
        for(Pengiriman pengiriman : pengirimanMap.get(kurir)){
            for(Parsel parsel : pengiriman.getListOfParsel()){
                parsel.setStatus(ParselStatus.REGISTERED);
                ParselAktif.add(parsel);
            }
        }
        KurirAktif.remove(kurir);
    }


    //TODO: implement logic delete pengiriman like add parsel in that pengiriman
    // to active parsel and set those parsel to registered
    public void handleDeletePengiriman(Pengiriman pengiriman){
        for(Parsel parsel : pengiriman.getListOfParsel()){
            parsel.setStatus(ParselStatus.REGISTERED);
            ParselAktif.add(parsel);
        }
    }

    public List<Pengiriman> getPengirimanByKurir(Kurir kurir){
        System.out.println("INI DIA DATANYA " + pengirimanMap.get(kurir));
        return pengirimanMap.get(kurir);
    }

    public void updateStatus(int idPengiriman, StatusPengiriman status){
        Pengiriman pengiriman = this.repo.findById(idPengiriman);
        pengiriman.setStatusPengiriman(status);
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
                notifiedKurirDeleted(kurirDelete);
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
