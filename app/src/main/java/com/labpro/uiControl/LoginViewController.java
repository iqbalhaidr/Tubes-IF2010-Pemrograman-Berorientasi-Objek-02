package com.labpro.uiControl;

import com.labpro.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Scene; // Import Scene
import javafx.scene.Parent; // Import Parent

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.io.IOException;
import java.util.Optional;

public class LoginViewController {
    @FXML private TextField usernameField;
    @FXML private Button loginButton;

    private User activeUser;
    private List<User> users;
    private RepoKurirController repoKurirController;
    private RepoPengirimanController repoPengirimanController; // Tambahkan ini
    private RepoParselController repoParselController;

    private Stage loginStage;

    public void initialize(URL location, ResourceBundle resources) {
        setupEventHandlers();
    }

    public void setRepoKurirController(RepoKurirController controller) {
        this.repoKurirController = controller;
        initializeUsers();
    }

    public void setRepoPengirimanController(RepoPengirimanController controller) {
        this.repoPengirimanController = controller;
    }

    public void setRepoParselController(RepoParselController controller) {
        this.repoParselController = controller;
    }

    public void setLoginStage(Stage stage) {
        this.loginStage = stage;
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
            navigateToMainApplication();
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
        if (loginStage != null) {
            loginStage.hide(); // Sembunyikan stage login
        }
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

    private void navigateToMainApplication() {
        try {
            Parent root = null;
            FXMLLoader loader = null;
            Stage mainStage = new Stage();

            // Callback untuk kembali ke login
            Runnable returnToLoginCallback = () -> {
                if (loginStage != null) {
                    loginStage.show();
                    usernameField.clear();
                    usernameField.requestFocus();
                }
            };

            if (activeUser.isAdmin()) {
                loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));
                AdminController adminController = new AdminController();

                adminController.setRepoKurirController(this.repoKurirController);
                adminController.setRepoPengirimanController(this.repoPengirimanController); // Aktifkan ini
                adminController.setRepoParselController(this.repoParselController);
                adminController.setLogoutCallback(returnToLoginCallback);

                loader.setController(adminController); // Set controller ke FXMLLoader
                root = loader.load(); // Muat FXML
                mainStage.setTitle("Admin Dashboard");
            } else {
                loader = new FXMLLoader(getClass().getResource("/fxml/kurirDashboard.fxml")); // Pastikan nama FXML benar
                kurirDashboardController kurirDashController = new kurirDashboardController();

                Optional<Kurir> kurirOpt = repoKurirController.getAllKurir().stream()
                        .filter(k -> k.getName().equalsIgnoreCase(activeUser.getUsername()))
                        .findFirst();

                if (kurirOpt.isPresent()) {
                    Kurir loggedInKurirObj = kurirOpt.get();

                    ProxyPengiriman proxyPengirimanService = new ProxyPengiriman(this.repoPengirimanController);

                    kurirDashController.setLoggedInKurir(loggedInKurirObj);
                    kurirDashController.setPengirimanService(proxyPengirimanService);
                    kurirDashController.setLogoutCallback(returnToLoginCallback);

                    loader.setController(kurirDashController); // Set controller ke FXMLLoader
                    root = loader.load(); // Muat FXML
                    mainStage.setTitle("Kurir Dashboard");

                } else {
                    showError("Login Gagal", "Data kurir tidak ditemukan untuk username ini.");
                    return; // Hentikan navigasi jika kurir tidak ditemukan
                }

            }
            if (root != null) {
                mainStage.setScene(new Scene(root));
                mainStage.show();
                closeDialog();
            } else {
                // Ini akan terjadi jika ada masalah dengan loading FXML
                showError("Navigasi Gagal", "Konten dashboard utama tidak dapat dimuat.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (Exception e) { // Catch Exception umum untuk kesalahan tak terduga
            System.err.println("Kesalahan saat navigasi: " + e.getMessage());
            e.printStackTrace();
            showError("Navigasi Gagal", "Terjadi kesalahan saat navigasi.\nDetail: " + e.getMessage());
        }

    }
}
