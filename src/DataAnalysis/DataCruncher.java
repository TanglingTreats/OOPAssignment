package DataAnalysis;

import java.awt.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Dictionary;

public class DataCruncher
{
	public Product[] crunch(double cumulativePrice, int count, Product[] products,
							Results results, Dictionary<String, Color> brandColors)
	{
//		this values are precalculated in database.update method,
//		so that i do not have to iterate through the array once more here
		double mean = cumulativePrice / count;
		double variance = 0;

		for (Product product : products)
		{
//			This sets the color of a product based on it's brand
//			The brandColors.get method is built in; it gets the value based on the key provided.
//			I am providing the product's brand here.
			product.setColor(brandColors.get(product.getBrand()));
//			This method sets the product's price difference from the mean.
//			MeanDelta does not really make sense but i did not want to give it such a long name
			product.setMeanDelta(product.getPricePerUnit() - mean);

//			calculation of variance, standard math things. This is summation portion of it
			variance += Math.pow((product.getPricePerUnit() - mean), 2);

//			This portion is setting the range for the economical brand
//			It is essentially a linear sort for the lowest and highest prices.

//			product.getBrand().equals(results.getEconomicalBrand())
//			will be true is the product's brand is the same as the most economical's brand
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

//			same thing as above, but for the most expensive brand
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

//		After the entire iteration, variance has been found, so we can calculate the SD here
		double SD = Math.sqrt(variance / count);
//		setting the statistics, mean and SD
		results.setStatistics(new double[]{mean, SD});
//		returns the products
		return products;
	}

	public Product[] removeOutliers(Product[] products, String option)
	{
		double Q1 = 0;
		double Q3 = 0;
		double IQR;
		int medianIndex, Q1Index, Q3Index;

//		Sort first, using the built in array.sort function, and a lambda call in the comparator.
		if (option.equals("Volume"))
		{
			Arrays.sort(products, new Comparator<Product>()
			{
				public int compare(Product product1, Product product2)
				{
					return (int) (product1.getVolume() - product2.getVolume());
				}
			});
		} else if (option.equals("Price"))
		{
			Arrays.sort(products, new Comparator<Product>()
			{
				public int compare(Product product1, Product product2)
				{
					return (int) (product1.getPrice() - product2.getPrice());
				}
			});
		}


//		if even length
		if (products.length % 2 == 0)
		{
//			if data set is even, halve the data set and find q1 and q3 as the middle of the two halves
//			get middle two numbers, sum and halve to find median

//			Q1 will always be at the 25th percentile of the product, hence this.
			Q1Index = (int) Math.ceil(products.length / 4.0);
//			Q3 will be at median + 25th percentile (75th percentile), hence this.
			Q3Index = ((products.length / 2) + 1) + (int) Math.floor(products.length / 4.0);

			if (option.equals("Volume"))
			{
//				index - 1 because array starts from 0
				Q1 = products[Q1Index - 1].getVolume();
				Q3 = products[Q3Index - 1].getVolume();
			} else if (option.equals("Price"))
			{
				Q1 = products[Q1Index - 1].getPrice();
				Q3 = products[Q3Index - 1].getPrice();
			}
		} else
		{
//        	data set is odd,
			medianIndex = (int) Math.ceil(products.length / 2.0);

			if (medianIndex % 2 == 0)
//        	if q1 and q3 are between even numbers
			{
//				same explanation as the above portions
				Q1Index = (int) Math.floor(medianIndex / 2.0);

				Q3Index = medianIndex + Q1Index;

				if (option.equals("Volume"))
				{
					Q1 = (products[Q1Index - 1].getVolume() + products[Q1Index].getVolume()) / 2;
					Q3 = (products[Q3Index - 1].getVolume() + products[Q3Index].getVolume()) / 2;
				} else if (option.equals("Price"))
				{
					Q1 = (products[Q1Index - 1].getPrice() + products[Q1Index].getPrice()) / 2;
					Q3 = (products[Q3Index - 1].getPrice() + products[Q3Index].getPrice()) / 2;
				}
			} else
//			if q1 and a3 are between odd numbers
			{
				Q1Index = (int) Math.ceil(products.length / 4.0);
				Q3Index = medianIndex + Q1Index;

				if (option.equals("Volume"))
				{
					Q1 = products[Q1Index - 1].getVolume();
					Q3 = products[Q3Index - 1].getVolume();
				} else if (option.equals("Price"))
				{
					Q1 = products[Q1Index - 1].getPrice();
					Q3 = products[Q3Index - 1].getPrice();
				}
			}
		}
		IQR = 1.5 * (Q3 - Q1);
		final double fQ1 = Q1;
		final double fQ3 = Q3;

//		lambda stream call to filter, if the items are between the IQR.
		if (option.equals("Volume"))
			products = Arrays.stream(products).filter(elem -> elem.getVolume() >= (fQ1 - IQR) && elem.getVolume() <= (fQ3 + IQR)).toArray(Product[]::new);
		else if (option.equals("Price"))
			products = Arrays.stream(products).filter(elem -> elem.getPrice() >= (fQ1 - IQR) && elem.getPrice() <= (fQ3 + IQR)).toArray(Product[]::new);

		return products;
	}
}
