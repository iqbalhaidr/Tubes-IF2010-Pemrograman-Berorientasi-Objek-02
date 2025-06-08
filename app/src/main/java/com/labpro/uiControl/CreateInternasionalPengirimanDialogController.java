package com.labpro.uiControl;

import com.labpro.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;  // Assuming empty list for Parsel now
import java.util.List;

public class CreateInternasionalPengirimanDialogController {

    private RepoPengirimanController repoPengirimanController;

    public void setRepoPengirimanController(RepoPengirimanController controller) {
        this.repoPengirimanController = controller;
        ArrayList<Kurir> kurirList = repoPengirimanController.getKurirAktif(); // Assume this returns List<Kurir>
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

        statusComboBox.setItems(FXCollections.observableArrayList(StatusPengiriman.values()));

    }

    @FXML private TextField namaPengirimField;
    @FXML private TextField namaPenerimaField;
    @FXML private TextField noTelpField;
    @FXML private TextField noTelpPenerimaField;
    @FXML private TextField tujuanField;
    @FXML private ComboBox<StatusPengiriman> statusComboBox;
    @FXML private ComboBox<Kurir> kurirComboBox;

    @FXML private TextField noResiField;
    @FXML private DatePicker tanggalPembuatanPicker;
    @FXML private TextField pdfFilePathField;
    @FXML private TextField kodePajakField;




    @FXML
    private void handleCancel() {
        ((Stage) namaPengirimField.getScene().getWindow()).close();
    }

    @FXML
    private void handleSimpan() {
        try {
            // Collect all input values
            String noResi = noResiField.getText();
            String tujuan = tujuanField.getText();
            String statusStr = statusComboBox.getValue().toString();
            StatusPengiriman status = statusComboBox.getValue();  // convert to enum
            LocalDate tanggalPembuatan = tanggalPembuatanPicker.getValue();

            String namaPengirim = namaPengirimField.getText();
            String noTelp = noTelpField.getText();
            String namaPenerima = namaPenerimaField.getText();
            String noTelpPenerima = noTelpPenerimaField.getText();
            List<Parsel> listOfParsel = Collections.emptyList(); // replace with actual parsels list if needed

            Kurir selectedKurir = kurirComboBox.getValue();
            Integer kurirId = (selectedKurir != null) ? selectedKurir.getID() : null;

            String pdfFilePath = pdfFilePathField.getText();
            String kodePajak = kodePajakField.getText();

            // Create PengirimanDomestik object
            Pengiriman newPengiriman = repoPengirimanController.getRepo().create(
                    null,               // idPengiriman, null for new
                    noResi,
                    tujuan,
                    status,
                    tanggalPembuatan,
                    namaPengirim,
                    noTelp,
                    namaPenerima,
                    noTelpPenerima,
                    listOfParsel,
                    kurirId,
                    pdfFilePath,
                    kodePajak,
                    selectedKurir
            );

            // Save to repository
            repoPengirimanController.getRepo().add(newPengiriman);
            repoPengirimanController.addPengiriman(newPengiriman);

            // Close dialog
            ((Stage) namaPengirimField.getScene().getWindow()).close();

            // Optionally notify main controller or refresh data
//            repoPengirimanController.refreshData();

        } catch (Exception e) {
            e.printStackTrace();
            // Show error alert
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal menyimpan data pengiriman");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}