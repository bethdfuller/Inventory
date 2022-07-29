package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.InHouse;
import model.Inventory;
import model.Outsourced;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Add Part screen <br>
 * RUNTIME_ERROR: Runtime error occurred when saving a part - maximum and minimum were not called correctly & exchanged causing the program to request a minimum that was higher than the maximum.
 */


public class AddPart implements Initializable {

    Stage stage;
    Parent scene;

    private static int partId = 1;

    @FXML private RadioButton PartInHouseRB;
    @FXML private RadioButton PartOutsourcedRB;
    @FXML private Label PartID;
    @FXML private TextField PartNameText;
    @FXML private TextField PartInvText;
    @FXML private TextField PartPriceCostText;
    @FXML private TextField PartMinText;
    @FXML private TextField PartMaxText;
    @FXML private Label PartMachineIDorCompanyName;
    @FXML private TextField PartMachineIDorCompanyNameText;

    /**
     *  FUTURE_ENHANCEMENT: Add photo of part
     * @param event
     */


    /**
     * Sets InHouse radio button with Machine ID label and Outsourced radio button to Company Name Label.
     * @param event
     */
    @FXML void onActionInHouse(ActionEvent event) {
        PartMachineIDorCompanyName.setText("Machine ID");
    }
    @FXML void onActionOutsourced(ActionEvent event) {
        PartMachineIDorCompanyName.setText("Company Name");
    }

    /**
     * Create unique part ID
     */
    public static int getPartId() {
        partId++;
        return partId;
    }

    /**
     * Saves Part
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionSavePart(ActionEvent event) throws IOException {

        try {
            int id = Integer.parseInt(PartID.getText());
            String name = PartNameText.getText();
            int stock = Integer.parseInt(PartInvText.getText());
            double price = Double.parseDouble(PartPriceCostText.getText());
            int min = Integer.parseInt(PartMinText.getText());
            int max = Integer.parseInt(PartMaxText.getText());

            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: The part name cannot be empty.");
                alert.showAndWait();
            } else if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: The part maximum must be greater than the part minimum.");
                alert.showAndWait();
            } else if (stock < min || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Error: Part inventory must be between part minimum and maximum");
                alert.showAndWait();
            }
            else {
                if (PartInHouseRB.isSelected()) {
                    int machineID = Integer.parseInt(PartMachineIDorCompanyNameText.getText());
                    Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                } else if (PartOutsourcedRB.isSelected()) {
                    String companyName = PartMachineIDorCompanyNameText.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                }
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
        catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning: Input Error");
            alert.setContentText("Part form needs valid input type for all fields.");
            alert.showAndWait();
        }
    }

    /**
     * Cancel button returns user to Main screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionReturnMainScreen(ActionEvent event) throws IOException {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Set Add Part Radio button to default InHouse, set partId
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    PartInHouseRB.setSelected(true);

    partId = getPartId();
    PartID.setText(String.valueOf(partId));

    }
}
