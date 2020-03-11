package DataAnalysis;

public class Sanitiser extends Product{
    private double PricePerUnit;

    public void setPricePerUnit() {PricePerUnit = price/volume;}

    public double getPricePerUnit() {
        return this.price / volume;
    }
}