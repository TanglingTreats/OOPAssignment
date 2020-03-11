package CoreGUI;

import DataAnalysis.Comparison;
import DataAnalysis.Product;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import ScraperApp.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
    }


    public static void main(String[] args) {

//        String baseUrl = "https://www.fairprice.com.sg/search?query=sanitizer";
//
//        Scraper scraper = new Scraper();
//
//        scraper.getProduct(baseUrl);

//        testing
        JsonFileHandler test = new JsonFileHandler();
        Comparison comparison = new Comparison();
        Product first = comparison.compare(test.readFile(), "Sanitiser")[0];
        Product second = comparison.compare(test.readFile(), "Sanitiser")[1];
        Product third = comparison.compare(test.readFile(), "Sanitiser")[2];

        System.out.println(first.getTitle());
        System.out.println(second.getTitle());
        System.out.println(third.getTitle());
    }
}
