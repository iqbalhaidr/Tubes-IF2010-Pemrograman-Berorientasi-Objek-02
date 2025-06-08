package com.labpro.uiControl;

import com.labpro.DataFormat; // Import enum baru
import com.labpro.Settings;
import com.labpro.SettingsManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SettingsView {
    @FXML private TextField dataStorageLocationField;
    @FXML private Button browseDataFolderButton;
    @FXML private TextField newPluginPathField;
    @FXML private Button browsePluginJarButton;
    @FXML private Button addPluginPathButton;
    @FXML private ListView<String> configuredPluginsListView;
    @FXML private Button removePluginPathButton;
    @FXML private Button loadPluginsButton;
    @FXML private Button unloadAllPluginsButton;
    @FXML private ListView<String> activePluginsListView;
    @FXML private Button saveSettingsButton;
    @FXML private Button cancelSettingsButton;
    @FXML private ComboBox<DataFormat> dataStoreFormatComboBox;


    private SettingsManager settingsManager;
    private Settings currentSettings;
    private ObservableList<String> configuredPluginsList = FXCollections.observableArrayList();

    private Stage dialogStage;

    public SettingsView() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

//    public void setSettings(Settings settings, SettingsManager manager) {
//        this.currentSettings = settings;
//        this.settingsManager = manager;
//    }

    @FXML
    public void initialize(){
        settingsManager = new SettingsManager();
        currentSettings = settingsManager.loadSettings();

        configuredPluginsList.addAll(currentSettings.getLoadedPlugins());
        configuredPluginsListView.setItems(configuredPluginsList);

        activePluginsListView.setItems(FXCollections.observableArrayList());

        loadSettings();

    }

    private void loadSettings() {
        if (dataStoreFormatComboBox != null) {
            dataStoreFormatComboBox.setItems(FXCollections.observableArrayList(DataFormat.values()));
        }

        if (currentSettings != null) {
            dataStorageLocationField.setText(currentSettings.getDataStoreLocation());
            if (currentSettings.getDataStoreFormat() != null) {
                dataStoreFormatComboBox.getSelectionModel().select(currentSettings.getDataStoreFormat());
            } else {
                dataStoreFormatComboBox.getSelectionModel().selectFirst();
            }
            configuredPluginsList.clear();
            configuredPluginsList.addAll(currentSettings.getLoadedPlugins());
        } else {
            System.err.println("currentSettings is null. Cannot load UI from settings.");
            dataStorageLocationField.setText("");
            if (dataStoreFormatComboBox != null) {
                dataStoreFormatComboBox.getSelectionModel().selectFirst();
            }
            configuredPluginsList.clear();
        }
    }
    @FXML
    private void handleBrowseDataFolder() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Pilih Folder Penyimpanan Data");
        File selectedDirectory = directoryChooser.showDialog(dialogStage);
        if (selectedDirectory != null) {
            dataStorageLocationField.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void handleBrowsePluginJar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih file Plugin JAR");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JAR Files", "*.jar"));

        File selectedFile = fileChooser.showOpenDialog(dialogStage);
        if (selectedFile != null) {
            newPluginPathField.setText(selectedFile.getAbsolutePath());
        }
    }

    @FXML
    private void handleAddPluginPath() {
        String newPath = newPluginPathField.getText().trim();
        if (!newPath.isEmpty() && !configuredPluginsList.contains(newPath)) {
            configuredPluginsList.add(newPath);
            newPluginPathField.clear();
        } else if (configuredPluginsList.contains(newPath)) {
            showAlert(Alert.AlertType.WARNING, "Duplikat", "Plugin sudah terdaftar.", "Path plugin ini sudah ada di dalam daftar.");
        }
    }

    @FXML
    private void handleRemovePluginPath() {
        String selectedPluginPath = activePluginsListView.getSelectionModel().getSelectedItem();
        if (selectedPluginPath != null) {
            configuredPluginsList.remove(selectedPluginPath);
        } else {
            showAlert(Alert.AlertType.WARNING, "Peringatan", "Tidak Ada Pilihan", "Pilih plugin yang ingin dihapus dari daftar.");
        }
    }

    @FXML
    private void handleLoadPlugins() {

    }

    @FXML
    private void handleUnloadAllPlugins() {
        configuredPluginsList.clear();
        activePluginsListView.getItems().clear();
        showAlert(Alert.AlertType.INFORMATION, "Plugin", "Copot Semua Plugin", " Semua plugin dicopot setelah pengaturan disimpan.");
    }

    @FXML
    private void handleSaveSettings() {
        currentSettings.setDataStoreFormat(dataStoreFormatComboBox.getValue());
        currentSettings.setDataStoreLocation(dataStorageLocationField.getText());
        currentSettings.setLoadedPlugins(configuredPluginsList);

        settingsManager.saveSettings(currentSettings);

        showAlert(Alert.AlertType.INFORMATION, "Pengaturan Tersimpan", "Pengaturan Aplikasi", "Pengaturan telah berhasil disimpan.");
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelSettings() {
        if (dialogStage != null) {
            dialogStage.close();
        }
    }

    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }






}
