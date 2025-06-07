package com.labpro;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;

public class PengirimanView {
    @FXML private TableView<Pengiriman> kurirTable;
//    @FXML private TableColumn<Pengiriman, String> namaKurir;
    @FXML private TableColumn<Pengiriman, String> namaKurir;
    @FXML private TableColumn<Pengiriman, String> namaParsel;
    @FXML private TableColumn<Pengiriman, String> alamat;

    public void initialize(RepoPengirimanController controller) {
        namaKurir.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKurirName()));
//        namaParsel.setCellValueFactory(cellData ->
//                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStringParsel()));
        alamat.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTujuan()));

        kurirTable.setItems(FXCollections.observableList(controller.getListPengiriman()));
    }
}
