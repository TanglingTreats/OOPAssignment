package CoreGUI;

import DataAnalysis.Database;
import DataAnalysis.Product;
import DataAnalysis.Results;
import ScraperApp.JsonFileHandler;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

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


	public static void main(String[] args)
	{

//        String baseUrl = "https://www.fairprice.com.sg/search?query=sanitizer";
//
//        Scraper scraper = new Scraper();
//
//        scraper.getProduct(baseUrl);
        launch(args);

//        testing
		JsonFileHandler test = new JsonFileHandler();
		Database database = new Database();
		Results results = new Results();
		database.compare(test.readFile(), "bodywash", results);
		Product[] filtered = database.filter(results.getTop3()[2].getBrand());

		for (Product x :results.getTop3())
            System.out.println(x.getBrand());
		System.out.println(results.getExpensive().getBrand());

		System.out.println(results.getLowestEconomical() + "," + results.getHighestEconomical() + "," + results.getEconomicalBrand());
		System.out.println(results.getLowestExpensive() + "," + results.getHighestExpensive() + "," + results.getExpensiveBrand());

		System.out.println(filtered.length);
		for (Product x : filtered)
			System.out.println(x.getBrand());
	}
}
