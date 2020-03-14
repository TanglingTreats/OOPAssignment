package DataAnalysis;

import java.awt.*;
import java.lang.Math;
import java.util.Dictionary;

public class DataCruncher
{
	public void crunch(double cumulativePrice, int count, Product[] chosenProducts, Results results, Dictionary<String, Color> brandColors)
	{
		double mean = cumulativePrice / count;
		double variance = 0;

		for (Product product : chosenProducts)
		{
			product.setColor(brandColors.get(product.getBrand()));
			product.setMeanDelta(product.getPricePerUnit() - mean);

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

		double SD = Math.sqrt(variance / count);
		results.setStatistics(new double[]{mean, SD});
	}
//	calculate price per SD difference
}
