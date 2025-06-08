package com.labpro.uiControl;

import com.labpro.Kurir;
import com.labpro.Pengiriman;
import com.labpro.RepoPengirimanController;
import com.labpro.StatusPengiriman;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class EditPengirimanDialogController {

    @FXML private ComboBox<Kurir> kurirComboBox;
    @FXML private ListView<String> parselListView;
    @FXML private TextField tujuanField;
    @FXML private ComboBox<StatusPengiriman> statusComboBox;
    @FXML private TextField namaPengirimField;
    @FXML private TextField noTelpField;
    @FXML private TextField namaPenerimaField;
    @FXML private TextField noTelpPenerimaField;

    private Pengiriman pengiriman;
    private RepoPengirimanController controller;

    public void setData(Pengiriman pengiriman, RepoPengirimanController controller) {
        this.pengiriman = pengiriman;

        tujuanField.setText(pengiriman.getTujuan());
        namaPengirimField.setText(pengiriman.getNamaPengiriman());
        noTelpField.setText(pengiriman.getNoTelp());
        namaPenerimaField.setText(pengiriman.getNamaPenerima());
        noTelpPenerimaField.setText(pengiriman.getNoTelpPenerima());
        this.controller = controller;


        ArrayList<Kurir> kurirList = controller.getKurirAktif(); // Assume this returns List<Kurir>
        kurirComboBox.setItems(FXCollections.observableArrayList(kurirList));
        kurirComboBox.setConverter(new StringConverter<Kurir>() {
            @Override
            public String toString(Kurir kurir) {
                return kurir != null ? kurir.getName() : "";
            }

            @Override
            public Kurir fromString(String string) {
                // Optional: implement this if you want to convert back from name to Kurir
                return null;
            }
        });

        if (pengiriman.getKurir() != null) {
            kurirComboBox.setValue(pengiriman.getKurir());
        }

        statusComboBox.setItems(FXCollections.observableArrayList(StatusPengiriman.values()));

        statusComboBox.setValue(pengiriman.getStatusPengiriman());


    }

    @FXML
    private void handleSimpan() {
        // Save changes back to the `pengiriman` object or through a service/controller
        pengiriman.setTujuan(tujuanField.getText());
        pengiriman.setNamaPengiriman(namaPengirimField.getText());
        pengiriman.setNoTelp(noTelpField.getText());
        pengiriman.setNamaPenerima(namaPenerimaField.getText());
        pengiriman.setNoTelpPenerima(noTelpPenerimaField.getText());
        Kurir selectedKurir = kurirComboBox.getValue();
        pengiriman.setKurir(selectedKurir);
        pengiriman.setStatusPengiriman(statusComboBox.getValue());



        // Close the window
        Stage stage = (Stage) tujuanField.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleBatal() {
        Stage stage = (Stage) tujuanField.getScene().getWindow();
        stage.close();
    }
}