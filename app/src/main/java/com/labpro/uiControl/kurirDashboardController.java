package com.labpro.uiControl;

import com.labpro.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;

import java.util.List;

public class kurirDashboardController {
    @FXML private TableView<Pengiriman> kurirPengirimanTable;
    @FXML private TableColumn<Pengiriman, String> noResiColumn;
    @FXML private TableColumn<Pengiriman, String> penerimaColumn;
    @FXML private TableColumn<Pengiriman, String> alamatColumn;
    @FXML private TableColumn<Pengiriman, String> statusColumn;
    @FXML private TableColumn<Pengiriman, Void> aksiColumn;
    @FXML private Pagination pagination;
    @FXML private Text kurirName;

    //TODO: how the user log in how to know if its kurir by its name or what?
    private Kurir loggedInKurir;
    private RepoPengirimanController pengirimanService;
    private final int rowsPerPage = 5; // Jumlah baris per halaman
    private List<Pengiriman> masterDataPengiriman; // Daftar lengkap semua data pengiriman

    // Metode ini akan dipanggil otomatis oleh JavaFX setelah FXML dimuat
    @FXML
    public void initialize() {
        setupTable();
        kurirName.setText("Selamat Datang, Kurir!");
    }

    // Setter untuk RepoPengirimanController
    public void setPengirimanService(RepoPengirimanController service) {
        this.pengirimanService = service;
        loadTableData();
    }

    private void setupTable() {
        noResiColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoResi()));
        penerimaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaPenerima()));
        alamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTujuan()));

        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusPengiriman().toString()));
        statusColumn.setCellFactory(column -> new TableCell<Pengiriman, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty ? null : item);
                getStyleClass().removeAll("status-menunggu_kurir", "status-dikirim", "status-tiba_di_tujuan", "status-gagal");

                if (item != null && !empty) {
                    String cssClass = item.toLowerCase().replace(" ", "_");
                    getStyleClass().add("status-" + cssClass);
                }
            }
        });

        aksiColumn.setCellFactory(param -> new TableCell<Pengiriman, Void>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>(FXCollections.observableArrayList(
                    "Menunggu Kurir", "Dikirim", "Tiba Di Tujuan", "Gagal"
            ));
            private final Button saveButton = new Button("Simpan");
            private final HBox pane = new HBox(5, statusComboBox, saveButton);

            {
                statusComboBox.setMaxWidth(Double.MAX_VALUE);
                HBox.setHgrow(statusComboBox, Priority.ALWAYS);
                saveButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white;");

                saveButton.setOnAction(event -> {
                    Pengiriman pengirimanInTable = getTableView().getItems().get(getIndex());
                    String selectedOption = statusComboBox.getValue();
                    if (selectedOption != null) {
                        if (!selectedOption.equals(pengirimanInTable.getStatusPengiriman().getDeskripsi())) {
                            StatusPengiriman newStatusEnum = StatusPengiriman.fromDeskripsi(selectedOption);

                            for (Pengiriman p : masterDataPengiriman) {
                                if (p.getNoResi().equals(pengirimanInTable.getNoResi())) {
                                    p.setStatusPengiriman(newStatusEnum); // Update status di master list
                                    break;
                                }
                            }

                            pagination.setPageFactory(pageIndex -> {
                                return createPage(pageIndex);
                            });
                        }
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    Pengiriman pengiriman = getTableView().getItems().get(getIndex());
                    statusComboBox.setValue(pengiriman.getStatusPengiriman().getDeskripsi());
                    setGraphic(pane);
                }
            }
        });
    }

    private void loadTableData() {
//        //TODO: get pengiriman for specific kurir
//        if (pengirimanService != null) {
//            masterDataPengiriman = pengirimanService.getPengirimanByKurir(loggedInKurir); // Ambil semua data
//            int pageCount = (int) Math.ceil((double) masterDataPengiriman.size() / rowsPerPage);
//            pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
//            pagination.setPageFactory(this::createPage);
//        }
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, masterDataPengiriman.size());

        // Ambil sub-list data untuk halaman saat ini
        List<Pengiriman> pageData = masterDataPengiriman.subList(fromIndex, toIndex);
        kurirPengirimanTable.setItems(FXCollections.observableArrayList(pageData));

        return kurirPengirimanTable;
    }

    public void setLoggedInKurir(Kurir loggedInKurir) {
        this.loggedInKurir = loggedInKurir;
    }
}