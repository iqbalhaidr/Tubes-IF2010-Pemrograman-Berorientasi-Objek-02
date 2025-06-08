package com.labpro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Repository<T extends Data> {
    protected final List<T> listOfEntity;

    public Repository(List<T> entity) {
        this.listOfEntity =  entity;
    }

    public List<T> findAll() {
        return listOfEntity;
    }

    public void add(T entity){
        listOfEntity.add(entity);
    }
    public T findById(int id) {

        for (T entity : listOfEntity) {
            if (entity.getID() == id) {
                return entity;
            }
        }
        return null;

    }


//    public void saveData(String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (T entity : listOfEntity) {
//                entity.write(writer);
//            }
//            System.out.println("Data berhasil disimpan ke: " + filePath);
//        } catch (IOException e) {
//            System.err.println("Gagal menyimpan data: " + e.getMessage());
//        }
//    }
}
