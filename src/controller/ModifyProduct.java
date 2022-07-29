package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This class is the controller for the Modify Product Screen <br>
 * RUNTIME_ERROR: Runtime error occurred in that the part to be removed was not being selected from the appropriate table - was wanting user to select part from the Part Table & not the Associated Table to remove part. Now updated to select from the Associated Part table to remove a part.
 */

public class ModifyProduct implements Initializable {

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

    private Product selectedProduct;

    Stage stage;
    Parent scene;

    @FXML private Label ProductID;
    @FXML private TextField ProductNameText;
    @FXML private TextField ProductInvText;
    @FXML private TextField ProductPriceText;
    @FXML private TextField ProductMaxText;
    @FXML private TextField ProductMinText;

    @FXML private TextField PartSearch;

    /**
     * Part Table
     */
    @FXML private TableView<Part> PartTable;
    @FXML private TableColumn<?, ?> PartIDCol;
    @FXML private TableColumn<?, ?> PartNameCol;
    @FXML private TableColumn<?, ?> PartInvLevelCol;
    @FXML private TableColumn<?, ?> PartPriceCostCol;

    /**
     * Associated Part Table
     */
    @FXML private TableView<Part> AssocPartTable;
    @FXML private TableColumn<?, ?> AssocPartIDCol;
    @FXML private TableColumn<?, ?> AssocPartNameCol;
    @FXML private TableColumn<?, ?> AssocPartInvLevelCol;
    @FXML private TableColumn<?, ?> AssocPriceCostCol;

    /**
     * Searches Part table for Part
     * @param event
     */
    @FXML
    void onActionSearch(ActionEvent event){
        String searchString = PartSearch.getText();
        ObservableList<Part> results = Inventory.lookupPart(searchString);
        try {
            while (results.size() == 0) {
                int partId = Integer.parseInt(searchString);
                results.add(Inventory.lookupPart(partId));
            }
            PartTable.setItems(results);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part not found.");
            alert.showAndWait();
        }
    }

    /**
     * Adds selected part to product
     * @param event
     */
    @FXML void OnActionAddPart(ActionEvent event) {
        Part selectedPart = PartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert (Alert.AlertType.WARNING);
            alert.setTitle("Input error");
            alert.setContentText("Select a part from the list.");
            alert.showAndWait();
        } else {
            associatedParts.add(selectedPart);
            AssocPartTable.setItems(associatedParts);
        }
    }


    /**
     * Removes selected Part from product
     * @param event
     */
    @FXML
    void OnActionRemovePart(ActionEvent event) {
        Part selectedPart = AssocPartTable.getSelectionModel().getSelectedItem();
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input error");
            alert.setContentText("Select a part from the list.");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected part from the product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                associatedParts.remove(selectedPart);
                AssocPartTable.setItems(associatedParts);
            }
        }
    }

    /**
     * Saves Product button
     * @param event
     * @throws IOException
     */
    @FXML void OnActionSaveProduct(ActionEvent event) throws IOException{
        try {
            int id = Integer.parseInt(ProductID.getText());
            String name = ProductNameText.getText();
            int stock = Integer.parseInt(ProductInvText.getText());
            double price = Double.parseDouble(ProductPriceText.getText());
            int min = Integer.parseInt(ProductMinText.getText());
            int max = Integer.parseInt(ProductMaxText.getText());

            if (name.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The product name cannot be empty.");
                alert.showAndWait();
                return;

            } else if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The product maximum must be greater than the part minimum.");
                alert.showAndWait();
                return;

            } else if (stock < min || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product inventory must be between part minimum and maximum");
                alert.showAndWait();
                return;
            }
            else {
                Product modifiedProduct = new Product(id, name, price, stock, min, max);

                for (Part part : associatedParts) {
                    if (part != associatedParts)
                        modifiedProduct.addAssociatedPart(part);
                }

                Inventory.getAllProduct().add(modifiedProduct);
                Inventory.deleteProduct(selectedProduct);
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }

        } catch (NumberFormatException e) {
            PartTable.setItems(null);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Product form needs valid input type for all fields.");
            alert.showAndWait();
        }
    }

    /**
     * Cancel Product button returns user to Main Screen
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    selectedProduct = MainScreen.getProductToModify();
    associatedParts = selectedProduct.getAllAssociatedParts();

    PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    PartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    PartPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    PartTable.setItems(Inventory.getAllParts());

    AssocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
    AssocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
    AssocPartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
    AssocPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    AssocPartTable.setItems(associatedParts);

    ProductID.setText(String.valueOf(selectedProduct.getId()));
    ProductNameText.setText(selectedProduct.getName());
    ProductInvText.setText(String.valueOf(selectedProduct.getStock()));
    ProductPriceText.setText(String.valueOf(selectedProduct.getPrice()));
    ProductMaxText.setText(String.valueOf(selectedProduct.getMax()));
    ProductMinText.setText(String.valueOf(selectedProduct.getMin()));

    }

}