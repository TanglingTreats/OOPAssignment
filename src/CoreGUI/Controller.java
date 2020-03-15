package CoreGUI;

import java.awt.*;

import java.awt.color.ProfileDataException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
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

    final int numberOfRows = 3;
    int numberOfProducts = 3;
    float productHeight = 560f;

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
    private Pane root;

    private GridPane[] prodPane = new GridPane[3];

    private Text keyword1 = new Text("Sanitiser");
    private Text keyword2 = new Text("Soap");
    private Text keyword3 = new Text("Toilet Roll");

    private TextFlow popularKeywords = new TextFlow(keyword1, keyword2, keyword3);

    private JsonFileHandler fileHandler;
    private Database database;
    private Results results;
    private Product[] products;

    private ArrayList<Product> productArrayList = new ArrayList<Product>();

    private TestProduct test = new TestProduct("Thomasin McKenzie",
            "I wonder what it is",
            "https://i.pinimg.com/originals/51/c5/5f/51c55f4a4b1166e51abd7934b2d5ce24.png");

    public void initialize() {
        System.out.println("Initialize ran!");

//        productArrayList.add(new TestProduct("Thomasin McKenzie",
//                "I wonder what it is",
//                "https://i.pinimg.com/originals/51/c5/5f/51c55f4a4b1166e51abd7934b2d5ce24.png"));
//
//        productArrayList.add(new TestProduct("Elizabeth Olsen",
//                "Scarlet Witch The Sexy",
//                "https://static.tvtropes.org/pmwiki/pub/images/elizabeth_olsen_56.jpg"));
//
//        productArrayList.add(new TestProduct("Scarlet Johannson",
//                "Black Widow maaaaan",
//                "https://cdn.britannica.com/59/182359-050-C6F38CA3/Scarlett-Johansson-Natasha-Romanoff-Avengers-Age-of.jpg"));

        fileHandler = new JsonFileHandler();
        database = new Database();
        results = new Results();
        products = null;

        createProdBox();

        contentGrid.setHgap(10);
        contentGrid.setVgap(20);

        setProductPane(prodPane);

        initialiseEventListeners();

        System.out.println("Text field has this: "+ searchText.getText() );
    }

    public void createProdBox() {
        System.out.println("This method ran");
    }

    public void initialiseEventListeners()
    {
//        searchText.textProperty().addListener(((observableValue, oldValue, newValue) -> {
//            System.out.println("Textfield change from " + oldValue + " to " + newValue);
//        }));

        searchText.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                //System.out.println("The input text is:" + searchText.getText());
                System.out.println("The enter key was pressed!");

                String input = searchText.getText().toLowerCase();

                if(input.isBlank() || input.isEmpty() || input == " " || input == "") {
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

                    products = fileHandler.readFile(input, products);
                    database.update(products, results);

                    //database.compare(fileHandler.readFile(input, products), input, results);

                    Product[] top3Products = results.getTop3();

                    Product[] filtered = database.filter(products, results.getTop3()[2].getBrand());

                    for (Product x :top3Products) {
                        System.out.println("Product name: " + x.getTitle());
                        productArrayList.add(x);
                        System.out.println("Image url: " + x.getPrice());
                        System.out.println("Image url: " + x.getImage());
                    }
                    System.out.println("Product in array list: " + productArrayList.get(0).getTitle());
                    System.out.println("Product in array list: " + productArrayList.get(0).getImage());

                    System.out.println(results.getExpensive().getBrand());
                    // Accept input here and pass it in to a function
                    // convert query into lowercase letters
                    insertItemsIntoPane(prodPane, productArrayList);
                    setGridPane(prodPane);

                }
            }
        });
    }

    public void setProductPane(GridPane[] prodPane) {

        for(int i = 0; i < numberOfProducts; i++) {
            GridPane panel = new GridPane();
            panel.setAlignment(Pos.CENTER);

            for(int j = 0; j < numberOfRows; j++) {
                if(j == 0)
                {
                    RowConstraints rowConst = new RowConstraints();
                    rowConst.setMaxHeight(productHeight / numberOfRows-1);
                }

                RowConstraints rowConst = new RowConstraints();
                rowConst.setMaxHeight(productHeight / numberOfRows+1);
                rowConst.setValignment(VPos.CENTER);
                panel.getRowConstraints().add(rowConst);

            }
            ColumnConstraints columnConst = new ColumnConstraints();
            columnConst.setHalignment(HPos.CENTER);
            panel.getColumnConstraints().add(columnConst);

            panel.getStyleClass().add("productPane");

            prodPane[i] = panel;
            prodPane[i].setVgap(10.0);
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

                            System.out.println("Image URL: " + testProducts.get(counter).getImage());
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
        for(int i = 0; i < numberOfProducts; i++) {
            contentGrid.add(prodPane[i], i, 0);
        }
    }

}


