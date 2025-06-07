package com.labpro;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

public class PengirimanSearcher<T extends Pengiriman> {
    private ArrayList<T> listpengiriman;
    private SearchCriteria searchCriteria;

    public PengirimanSearcher(ArrayList<T> pengiriman) {
        this.listpengiriman = pengiriman;
    }

    public Collection<? extends T> search(SearchCriteria searchCriteria){
        Collection<T> hasilpengiriman = new ArrayList<T>();
        for (T pengiriman : listpengiriman) {
            Collection<String> allDetailString;
            Map<String, String> allDetail = pengiriman.getDetails();
            allDetailString = allDetail.values();
            for (String detail: allDetailString) {
                if (detail.contains(searchCriteria.getQuery())){
                    hasilpengiriman.add(pengiriman);
                    break;
                }
            }
        }

        return hasilpengiriman;
    }
}
