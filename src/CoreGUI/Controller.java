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
    private NumberAxis xAxisPTV;
    @FXML
    private NumberAxis yAxisPTV;

    @FXML
    private ScatterChart priceToMeanDeltaChart;
    @FXML
    private NumberAxis xAxisPTMD;
    @FXML
    private NumberAxis yAxisPTMD;

    @FXML
    private ScatterChart pricePerUnitChart;
    @FXML
    private NumberAxis xAxisPPU;
    @FXML
    private NumberAxis yAxisPPU;

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
                    if(!results.isNull())
                    {
                        results = new Results();
                        database = new Database();
                        contentGrid.getChildren().clear();
                    }
                    database.update(products, results);
                    numberOfProducts = products.length;

                    // Calculate number of rows needed to display products
                    contentNumOfRows = (int)Math.ceil(numberOfProducts/contentNumOfCol);

                    this.prodPane = new GridPane[numberOfProducts];

                    setProductPane(prodPane);

                    Product[] top3Products = results.getTop3();

                    if(!productArrayList.isEmpty())
                    {
                        productArrayList.clear();
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

                    plotPriceToVolume();
                    plotPriceToMeanDelta();
                    plotPricePerUnit();
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
        if(!priceToVolumeChart.getData().isEmpty())
        {
            priceToVolumeChart.getData().clear();
        }

        Product[] fairpriceProd = database.filterSupermarket(products, "Fairprice");
        Product[] giantProd = database.filterSupermarket(products, "Giant");

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getVolume() - product1.getVolume());
            }
        });
        System.out.println("Most volume item: " + products[0].getVolume());

        xAxisPTV.setUpperBound(Math.ceil(products[0].getVolume()));
        if(products[1] instanceof aboveOneKilogram)
        {
            xAxisPTV.setLowerBound(1);
            xAxisPTV.setTickUnit(1);
        }
        else if(products[1] instanceof belowOneKilogram){
            System.out.println("volume: " + products[1].getTitle());
            xAxisPTV.setLowerBound(0);
            xAxisPTV.setTickUnit(10);
        }
        xAxisPTV.setLabel("Volume");
        xAxisPTV.setAutoRanging(false);

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getPrice() - product1.getPrice());
            }
        });

        yAxisPTV.setLowerBound(0);
        yAxisPTV.setUpperBound(Math.ceil(products[0].getPrice()));
        yAxisPTV.setTickUnit(5);
        yAxisPTV.setLabel("Price");
        yAxisPTV.setAutoRanging(false);

        priceToVolumeChart.setTitle("Price To Volume");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Fairprice");

        for(Product prod : fairpriceProd){
            series1.getData().add(new XYChart.Data(prod.getVolume(), prod.getPrice()));
        }

        XYChart.Series series2 = new XYChart.Series();

        series2.setName("Giant");

        for(Product prod : giantProd){
            series2.getData().add(new XYChart.Data(prod.getVolume(), prod.getPrice()));
        }

        priceToVolumeChart.getData().addAll(series1, series2);

    }

    public void plotPriceToMeanDelta() {
        if(!priceToMeanDeltaChart.getData().isEmpty()){
            priceToMeanDeltaChart.getData().clear();
        }

        priceToMeanDeltaChart.setTitle("Price to Mean Delta");

        Product[] fairpriceProd = database.filterSupermarket(products, "Fairprice");
        Product[] giantProd = database.filterSupermarket(products, "Giant");

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getMeanDelta() - product1.getMeanDelta());
            }
        });

        yAxisPTMD.setUpperBound(Math.ceil(products[0].getMeanDelta()));
        yAxisPTMD.setLowerBound(0);
        yAxisPTMD.setTickUnit(0.2);
        yAxisPTMD.setAutoRanging(false);
        yAxisPTMD.setLabel("Mean Delta");

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getPrice() - product1.getPrice());
            }
        });

        xAxisPTMD.setUpperBound(Math.ceil(products[0].getPrice()));
        xAxisPTMD.setLowerBound(0);
        xAxisPTMD.setTickUnit(10);
        xAxisPTMD.setAutoRanging(false);
        xAxisPTMD.setLabel("Price");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Fairprice");

        for(Product prod : fairpriceProd){
            series1.getData().add(new XYChart.Data(prod.getPrice(), prod.getMeanDelta()));
        }

        XYChart.Series series2 = new XYChart.Series();

        series2.setName("Giant");

        for(Product prod : giantProd){
            series2.getData().add(new XYChart.Data(prod.getPrice(), prod.getMeanDelta()));
        }

        priceToMeanDeltaChart.getData().addAll(series1, series2);

    }

    private void plotPricePerUnit(){
        if(!pricePerUnitChart.getData().isEmpty()) {
            pricePerUnitChart.getData().clear();
        }

        pricePerUnitChart.setTitle("PrICe pER uNIt ChaRt");

        Product[] fairpriceProd = database.filterSupermarket(products, "Fairprice");
        Product[] giantProd = database.filterSupermarket(products, "Giant");

        Arrays.sort(products, new Comparator<Product>() {
            public int compare(Product product1, Product product2) {
                return (int) (product2.getPricePerUnit() - product1.getPricePerUnit());
            }
        });


        xAxisPPU.setUpperBound(products[0].getPricePerUnit());
        xAxisPPU.setLowerBound(0);
        xAxisPPU.setTickUnit(1);
        xAxisPPU.setAutoRanging(false);

        xAxisPPU.setLabel("Price Per Unit");

        yAxisPPU.setUpperBound(2);
        yAxisPPU.setLowerBound(0);
        yAxisPPU.setTickUnit(1);
        yAxisPPU.setAutoRanging(false);

        yAxisPPU.setLabel("Unit");

        XYChart.Series series1 = new XYChart.Series();

        series1.setName("Fairprice");

        for(Product prod : fairpriceProd){
            series1.getData().add(new XYChart.Data(prod.getPricePerUnit(), 1));
        }

        XYChart.Series series2 = new XYChart.Series();

        series2.setName("Giant");

        for(Product prod : giantProd){
            series2.getData().add(new XYChart.Data(prod.getPricePerUnit(), 1));
        }

        pricePerUnitChart.getData().addAll(series1, series2);
    }

}


