package ScraperApp;

import DataAnalysis.Product;
import DataAnalysis.Sanitiser;
import DataAnalysis.Soap;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonFileHandler {
    public JsonFileHandler() {
        // Empty constructor
    }

    public void CreateFile(String fileName, JSONObject obj) {

    }

    public void UpdateFile(String fileName, JSONObject obj) {

    }

    public Product[] readFile() {
        JSONParser parser = new JSONParser();
        try {
            JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/src/Data/data.json"));
            // A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.

            JSONArray jsonArray = (JSONArray) jsonObject.get("data");

            Product[] array = new Product[jsonArray.size()];
            int position = 0;

            for (JSONObject test : (Iterable<JSONObject>) jsonArray) {
                String category = (String) test.get("category");
                if (category.equals("bodywash"))
                {
                    Soap object = new Soap();
                    object.setTitle((String) test.get("name"));
                    object.setPrice((Double) test.get("price"));
                    object.setVolume((Long) test.get("vol"));
                    object.setBrand((String) test.get("brand"));
                    object.setSupermarket((String) test.get("supermarket"));
                    object.setSupermarketImg((String) test.get("supermarketImg"));
                    object.setLink((String) test.get("url"));
                    object.setLink((String) test.get("img"));

                    array[position] = object;
                    position++;
                }

                if (category.equals("sanitiser")) {
                    Sanitiser object = new Sanitiser();
                    object.setTitle((String) test.get("name"));
                    object.setPrice((Double) test.get("price"));
                    object.setVolume((Long) test.get("vol"));
                    object.setBrand((String) test.get("brand"));
                    object.setSupermarket((String) test.get("supermarket"));
                    object.setSupermarketImg((String) test.get("supermarketImg"));
                    object.setLink((String) test.get("url"));
                    object.setLink((String) test.get("img"));

                    array[position] = object;
                    position++;
                }
            }

            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Product[0];
    }
}