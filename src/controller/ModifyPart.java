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
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Modify Part screen
 */

public class ModifyPart implements Initializable{

    private Part selectedPart;

    Stage stage;
    Parent scene;

    @FXML private RadioButton PartInHouseRB;
    @FXML private RadioButton PartOutsourcedRB;
    @FXML private Label PartID;
    @FXML private TextField PartNameText;
    @FXML private TextField PartInvText;
    @FXML private TextField PartPriceCostText;
    @FXML private TextField PartMaxText;
    @FXML private TextField PartMinText;
    @FXML private Label PartMachineIDorCompanyName;
    @FXML private TextField PartMachineIDorCompanyNameText;

    /**
     * Sets InHouse radio button with Machine ID label and Outsourced radio button to Company Name Label.
     * @param event
     */
    @FXML void onActionInHouse(ActionEvent event) {PartMachineIDorCompanyNameText.setText("Machine ID");}
    @FXML void onActionOutsourced(ActionEvent event) {
        PartMachineIDorCompanyName.setText("Company Name");
    }

    /**
     * Saves modified Part
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

            boolean partAddSuccessful = false;

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
                if (PartInHouseRB.isSelected()) {
                    try {
                        int machineID = Integer.parseInt(PartMachineIDorCompanyNameText.getText());
                        Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineID));
                        partAddSuccessful = true;
                    } catch (Exception e) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error");
                        alert.setContentText("Invalid value for the Machine ID");
                        alert.showAndWait();
                    }
                }

                if (PartOutsourcedRB.isSelected()) {
                    String companyName = PartMachineIDorCompanyNameText.getText();
                    Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                    partAddSuccessful = true;
                }

                if (partAddSuccessful) {
                    Inventory.deletePart(selectedPart);
                    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                    scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                    stage.setScene(new Scene(scene));
                    stage.show();
                 }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning: Input Error");
            alert.setContentText("Part form needs valid input type for all fields.");
            alert.showAndWait();
        }
    }

    /**
     * Cancel Part button - returns user to Main Screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionReturnMainScreen(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override public void initialize(URL url, ResourceBundle resourceBundle) {

/**
 * Get part to modify from Main Screen
 */
    selectedPart = MainScreen.getPartToModify();

    if (selectedPart instanceof InHouse) {
        PartInHouseRB.setSelected(true);
        PartMachineIDorCompanyName.setText("Machine ID");
        PartMachineIDorCompanyNameText.setText(String.valueOf(((InHouse)selectedPart).getMachineID()));
    }

    if (selectedPart instanceof Outsourced) {
        PartOutsourcedRB.setSelected(true);
        PartMachineIDorCompanyName.setText("Company Name");
        PartMachineIDorCompanyNameText.setText(((Outsourced)selectedPart).getCompanyName());
    }

    PartID.setText(String.valueOf(selectedPart.getId()));
    PartNameText.setText(selectedPart.getName());
    PartInvText.setText(String.valueOf(selectedPart.getStock()));
    PartPriceCostText.setText(String.valueOf(selectedPart.getPrice()));
    PartMaxText.setText(String.valueOf(selectedPart.getMax()));
    PartMinText.setText(String.valueOf(selectedPart.getMin()));
    }
}
