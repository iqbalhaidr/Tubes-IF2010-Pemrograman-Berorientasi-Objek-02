package com.labpro.uiControl;

import com.labpro.*;

import javafx.beans.property.ReadOnlyObjectWrapper;
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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
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
    private List<Parsel> parselData;
    private final int rowsPerPage = 10;

    public void setRepoParselController(RepoParselController controller) {
        this.repoParselController = controller;
        loadParselData();
    }

    @FXML
    public void initialize() {
        parselData = new ArrayList<>();

        //setUp Table Columns
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

        //setup Action Columns
        actionsColumn.setCellFactory(param -> new TableCell<Parsel, Void>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actionBox = new HBox(5);

            {
                editButton.setStyle("-fx-background-color: #FFC107; -fx-border-color: transparent;");
                editButton.setPrefSize(50, 20);

                deleteButton.setStyle("-fx-background-color: #F44336; -fx-border-color: transparent;");
                deleteButton.setPrefSize(50, 20);

                actionBox.getChildren().addAll(editButton, deleteButton);
                actionBox.setAlignment(Pos.CENTER);

                editButton.setOnAction(event -> {
                    Parsel item = getTableView().getItems().get(getIndex());
                    handleEditAction(item);
                });

                deleteButton.setOnAction(event -> {
                    Parsel item = getTableView().getItems().get(getIndex());
                    handleDeleteAction(item);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
//
                if (empty) {
                    setGraphic(null);
                } else {
                    Parsel parsel = getTableView().getItems().get(getIndex());
                    if(parsel.getDeleteStatus()) {
                        setGraphic(null);
                    } else {
                        setGraphic(actionBox);
                    }
                }
            }
        });

        pagination.setPageFactory(this::createPage);
    }

    @FXML
    private void handleButtonClick() {
        openCreateParselDialog();
    }

    private void openCreateParselDialog() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateParselDialog.fxml"));
            Parent root = loader.load();

            CreateParselDialogController controller = loader.getController();
            controller.setRepoParselController(repoParselController);

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Tambah Parsel Baru");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(createParsel.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setResizable(false);

            dialogStage.setOnHidden(event -> {
                // Refresh data after dialog closes
                loadParselData();
            });

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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditParselDialog.fxml"));
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

            dialogStage.setOnHidden(event -> {
                // Refresh data after dialog closes
                loadParselData();
            });

            dialogStage.showAndWait();
//            loadParselData();
//            System.out.println(parsel.toString());
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
                System.out.println(repoParselController.getAllParsel());
                loadParselData();

            } catch (IllegalArgumentException e) {
                showError("Error", "Gagal menghapus parsel: " + e.getMessage());
            } catch (Exception e) {
                showError("Error", "Terjadi kesalahan saat menghapus parsel: " + e.getMessage());
            }
        }
    }

    public Node createPage(int pageIndex) {
        if (parselData == null || parselData.isEmpty()) {
            parselTable.setItems(FXCollections.observableArrayList());
            return new VBox(parselTable);
        }

        int pageCount = (int) Math.ceil((double) parselData.size() / rowsPerPage);

        if (pageIndex >= pageCount) {
            pageIndex = Math.max(0, pageCount - 1);
        }
        if (pageIndex < 0) {
            pageIndex = 0;
        }

        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, parselData.size());

        if (fromIndex >= parselData.size()) {
            parselTable.setItems(FXCollections.observableArrayList());
            return new VBox(parselTable);
        }

        List<Parsel> subList = parselData.subList(fromIndex, toIndex);
        ObservableList<Parsel> pageData = FXCollections.observableArrayList(subList);

        parselTable.getItems().clear();
        parselTable.setItems(pageData);
        parselTable.refresh(); // Force refresh tampilan

        return new VBox(parselTable);
    }

    private void loadParselData() {
        try {
            if (repoParselController == null) {
                showError("Error", "Repository controller tidak tersedia.");
                return;
            }

            parselData = repoParselController.getAllParsel();
            if (parselData == null) {
                parselData = new ArrayList<>();
            }

            int pageCount = parselData.isEmpty() ? 1 : (int) Math.ceil((double) parselData.size() / rowsPerPage);

            pagination.setPageFactory(null);
            pagination.setPageCount(pageCount);

            // Atur currentPageIndex supaya valid
            int currentPageIndex = pagination.getCurrentPageIndex();
            if (currentPageIndex >= pageCount) {
                currentPageIndex = Math.max(0, pageCount - 1);
                pagination.setCurrentPageIndex(currentPageIndex);
            }

            // Force refresh pagination untuk memperbarui tampilan
            pagination.setPageFactory(this::createPage);

        } catch (Exception e) {
            showError("Error", "Gagal memuat data parsel: " + e.getMessage());
        }
    }

    public void refreshData() {
        loadParselData();
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

        // Buat TextArea untuk menampilkan pesan panjang
        TextArea textArea = new TextArea(message);
        textArea.setEditable(false);
        textArea.setWrapText(true);

        // Ukuran lebih besar
        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);

        // Letakkan di dalam dialog
        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(textArea, 0, 0);

        // Masukkan ke dialog sebagai expandable content
        alert.getDialogPane().setExpandableContent(expContent);
        alert.getDialogPane().setExpanded(true); // Langsung tampilkan isi panjang

        alert.showAndWait();
    }

    private void showWarning(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

//    private void setupTableColumns() {
//        IDColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getID()).asObject());
//        statusColumn.setCellValueFactory(cellData -> {
//            ParselStatus status = cellData.getValue().getStatus();
//            return new javafx.beans.property.SimpleStringProperty(status.toString());
//        });
//        dimensiColumn.setCellValueFactory(cellData -> {
//            int[] dimensi = cellData.getValue().getDimensi();
//            String dimensiStr = dimensi[0] + " x " + dimensi[1] + " x " + dimensi[2] + " cm";
//            return new javafx.beans.property.SimpleStringProperty(dimensiStr);
//        });
//
//        beratColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getBerat()).asObject());
//        beratColumn.setCellFactory(column -> new TableCell<Parsel, Double>() {
//            @Override
//            protected void updateItem(Double item, boolean empty) {
//                super.updateItem(item, empty);
//                if (empty || item == null) {
//                    setText(null);
//                } else {
//                    setText(String.format("%.2f kg", item));
//                }
//            }
//        });
//
//        jenisColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getJenisBarang()));
//    }

//    private void setupActionsColumn() {
////        actionsColumn.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(null)); // << Tambahkan ini
//
//        actionsColumn.setCellFactory(param -> new TableCell<Parsel, Void>() {
//            private final Button editButton = new Button("Edit");
//            private final Button deleteButton = new Button("Delete");
//            private final HBox actionBox = new HBox(5, editButton, deleteButton);
//
//            {
//                editButton.setStyle("-fx-background-color: #FFC107; -fx-border-color: transparent;");
//                editButton.setPrefSize(50, 20);
//
//                deleteButton.setStyle("-fx-background-color: #F44336; -fx-border-color: transparent;");
//                deleteButton.setPrefSize(50, 20);
//
//                actionBox.getChildren().addAll(editButton, deleteButton);
//                actionBox.setAlignment(Pos.CENTER);
//
//                editButton.setOnAction(event -> {
//                    Parsel item = getTableView().getItems().get(getIndex());
//                    handleEditAction(item);
//                });
//
//                deleteButton.setOnAction(event -> {
//                    Parsel item = getTableView().getItems().get(getIndex());
//                    handleDeleteAction(item);
//                });
//            }
//
//            @Override
//            protected void updateItem(Void item, boolean empty) {
//                super.updateItem(item, empty);
////                setGraphic(empty ? null : actionBox);
//                if (empty) {
//                    setGraphic(null);
//                } else {
//                    Parsel parsel = getTableView().getItems().get(getIndex());
//                    if(parsel.getDeleteStatus()) {
//                        setGraphic(null);
//                    } else {
//                        setGraphic(actionBox);
//                    }
//                }
//            }
//        });
//    }




//    private void showError(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }


}
