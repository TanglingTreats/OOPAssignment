package ScraperApp;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScraperProduct
{
	String url;
	String image;
	String title;
	String brand;
	String category;
	String supermarket;
	String supermarketImage;

	float volume;
	double price;

	public ScraperProduct(String url, String image, String title, String brand, String category, String supermarket, String supermarketImage, float volume, double price)
	{
		this.url = url;
		this.image = image;
		this.title = title;
		this.brand = brand;
		this.category = category;
		this.supermarket = supermarket;
		this.supermarketImage = supermarketImage;
		this.volume = volume;
		this.price = price;
	}


	// end of extra functions

	public float getVolume()
	{
		return volume;
	}

	public void setVolume(float volume)
	{
		this.volume = volume;
	}

	public double getPrice()
	{
		return price;
	}

	public void setPrice(double price)
	{
		this.price = price;
	}
}