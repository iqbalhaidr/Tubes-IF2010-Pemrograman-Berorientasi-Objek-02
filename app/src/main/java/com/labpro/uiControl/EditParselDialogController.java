package com.labpro.uiControl;

import com.labpro.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditParselDialogController implements Initializable {

    @FXML
    private Label idLabel;

    @FXML
    private Label statusLabel;

    @FXML
    private TextField panjangField;

    @FXML
    private TextField lebarField;

    @FXML
    private TextField tinggiField;

    @FXML
    private TextField beratField;

    @FXML
    private TextField jenisBarangField;

    @FXML
    private Button saveButton;

    @FXML
//    private Button cancelButton;

    private RepoParselController repoParselController;
    private Parsel originalParsel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add input validation
        setupInputValidation();
    }

    public void setRepoParselController(RepoParselController repoParselController) {
        this.repoParselController = repoParselController;
    }

    public void setParselToEdit(Parsel parsel) {
        this.originalParsel = parsel;
        populateFields();
    }

    private void populateFields() {
        if (originalParsel != null) {
            idLabel.setText("ID: " + originalParsel.getID());
            statusLabel.setText("Status: " + originalParsel.getStatus().toString());

            // Set style for status label
            if (originalParsel.getStatus() == ParselStatus.REGISTERED) {
                statusLabel.setStyle("-fx-text-fill: #f44336; -fx-font-weight: bold;");
            } else {
                statusLabel.setStyle("-fx-text-fill: #4CAF50; -fx-font-weight: bold;");
            }

            // Populate current values
            int[] dimensi = originalParsel.getDimensi();
            panjangField.setText(String.valueOf(dimensi[0]));
            lebarField.setText(String.valueOf(dimensi[1]));
            tinggiField.setText(String.valueOf(dimensi[2]));
            beratField.setText(String.valueOf(originalParsel.getBerat()));
            jenisBarangField.setText(originalParsel.getJenisBarang());
        }
    }

    private void setupInputValidation() {
        // Only allow numeric input for dimension fields
        panjangField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                panjangField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        lebarField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                lebarField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        tinggiField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                tinggiField.setText(newValue.replaceAll("\\D", ""));
            }
        });

        // Allow decimal for weight
        beratField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*(\\.\\d*)?")) {
                beratField.setText(oldValue);
            }
        });
    }

    @FXML
    private void handleSave() {
        try {
            // Check if parsel can be edited
            if (originalParsel.getStatus() == ParselStatus.REGISTERED) {
                showError("Edit Tidak Diizinkan", "Parsel dengan status REGISTERED tidak dapat diedit.");
                return;
            }

            // Validate input
            if (!validateInput()) {
                return;
            }

            // Parse values - allow null for unchanged values
            Integer panjang = null;
            Integer lebar = null;
            Integer tinggi = null;
            Double berat = null;
            String jenisBarang = null;

            // Check if values have changed
            int[] originalDimensi = originalParsel.getDimensi();

            if (!panjangField.getText().trim().equals(String.valueOf(originalDimensi[0]))) {
                panjang = Integer.parseInt(panjangField.getText().trim());
            }

            if (!lebarField.getText().trim().equals(String.valueOf(originalDimensi[1]))) {
                lebar = Integer.parseInt(lebarField.getText().trim());
            }

            if (!tinggiField.getText().trim().equals(String.valueOf(originalDimensi[2]))) {
                tinggi = Integer.parseInt(tinggiField.getText().trim());
            }

            if (!beratField.getText().trim().equals(String.valueOf(originalParsel.getBerat()))) {
                berat = Double.parseDouble(beratField.getText().trim());
            }

            if (!jenisBarangField.getText().trim().equals(originalParsel.getJenisBarang())) {
                jenisBarang = jenisBarangField.getText().trim();
            }

            // Check if any changes were made
            if (panjang == null && lebar == null && tinggi == null && berat == null && jenisBarang == null) {
                showInfo("Tidak Ada Perubahan", "Tidak ada data yang diubah.");
                return;
            }

            // Update parsel
            repoParselController.updateParsel(
                    originalParsel.getID(),
                    panjang,
                    lebar,
                    tinggi,
                    berat,
                    jenisBarang
            );

            // Close dialog
            closeDialog();

        } catch (NumberFormatException e) {
            showError("Input Error", "Pastikan semua nilai numerik diisi dengan benar.");
        } catch (IllegalArgumentException e) {
            showError("Validation Error", e.getMessage());
        } catch (Exception e) {
            showError("Error", "Terjadi kesalahan: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancel() {
        closeDialog();
    }

    private boolean validateInput() {
        StringBuilder errors = new StringBuilder();

        if (panjangField.getText().trim().isEmpty()) {
            errors.append("- Panjang harus diisi\n");
        } else {
            try {
                int panjang = Integer.parseInt(panjangField.getText().trim());
                if (panjang <= 0) {
                    errors.append("- Panjang harus lebih besar dari 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Panjang harus berupa angka\n");
            }
        }

        if (lebarField.getText().trim().isEmpty()) {
            errors.append("- Lebar harus diisi\n");
        } else {
            try {
                int lebar = Integer.parseInt(lebarField.getText().trim());
                if (lebar <= 0) {
                    errors.append("- Lebar harus lebih besar dari 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Lebar harus berupa angka\n");
            }
        }

        if (tinggiField.getText().trim().isEmpty()) {
            errors.append("- Tinggi harus diisi\n");
        } else {
            try {
                int tinggi = Integer.parseInt(tinggiField.getText().trim());
                if (tinggi <= 0) {
                    errors.append("- Tinggi harus lebih besar dari 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Tinggi harus berupa angka\n");
            }
        }

        if (beratField.getText().trim().isEmpty()) {
            errors.append("- Berat harus diisi\n");
        } else {
            try {
                double berat = Double.parseDouble(beratField.getText().trim());
                if (berat <= 0) {
                    errors.append("- Berat harus lebih besar dari 0\n");
                }
            } catch (NumberFormatException e) {
                errors.append("- Berat harus berupa angka\n");
            }
        }

        if (jenisBarangField.getText().trim().isEmpty()) {
            errors.append("- Jenis barang harus diisi\n");
        } else if (jenisBarangField.getText().trim().length() < 2) {
            errors.append("- Jenis barang minimal 2 karakter\n");
        }

        if (errors.length() > 0) {
            showError("Input Error", "Harap perbaiki kesalahan berikut:\n" + errors);
            return false;
        }

        return true;
    }

    private void closeDialog() {
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.close();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}