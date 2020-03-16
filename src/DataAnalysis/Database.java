package DataAnalysis;

import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

public class Database
{
    private int uniqueBrands = 0;
    private String[] brands;

    private Dictionary<String, Color> brandColors = new Hashtable<String, Color>();

    public Database()
    {
//        empty constructor
    }

    public void update(Product[] products, Results results)
    {
        int count = products.length;
        double cumulativePrice = 0;
        brands = new String[products.length];

        int productWeight = products[0].getVolume() <= 10 ? 1 : 0;

        for (Product product : products)
        {
            switch (productWeight)
            {
                case 0:
                    belowOneKilogram belowOneKilogram = (belowOneKilogram) product;
                    belowOneKilogram.setPricePerUnit();
                    cumulativePrice += belowOneKilogram.getPricePerUnit();
                    break;
                case 1:
                    aboveOneKilogram aboveOneKilogram = (aboveOneKilogram) product;
                    aboveOneKilogram.setPricePerUnit();
                    cumulativePrice += aboveOneKilogram.getPricePerUnit();
                    break;
            }

            if (!brandExist(product.getBrand()))
            {
                brands[uniqueBrands] = product.getBrand();
                uniqueBrands++;
            }

//          Technically, the following portion is calculations, which should be put in dataCruncher.
//			It is however put in this loop to lower the total number of loops needed for calculations.
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

        generateColors();
        new DataCruncher().crunch(cumulativePrice, count, products, results, brandColors);

    }

    public static Product[] filterBrand(Product[] products, String brand)
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

    public static Product[] filterSupermarket(Product[] products, String supermarket)
    {
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
        boolean exist = false;
        for (int i = 0; i < uniqueBrands; i++)
        {
            if (brands[i].equals(brand))
            {
                exist = true;
                break;
            }
        }
        return exist;
    }

    private void generateColors()
    {
        float interval = 360.0f / uniqueBrands;
        for (int i = 0; i < uniqueBrands; i++)
            brandColors.put(brands[i], Color.getHSBColor(i * interval / 360, 1, 1));

    }
}
