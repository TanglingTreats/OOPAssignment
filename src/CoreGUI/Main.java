package CoreGUI;

import DataAnalysis.Database;
import DataAnalysis.Product;
import DataAnalysis.Results;
import ScraperApp.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{
		FXMLLoader loader = new FXMLLoader();
		Parent root = loader.load(getClass().getResource("newPage.fxml"));
		Scene scene = new Scene(root);

		Controller controller = new Controller();
		loader.setController(controller);

		scene.getStylesheets().add(getClass().getResource("page.css").toExternalForm());

		primaryStage.setTitle("Get The Giant Fair-Price!");
		primaryStage.setScene(scene);
		primaryStage.show();





	}


	public static void main(String[] args) throws IOException
	{

//        String baseUrl = "https://www.fairprice.com.sg/search?query=sanitizer";
//
//        Scraper scraper = new Scraper();
//
//        scraper.getProduct(baseUrl);
       launch(args);

//        testing
		Scraper.scrape("bodywash");
		Database database = new Database();
		Results results = new Results();
		Product[] products = null;

		products = new JsonFileHandler().readFile("bodywash", products);
		database.update(products, results);

//		for (Product x : products)
//		{
//			System.out.println(x.getColor());
//		}

		Product[] filtered = database.filter(products, results.getTop3()[0].getBrand());

		for (Product x : filtered)
		{
			System.out.println(x.getColor());
		}

	}
}

