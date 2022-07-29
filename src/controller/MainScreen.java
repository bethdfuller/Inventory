package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
 * This class is the controller for the Main Screen
 */

public class MainScreen implements Initializable{

    private static Part partToModify;
    private static Product productToModify;

    Stage stage;
    Parent scene;

    /**
     * Part Table
     */
    @FXML private TableView<Part> PartTable;
    @FXML private TextField PartSearchText;
    @FXML private TableColumn<Part, Integer> PartIDCol;
    @FXML private TableColumn<Part, String> PartNameCol;
    @FXML private TableColumn<Part, Integer> PartInvLevelCol;
    @FXML private TableColumn<Part, Double> PartPriceCostCol;

    /**
     * Product Table
     */
    @FXML private TableView<Product> ProductTable;
    @FXML private TextField ProductSearchText;
    @FXML private TableColumn<Part, Integer> ProductIDCol;
    @FXML private TableColumn<Part, String> ProductNameCol;
    @FXML private TableColumn<Part, Integer> ProductInvCol;
    @FXML private TableColumn<Part, Double> ProductPriceCostCol;

    /**
     * Searches Parts table for Part
     * @param event
     */

    @FXML
    void PartSearch(ActionEvent event) {
        String searchString = PartSearchText.getText();
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
     * Add Part - takes user to Add Part screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionOpenAddPart(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddPart.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Modify Part - takes user to Modify Part screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionOpenModifyPart(ActionEvent event) throws IOException {

        partToModify = PartTable.getSelectionModel().getSelectedItem();

        if (partToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Part not found.");
            alert.showAndWait();
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyPart.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes selected Part
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionDeletePart(ActionEvent event) throws IOException {
        Part part = PartTable.getSelectionModel().getSelectedItem();
        if (PartTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No part is selected.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deletePart(part);
            }
            else {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
            }
        }
    }

    /**
     * Searches Product table for Product
     * @param event
     */
    @FXML
    void ProductSearch(ActionEvent event) {
        String searchProductString = ProductSearchText.getText();
        ObservableList<Product> productResults = Inventory.lookupProduct(searchProductString);
        try {
            while (productResults.size() == 0) {
                int ProductID = Integer.parseInt(searchProductString);
                productResults.add(Inventory.lookupProduct(ProductID));
            }
            ProductTable.setItems(productResults);
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product not found.");
            alert.showAndWait();
        }
    }

    /**
     * Add Product - takes user to Add Product screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionOpenAddProduct(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/AddProduct.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Modify Product - takes user to Modify Product screen
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionOpenModifyProduct(ActionEvent event) throws IOException {

        productToModify = ProductTable.getSelectionModel().getSelectedItem();

        if (productToModify == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Product not found.");
            alert.showAndWait();
        } else {
            Parent parent = FXMLLoader.load(getClass().getResource("/view/ModifyProduct.fxml"));
            Scene scene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }
    }

    /**
     * Deletes selected Product
     * @param event
     * @throws IOException
     */
    @FXML
    void OnActionDeleteProduct(ActionEvent event) throws IOException {
        Product product = ProductTable.getSelectionModel().getSelectedItem();
        if (ProductTable.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("No product is selected.");
            alert.showAndWait();
        }
        else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete the selected product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
                stage.setScene(new Scene(scene));
                stage.show();
                Inventory.deleteProduct(product);
                if (!product.getAllAssociatedParts().isEmpty()) {
                    Alert associatedAlert = new Alert(Alert.AlertType.WARNING);
                    associatedAlert.setTitle("Warning");
                    associatedAlert.setContentText("This product has at least one associated part.");
                    associatedAlert.showAndWait();
                } else {
                    Inventory.deleteProduct(product);
                }
            }
        }
    }

    /**
     * Exit Application
     * @param event
     */

    @FXML
    void OnActionExitApplication(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Gets Part/Product selected object
     * @return
     */
    public static Part getPartToModify() {return partToModify;}
    public static Product getProductToModify() {return productToModify;}

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        /**
         * Parts table columns
         */
        PartTable.setItems(Inventory.getAllParts());
        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        /**
         * Product table columns
         */
        ProductTable.setItems(Inventory.getAllProduct());
        ProductIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        ProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        ProductInvCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        ProductPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

    }
}