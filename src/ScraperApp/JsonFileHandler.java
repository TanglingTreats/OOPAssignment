package ScraperApp;

import DataAnalysis.*;

import org.json.simple.*;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;

public class JsonFileHandler
{

	public JsonFileHandler()
	{
		// Empty constructor
	}

	public void CreateFile(String fileName, JSONObject obj)
	{

	}

	public void UpdateFile(String fileName, JSONObject obj)
	{

	}

	public Product[] readFile(String productName, Product[] chosenProducts)
	{
		JSONParser parser = new JSONParser();
		try
		{
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/src/Data/data.json"));
			// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.

			JSONArray jsonArray = (JSONArray) jsonObject.get("data");


			int position = 0;
			int productCount = 0;
			int productWeight = -1;

			for (JSONObject product : (Iterable<JSONObject>) jsonArray)
			{
				if (productName.equals(product.get("category")))
				{
					productCount++;
					productWeight = productWeight == -1 ? Double.parseDouble(product.get("vol").toString()) <= 25 ? 1 : 0 : productWeight;
				}
			}

			chosenProducts = new Product[productCount];


			for (JSONObject product : (Iterable<JSONObject>) jsonArray)
			{
				if (productName.equals(product.get("category")))
				{
					Product object = null;
					switch (productWeight)
					{
						case 0:
							object = new belowOneKilogram();
							break;
						case 1:
							object = new aboveOneKilogram();
							break;
					}

					object.setTitle((String) product.get("name"));
					object.setPrice(Double.parseDouble(product.get("price").toString()));
					object.setVolume(Double.parseDouble(product.get("vol").toString()));
					object.setBrand((String) product.get("brand"));
					object.setSupermarket((String) product.get("supermarket"));
					object.setSupermarketImg((String) product.get("supermarketImg"));
					object.setLink((String) product.get("url"));
					object.setImage((String) product.get("img"));

					chosenProducts[position] = object;
					position++;
				}
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return chosenProducts;
	}
}
