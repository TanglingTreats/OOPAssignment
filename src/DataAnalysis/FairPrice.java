package DataAnalysis;

public class FairPrice extends Product {
    public double getPricePerUnit()
    {
        return 0.0;
    }

    @Override
    public void setLink(String link) {
        super.setLink("https://" + link);
    }
}
