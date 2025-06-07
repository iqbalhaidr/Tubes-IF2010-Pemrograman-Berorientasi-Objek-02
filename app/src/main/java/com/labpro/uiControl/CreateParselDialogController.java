package com.labpro.uiControl;

import com.labpro.RepoParselController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateParselDialogController implements Initializable {

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add input validation
        setupInputValidation();
    }

    public void setRepoParselController(RepoParselController repoParselController) {
        this.repoParselController = repoParselController;
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
            // Validate input
            if (!validateInput()) {
                return;
            }

            // Parse values
            Integer panjang = Integer.parseInt(panjangField.getText().trim());
            Integer lebar = Integer.parseInt(lebarField.getText().trim());
            Integer tinggi = Integer.parseInt(tinggiField.getText().trim());
            Double berat = Double.parseDouble(beratField.getText().trim());
            String jenisBarang = jenisBarangField.getText().trim();

            // Create parsel
            repoParselController.createParsel(panjang, lebar, tinggi, berat, jenisBarang);

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
        }

        if (lebarField.getText().trim().isEmpty()) {
            errors.append("- Lebar harus diisi\n");
        }

        if (tinggiField.getText().trim().isEmpty()) {
            errors.append("- Tinggi harus diisi\n");
        }

        if (beratField.getText().trim().isEmpty()) {
            errors.append("- Berat harus diisi\n");
        }

        if (jenisBarangField.getText().trim().isEmpty()) {
            errors.append("- Jenis barang harus diisi\n");
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
}