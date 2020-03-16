package DataAnalysis;

public class belowOneKilogram extends Product
{
	//  Price per litre
	private double PricePerUnit;

	public void setPricePerUnit()
	{
		PricePerUnit = getPrice() / getVolume() * 1000;
	}

	public double getPricePerUnit()
	{
		return PricePerUnit;
	}

}

