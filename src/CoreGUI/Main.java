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
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

public class Main extends Application
{

	@Override
	public void start(Stage primaryStage) throws Exception
	{

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


	public static void main(String[] args) throws IOException
	{

//        String baseUrl = "https://www.fairprice.com.sg/search?query=sanitizer";
//
//        Scraper scraper = new Scraper();
//
//        scraper.getProduct(baseUrl);
//        launch(args);

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

		Arrays.sort(products, new Comparator<Product>() {
			public int compare(Product product1, Product product2) {
				return (int) (product1.getPricePerUnit() - product2.getPricePerUnit());
			}
		});

//		database.sort(products, 0, products.length - 1,"PricePerUnit");
//		Product[] filtered = database.filter(products, results.getTop3()[0].getBrand());

		for (Product x : products)
		{
			System.out.println(x.getPricePerUnit());
		}

	}
}

