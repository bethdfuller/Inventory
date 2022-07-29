package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * Inventory Model
 */

public class Inventory {

    /**
     * All Parts/Products in Inventory
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * Get all parts/products - observable list
     * @return
     */
    public static ObservableList<Part> getAllParts(){return allParts;}
    public static ObservableList<Product> getAllProduct(){return allProducts;}

    /**
     * Add Part/Product
     * @param newPart
     */
    public static void addPart (Part newPart){
        allParts.add(newPart);
    }
    public static void addProduct (Product newProduct){allProducts.add(newProduct);}

    /**
     * Lookup PartID/ProductID/PartName/ProductName
     * PartID Lookup
     * @param partID
     * @return
     */
    public static Part lookupPart(int partID) {
        for(Part part: Inventory.getAllParts()) {
            while (part.getId() == partID) {
                return part;
            }
        }
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Part not found.");
        alert.show();
        return null;
    }

    /**
     * ProductID Lookup
     * @param productID
     * @return
     */
    public static Product lookupProduct(int productID) {
        for (Product product: Inventory.getAllProduct()){
            while (product.getId() == productID)
                return product;
        }
        return null;
    }

    /**
     * Part Name Lookup
     * @param partName
     * @return
     */
    public static ObservableList<Part> lookupPart(String partName) {
        ObservableList<Part> PartName = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().contains(partName)) {
                PartName.add(part);
            }
        }
        return PartName;
    }

    /**
     * Product Name Lookup
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName){
        ObservableList<Product> ProductName = FXCollections.observableArrayList();

        for (Product product: allProducts) {
            if (product.getName().contains(productName)) {
                ProductName.add(product);
            }
        }
        return ProductName;
    }

    /**
     * Update Part/Product
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }
    public static void updateProduct(int index, Product selectedProduct) {
        allProducts.set(index, selectedProduct);
    }

    /**
     * Delete part/product
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        if (allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
            return true;
        } else {
            return false;
        }
    }

    public static boolean deleteProduct (Product selectedProduct){
        if (allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        } else {
            return true;
        }
    }

}