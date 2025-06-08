package com.labpro.uiControl;

import com.labpro.LogStatus;
import com.labpro.Parsel;
import com.labpro.Pengiriman;
import com.labpro.TimeThread;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.ArrayList;
import java.util.List;

public class TrackingView {
    @FXML private Label resiLabel;
    @FXML private Label statusLabel;
    @FXML private Label kurirLabel;
    @FXML private Label pengirimLabel;
    @FXML private Label penerimaLabel;

    @FXML private TableView<Parsel> parselTable;
    @FXML private TableColumn<Parsel, String> idParselColumn;
    @FXML private TableColumn<Parsel, String> dimensiColumn;
    @FXML private TableColumn<Parsel, Double> beratColumn;
    @FXML private TableColumn<Parsel, String> jenisBarangColumn;

    @FXML private TableView<LogStatus> logTable;
    @FXML private TableColumn<LogStatus, String> dateCollumn;
    @FXML private TableColumn<LogStatus, String> statusCollumn;
    @FXML private TableColumn<LogStatus, String> pesanCollumn;

    @FXML
    private Label timeLabel;

    @FXML
    public void initialize() {
        TimeThread timeThread = new TimeThread(timeLabel);
        timeThread.start();
        idParselColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));

        dimensiColumn.setCellValueFactory(cellData -> {
            int[] d = cellData.getValue().getDimensi();
            String dimensiString = d[0] + "x" + d[1] + "x" + d[2];
            return new javafx.beans.property.SimpleStringProperty(dimensiString);
        });

        beratColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleDoubleProperty(cellData.getValue().getBerat()).asObject()
        );

        jenisBarangColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getJenisBarang())
        );

        dateCollumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getDatetime().toString())
        );

        statusCollumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getStatus().toString())
        );

        pesanCollumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getPesan())
        );
    }

    public void setData(Pengiriman pengiriman) {
        resiLabel.setText(pengiriman.getIdPengiriman().toString());
        statusLabel.setText(pengiriman.getStatusPengiriman().toString());
        kurirLabel.setText(pengiriman.getKurirName());
        pengirimLabel.setText(pengiriman.getNamaPengiriman());
        penerimaLabel.setText(pengiriman.getNamaPengiriman());

        parselTable.getItems().setAll(pengiriman.getListOfParsel());
        List<LogStatus> logStatusList = pengiriman.getLogStatus();

        if  (logStatusList != null) {
            logTable.getItems().setAll(pengiriman.getLogStatus());
        }
    }
}