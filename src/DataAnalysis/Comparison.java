package DataAnalysis;


public class Comparison {
    public Comparison(){
//        empty constructor
    }


    public Product[] compare(Product[] products, String option)
    {
        Product[] results = new Product[3];
        if (option.equals("Soap")) {
            Soap first = new Soap();
            Soap second = new Soap();
            Soap third = new Soap();

            for (Product product : products) {
                if (product instanceof Soap) {
                    Soap soap = (Soap) product;
                    soap.setPricePerUnit();
                    if (first.isNull()) {
                        first = soap;
                    } else if (product.getPricePerUnit() < first.getPricePerUnit()) {
                        third = second;
                        second = first;
                        first = soap;
                    }
                }

                results[0] = first;
                results[1] = second;
                results[2] = third;
            }
        }
                if (option.equals("Sanitiser"))
                {
                    Sanitiser first = new Sanitiser();
                    Sanitiser second = new Sanitiser();
                    Sanitiser third = new Sanitiser();
                    for (Product product : products) {
                        if (product instanceof Sanitiser) {
                            Sanitiser sanitiser = (Sanitiser) product;
                            sanitiser.setPricePerUnit();
                            if (first.isNull()) {
                                first = sanitiser;
                            } else if (product.getPricePerUnit() < first.getPricePerUnit()) {
                                third = second;
                                second = first;
                                first = sanitiser;
                            }
                        }
                    }

                    results[0] = first;
                    results[1] = second;
                    results[2] = third;
            }
        return results;
    }
}
