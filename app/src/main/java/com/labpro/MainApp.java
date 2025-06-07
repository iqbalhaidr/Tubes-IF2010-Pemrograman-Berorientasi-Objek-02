package com.labpro;

import com.labpro.uiControl.kurirDashboardController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.IOException;
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
        primaryStage.setTitle("Aplikasi Pengiriman - Tes Kurir Dashboard");

        try {
            String kurirJsonPath = "E:\\if2010-tubes-2-2425-lah - CopyIniRill\\app\\src\\main\\java\\com\\labpro\\dummyData\\Kurir.json";
            String pengirimanPath = "E:\\if2010-tubes-2-2425-lah - CopyIniRill\\app\\src\\main\\java\\com\\labpro\\dummyData\\Pengiriman.json";
            String parselPath = "E:\\if2010-tubes-2-2425-lah - CopyIniRill\\app\\src\\main\\java\\com\\labpro\\dummyData\\Parsel.json";

            String absoluteKurirPath = Paths.get(kurirJsonPath).toAbsolutePath().toString();
            System.out.println("Mencoba membaca Kurir dari: " + absoluteKurirPath);

            //load kurir
            Adapter<Kurir> kurirAdapter = new Adapter<>(absoluteKurirPath, Kurir.class);
            List<Kurir> allKurirs = kurirAdapter.parseList();
            KurirRepository kurirRepository = new KurirRepository(allKurirs);
            List<Kurir> activeKurirs = allKurirs.stream()
                    .filter(kurir -> !kurir.getDeleteStatus())
                    .collect(Collectors.toList());
            System.out.println("INI HASIL FILTERED KURIR"+activeKurirs);

            //load parsel
            Adapter<Parsel> parselAdapter = new Adapter<>(parselPath, Parsel.class);
            List<Parsel> allParsel = parselAdapter.parseList();
            ParselRepository parselRepository = new ParselRepository(allParsel);

            List<Parsel> filteredParsels = allParsel.stream()
                    .filter(parsel -> parsel.getStatus() == ParselStatus.REGISTERED && !parsel.getDeleteStatus())
                    .collect(Collectors.toList());
            System.out.println("INI HASIL FILTERED PARSEL"+filteredParsels);

            //load pengiriman
            Map<String, Class<? extends Pengiriman>> map = new HashMap<>();
            map.put("INTERNASIONAL", PengirimanInternasional.class);
            map.put("DOMESTIK", PengirimanDomestik.class);

            Adapter<Pengiriman> adapterPengiriman = new Adapter<Pengiriman>(pengirimanPath, Pengiriman.class, "tipe", map);
            List<Pengiriman> allPengiriman = adapterPengiriman.parseList();
            System.out.println("INI HASIL FILTERED PENGIRIMAN"+allPengiriman);
            for(Pengiriman pengiriman : allPengiriman){
                pengiriman.generateParselList(parselRepository);
                pengiriman.generateKurir(kurirRepository);
            }


            PengirimanRepository pengirimanRepository = new PengirimanRepository(allPengiriman);
            RepoPengirimanController pengirimanService = new RepoPengirimanController(
                    (ArrayList<Kurir>) activeKurirs, (ArrayList<Parsel>) filteredParsels, pengirimanRepository);


            // Login
            Kurir loggedInKurir = null;
            if (!allKurirs.isEmpty()) {
                loggedInKurir = allKurirs.get(0);
                try {
                    System.out.println("KURIR INI LOGIN "+loggedInKurir);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }

            } else {
                System.out.println("Tidak ada data Kurir yang ditemukan di " + absoluteKurirPath);
                return;
            }

            // Load FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/kurirDashboard.fxml")); // Pastikan path FXML benar
            Parent kurirDashboardRoot = loader.load(); // Memuat FXML

            kurirDashboardController controller = loader.getController();
            controller.setLoggedInKurir(loggedInKurir);
            controller.setPengirimanService(pengirimanService);

            Scene scene = new Scene(kurirDashboardRoot, 800, 500);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/styles.css")).toExternalForm()); // Path CSS

            primaryStage.setTitle("Aplikasi Pengiriman - Kurir Dashboard"); // Judul jendela
            primaryStage.setScene(scene); // Menetapkan scene ke stage
            primaryStage.show(); // Menampilkan stage

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