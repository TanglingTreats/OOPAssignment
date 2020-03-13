package DataAnalysis;

public class Database
{
	//    Returns top 3 most worth it
	//    Mean, SD
	//    Least worth it

	private double cumulativePrice = 0;
	private int count = 0;

	private Product[] chosenProducts = null;

	DataCruncher bigBoy = new DataCruncher();

	public Database()
	{
//        empty constructor
	}

//	Consider splitting this into multiple methods, ie Filter by Products,
	public Results compare(Product[] products, String option, Results results)
	{

		for (Product product : products)
		{
			boolean checkThis = false;
			if (option.equals("bodywash") && product instanceof Bodywash)
			{
				Bodywash bodywash = (Bodywash) product;
				bodywash.setPricePerUnit();
				cumulativePrice += bodywash.getPricePerUnit();
				count++;
				checkThis = true;
			}
			if (option.equals("sanitiser") && product instanceof Sanitiser)
			{
				Sanitiser sanitiser = (Sanitiser) product;
				sanitiser.setPricePerUnit();
				cumulativePrice += sanitiser.getPricePerUnit();
				count++;
				checkThis = true;
			}
			if (checkThis)
			{
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
		}

		if (option.equals("bodywash"))
			chosenProducts = new Bodywash[count];
		else if (option.equals("sanitiser"))
			chosenProducts = new Sanitiser[count];

		int index = 0;
		for (Product product : products)
		{
			if (option.equals("bodywash") && product instanceof Bodywash)
			{
				chosenProducts[index] = (Bodywash) product;
				index++;
			}
			if (option.equals("sanitiser") && product instanceof Sanitiser)
			{
				chosenProducts[index] = (Sanitiser) product;
				index++;
			}
		}

		bigBoy.crunch(cumulativePrice, count, chosenProducts, results);
		return results;
	}

	public Product[] filter(String brand)
	{
		Product[] filteredResults = null;
		int count = 0;
		for (Product product : chosenProducts)
			if (product.getBrand().equals(brand))
				count++;
		filteredResults = new Product[count];
		count = 0;
		for (Product product : chosenProducts)
			if (product.getBrand().equals(brand))
			{
				filteredResults[count] = product;
				count++;
			}
		return filteredResults;
	}
}
