package com.labpro.uiControl;

import com.labpro.RepoPengirimanController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SelectPengirimanTypeController {

    private RepoPengirimanController repoPengirimanController;

    public void setRepoPengirimanController(RepoPengirimanController controller) {
        this.repoPengirimanController = controller;
        System.out.println("Data pengiriman: " + repoPengirimanController.getPengiriman().size());
    }

    @FXML
    private void handleDomestik() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/createPengirimanDialogDomestik.fxml"));
        Parent root = loader.load();

        CreateDomestikPengirimanDialogController controller = loader.getController();
        controller.setRepoPengirimanController(repoPengirimanController);

        Stage stage = new Stage();
        stage.setTitle("Buat Pengiriman Domestik");
        stage.setScene(new Scene(root));
        stage.show();

        // Optional: Close the selection window
        // ((Stage) someButton.getScene().getWindow()).close();  // Replace with a valid Node if needed
    }

    @FXML
    private void handleInternasional() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CreateInternasionalPengirimanDialog.fxml"));
        Parent root = loader.load();

        CreateInternasionalPengirimanDialogController controller2 = loader.getController();
        controller2.setRepoPengirimanController(repoPengirimanController);

        Stage stage = new Stage();
        stage.setTitle("Buat Pengiriman Internasional");
        stage.setScene(new Scene(root));
        stage.show();
    }
}