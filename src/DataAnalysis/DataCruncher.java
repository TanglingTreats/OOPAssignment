package DataAnalysis;

import java.awt.*;
import java.lang.Math;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.stream.Collectors;

public class DataCruncher
{
    public void crunch(double cumulativePrice, int count, Product[] products, Results results, Dictionary<String, Color> brandColors)
    {
    	System.out.println(products.length);
    	removeOutliers(products);
		System.out.println(products.length);
        double mean = cumulativePrice / count;
        double variance = 0;

        for (Product product : products)
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

    private void removeOutliers(Product[] products)
    {
        double Q1, Q3, IQR;
        int medianIndex, Q1Index, Q3Index;
//		if even length
        if (products.length % 2 == 0)
        {
//			if data set is even, halve the data set and find q1 and q3 as the middle of the two halves
//			get middle two numbers, sum and halve to find median

            Q1Index = (int) Math.ceil(products.length / 4.0);
            Q3Index = (products.length / 2 + 1) + (int) Math.floor(products.length / 4.0);

			Q1 = products[Q1Index - 1].getVolume();
			Q3 = products[Q3Index - 1].getVolume();
        } else
        {
//        	data set is odd,
            medianIndex = (int) Math.ceil(products.length / 2.0);

            if (medianIndex % 2 == 0)
//        	q1 and q3 are between even numbers
            {
            	Q1Index = (int)Math.floor(medianIndex / 2.0);

            	Q3Index = medianIndex + Q1Index;

            	Q1 = (products[Q1Index-1].getVolume() + products[Q1Index].getVolume())/2;
				Q3 = (products[Q3Index-1].getVolume() + products[Q3Index].getVolume())/2;
            } else
//			q1 and a3 are between odd numbers
            {
				Q1Index = (int) Math.ceil(products.length / 4.0);
				Q3Index = medianIndex + Q1Index;

				Q1 = products[Q1Index - 1].getVolume();
				Q3 = products[Q3Index - 1].getVolume();
			}
        }
        IQR = 1.5 * (Q3 - Q1);

		products = Arrays.stream(products).filter(elem -> elem.getVolume() < (Q1 - IQR)).toArray(Product[]::new);
		products = Arrays.stream(products).filter(elem -> elem.getVolume() > (Q3 + IQR)).toArray(Product[]::new);
    }
}
