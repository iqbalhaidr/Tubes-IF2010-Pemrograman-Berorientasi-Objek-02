package com.labpro;

import com.labpro.AdminController; // Menggunakan AdminController
// Import semua kelas model, repository, service, dan utility yang diperlukan
import com.labpro.RepoKurirController;
import com.labpro.RepoPengirimanController;
import com.labpro.RepoParselController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public class MainApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Aplikasi Pengiriman - Admin Dashboard");

        try {
            // --- 1. Definisi Path File JSON ---
            String kurirJsonPath = "D:\\Codes\\SEMESTER4IF\\Codes\\OOP\\if2010-tubes-2-2425-lah\\app\\src\\main\\java\\com\\labpro\\dummyData\\Kurir.json";
            String parselPath = "D:\\Codes\\SEMESTER4IF\\Codes\\OOP\\if2010-tubes-2-2425-lah\\app\\src\\main\\java\\com\\labpro\\dummyData\\Parsel.json";
            String pengirimanPath = "D:\\Codes\\SEMESTER4IF\\Codes\\OOP\\if2010-tubes-2-2425-lah\\app\\src\\main\\java\\com\\labpro\\dummyData\\Pengiriman.json";

            System.out.println("Memulai inisialisasi data...");

            // --- 2. Inisialisasi Repository Layer ---
            // Kurir Repository
            Adapter<Kurir> kurirAdapter = new Adapter<>(kurirJsonPath, Kurir.class);
            List<Kurir> allKurirs = kurirAdapter.parseList();
            KurirRepository kurirRepository = new KurirRepository(allKurirs);
            List<Kurir> activeKurirs = allKurirs.stream()
                    .filter(kurir -> !kurir.getDeleteStatus())
                    .collect(Collectors.toList());
            System.out.println("KurirRepository diinisialisasi. Kurir Aktif: " + activeKurirs.size());

            // Parsel Repository
            Adapter<Parsel> parselAdapter = new Adapter<>(parselPath, Parsel.class);
            List<Parsel> allParsel = parselAdapter.parseList();
            ParselRepository parselRepository = new ParselRepository(allParsel);
            List<Parsel> filteredParsels = allParsel.stream()
                    .filter(parsel -> parsel.getStatus() == ParselStatus.REGISTERED && !parsel.getDeleteStatus())
                    .collect(Collectors.toList());
            System.out.println("ParselRepository diinisialisasi. Parsel Terfilter: " + filteredParsels.size());


            // Pengiriman Repository
            Map<String, Class<? extends Pengiriman>> pengirimanSubtypeMap = new HashMap<>();
            pengirimanSubtypeMap.put("INTERNASIONAL", PengirimanInternasional.class);
            pengirimanSubtypeMap.put("DOMESTIK", PengirimanDomestik.class);
            Adapter<Pengiriman> adapterPengiriman = new Adapter<>(pengirimanPath, Pengiriman.class, "type", pengirimanSubtypeMap);

            List<Pengiriman> allPengiriman = adapterPengiriman.parseList();

            // Generate ParselList dan Kurir untuk setiap Pengiriman setelah deserialisasi
            // Ini penting karena relasi Kurir dan Parsel di Pengiriman tidak langsung di-deserialize oleh GSON
            for(Pengiriman pengiriman : allPengiriman){
                pengiriman.generateParselList(parselRepository);
                // Pastikan Pengiriman memiliki metode generateKurir(KurirRepository)
                pengiriman.generateKurir(kurirRepository);
            }
            PengirimanRepository pengirimanRepository = new PengirimanRepository(allPengiriman);
            System.out.println("PengirimanRepository diinisialisasi. Total Pengiriman: " + allPengiriman.size());

            // --- 3. Inisialisasi Service Layer (Repo...Controller) ---
            // Pastikan konstruktor RepoKurirController hanya butuh KurirRepository
            RepoKurirController repoKurirService = new RepoKurirController(kurirRepository);
            // Pastikan konstruktor RepoParselController hanya butuh ParselRepository
            RepoParselController repoParselService = new RepoParselController(parselRepository);
            // Pastikan konstruktor RepoPengirimanController sesuai dengan yang Anda inginkan
            // Contoh: menerima List<Kurir>, List<Parsel>, PengirimanRepository
            RepoPengirimanController repoPengirimanService = new RepoPengirimanController(
                    new ArrayList<>(activeKurirs), // Menggunakan data aktif kurir
                    new ArrayList<>(filteredParsels), // Menggunakan data terfilter parsel
                    pengirimanRepository
            );
            System.out.println("Semua Service Layer (Repo...Controller) diinisialisasi.");


            // --- 4. Muat AdminDashboard.fxml dan Injeksi Dependensi ---
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/AdminDashboard.fxml"));

            // Membuat instance AdminController secara manual
            AdminController adminController = new AdminController();

            // Menyuntikkan semua service layer ke AdminController
            adminController.setRepoKurirController(repoKurirService);
            adminController.setRepoPengirimanController(repoPengirimanService);
            adminController.setRepoParselController(repoParselService);
            // Inject instance ini ke FXMLLoader agar tidak membuat yang baru
            loader.setControllerFactory(param -> {
                if (param == AdminController.class) {
                    return adminController;
                } else {
                    try {
                        return param.getDeclaredConstructor().newInstance();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });





            // Muat root FXML
            Parent adminDashboardRoot = loader.load();
            System.out.println("Admin Dashboard FXML dimuat.");

            // --- 5. Atur Scene dan Tampilkan Stage ---
            Scene scene = new Scene(adminDashboardRoot); // Biarkan Scene menentukan ukuran dari FXML root

            // Pastikan CSS untuk AdminDashboard terhubung di AdminDashboard.fxml
            // Jika tidak, Anda bisa menambahkannya di sini:

             scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/AdminDashboard.css")).toExternalForm());
            primaryStage.initStyle(StageStyle.DECORATED);
            primaryStage.setScene(scene);
            primaryStage.show();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Memuat Aplikasi", "Gagal memuat aplikasi.",
                    "Pastikan file FXML, JSON, dan CSS ada di lokasi yang benar dan tidak ada kesalahan sintaks.");
        } catch (Exception e) { // Tangkap Exception umum untuk parseList atau kesalahan tak terduga lainnya
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error Inisialisasi Aplikasi", "Terjadi Kesalahan Tak Terduga.",
                    "Detail: " + e.getMessage() + "\nPeriksa konsol untuk detail lebih lanjut.");
        }
    }

    // Helper method untuk menampilkan alert
    private void showAlert(Alert.AlertType type, String title, String header, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
