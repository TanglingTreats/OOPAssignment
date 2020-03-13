package DataAnalysis;

import java.lang.Math;

public class DataCruncher
{
	private double mean = 0;
	private double variance = 0;
	private double SD = 0;


	public void crunch(double cumulativePrice, int count, Product[] chosenProducts, Results results)
	{
		mean = cumulativePrice / count;

		for (Product product : chosenProducts)
		{
			variance += Math.pow((product.getPricePerUnit() - mean), 2);
			if (product.getBrand().equals(results.getEconomicalBrand()))
			{
				if (results.economicalIsNull())
				{
					results.setLowestEconomical(product.getPricePerUnit());
					results.setHighestEconomical(product.getPricePerUnit());
				} else
				{
					if (product.getPricePerUnit() < results.getLowestEconomical())
						results.setLowestEconomical(product.getPricePerUnit());
					else if (product.getPricePerUnit() > results.getHighestEconomical())
						results.setHighestEconomical(product.getPricePerUnit());
				}
			}

			if (product.getBrand().equals(results.getExpensiveBrand()))
			{
				if (results.expensiveIsNull())
				{
					results.setLowestExpensive(product.getPricePerUnit());
					results.setHighestExpensive(product.getPricePerUnit());
				} else
				{
					if (product.getPricePerUnit() < results.getLowestExpensive())
						results.setLowestExpensive(product.getPricePerUnit());
					else if (product.getPricePerUnit() > results.getHighestExpensive())
						results.setHighestExpensive(product.getPricePerUnit());
				}
			}
		}

		SD = Math.sqrt(variance / count);
		results.setStatistics(new double[]{mean, SD});
	}
//	Brand that caters to the biggest group
//	tag delta between mean and product's price to each product
//	calculate price per SD difference
}
