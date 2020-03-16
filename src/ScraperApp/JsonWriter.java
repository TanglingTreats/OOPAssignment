package ScraperApp;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileWriter;
import java.io.IOException;

public class JsonWriter
{
	public static void writeJson() throws IOException
	{
		JSONObject objects;
		JSONArray arrays = new JSONArray();
		for (Object a : Scraper.productsList)
		{
			objects = new JSONObject();
			ScraperProduct obj = (ScraperProduct) a;
			objects.put("url", obj.url);
			objects.put("img", obj.image);
			objects.put("name", obj.title);
			objects.put("brand", obj.brand);
			objects.put("category", obj.category);
			objects.put("supermarket", obj.supermarket);
			objects.put("supermarketImg", obj.supermarketImage);
			objects.put("vol", obj.volume);
			objects.put("price", obj.price);
			arrays.put(objects);
		}

		JSONObject data = new JSONObject().put("data", arrays);

		String rootFolder = System.getProperty("user.dir") + "/src/Data/data.json";
		FileWriter writer = new FileWriter(rootFolder);
		writer.write(data.toString());
		writer.close();
	}
}
