package DataAnalysis;

public class aboveOneKilogram extends Product
{
	//  Price per litre
	private double PricePerUnit;

	public void setPricePerUnit()
	{
		PricePerUnit = getPrice() / getVolume();
	}

	public double getPricePerUnit()
	{
		return PricePerUnit;
	}
}
