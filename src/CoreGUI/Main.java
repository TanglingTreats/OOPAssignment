package CoreGUI;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();
        Controller controller = new Controller();

        loader.setController(controller);
        Parent root = loader.load(getClass().getResource("mainPage.fxml"));

        Scene scene = new Scene(root, 1080, 720);

        scene.getStylesheets().add("page.css");


        primaryStage.setTitle("Price Comparison");
        primaryStage.setScene(scene);


        primaryStage.show();

    }


    public static void main(String[] args) {

//        String baseUrl = "https://www.fairprice.com.sg/search?query=sanitizer";
//
//        Scraper scraper = new Scraper();
//
//        scraper.getProduct(baseUrl);
        launch(args);
    }
}
