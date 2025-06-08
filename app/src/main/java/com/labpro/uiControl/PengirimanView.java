package com.labpro.uiControl;

import com.labpro.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class PengirimanView {
    @FXML private TableView<Pengiriman> kurirTable;
    //    @FXML private TableColumn<Pengiriman, String> namaKurir;
    @FXML private TableColumn<Pengiriman, String> namaKurir;
    @FXML private TableColumn<Pengiriman, String> namaParsel;
    @FXML private TableColumn<Pengiriman, String> alamat;
    @FXML private TableColumn<Pengiriman, String> idPengiriman;
    @FXML private TableColumn<Pengiriman, String> tipe;
    @FXML private TableColumn<Pengiriman, String> status;
    @FXML private TableColumn<Pengiriman, String> tanggalpembuatan;
    @FXML private TableColumn<Pengiriman, String> tanggalpembaruan;

    @FXML private ToggleButton btnDomestik;
    @FXML private Button createBtn;
    @FXML private ToggleButton btnInternasional;
    @FXML private ToggleButton btnSemua;
    @FXML private ToggleGroup filterGroup;
    @FXML private ComboBox<StatusPengiriman> statusFilter;

    @FXML private Pagination pagination;

    @FXML private TextField searchField;

    @FXML private Label timeLabel;

    private final int rowsPerPage = 2;
    private ArrayList<Pengiriman> allData;
    private ArrayList<Pengiriman> currentData;
    private PengirimanSearcher<Pengiriman> searcher;
    private String selectedTipe = "Semua";
    private StatusPengiriman selectedStatus = null;

//    @FXML private TableColumn<Pengiriman, String> status;

    private RepoPengirimanController controller;

    public void setRepoPengirimanController(RepoPengirimanController controller) {
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        TimeThread timeThread = new TimeThread(timeLabel);
        timeThread.start();

        statusFilter.setItems(FXCollections.observableArrayList(StatusPengiriman.values()));

        System.out.println("PengirimanView initialize");
//        System.out.println(controller.getPengiriman().get(0).getKurir().getName());
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

        tanggalpembuatan.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTanngalPembuatan().toString()));


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

                    statusLabel.getStyleClass().removeAll("status-menunggu_kurir", "status-dikirim", "status-tiba_di_tujuan", "status-gagal", "status-diproses");
                    String cssClass = "status-" + status.toLowerCase().replace(" ", "_");
                    statusLabel.getStyleClass().addAll("status-label", cssClass);

                    setGraphic(statusLabel);
                    setText(null);
                }
            }
        });


        TableColumn<Pengiriman, Void> actionColumn = new TableColumn<>("Aksi");
        actionColumn.setCellFactory(col -> new TableCell<Pengiriman, Void>() {
            private final Button button = new Button("Lihat Tracking");

            {
                button.setOnAction(e -> {
                    Pengiriman selected = getTableView().getItems().get(getIndex());
                    try {
                        handleLihatTracking(selected);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });
                button.setStyle("-fx-cursor: hand; -fx-background-color: #4CAF50; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        TableColumn<Pengiriman, Void> editColumn = new TableColumn<>("Edit");
        editColumn.setCellFactory(col -> new TableCell<Pengiriman, Void>() {
            private final Button button = new Button("Edit");

            {
                button.setOnAction(e -> {
                    Pengiriman selected = getTableView().getItems().get(getIndex());
                    try {
                        handleEditPengiriman(selected, controller);
                        updatePagination(currentData);
                        kurirTable.refresh();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                });

//                updatePagination(currentData);
//                kurirTable.refresh();
                button.setStyle("-fx-cursor: hand; -fx-background-color: #2196F3; -fx-text-fill: white;");
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(button);
                }
            }
        });

        kurirTable.getColumns().add(editColumn);

        kurirTable.getColumns().add(actionColumn);
    }

    @FXML
    public void initializeData() {

        if (controller != null) {
            allData = (ArrayList<Pengiriman>) controller.getPengiriman();
            currentData = new ArrayList<>(allData);
            searcher = new PengirimanSearcher<>(allData);
            if (allData != null && !allData.isEmpty()) {
                int pageCount = (int) Math.ceil((double) allData.size() / rowsPerPage);
                pagination.setPageCount(pageCount);
                pagination.setCurrentPageIndex(0);

                updateTable(0);

                pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
                    updateTable(newIndex.intValue());
                });


            } else {
                System.out.println("No pengiriman data found.");
            }
        } else {
            System.err.println("RepoPengirimanController is not set!");
        }
    }

    private void updateTable(int pageIndex) {
        int fromIndex = pageIndex * rowsPerPage;
        int toIndex = Math.min(fromIndex + rowsPerPage, currentData.size());
        kurirTable.setItems(FXCollections.observableArrayList(currentData.subList(fromIndex, toIndex)));
    }

    private void updatePagination(ArrayList<Pengiriman> data) {
        int pageCount = (int) Math.ceil((double) data.size() / rowsPerPage);
        pagination.setPageCount(pageCount);
        pagination.setCurrentPageIndex(0);
        updateTable(0);

        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            updateTable(newIndex.intValue());
        });
    }

    @FXML
    private void handleSearchButton() {
        String query = searchField.getText();
        SearchCriteria criteria = new SearchCriteria(query);
        Collection<? extends Pengiriman> results = searcher.search(criteria);
        currentData = results.stream()
                .filter(p -> "Semua".equals(selectedTipe) || selectedTipe.equals(p.getType()))
                .filter(p -> selectedStatus == null || selectedStatus.equals(p.getStatusPengiriman()))
                .collect(Collectors.toCollection(ArrayList::new));

        updatePagination(currentData);
    }

    @FXML
    private void handleClearSearch() {
        searchField.clear(); // Clear search
        selectedTipe = "Semua";
        selectedStatus = null;
        statusFilter.getSelectionModel().clearSelection(); // reset ComboBox
        currentData = new ArrayList<>(allData);
        filterGroup.selectToggle(btnSemua);
        updatePagination(currentData);
    }
    @FXML
    private void handleDomestik() {
        selectedTipe = "Domestik";
        applyCombinedFilter();
    }

    @FXML
    private void handleInternasional() {
        selectedTipe = "Internasional";
        applyCombinedFilter();
    }

    @FXML
    private void handleSemua() {
        selectedTipe = "Semua";
        applyCombinedFilter();
    }

    @FXML
    private void handleStatusFilter(){
        selectedStatus = statusFilter.getValue();
        applyCombinedFilter();
    }

    private void applyCombinedFilter() {
        handleSearchButton();
    }

    @FXML
    private void handleLihatTracking(Pengiriman selected) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/trackingPengiriman.fxml"));
        Parent root = loader.load();

        TrackingView controller = loader.getController();
        controller.setData(selected);

        Stage stage = new Stage();
        stage.setTitle("Tracking Pengiriman");
        stage.setScene(new Scene(root));
        stage.show();
    }

    @FXML
    private void handleEditPengiriman(Pengiriman selected, RepoPengirimanController controller) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EditPengirimanDialog.fxml"));
        Parent root = loader.load();

        EditPengirimanDialogController Editcontroller = loader.getController();
        Editcontroller.setData(selected, controller); // pass the selected pengiriman

        Stage stage = new Stage();
        stage.setTitle("Edit Pengiriman");
        stage.setScene(new Scene(root));

        stage.setOnHidden(e -> {
            allData = controller.getPengiriman();
            currentData = new ArrayList<>(allData);
            updatePagination(currentData);
            kurirTable.refresh();
        });

        stage.show();
    }

    @FXML
    private void handleCreatePengiriman() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/selectPengiriman.fxml"));
        Parent root = loader.load();

        SelectPengirimanTypeController controller2 = loader.getController();
        controller2.setRepoPengirimanController(this.controller);

        Stage stage = new Stage();
        stage.setTitle("Pilih Tipe Pengiriman");
        stage.setScene(new Scene(root));

        // Refresh after dialog closes
        stage.setOnHidden(e -> {
            // Refresh your allData and currentData from repoPengirimanController
            allData = new ArrayList<>(controller.getPengiriman());
            currentData = new ArrayList<>(allData);
            updatePagination(currentData);
            kurirTable.refresh();
        });

        stage.show();
    }




}