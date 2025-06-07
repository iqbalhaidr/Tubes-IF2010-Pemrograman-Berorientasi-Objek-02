package com.labpro;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RepoPengirimanController implements Listener {
    private ArrayList<Kurir> listKurir;
    private ArrayList<ArrayList<Parsel>> listParsel;
    private PengirimanRepository repo;
    private ArrayList<Pengiriman> listPengiriman;
    public RepoPengirimanController(PengirimanRepository repo) {
        this.repo = repo;

        listPengiriman = (ArrayList<Pengiriman>)repo.findAll();
        listParsel = new ArrayList<>();
        for (Pengiriman pengiriman : listPengiriman) {
            listParsel.add((ArrayList<Parsel>) pengiriman.getListOfParsel());
        }

        listKurir = new ArrayList<>();
        for (Pengiriman pengiriman : listPengiriman) {
            listKurir.add(pengiriman.getKurir());
        }
    }

    public ArrayList<Kurir> getListKurir() {
        return listKurir;
    }

    public ArrayList<ArrayList<Parsel>> getListIdParsel() {
        return listParsel;

    }

    @Override
    public void update(Object data, ObservableEventType eventType) {
        switch (eventType) {
            case CreateKurir:
                Kurir kurir = (Kurir) data;
                listKurir.add(kurir);
                break;

            case UpdateKurir:
                Kurir kurirUpdate = (Kurir) data;
                for (int i = 0; i < listParsel.size(); i++){
                    if (listKurir.get(i).getID().equals(kurirUpdate.getID())){
                        listKurir.set(i, kurirUpdate);
                        break;
                    }
                }
                break;
            case DeleteKurir:
                 Kurir kurirDelete = (Kurir) data;
                 listKurir.remove(kurirDelete);
                 break;
//            case CreateParsel:
//                Parsel parsel = (Parsel) data;
//                listParsel.add(parsel);
//                break;
//            case UpdateParsel:
//                Parsel parselUpdate = (Parsel) data;
//                for (int i = 0; i < listIdParsel.size(); i++){
//                    if (listIdParsel.get(i) == parselUpdate){
//                        listIdParsel.set(i, parselUpdate);
//                    }
//                }
//                break;
//            case DeleteParsel:
//                Parsel parselDelete = (Parsel) data;
//                listIdParsel.remove(parselDelete);
//                break;
        }
    }

    public ArrayList<Pengiriman> getListPengiriman(){
        return listPengiriman;
    }

    public void setSample(){

    }

    public void addPengiriman(Pengiriman pengiriman) {
    }




}
