package com.labpro.uiControl;

import com.labpro.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class LoginViewController {
    @FXML private TextField usernameField;
    @FXML private Button loginButton;

    private User activeUser;
    private List<User> users;
    private RepoKurirController repoKurirController;

    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
    }

    public void setRepoKurirController(RepoKurirController controller) {
        this.repoKurirController = controller;
        initializeUsers();
    }

    private void initializeUsers() {
        users = new ArrayList<>();
        try {
            if (repoKurirController == null) {
                showError("Error", "Repository controller tidak tersedia.");
                return;
            }

            // Tambahkan admin default
            users.add(new User("admin", "admin", true));

            // Ambil semua kurir dari database dan tambahkan sebagai user
            List<Kurir> kurirList = repoKurirController.getAllKurir();
            if (kurirList != null) {
                for (Kurir kurir : kurirList) {
                    // Skip kurir yang sudah di-delete
                    if (!kurir.getDeleteStatus()) {
                        // Gunakan nama kurir sebagai username, password dummy
                        users.add(new User(kurir.getName(), "dummy", false));
                    }
                }
            }
            System.out.println("Berhasil memuat " + users.size() + " user (termasuk admin)");

        } catch (Exception e) {
            System.err.println("Error saat inisialisasi users: " + e.getMessage());
            showError("Error", "Gagal memuat data user: " + e.getMessage());

            // Fallback: minimal ada admin
            users.clear();
            users.add(new User("admin", "admin", true));
        }
    }

    private void setupEventHandlers() {
        usernameField.setOnKeyPressed(event -> {
            if (event.getCode().toString().equals("ENTER")) {
                handleLogin(null);
            }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = usernameField.getText();

        if (username == null || username.trim().isEmpty()) {
            showError("Input Error", "Username tidak boleh kosong!");
            usernameField.requestFocus();
            return;
        }

        username = username.trim();

        User foundUser = findUserByUsername(username);

        if (foundUser != null) {
            activeUser = foundUser;

            // Show success message
            showSuccess("Login Berhasil",
                    "Selamat datang, " + username + "!" +
                            (foundUser.isAdmin() ? " (Administrator)" : " (Kurir)"));

            // TODO: Navigate to main application
//            navigateToMainApp();
            closeDialog();

        } else {
            showError("Login Gagal",
                    "Username '" + username + "' tidak ditemukan!\n\n" +
                            "Pastikan username sesuai dengan nama kurir yang terdaftar atau gunakan 'admin'.");
            usernameField.clear();
            usernameField.requestFocus();
        }
    }

    private void closeDialog() {
        Stage stage = (Stage) loginButton.getScene().getWindow();
        stage.close();
    }

    private User findUserByUsername(String username) {
        return users.stream()
                .filter(user -> user.getUsername().equalsIgnoreCase(username))
                .findFirst()
                .orElse(null);
    }

    public void refreshUsers() {
        initializeUsers();
    }

    public User getActiveUser() {
        return activeUser;
    }

    public List<User> getAllUsers() {
        return new ArrayList<>(users);
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
