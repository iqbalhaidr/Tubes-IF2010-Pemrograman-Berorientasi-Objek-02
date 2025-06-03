package com.labpro;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class Repository<T> {
    protected final List<T> listOfEntity;

    public Repository() {
        this.listOfEntity = new ArrayList<>();
    }

    public List<T> findAll() {
        return listOfEntity;
    }

    public T findById(int id) {
        for (T entity : listOfEntity) {
            if (entity.getID() == id) {
                return entity;
            }
        }
        return null;
    }

    public void delete(int id) {
        listOfEntity.removeIf(entity -> entity.getID() == id);
    }

    public void saveData(String filePath) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T entity : listOfEntity) {
                entity.write(writer);
            }
            System.out.println("Data berhasil disimpan ke: " + filePath);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan data: " + e.getMessage());
        }
    }
}
