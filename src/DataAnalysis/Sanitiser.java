package DataAnalysis;

public class Sanitiser extends Product
{
	//  Price per litre
	private double PricePerUnit;

	public void setPricePerUnit()
	{
		PricePerUnit = price / volume * 1000;
	}

	public double getPricePerUnit()
	{
		return PricePerUnit;
	}
}