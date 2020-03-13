package ScraperApp;

import DataAnalysis.Product;
import DataAnalysis.Sanitiser;
import DataAnalysis.Bodywash;

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

	public Product[] readFile()
	{
		JSONParser parser = new JSONParser();
        Product[] array = null;
		try
		{
			JSONObject jsonObject = (JSONObject) parser.parse(new FileReader(System.getProperty("user.dir") + "/src/Data/data.json"));
			// A JSON object. Key value pairs are unordered. JSONObject supports java.util.Map interface.

			JSONArray jsonArray = (JSONArray) jsonObject.get("data");

			array = new Product[jsonArray.size()];
			int position = 0;

			for (JSONObject test : (Iterable<JSONObject>) jsonArray)
			{
				String category = (String) test.get("category");
				boolean valid = false;
				Product object = null;
				if (category.equals("bodywash"))
				{
					object = new Bodywash();
					valid = true;
				}

				if (category.equals("sanitiser"))
				{
					object = new Sanitiser();
					valid = true;
				}
				if (valid)
				{
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
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		return array;
	}
}