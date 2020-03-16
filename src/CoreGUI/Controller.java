package CoreGUI;

import java.awt.*;

import java.awt.color.ProfileDataException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

import ScraperApp.Scraper;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;

import DataAnalysis.Database;
import DataAnalysis.Product;
import DataAnalysis.Results;
import ScraperApp.JsonFileHandler;

import DataAnalysis.*;

public class Controller {
    // Properties

    private final int numberOfRows = 3;
    private final float contentNumOfCol = 3.0f;
    private int contentNumOfRows = 3;
    private int numberOfProducts = 0;
    private float productHeight = 600f;

    @FXML
    private TextField searchText;

    @FXML
    private GridPane contentGrid;
    @FXML
    private GridPane statsGrid;

    @FXML
    private Text welcomeTxt;

    @FXML
    private Text invalidTxt;

    @FXML
    private ScatterChart priceToVolumeChart;
    @FXML
    private ScatterChart priceToMeanDeltaChart;
    @FXML
    private ScatterChart pricePerUnitChart;

    @FXML
    private Pane root;

    @FXML
    private AnchorPane anchorPane;

    private GridPane[] prodPane = new GridPane[numberOfProducts];

    private Text keyword1 = new Text("Sanitiser");
    private Text keyword2 = new Text("Bodywash");
    private Text keyword3 = new Text("Wheat Biscuits");
    private Text keyword4 = new Text("Detergent");

    private TextFlow popularKeywords = new TextFlow(keyword1, keyword2, keyword3);

    private JsonFileHandler fileHandler;
    private Database database;
    private Results results;
    private Product[] products;

    private ArrayList<Product> productArrayList = new ArrayList<Product>();

    public void initialize() {
        System.out.println("Initialize ran!");

        fileHandler = new JsonFileHandler();
        database = new Database();
        results = new Results();
        products = null;

        initialiseEventListeners();
    }

    public void initialiseContentGrid(int rowsNeeded) {
        int currentNumOfRows = contentGrid.getRowCount();

        for(int i = currentNumOfRows; i < rowsNeeded; i++) {
            this.contentGrid.addRow(i);
        }
        this.contentGrid.setHgap(10);
        this.contentGrid.setVgap(20);

        this.contentGrid.setPadding(new Insets(5, 10, 10, 10));

        this.anchorPane.setPrefHeight((300 * rowsNeeded ) + (this.contentGrid.getVgap() * rowsNeeded) + 200);

        //System.out.println("Get row count:" + contentGrid.getRowCount());

        for(int i = 0; i < currentNumOfRows; i++) {
            this.contentGrid.getRowConstraints().add(new RowConstraints(300));
        }

    }

    public void initialiseEventListeners()
    {

        searchText.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {

                String input = searchText.getText().toLowerCase();

                if(input.isBlank() || input.isEmpty() || input == " " || input == ""){
                    if(!invalidTxt.isVisible()) {
                        invalidTxt.setVisible(true);
                    }
                    if(welcomeTxt.isVisible()) {
                        welcomeTxt.setVisible(false);
                    }
                }
                else if(!input.isEmpty() || !input.isBlank()){

                    if(invalidTxt.isVisible()) {
                        invalidTxt.setVisible(false);
                    }
                    if(welcomeTxt.isVisible()) {
                        welcomeTxt.setVisible(false);
                    }
                    System.out.println("Input in lowercase: " + input);

                    try {
                        Scraper.scrape(input);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    products = fileHandler.readFile(input, products);

                    System.out.println("Is there products " + products.length);
                    database.update(products, results);
                    numberOfProducts = products.length;

                    // Calculate number of rows needed to display products
                    contentNumOfRows = (int)Math.ceil(numberOfProducts/contentNumOfCol);

                    this.prodPane = new GridPane[numberOfProducts];

                    setProductPane(prodPane);

                    //database.compare(fileHandler.readFile(input, products), input, results);

                    Product[] top3Products = results.getTop3();

                    Product[] filtered = database.filter(products, results.getTop3()[2].getBrand());

                    if(!productArrayList.isEmpty())
                    {
                        productArrayList.clear();
                        this.contentGrid = new GridPane();
                    }

                    for (Product x :products) {
                        productArrayList.add(x);
                    }

                    System.out.println(results.getExpensive().getBrand());
                    // Accept input here and pass it in to a function
                    // convert query into lowercase letters
                    insertItemsIntoPane(prodPane, productArrayList);
                    setGridPane(prodPane);
                    initialiseContentGrid(contentNumOfRows);
                }
            }
        });
    }

    // Initialise grid panel for products to be stored in.
    public void setProductPane(GridPane[] prodPane) {
        for(int i = 0; i < numberOfProducts; i++) {
            GridPane panel = new GridPane();
            panel.setAlignment(Pos.CENTER);

            for(int j = 0; j < numberOfRows; j++) {
                RowConstraints rowConst = new RowConstraints();

                rowConst.setValignment(VPos.CENTER);
                panel.getRowConstraints().add(rowConst);

            }
            ColumnConstraints columnConst = new ColumnConstraints();
            columnConst.setHalignment(HPos.CENTER);
            panel.getColumnConstraints().add(columnConst);

            panel.setMaxHeight(productHeight);
            panel.getStyleClass().add("productPane");
            panel.setVgap(5.0);
            panel.setPrefHeight(360);
            prodPane[i] = panel;
        }
    }

    // Adds images and texts according to the top 3 products
    public void insertItemsIntoPane(GridPane[] prodPane, ArrayList<Product> testProducts) {

        int counter = 0;
        for(int i = 0; i < numberOfProducts; i++) {
            for(int j = 0; j < numberOfRows; j++) {
                TextFlow newTitle;
                Text toPushText;

                switch (j)
                {
                    case 0:
                        if(testProducts.get(counter).getImage() != null || !testProducts.get(counter).getImage().isEmpty() || !testProducts.get(counter).getImage().isBlank()) {
                            Image image = new Image(testProducts.get(counter).getImage(), 0, productHeight / numberOfRows-1, true, true, true);
                            ImageView iv1 = new ImageView();
                            iv1.setImage(image);
                            prodPane[i].add(iv1, 0, j);
                        }

                        break;
                    case 1:
                        toPushText = new Text(testProducts.get(counter).getTitle());
                        newTitle = new TextFlow(toPushText);
                        newTitle.setTextAlignment(TextAlignment.CENTER);

                        prodPane[i].add(newTitle, 0, j);
                        break;
                    case 2:
                        toPushText = new Text(String.format("$%.2f", testProducts.get(counter).getPrice()));
                        newTitle = new TextFlow(toPushText);
                        newTitle.setTextAlignment(TextAlignment.CENTER);
                        prodPane[i].add(newTitle, 0, j);
                        break;
                }

            }
            counter++;
        }

    }

    // Adds product panels into Grid Pane
    public void setGridPane(GridPane[] prodPane) {
        int k = 0;
        for(int i = 0; i < contentNumOfRows; i++) {
            for(int j = 0; j < contentNumOfCol; j++) {
                if(k == numberOfProducts) {
                    break;
                }
                contentGrid.add(prodPane[k], j, i);
                k++;
            }
        }
    }

    //Plot points on the scatter chart
    public void plotPriceToVolume() {

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getVolume() - product1.getVolume());
            }
        });

        NumberAxis xAxis = new NumberAxis(0, products[0].getVolume(), 1);
        xAxis.setLabel("Volume");

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getPrice() - product1.getPrice());
            }
        });
        NumberAxis yAxis = new NumberAxis(0, products[0].getPrice(), 1);
        yAxis.setLabel("Price");

        priceToVolumeChart = new ScatterChart<Number, Number>(xAxis, yAxis);
        priceToVolumeChart.setTitle("Price To Volume");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Fairprice");
        


    }

}


