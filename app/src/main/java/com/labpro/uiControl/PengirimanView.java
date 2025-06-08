package com.labpro.uiControl;

import com.labpro.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import javafx.geometry.Pos;
import javafx.scene.control.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List; // Menggunakan List
import java.util.stream.Collectors;

public class PengirimanView {
    @FXML private TableView<Pengiriman> kurirTable;
    @FXML private TableColumn<Pengiriman, String> namaKurir;
    @FXML private TableColumn<Pengiriman, String> namaParsel;
    @FXML private TableColumn<Pengiriman, String> alamat;
    @FXML private TableColumn<Pengiriman, String> idPengiriman;
    @FXML private TableColumn<Pengiriman, String> tipe;
    @FXML private TableColumn<Pengiriman, String> status;

    @FXML private ToggleButton btnDomestik;
    @FXML private ToggleButton btnInternasional;
    @FXML private ToggleButton btnSemua;
    @FXML private ToggleGroup filterGroup;
    @FXML private ComboBox<StatusPengiriman> statusFilter;

    @FXML private Pagination pagination;

    @FXML private TextField searchField;

    private final int rowsPerPage = 4;
    private ArrayList<Pengiriman> allData;
    private ArrayList<Pengiriman> currentData;
    private PengirimanSearcher<Pengiriman> searcher;
    private String selectedTipe = "Semua";
    private StatusPengiriman selectedStatus = null;

    private RepoPengirimanController controller;

    public void setController(RepoPengirimanController controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {

        statusFilter.setItems(FXCollections.observableArrayList(StatusPengiriman.values()));
        // Listener untuk statusFilter (jika belum ada, tambahkan)
        statusFilter.valueProperty().addListener((
                obs,
                oldVal,
                newVal) -> {
            selectedStatus = newVal;
            applyCombinedFilter();
        });


        System.out.println("PengirimanView initialize");
        idPengiriman.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getIdPengiriman().toString()))
        ;

        namaKurir.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getKurirName()));
//        namaParsel.setCellValueFactory(cellData ->
//                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStringParsel()));
        alamat.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTujuan()));

        tipe.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType()));


        status.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatusPengiriman().toString()));

        status.setCellFactory(col -> new TableCell<Pengiriman, String>() {
            private final Label label = new Label();

            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setText(null);
                    setGraphic(null);
                } else{
                    Label statusLabel = new Label(status);
                    statusLabel.setAlignment(Pos.CENTER);

                    statusLabel.getStyleClass().removeAll(
                            "status-menunggu_kurir",
                            "status-dikirim",
                            "status-tiba_di_tujuan",
                            "status-gagal",
                            "status-diproses");
                    String cssClass = "status-" + status.toLowerCase().replace(" ", "_");
                    statusLabel.getStyleClass().addAll("status-label", cssClass);

                    setGraphic(statusLabel);
                    setText(null);
                }
            }
        });

        // Listener untuk searchField
        searchField.textProperty().addListener((
                obs,
                oldText,
                newText) -> {
            applyCombinedFilter();
        });

        // Listener untuk filterGroup
        if (filterGroup != null) {
            filterGroup.selectedToggleProperty().addListener((
                    obs,
                    oldToggle,
                    newToggle) -> {
                if (newToggle == btnDomestik) selectedTipe = "DOMESTIK";
                else if (newToggle == btnInternasional) selectedTipe = "INTERNASIONAL";
                else selectedTipe = "Semua";
                applyCombinedFilter();
            });
        }


        if (controller == null) {
            System.err.println("WARNING: RepoPengirimanController bernilai null di initialize(). Pastikan sudah diinjeksi.");
        } else {
            initializeData();
        }

        // Listener untuk pagination
        pagination.currentPageIndexProperty().addListener((
                obs,
                oldIndex,
                newIndex) -> {
            updateTable(newIndex.intValue());
        });
    }

    @FXML
    public void initializeData() {
        if (controller != null) {
            allData = (ArrayList<Pengiriman>) controller.getPengiriman();
            currentData = new ArrayList<>(allData);
            searcher = new PengirimanSearcher<>(allData);

            // PENTING: Inisialisasi state filter awal
            searchField.clear();
            selectedTipe = "Semua";
            selectedStatus = null;
            if (statusFilter != null) {
                statusFilter.getSelectionModel().clearSelection();
            }
            if (filterGroup != null && btnSemua != null) {
                filterGroup.selectToggle(btnSemua);
            }

            // Panggil applyCombinedFilter untuk melakukan pemuatan data awal berdasarkan default filter
            applyCombinedFilter();

        } else {
            System.err.println("RepoPengirimanController is not set!");
        }
    }

    private void updateTable(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, currentData.size());

        // Pastikan currentData tidak kosong atau indeks tidak di luar batas
        if (currentData.isEmpty() || fromIndex >= currentData.size()) {
            kurirTable.setItems(FXCollections.observableArrayList());
            return;
        }

        kurirTable.setItems(FXCollections.observableArrayList(currentData.subList(fromIndex, toIndex)));
    }

    private void updatePagination(ArrayList<Pengiriman> data) {
        int pageCount = (data != null && !data.isEmpty()) ? (int) Math.ceil((double) data.size() / rowsPerPage) : 1;
        pagination.setPageCount(pageCount);

        // Atur ulang currentPageIndex ke 0 setiap kali filter berubah
        pagination.setCurrentPageIndex(0);

        updateTable(0); // Update tabel untuk halaman pertamag baru
    }

    @FXML
    private void handleSearchButton() {
        applyCombinedFilter();
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear();
        selectedTipe = "Semua";
        selectedStatus = null;
        statusFilter.getSelectionModel().clearSelection();
        filterGroup.selectToggle(btnSemua);
        applyCombinedFilter();
    }

    @FXML
    private void handleDomestik() {
        selectedTipe = "DOMESTIK";
        applyCombinedFilter();
    }

    @FXML
    private void handleInternasional() {
        selectedTipe = "INTERNASIONAL";
        applyCombinedFilter();
    }

    @FXML
    private void handleSemua() {
        selectedTipe = "Semua";
        filterGroup.selectToggle(btnSemua);
        applyCombinedFilter();
    }

    @FXML
    private void handleStatusFilter(){
        selectedStatus = statusFilter.getValue();
        applyCombinedFilter();
    }

    private void applyCombinedFilter() {
        if (allData == null) {
            System.err.println("allData is null in applyCombinedFilter. Cannot filter.");
            currentData = new ArrayList<>();
            updatePagination(currentData);
            return;
        }

        List<Pengiriman> filteredList = new ArrayList<>(allData);

        // 1. Terapkan filter pencarian teks
        String query = searchField.getText();
        if (query != null && !query.trim().isEmpty()) {
            SearchCriteria criteria = new SearchCriteria(query.trim());
            Collection<? extends Pengiriman> searchResults = searcher.search(criteria);
            filteredList = new ArrayList<>(searchResults);
        }

        // 2. Terapkan filter tipe (Domestik/Internasional/Semua)
        if (!"Semua".equals(selectedTipe)) {
            filteredList = filteredList.stream()
                    .filter(p -> selectedTipe.equalsIgnoreCase(p.getType()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        // 3. Terapkan filter status
        if (selectedStatus != null) {
            filteredList = filteredList.stream()
                    .filter(p -> selectedStatus.equals(p.getStatusPengiriman()))
                    .collect(Collectors.toCollection(ArrayList::new));
        }

        currentData = new ArrayList<>(filteredList);
        updatePagination(currentData);
    }
}
