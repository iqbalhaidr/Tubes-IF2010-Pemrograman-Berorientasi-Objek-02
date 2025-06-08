//package com.labpro;
////D:\Codes\SEMESTER4IF\Codes\OOP\if2010-tubes-2-2425-lah\app\src\main\java\com\labpro\Main.java
//import com.labpro.uiControl.ManajemenKurirDashboardController; // Import ManajemenKurirController (dari paket uiControl)
//
//import javafx.application.Application;
//import javafx.fxml.FXMLLoader;
//import javafx.scene.Parent;
//import javafx.scene.Scene;
//import javafx.stage.Stage;
//import javafx.stage.StageStyle; // Import ini tetap diperlukan jika Anda ingin opsi untuk mengaturnya nanti
//
//import java.io.IOException;
//
//public class TestMain extends Application {
//
//    // Variabel untuk melacak posisi mouse saat dragging
//    private double xOffset = 0;
//    private double yOffset = 0;
//
//    @Override
//    public void start(Stage primaryStage) {
//        try {
//            // 1. Inisialisasi Repository Layer
//            KurirRepository kurirRepository = new KurirRepository();
//            assert kurirRepository != null : "KurirRepository should not be null after initialization in MainApp.";
//
//            // 2. Inisialisasi Service Layer (yang Anda namai RepoKurirController),
//            //    meneruskan Repository ke Service
//            // Catatan: Jika Anda telah mengganti RepoKurirController dengan KurirService,
//            // pastikan baris ini sesuai dengan nama kelas yang benar.
//            RepoKurirController repoKurirController = new RepoKurirController(kurirRepository);
//            assert repoKurirController != null : "RepoKurirController should not be null after initialization in MainApp.";
//
//            // 3. Buat FXMLLoader untuk FXML UI yang dikelola oleh ManajemenKurirController
//            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/ManajemenKurirDashboard.fxml"));
//
//            // 4. Set Controller Factory untuk menyediakan instance controller dengan Service
//            loader.setControllerFactory(controllerClass -> {
//                if (controllerClass == ManajemenKurirDashboardController.class) {
//                    return new ManajemenKurirDashboardController(repoKurirController); // Meneruskan RepoKurirController
//                }
//                else {
//                    try {
//                        return controllerClass.newInstance();
//                    } catch (Exception e) {
//                        throw new RuntimeException("Gagal membuat instance controller " + controllerClass.getName(), e);
//                    }
//                }
//            });
//
//            // 5. Muat FXML
//            Parent root = loader.load();
//
//            // 6. Buat Scene dan tampilkan Stage
//            Scene scene = new Scene(root);
//
//            primaryStage.setTitle("Aplikasi Manajemen Kurir");
//            primaryStage.setScene(scene);
//            primaryStage.initStyle(StageStyle.DECORATED);
//            primaryStage.show();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.err.println("Gagal memuat FXML: " + e.getMessage());
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.err.println("Terjadi kesalahan saat memulai aplikasi: " + e.getMessage());
//        }
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}
