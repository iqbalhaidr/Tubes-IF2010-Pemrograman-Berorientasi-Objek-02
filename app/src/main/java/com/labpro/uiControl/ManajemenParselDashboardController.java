package com.labpro.uiControl;

import com.labpro.*;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public class ManajemenParselDashboardController{
    @FXML private TableView<Parsel> parselTable;
    @FXML private TableColumn<Parsel, Integer> IDColumn;
    @FXML private TableColumn<Parsel, String> statusColumn;
    @FXML private TableColumn<Parsel, String> dimensiColumn;
    @FXML private TableColumn<Parsel, Double> beratColumn;
    @FXML private TableColumn<Parsel, String> jenisColumn;
    @FXML private TableColumn<Parsel, Void> actionsColumn;
    @FXML private Button createParsel;
    @FXML private Pagination pagination;

    private RepoParselController repoParselController;
    private ObservableList<Parsel> parselData;
    private final int rowsPerPage = 5;

    @FXML
    public void initialize() {
        setupTableColumns();
        setupActionsColumn();
    }

    public void setRepoParselController(RepoParselController controller) {
        this.repoParselController = controller;
        loadParselData();
    }

    private void setupTableColumns() {
        IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
        statusColumn.setCellValueFactory(cellData -> {
            ParselStatus status = cellData.getValue().getStatus();
            return new javafx.beans.property.SimpleStringProperty(status.toString());
        });
        dimensiColumn.setCellValueFactory(cellData -> {
            int[] dimensi = cellData.getValue().getDimensi();
            String dimensiStr = dimensi[0] + " x " + dimensi[1] + " x " + dimensi[2] + " cm";
            return new javafx.beans.property.SimpleStringProperty(dimensiStr);
        });

        beratColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBerat()).asObject());
        beratColumn.setCellFactory(column -> new TableCell<Parsel, Double>() {
            @Override
            protected void updateItem(Double item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(String.format("%.2f kg", item));
                }
            }
        });

        jenisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisBarang()));
    }

    private void setupActionsColumn() {
        actionsColumn.setCellFactory(param -> new TableCell<Parsel, Void>() {
            private final Button editButton = new Button();
            private final Button deleteButton = new Button();
            private final HBox actionBox = new HBox(5); // spacing 5px

            {
                editButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                editButton.setPrefSize(20, 20);

                ImageView editIcon = new ImageView();
                editIcon.setFitWidth(16);
                editIcon.setFitHeight(16);
                editIcon.setImage(new Image("../images/edit.png"));
                editButton.setGraphic(editIcon);


                deleteButton.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");
                deleteButton.setPrefSize(20, 20);

                ImageView deleteIcon = new ImageView();
                deleteIcon.setFitWidth(16);
                deleteIcon.setFitHeight(16);
                deleteIcon.setImage(new Image("../images/delete.png"));
                deleteButton.setGraphic(deleteIcon);

                actionBox.getChildren().addAll(editButton, deleteButton);
                actionBox.setAlignment(Pos.CENTER);

                // Event handlers
                editButton.setOnAction(event -> {
                    Parsel item = getTableView().getItems().get(getIndex());
                    handleEditAction(item);
                });

                deleteButton.setOnAction(event -> {
                    Parsel item = getTableView().getItems().get(getIndex());
                    handleDeleteAction(item);
                });
            }
        });
    }

    @FXML
    private void handleButtonClick() {
        openCreateParselDialog();
    }

    private void openCreateParselDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../resources/fxml/CreateParselDialog.fxml"));
            Parent root = loader.load();

            CreateParselDialogController controller = loader.getController();
            controller.setRepoParselController(repoParselController);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tambah Parsel Baru");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(createParsel.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

        } catch (IOException e) {
            showError("Error", "Tidak dapat membuka dialog tambah parsel: " + e.getMessage());
        }
    }

    private void handleEditAction(Parsel parsel) {
        if (parsel.getStatus() == ParselStatus.REGISTERED) {
            showWarning("Peringatan", "Parsel dengan status REGISTERED tidak dapat diedit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../../../resources/fxml/EditParselDialog.fxml"));
            Parent root = loader.load();

            EditParselDialogController controller = loader.getController();
            controller.setRepoParselController(repoParselController);
            controller.setParselToEdit(parsel);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Parsel - ID: " + parsel.getID());
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(parselTable.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.showAndWait();

        } catch (IOException e) {
            showError("Error", "Tidak dapat membuka dialog edit parsel: " + e.getMessage());
        }
    }

    private void handleDeleteAction(Parsel parsel) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Hapus");
        alert.setHeaderText("Hapus Parsel");
        alert.setContentText(
                "Apakah Anda yakin ingin menghapus parsel berikut?\n\n" +
                        "ID: " + parsel.getID() + "\n" +
                        "Jenis Barang: " + parsel.getJenisBarang() + "\n" +
                        "Status: " + parsel.getStatus()
        );

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                repoParselController.deleteParsel(parsel.getID());
                showSuccess("Berhasil", "Parsel berhasil dihapus.");
            } catch (IllegalArgumentException e) {
                showError("Error", "Gagal menghapus parsel: " + e.getMessage());
            }
        }
    }

    public Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, parselData.size());
        ObservableList<Parsel> pageData = FXCollections.observableArrayList(
                parselData.subList(fromIndex, toIndex)
        );
        parselTable.setItems(pageData);
        return new VBox(parselTable); // required return type (wrap in container)
    }

    private void loadParselData() {
        try {
            List<Parsel> parselList = repoParselController.getAllParsel();
            parselData.clear();
            parselData.addAll(parselList);

            int pageCount = (int) Math.ceil((double) parselData.size() / rowsPerPage);
            pagination.setPageCount(pageCount);
            pagination.setCurrentPageIndex(0);

            pagination.setPageFactory(this::createPage);
        } catch (Exception e) {
            showError("Error", "Gagal memuat data parsel: " + e.getMessage());
        }
    }

    // Utility methods for showing alerts
    private void showSuccess(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
