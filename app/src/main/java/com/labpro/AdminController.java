package com.labpro;

// Import kelas UI Dashboard yang spesifik
import com.labpro.uiControl.ManajemenKurirDashboardController;
import com.labpro.uiControl.PengirimanView;
import com.labpro.uiControl.ManajemenParselDashboardController;


import com.labpro.uiControl.SettingsView;
import com.sun.javafx.scene.SceneEventDispatcher;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.control.Alert;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AdminController {

    @FXML
    private StackPane contentArea;

    private RepoKurirController repoKurirController;
    private RepoPengirimanController repoPengirimanController;
    private RepoParselController repoParselController;
    private final Map<String, Parent> loadedViews = new HashMap<>();
    private final Map<String, Object> loadedControllers = new HashMap<>();
    private Runnable logoutCallback; // buat balik ke login

    private Settings currentSettings;
    private SettingsManager settingsManager;

    public AdminController() {
        System.out.println("AdminController Constructor called.");
    }

    public void setRepoKurirController(RepoKurirController repoKurirController) {
        this.repoKurirController = repoKurirController;
        System.out.println("RepoKurirController injected.");
    }

    public void setRepoPengirimanController(RepoPengirimanController repoPengirimanController) {
        this.repoPengirimanController = repoPengirimanController;
        System.out.println("RepoPengirimanController injected.");
    }

    public void setRepoParselController(RepoParselController repoParselController) {
        this.repoParselController = repoParselController;
        System.out.println("RepoParselController injected.");
    }

    public void setSettings(Settings settings, SettingsManager settingsManager) {
        this.currentSettings = settings;
        this.settingsManager = settingsManager;
        System.out.println("Settings diterima di AdminDashboard: " + currentSettings);
    }

    public void setLogoutCallback(Runnable logoutCallback) {
        this.logoutCallback = logoutCallback;
    }

    @FXML
    public void initialize() {
        System.out.println("AdminController initialize() called.");
        if (repoKurirController == null) { // Sesuaikan ini untuk kurir
            System.err.println("ERROR: RepoKurirController is NULL in AdminController.initialize(). " +
                    "Pastikan telah diinjeksi dari kelas utama.");
            showAlert(Alert.AlertType.ERROR, "Initialization Error", "Service Not Available",
                    "Aplikasi tidak dapat memulai karena layanan kurir tidak tersedia.");
        } else {
            showManajemenKurirDashboard(); // Tampilkan modul Manajemen Kurir saat aplikasi dimulai
        }
    }

    private void switchContent(String fxmlPath, String viewKey, Class<?> targetControllerClass) {
        try {
            Parent view;
            Object moduleControllerInstance;

            // Cek apakah modul sudah ada di cache
            if (loadedViews.containsKey(viewKey)) {
                view = loadedViews.get(viewKey);
                moduleControllerInstance = loadedControllers.get(viewKey);
                System.out.println("Memuat " + viewKey + " dari cache.");
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));

                loader.setControllerFactory(controllerClass -> {
                    try {
                        if (controllerClass == ManajemenKurirDashboardController.class) {
                            ManajemenKurirDashboardController kurirController = new ManajemenKurirDashboardController();
                            if (this.repoKurirController == null) {
                                throw new IllegalStateException("RepoKurirController belum diinjeksi saat membuat ManajemenKurirDashboardController.");
                            }
                            kurirController.setController(this.repoKurirController);
                            return kurirController; // Selalu return instance di sini
                        }
                        else if (controllerClass == PengirimanView.class) {
                            PengirimanView pengirimanController = new PengirimanView(); // Buat instance langsung
                            if (this.repoPengirimanController == null) {
                                throw new IllegalStateException("RepoPengirimanController belum diinjeksi saat membuat PengirimanView.");
                            }
                            pengirimanController.setRepoPengirimanController(this.repoPengirimanController);
                            return pengirimanController;
                        }
                        else if (controllerClass == ManajemenParselDashboardController.class) { // Dikomentari: Belum jadi
                            ManajemenParselDashboardController parselController = new ManajemenParselDashboardController();
                            if (this.repoParselController == null) {
                                throw new IllegalStateException("RepoParselController belum diinjeksi saat membuat ManajemenParselDashboardController.");
                            }
                            parselController.setController(this.repoParselController);
                            return parselController;
                        }
                        else {
                            // Fallback jika tipe controller tidak dikenal atau tidak ada injeksi khusus.
                            // Ini akan membuat instance controller tanpa dependensi yang diinjeksi secara spesifik.
                            System.err.println("Peringatan: Tipe controller tidak dikenal atau tidak ada injeksi khusus untuk: " + controllerClass.getName() + ". Mencoba instansiasi default.");
                            return controllerClass.getDeclaredConstructor().newInstance(); // Selalu return instance di sini
                        }
                    } catch (Exception e) {
                        System.err.println("Gagal membuat instance controller untuk " + controllerClass.getName() + ": " + e.getMessage());
                        throw new RuntimeException("Gagal menginisialisasi controller.", e);
                    }
                });

                view = loader.load(); // loader.load() akan menggunakan factory untuk mendapatkan controller
                moduleControllerInstance = loader.getController(); // Dapatkan instance controller yang dibuat oleh factory
                if (moduleControllerInstance instanceof PengirimanView) {
                    ((PengirimanView) moduleControllerInstance).initializeData();
                }

                // Simpan ke cache
                loadedViews.put(viewKey, view);
                loadedControllers.put(viewKey, moduleControllerInstance);
                System.out.println("Memuat " + viewKey + " dan menambahkannya ke cache.");
            }

            contentArea.getChildren().setAll(view);

        } catch (IOException e) {
            System.err.println("Gagal memuat FXML: " + fxmlPath + ". Error: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error FXML", "Gagal Memuat Tampilan", "Tidak dapat memuat: " + viewKey);
        } catch (IllegalStateException e) {
            System.err.println("Kesalahan dependensi: " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Dependensi", "Layanan Tidak Tersedia", e.getMessage());
        } catch (Exception e) { // Tangkap Exception umum untuk kesalahan tak terduga
            System.err.println("Terjadi kesalahan saat loading " + viewKey + ": " + e.getMessage());
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Inisialisasi", "Gagal Inisialisasi Modul", "Detail: " + e.getMessage());
        }
    }

    @FXML
    private void showManajemenPengirimanDashboard() {
        if (this.repoPengirimanController == null) {
            showAlert(Alert.AlertType.ERROR, "Error Dependensi", "Service Pengiriman Hilang", "RepoPengirimanController belum diinisialisasi.");
            return;
        }
        switchContent("/fxml/PengirimanView.fxml", "pengiriman", PengirimanView.class);
    }

    @FXML
    private void showManajemenKurirDashboard() {
        if (this.repoKurirController == null) {
            showAlert(Alert.AlertType.ERROR, "Error Dependensi", "Service Kurir Hilang", "RepoKurirController belum diinisialisasi.");
            return;
        }
        switchContent("/fxml/ManajemenKurirDashboard.fxml", "kurir", ManajemenKurirDashboardController.class);
    }

    @FXML
    private void showManajemenParselDashboard() {
        if (this.repoParselController == null) {
            showAlert(Alert.AlertType.ERROR, "Error Dependensi", "Service Parsel Hilang", "RepoParselController belum diinisialisasi.");
            return;
        }
        switchContent("/fxml/ParselView.fxml", "parsel", ManajemenParselDashboardController.class);
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    @FXML
    private void handleLogout() {
        System.out.println("User logged out.");
        Stage stage = (Stage) contentArea.getScene().getWindow();
        if (stage != null) {
            stage.close();
        }
        // kembali ke layar login
        if (logoutCallback != null) {
            logoutCallback.run();
        } else {
            System.err.println("Callback belum diset");
        }
    }

    @FXML
    private void showSettingsView() {
        try {
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Pengaturan Aplikasi");
            settingsStage.initModality(Modality.APPLICATION_MODAL);
            settingsStage.initOwner(contentArea.getScene().getWindow());

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/SettingsView.fxml"));
            Parent settingsRoot = loader.load();

            SettingsView settingsController = loader.getController();
            settingsController.setDialogStage(settingsStage);
//            settingsController.setSettings(currentSettings, settingsManager);

            Scene scene = new Scene(settingsRoot);
            settingsStage.setScene(scene);
            settingsStage.showAndWait();
        } catch (IOException e) {
           e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Gagal Memuat Pengaturan");
            alert.setContentText("Tidak dapat membuka jendela pengaturan: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

