package com.labpro;

import com.labpro.uiControl.ManajemenParselDashboardController;
import com.labpro.uiControl.kurirDashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Paths; // Import Paths
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;


public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Aplikasi Parsel - Tes Parsel Dashboard");

        try {
            URL resourceUrl = getClass().getResource("/data/Parsel.json");
            if (resourceUrl == null) {
                throw new IllegalArgumentException("File Parsel.json not found in resources");
            }
            String parselPath = new File(resourceUrl.toURI()).getAbsolutePath();
            //load parsel
            Adapter<Parsel> parselAdapter = new Adapter<>(parselPath, Parsel.class);
            List<Parsel> allParsel = parselAdapter.parseList();
            ParselRepository parselRepository = new ParselRepository(allParsel);

//            List<Parsel> filteredParsels = allParsel.stream()
//                    .filter(parsel -> parsel.getStatus() == ParselStatus.REGISTERED && !parsel.getDeleteStatus())
//                    .collect(Collectors.toList());
//            System.out.println("INI HASIL FILTERED PARSEL"+filteredParsels);

            // Load FXML

            RepoParselController repoParselController = new RepoParselController(parselRepository);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ParselView.fxml"));
            Parent parselDashboardRoot = loader.load();
            ManajemenParselDashboardController controller = loader.getController();
            controller.setRepoParselController(repoParselController);

            Scene scene = new Scene(parselDashboardRoot);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Memuat Aplikasi");
            alert.setHeaderText("Gagal memuat aplikasi.");
            alert.setContentText("Pastikan file FXML dan JSON ada di lokasi yang benar dan tidak ada kesalahan sintaks.");
            alert.showAndWait();
        } catch (Exception e) { // Tangkap Exception umum untuk parseList
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Parsing Data");
            alert.setHeaderText("Gagal mengurai data dari JSON.");
            alert.setContentText("Pastikan format JSON sesuai dengan kelas Kurir dan Adapter Anda berfungsi.");
            alert.showAndWait();
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}