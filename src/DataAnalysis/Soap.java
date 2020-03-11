package DataAnalysis;

public class Soap extends Product{
    private double PricePerUnit;

    public void setPricePerUnit() {PricePerUnit = price/volume;}

    public double getPricePerUnit() {
        return PricePerUnit;
    }
}

