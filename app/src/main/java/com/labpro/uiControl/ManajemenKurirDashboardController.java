package com.labpro.uiControl;

import com.labpro.Kurir;
import com.labpro.JenisKelamin;
import com.labpro.RepoKurirController;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.Pagination;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.scene.control.TableCell;
import javafx.scene.layout.HBox;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.geometry.Pos;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
public class ManajemenKurirDashboardController {
    @FXML private TextField namaField;
    @FXML private ComboBox<String> jenisKelaminCombo;
    @FXML private DatePicker tanggalLahirPicker;
    @FXML private TextField pathFotoField;
    @FXML private Label messageLabel;
    @FXML private TableView<Kurir> kurirTable;
    @FXML private VBox kurirFormContainer;
    @FXML private Button createSubmitButton;
    @FXML private Button updateSubmitButton;
    @FXML private Button cancelButton;
    @FXML private Pagination pagination;


    @FXML private TableColumn<Kurir, ImageView> photoColumn;
    @FXML private TableColumn<Kurir, Integer> idColumn;
    @FXML private TableColumn<Kurir, String> namaColumn;
    @FXML private TableColumn<Kurir, JenisKelamin> jenisKelaminColumn;
    @FXML private TableColumn<Kurir, LocalDate> tanggalLahirColumn;
    @FXML private TableColumn<Kurir, Void> actionsColumn;

    private RepoKurirController kurirService;
    private List<Kurir> kurirData;

    private final int rowsPerPage = 5;
    private int currentEditingId = -1;

    public ManajemenKurirDashboardController(RepoKurirController repoKurirController) {
        if (repoKurirController == null) {
            throw new IllegalArgumentException("RepoKurirController tidak boleh kosong");
        }
        this.kurirService = repoKurirController;
    }

    @FXML
    public void initialize() {
        //setup table
        idColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getID()));
        namaColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getName()));

        jenisKelaminCombo.setItems(FXCollections.observableArrayList(
                JenisKelamin.LAKI_LAKI.name(), JenisKelamin.PEREMPUAN.name()));
        jenisKelaminColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getJenisKelamin()));
        tanggalLahirColumn.setCellValueFactory(cellData -> new SimpleObjectProperty<>(cellData.getValue().getTanggalLahir()));

        //setup kolom foto
        photoColumn.setCellValueFactory(cellData -> {
            String imgPath = cellData.getValue().getPathFoto();
            ImageView imageView = new ImageView();
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);

            if (imgPath != null && !imgPath.isEmpty()) {
                File file = new File(imgPath);
                if (file.exists()) {
                    try {
                        Image image = new Image(file.toURI().toString());
                        imageView.setImage(image);
                    } catch (Exception e) {
                        System.err.println("Gagal memuat gambar dari: " + imgPath + " - " + e.getMessage());
                    }
                }
            }
            return new SimpleObjectProperty<>(imageView);
        });

        actionsColumn.setCellFactory(param -> new TableCell<Kurir, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox pane = new HBox(5, editButton, deleteButton);

            {
                // Styling
                editButton.setStyle("-fx-background-color: #22c55e; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");
                deleteButton.setStyle("-fx-background-color: #ef4444; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 5 10; -fx-border-radius: 5; -fx-background-radius: 5;");

                // buka form update
                editButton.setOnAction(event -> {
                    Kurir kurir = getTableView().getItems().get(getIndex());
                    openUpdateForm(kurir);
                });

                deleteButton.setOnAction(event -> {
                    Kurir kurir = getTableView().getItems().get(getIndex());
                    handleDelete(kurir);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        //loadKurirData();

        // hide form di awal
        if (kurirFormContainer != null) {
            kurirFormContainer.setVisible(false);
            kurirFormContainer.setManaged(false);
        }
    }

    @FXML
    private void openCreateForm() {
        clearFormFields();
        currentEditingId = -1;
        if (createSubmitButton != null) {
            createSubmitButton.setVisible(true);
            if (kurirFormContainer != null) {
                kurirFormContainer.setVisible(true);
                kurirFormContainer.setManaged(true);
            }
            if (createSubmitButton != null) {
                createSubmitButton.setVisible(true);
                createSubmitButton.setManaged(true);
            }

            if (updateSubmitButton != null) {
                updateSubmitButton.setVisible(false);
                updateSubmitButton.setManaged(false);
            }
        }
    }

    @FXML
    private void handleSubmitCreate() {
        try {
            String nama = namaField.getText();
            String strJenisKelamin = jenisKelaminCombo.getValue();
            String pathFoto = pathFotoField.getText();
            LocalDate tanggalLahir = tanggalLahirPicker.getValue();

            kurirService.createKurir(nama, strJenisKelamin, pathFoto,tanggalLahir);

            messageLabel.setText("Kurir baru berhasil ditambahkan!");
            loadKurirData();
            closeForm();

        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
            System.err.println("Error create: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Error:: " + e.getMessage());
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private void openUpdateForm(Kurir kurir) {
        if (kurir != null) {
            currentEditingId = kurir.getID();
            namaField.setText(kurir.getName());
            jenisKelaminCombo.setValue(kurir.getJenisKelamin().name());
            tanggalLahirPicker.setValue(kurir.getTanggalLahir());
            pathFotoField.setText(kurir.getPathFoto());

            if (kurirFormContainer != null) {
                kurirFormContainer.setVisible(true);
                kurirFormContainer.setManaged(true);
            }

            if (createSubmitButton != null) {
                createSubmitButton.setVisible(false);
                createSubmitButton.setManaged(false);
            }

            if (updateSubmitButton != null) {
                updateSubmitButton.setVisible(true);
                updateSubmitButton.setManaged(true);
            }
        }
    }

    @FXML
    private void handleSubmitUpdate() {
        try {
            if (currentEditingId == -1) {
                messageLabel.setText("Error: Tidak ada Kurir yang dipilih untuk diupdate.");
                return;
            }

            String nama = namaField.getText();
            if (nama.isEmpty()) nama = null;

            String strJenisKelamin = jenisKelaminCombo.getValue();
            if (strJenisKelamin == null || strJenisKelamin.isEmpty()) strJenisKelamin = null;

            String pathFoto = pathFotoField.getText();
            if (pathFoto.isEmpty()) pathFoto = null;

            LocalDate tanggalLahir = tanggalLahirPicker.getValue();

            kurirService.handleUpdate(currentEditingId, nama, strJenisKelamin, pathFoto, tanggalLahir);

            messageLabel.setText("Kurir dengan ID " + currentEditingId + " berhasil diperbarui!");
            loadKurirData();
            closeForm();
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
            System.err.println("Error update: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Unexpected error: " + e.getMessage());
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    private void handleDelete(Kurir kurir) {
        try {
            kurirService.handleDelete(kurir.getID());
            messageLabel.setText("Kurir '" + kurir.getName() + "' (ID: " + kurir.getID() + ") berhasil di-delete.");
            loadKurirData();
        } catch (IllegalArgumentException e) {
            messageLabel.setText(e.getMessage());
            System.err.println("Error delete: " + e.getMessage());
        } catch (Exception e) {
            messageLabel.setText("Unexpected error: " + e.getMessage());
            System.err.println("Unexpected error: " + e.getMessage());
        }
    }

    @FXML
    private void loadKurirData() {
        kurirData = kurirService.getAllKurir();
        int pageCount = (int) Math.ceil((double) kurirData.size() / rowsPerPage);
        pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
        pagination.setPageFactory(this::createPage);

        clearFormFields();

    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, kurirData.size());

        List<Kurir> pageData = kurirData.subList(fromIndex, toIndex);
        kurirTable.setItems(FXCollections.observableArrayList(pageData));

        return kurirTable;
    }

    @FXML
    private void handlePilihFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih File foto Kurir");
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif"),
                new FileChooser.ExtensionFilter("All Files", "*.*")
        );

        File selectedFile = fileChooser.showOpenDialog(pathFotoField.getScene().getWindow());
        if (selectedFile != null) {
            String filePath = selectedFile.getAbsolutePath();
            pathFotoField.setText(filePath);
            messageLabel.setText("Foto dipilih: " + filePath);
        } else {
            messageLabel.setText("Pilih foto dibatalkan.");
        }
    }





    private void clearFormFields() {
        namaField.clear();
        jenisKelaminCombo.getSelectionModel().clearSelection();
        tanggalLahirPicker.setValue(null);
        pathFotoField.clear();
        messageLabel.setText("");
    }

    @FXML
    private void handleCancelForm() {
        closeForm();
        messageLabel.setText("Operasi dibatalkan.");
    }

    private void closeForm() {
        if (kurirFormContainer != null) {
            kurirFormContainer.setVisible(false);
            kurirFormContainer.setManaged(false);
        }
        clearFormFields();
    }

}
