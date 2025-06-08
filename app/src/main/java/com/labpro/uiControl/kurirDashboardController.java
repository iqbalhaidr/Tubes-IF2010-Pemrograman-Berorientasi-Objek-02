package com.labpro.uiControl;

import com.labpro.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;

public class kurirDashboardController {

    @FXML private Pagination pagination;
    @FXML private Text kurirName;

    @FXML
    private TableColumn<Pengiriman, Void> aksiColumn;

    @FXML
    private TableColumn<Pengiriman, String> alamatColumn;

    @FXML
    private TableView<Pengiriman> kurirPengirimanTable;

    @FXML
    private TableColumn<Pengiriman, String> noResColumn;

    @FXML
    private TableColumn<Pengiriman, String> penerimaColumn;

    @FXML
    private TableColumn<Pengiriman, String> statusColumn;

    private Kurir loggedInKurir;
    private ProxyPengiriman pengirimanService;
    private final int rowsPerPage = 1;
    private List<Pengiriman> masterDataPengiriman;

    public kurirDashboardController(){}

    @FXML
    public void initialize() {
        setupTable();
    }

    public void setPengirimanService(ProxyPengiriman service) {
        this.pengirimanService = service;
        if (this.loggedInKurir != null) {
            loadTableData();
        }
    }

    public void setLoggedInKurir(Kurir loggedInKurir) {
        this.loggedInKurir = loggedInKurir;
        if (loggedInKurir != null) {
            kurirName.setText("Selamat Datang, " + loggedInKurir.getName() + "!");
        } else {
            kurirName.setText("Selamat Datang, Kurir!");
        }
        if (this.pengirimanService != null) {
            loadTableData();
        }
    }

    private void setupTable() {
        noResColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNoResi()));
        penerimaColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNamaPenerima()));
        alamatColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTujuan()));

        // --- START MODIFIKASI UNTUK BADGE STATUS ---
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatusPengiriman().toString()));
        statusColumn.setCellFactory(column -> new TableCell<Pengiriman, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    getStyleClass().removeAll("status-menunggu_kurir", "status-dikirim", "status-tiba_di_tujuan", "status-gagal");
                } else {
                    Label statusLabel = new Label(item);
                    statusLabel.setAlignment(Pos.CENTER);

                    getStyleClass().removeAll("status-menunggu_kurir", "status-dikirim", "status-tiba_di_tujuan", "status-gagal");
                    String cssClass = item.toLowerCase().replace(" ", "_");
                    statusLabel.getStyleClass().add("status-" + cssClass);

                    setGraphic(statusLabel);
                    setText(null);
                }
            }
        });

        aksiColumn.setCellFactory(param -> new TableCell<Pengiriman, Void>() {
            private final ComboBox<String> statusComboBox = new ComboBox<>(); // Inisialisasi kosong
            private final Button saveButton = new Button("Simpan");
            private final HBox pane = new HBox(5, statusComboBox, saveButton); // Jarak 5px antar komponen

            {
                // Pengaturan Lebar dan Tinggi
                statusComboBox.setPrefWidth(120.0);
                statusComboBox.setPrefHeight(30.0);
                statusComboBox.setMaxWidth(Double.MAX_VALUE);

                saveButton.setPrefWidth(60.0);
                saveButton.setPrefHeight(30.0);
                saveButton.setMaxWidth(Double.MAX_VALUE);

                HBox.setHgrow(statusComboBox, Priority.ALWAYS);
                HBox.setHgrow(saveButton, Priority.ALWAYS);

                pane.setAlignment(Pos.CENTER_LEFT);
                saveButton.setStyle("-fx-background-color: #2196f3; -fx-text-fill: white; -fx-background-radius: 5;");

                saveButton.setOnAction(event -> {
                    Pengiriman pengirimanInTable = getTableView().getItems().get(getIndex());
                    String selectedOption = statusComboBox.getValue();
                    if (selectedOption != null) {
                        if (!selectedOption.equals(pengirimanInTable.getStatusPengiriman().getDeskripsi())) {
                            StatusPengiriman newStatusEnum = StatusPengiriman.fromDeskripsi(selectedOption);

                            for (Pengiriman p : masterDataPengiriman) {
                                if (p.getIdPengiriman().equals(pengirimanInTable.getIdPengiriman())) {
                                    System.out.println("Seblum "+p.getStatusPengiriman().getDeskripsi());
                                    int id = p.getIdPengiriman();
                                    pengirimanService.updateStatus(id, newStatusEnum);
//                                    p.setStatusPengiriman(newStatusEnum);
                                    System.out.println("Sesudah "+p.getStatusPengiriman().getDeskripsi());

                                    break;
                                }
                            }

                            int currentPage = pagination.getCurrentPageIndex();
                            createPage(currentPage);
                            kurirPengirimanTable.refresh();
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

                    StatusPengiriman currentStatus = pengiriman.getStatusPengiriman();
                    List<String> availableOptions = new ArrayList<>();
                    availableOptions.add(currentStatus.getDeskripsi());

                    switch (currentStatus) {
                        case MENUNGGU_KURIR:
                            availableOptions.add(StatusPengiriman.DIKIRIM.getDeskripsi());
                            availableOptions.add(StatusPengiriman.GAGAL.getDeskripsi());
                            break;
                        case DIKIRIM:

                            availableOptions.add(StatusPengiriman.TIBA_DI_TUJUAN.getDeskripsi());
                            availableOptions.add(StatusPengiriman.GAGAL.getDeskripsi());
                            break;
                        case TIBA_DI_TUJUAN:
                        case GAGAL:
                            break;
                    }

                    statusComboBox.setItems(FXCollections.observableArrayList(availableOptions));
                    statusComboBox.setValue(currentStatus.getDeskripsi());

                    boolean isFinalStatus = (currentStatus == StatusPengiriman.TIBA_DI_TUJUAN ||
                            currentStatus == StatusPengiriman.GAGAL);
                    statusComboBox.setDisable(isFinalStatus);
                    saveButton.setDisable(isFinalStatus);

                    setGraphic(pane);
                }
            }
        });
    }

    private void loadTableData() {

        if (pengirimanService != null && loggedInKurir != null) {
            this.masterDataPengiriman = pengirimanService.getPengirimanByKurir(loggedInKurir);
            int pageCount = (int) Math.ceil((double) masterDataPengiriman.size() / rowsPerPage);
            pagination.setPageCount(pageCount == 0 ? 1 : pageCount);
            pagination.setPageFactory(this::createPage);
            createPage(pagination.getCurrentPageIndex());
        } else {
            System.err.println("Error: pengirimanService or loggedInKurir is null. Cannot load table data.");
            kurirPengirimanTable.setItems(FXCollections.observableArrayList());
            pagination.setPageCount(1);
        }
    }

    private Node createPage(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, masterDataPengiriman.size());

        List<Pengiriman> pageData = masterDataPengiriman.subList(fromIndex, toIndex);
        kurirPengirimanTable.setItems(FXCollections.observableArrayList(pageData));

        return new VBox();
    }
}