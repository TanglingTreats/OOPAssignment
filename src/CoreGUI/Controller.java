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
import javafx.geometry.Rectangle2D;
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
import javafx.scene.text.TextFlow;
import javafx.event.ActionEvent;

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
    private Text welcomeTxt;

    @FXML
    private Pane root;

    private GridPane[] prodPane = new GridPane[3];

    private Text keyword1 = new Text("Sanitiser");
    private Text keyword2 = new Text("Soap");
    private Text keyword3 = new Text("Toilet Roll");

    private TextFlow popularKeywords = new TextFlow(keyword1, keyword2, keyword3);

    private ArrayList<Product> productArrayList = new ArrayList<Product>();

    private TestProduct test = new TestProduct("Thomasin McKenzie",
            "I wonder what it is",
            "https://i.pinimg.com/originals/51/c5/5f/51c55f4a4b1166e51abd7934b2d5ce24.png");

    public void initialize() {
        System.out.println("Initialize ran!");

        productArrayList.add(new TestProduct("Thomasin McKenzie",
                "I wonder what it is",
                "https://i.pinimg.com/originals/51/c5/5f/51c55f4a4b1166e51abd7934b2d5ce24.png"));

        productArrayList.add(new TestProduct("Elizabeth Olsen",
                "Scarlet Witch The Sexy",
                "https://static.tvtropes.org/pmwiki/pub/images/elizabeth_olsen_56.jpg"));

        productArrayList.add(new TestProduct("Scarlet Johannson",
                "Black Widow maaaaan",
                "https://cdn.britannica.com/59/182359-050-C6F38CA3/Scarlett-Johansson-Natasha-Romanoff-Avengers-Age-of.jpg"));

        createProdBox();

        contentGrid.setHgap(10);
        contentGrid.setVgap(20);

        setProductPane(prodPane);

        initialiseEventListeners();

        System.out.println("Text field has this: "+searchText.getText() );
    }

    public void createProdBox() {
        System.out.println("This method ran");
    }

    public void initialiseEventListeners()
    {
        searchText.textProperty().addListener(((observableValue, oldValue, newValue) -> {
            System.out.println("Textfield change from " + oldValue + " to " + newValue);
        }));

        searchText.setOnKeyPressed(keyEvent -> {
            if(keyEvent.getCode() == KeyCode.ENTER) {
                System.out.println("The input text is:" + searchText.getText());
                System.out.println("The enter key was pressed!");
                insertItemsIntoPane(prodPane, productArrayList);
                setGridPane(prodPane);
                if(welcomeTxt.isVisible()) {
                    welcomeTxt.setVisible(false);
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

                panel.getRowConstraints().add(rowConst);

            }
            ColumnConstraints columnConst = new ColumnConstraints();
            columnConst.setHalignment(HPos.CENTER);
            panel.getColumnConstraints().add(columnConst);

            panel.getStyleClass().add("productPane");

            prodPane[i] = panel;
        }
    }

    // Adds images and texts according to the top 3 products
    public void insertItemsIntoPane(GridPane[] prodPane, ArrayList<Product> testProducts) {

        int counter = 0;
        for(int i = 0; i < numberOfProducts; i++) {
            for(int j = 0; j < numberOfRows; j++) {
                Text stuff;

                switch (j)
                {
                    case 0:
                        Image image = new Image(testProducts.get(counter).getImage(), 0, productHeight / numberOfRows-1, true, true, true);
                        ImageView iv1 = new ImageView();
                        iv1.setImage(image);

                        System.out.println("Image URL: " + testProducts.get(counter).getImage());
                        prodPane[i].add(iv1, 0, j);

                        break;
                    case 1:
                        stuff = new Text(testProducts.get(counter).getTitle());

                        prodPane[i].add(stuff, 0, j);
                        break;
                    case 2:
                        stuff = new Text(testProducts.get(counter).getDesc());
                        prodPane[i].add(stuff, 0, j);
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


