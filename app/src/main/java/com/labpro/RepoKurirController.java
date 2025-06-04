package com.labpro;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.CheckBox;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDate;

public class RepoKurirController extends Observable{
    @FXML private TextField nameField;
    @FXML private ComboBox<String> jenisKelaminCombo;
    @FXML private TextField pathFotoField;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private CheckBox deleteStatusCheckbox;

    @FXML private Button createSubmitButton;
    @FXML private Button updateSubmitButton;


    private final KurirRepository kurirRepository;
    private ObservableList<Kurir> kurirData;

    public RepoKurirController(KurirRepository kurirRepository) {
        assert kurirRepository != null : "KurirRepository tidak boleh null";
        this.kurirRepository = kurirRepository;
    }


    @FXML
    private void handleCreate() { //belum ada validasi
        String nama = nameField.getText();
        String strJenisKelamin = jenisKelaminCombo.getValue();
        String pathFoto ="a";
        LocalDate tanggalLahir = tanggalLahirPicker.getValue();
        Kurir newKurir = null;
        try {
            newKurir = kurirRepository.create(nama, strJenisKelamin, pathFoto, tanggalLahir);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
        ObservableEventType createKurirEvent;
        createKurirEvent = ObservableEventType.valueOf("CreateKurir");
        notifyListeners(newKurir,createKurirEvent);
    }

    @FXML
    private void handleUpdate(int kurirID) {
        int id = kurirID;
        String nama  = nameField.getText();
        if (nama.isEmpty()) nama = null;

        String strJenisKelamin = jenisKelaminCombo.getValue();
        if (strJenisKelamin.isEmpty()) strJenisKelamin = null;

        String pathFoto = pathFotoField.getText();
        if (pathFoto.isEmpty()) pathFoto = null;

        LocalDate tanggalLahir  = tanggalLahirPicker.getValue();

        Kurir updatedKurir = null;
        try {
            updatedKurir = kurirRepository.update(id, nama, strJenisKelamin, pathFoto, tanggalLahir, false);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }

        ObservableEventType updateKurirEvent;
        updateKurirEvent = ObservableEventType.valueOf("UpdateKurir");
        notifyListeners(updatedKurir,updateKurirEvent);
    }

    @FXML
    private void handleDelete(int kurirID) {
        Kurir kurirToDelete = kurirRepository.findById(kurirID);
        if (!kurirToDelete.getDeleteStatus()) { //statusnya false
            kurirRepository.delete(kurirID);
            ObservableEventType deleteKurirEvent;
            deleteKurirEvent = ObservableEventType.valueOf("DeleteKurir");
            notifyListeners(kurirToDelete,deleteKurirEvent);
        } else {

        }
    }

    @FXML
    private void handlePilihFileFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih file foto kurir");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(pathFotoField.getScene().getWindow());

        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            pathFotoField.setText(filePath);
        }
//        validasi else nanti
    }

    private void clearFormFields() {
        nameField.clear();
        jenisKelaminCombo.getSelectionModel().clearSelection();
        tanggalLahirPicker.setValue(null);
        pathFotoField.clear();
    }

    private void loadAllKurir() {
        kurirData.clear();
        clearFormFields();
        kurirData.addAll(kurirRepository.findAll());
    }
}
