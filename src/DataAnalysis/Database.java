package DataAnalysis;

public class Database
{
	DataCruncher bigBoy = new DataCruncher();

	public Database()
	{
//        empty constructor
	}

	public void update(Product[] products, Results results)
	{
		int count = 0;
		double cumulativePrice = 0;

		int productWeight = products[0].getVolume() <= 10 ? 1 : 0;

		for (Product product : products)
		{
			switch (productWeight)
			{
				case 0:
					belowOneKilogram belowOneKilogram = (belowOneKilogram) product;
					belowOneKilogram.setPricePerUnit();
					cumulativePrice += belowOneKilogram.getPricePerUnit();
					count++;
					break;
				case 1:
					aboveOneKilogram aboveOneKilogram = (aboveOneKilogram) product;
					aboveOneKilogram.setPricePerUnit();
					cumulativePrice += aboveOneKilogram.getPricePerUnit();
					count++;
					break;
			}

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

		bigBoy.crunch(cumulativePrice, count, products, results);
	}

	public static Product[] filter(Product[] products,String brand)
	{
		Product[] filteredResults = null;
		int count = 0;
		for (Product product : products)
			if (product.getBrand().equals(brand))
				count++;
		filteredResults = new Product[count];
		count = 0;
		for (Product product : products)
			if (product.getBrand().equals(brand))
			{
				filteredResults[count] = product;
				count++;
			}
		return filteredResults;
	}
}
