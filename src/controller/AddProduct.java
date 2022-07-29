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
 * This class is the controller for the Add Product screen
 */

public class AddProduct implements Initializable{

    Stage stage;
    Parent scene;

    public static int productId = 1;

    private ObservableList<Part> associatedParts = FXCollections.observableArrayList();

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
    @FXML private TableColumn<Part, Integer> PartIDCol;
    @FXML private TableColumn<Part, String> PartNameCol;
    @FXML private TableColumn<Part, Integer> PartInvLevelCol;
    @FXML private TableColumn<Part, Double> PartPriceCostCol;

    /**
     * Associated Part Table
     */
    @FXML private TableView<Part> AssocPartTable;
    @FXML private TableColumn<Part, Integer> AssocPartIDCol;
    @FXML private TableColumn<Part, String> AssocPartNameCol;
    @FXML private TableColumn<Part, Integer> AssocPartInvLevelCol;
    @FXML private TableColumn<Part, Double> AssocPartPriceCostCol;

    /**
     * FUTURE_ENHANCEMENT: Add photo of part
     * @return
     */

    /**
     * Get unique product ID
     * @return
     */
    public static int getProductID() {
        productId++;
        return productId;
    }

    /**
     * Search part
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
     * Adds part to product
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
     * Removes Part from product
     * @param event
     */
    @FXML void OnActionRemovePart(ActionEvent event) {
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
     * Saves product button
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
            } else if (max < min) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "The product maximum must be greater than the part minimum.");
                alert.showAndWait();

            } else if (stock < min || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Product inventory must be between part minimum and maximum");
                alert.showAndWait();
            }
            else {
                Product product = new Product(id, name, price, stock, min, max);

                for (Part part : associatedParts) {
                    if (part != associatedParts)
                        product.addAssociatedPart(part);
                }
                Inventory.getAllProduct().add(product);
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
     * Cancel product button and returns user to main screen
     * @param event
     * @throws IOException
     */
    @FXML void OnActionReturnMainScreen(ActionEvent event) throws IOException {
        stage = (Stage)((Button)event.getSource()).getScene().getWindow();
        scene = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setScene(new Scene(scene));
        stage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        PartTable.setItems(Inventory.getAllParts());
        PartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        PartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        PartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        PartPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        AssocPartTable.setItems(Inventory.getAllParts());
        AssocPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        AssocPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        AssocPartInvLevelCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        AssocPartPriceCostCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productId = getProductID();
        ProductID.setText(String.valueOf(productId));
    }
}

