
/**
 * Author: Beth Fuller
 */
package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * The Main class creates the inventory management java application that manages parts and products. Products can contain multiple parts. Attributes for both parts and products that are managed include ID, price, stock and cost. <br>
 * FUTURE_ENHANCEMENT: An enhancement would be adding photographs on the main screen to Parts/Products. <br>
 * FUTURE_ENHANCEMENT: An enhancement would be adding a Sold screen that also shows a smaller summary on the Main screen that includes all products sold - including date, customer, etc.<br>
 * FUTURE_ENHANCEMENT: An enhancement would be adding a Customer screen that is searchable from the Main screen that shows particular customers purchase history and also captured information for future marketing endeavors.<br>
 * FUTURE_ENHANCEMENT: An enhancement would be making all search boxes (Main Screen, Add Product, Modify Product) not case-sensitive.
 */

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainScreen.fxml"));
        stage.setTitle("Inventory Management System");
        stage.setScene(new Scene(root,900,500));
        stage.show();
    }

    public static void main(String[] args) {

        Part string = new InHouse(100, "String", 7.99, 10, 0, 20, 179);
        Inventory.addPart(string);

        Part clasp = new InHouse(101, "Clasp", 8.99, 10, 0, 20, 180);
        Inventory.addPart(clasp);

        Part wire = new InHouse(102, "Wire", 9.99, 10, 0, 20, 181);
        Inventory.addPart(wire);

        Part blueBead = new Outsourced(104, "Blue Beads", 10.99, 10, 0, 20, "Miyuki");
        Inventory.addPart(blueBead);

        Part goldBead = new Outsourced(105, "Gold Beads", 10.99, 10, 0, 20, "Miyuki");
        Inventory.addPart(goldBead);

        Part greenBead = new Outsourced(106, "Green Beads", 10.99, 10, 0, 20, "Miyuki");
        Inventory.addPart(greenBead);

        Product necklace = new Product(107, "Necklace", 100, 10, 0,10);
        Inventory.addProduct(necklace);

        Product bracelet = new Product(108, "Bracelet", 100, 10, 0,10);
        Inventory.addProduct(bracelet);

        Product earrings = new Product(109, "Earrings", 100, 10, 0,10);
        Inventory.addProduct(earrings);

        launch(args);
    }
}
