package com.labpro;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Repository<T extends ID> {
    protected final List<T> listOfEntity;

    public Repository() {
        this.listOfEntity = new ArrayList<>();
    }

    public List<T> findAll() {
        return listOfEntity;
    }

    public T findById(int id) {
        return listOfEntity.stream()
                .filter(entity -> entity.getID() == id)
                .findFirst()
                .orElse(null);
    }

    public void delete(int id) {
        listOfEntity.removeIf(entity -> entity.getID() == id);
    }

    public void saveData(String filePath) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
//            for (T entity : listOfEntity) {
//                entity.write(writer);
//            }
//            System.out.println("Data berhasil disimpan ke: " + filePath);
//        } catch (IOException e) {
//            System.err.println("Gagal menyimpan data: " + e.getMessage());
//        }
    }
}
