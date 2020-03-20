package DataAnalysis;

import java.awt.*;
import java.util.Arrays;
import java.util.Hashtable;

public class Database
{
	private int uniqueBrands = 0;
	private String[] brands;

	private Hashtable<String, Color> brandColors = new Hashtable<String, Color>();

	public Database()
	{
//        empty constructor
	}

	public void clear()
	{
//		This resets the database, so that the object can be reused, instead of creating a new one each run
		uniqueBrands = 0;
		brandColors.clear();
	}

	public Product[] update(Product[] products, Results results)
	{
		DataCruncher bigboy = new DataCruncher();
//		products = bigboy.removeOutliers(products, "Volume");
		products = bigboy.removeOutliers(products, "Price");

//		initialising values, for the calculation of mean in the future
		int count = products.length;
		double cumulativePrice = 0;
//		keeping track of brands, only unique brands can come in
		brands = new String[products.length];

//		product weight, threshold is set at 25,
//		because the heaviest common product in our test scenarios is rice, at 25kg.
//		Anything higher than 25 is most likely in grams, and hence their weight will be normalised to 1000, instead of 1
		int productWeight = products[0].getVolume() <= 25 ? 1 : 0;

//		if the product weight is set to 0,
//		filter out any products that have weight above 1000
//		because the target products is supposed to be measured in grams
//		This will prevent issues where majority of the data is in kg, and the rare few is in grams.
		if (productWeight == 0)
		{
			products = Arrays.stream(products).filter(elem -> elem.getVolume() < 1000).toArray(Product[]::new);
		}

//		Normalising product's price to their weight. Target is 1kg or 1000grams.
		for (Product product : products)
		{
			switch (productWeight)
			{
				case 0:
					BelowOneKilogram belowOneKilogram = (BelowOneKilogram) product;
					belowOneKilogram.setPricePerUnit();
					cumulativePrice += belowOneKilogram.getPricePerUnit();
					break;
				case 1:
					AboveOneKilogram aboveOneKilogram = (AboveOneKilogram) product;
					aboveOneKilogram.setPricePerUnit();
					cumulativePrice += aboveOneKilogram.getPricePerUnit();
					break;
			}

//			If brand is not in string array, add to the array.
			if (!brandExist(product.getBrand()))
			{
				brands[uniqueBrands] = product.getBrand();
				uniqueBrands++;
			}

//          Technically, the following portion is calculations, which should be put in dataCruncher.
//			It is however put in this loop to lower the total number of loops needed for calculations.
//			This is the linear sort to find the top 3 most economical and the least economical
			if (results.isNull())
			{
				results.initialise(product);
			} else if (results.compareLower(product))
			{
				results.newTop(product);
			}

			if (results.compareHigher(product))
				results.newExpensive(product);
		}

//		Creates colors based on number of brands. This updates the brandcolor hashtable
		generateColors();
//		this will do all the big calculations
		return bigboy.crunch(cumulativePrice, count, products, results, brandColors);
	}

	public static Product[] filterBrand(Product[] products, String brand)
	{
//		This function filters products by brand.
		Product[] filteredResults = null;
		int count = 0;
//		count number of products that have the brand
		for (Product product : products)
			if (product.getBrand().equals(brand))
				count++;
//		initialise an array with size: count
		filteredResults = new Product[count];

//		reset count, so that it can be used as an index throughout
		count = 0;
		for (Product product : products)
			if (product.getBrand().equals(brand))
			{
				filteredResults[count] = product;
				count++;
			}
		return filteredResults;
	}

	public static Product[] filterSupermarket(Product[] products, String supermarket)
	{
//		This function filters products by supermarket
//		It functions similarly to the previous method
//		This was written so that we can potentially show the distribution of prices in the supermarkets
//		But time was short so we dropped this feature
		Product[] filteredResults = null;
		int count = 0;
		for (Product product : products)
			if (product.getSupermarket().equals(supermarket))
				count++;
		filteredResults = new Product[count];

		count = 0;
		for (Product product : products)
			if (product.getSupermarket().equals(supermarket))
			{
				filteredResults[count] = product;
				count++;
			}
		return filteredResults;
	}


	private boolean brandExist(String brand)
	{
//		This method is a linear search that checks if the brand already exists in the array
		boolean exist = false;
		for (int i = 0; i < uniqueBrands; i++)
		{
			if (brands[i].equals(brand))
			{
				exist = true;
				break;
			}
		}
//		this will return false if the inner conditional statement does not pass
		return exist;
	}

	private void generateColors()
	{
//		divides the color circle by number of unique brands,
//		to get a float number that i can increase the value by every iteration
		float interval = 360.0f / uniqueBrands;
		for (int i = 0; i < uniqueBrands; i++)
			brandColors.put(brands[i], Color.getHSBColor(i * interval / 360, 1, 1));

	}

//	getters
	public int getUniqueBrands()
	{
		return uniqueBrands;
	}

	public String[] getBrands()
	{
		return brands;
	}
}
