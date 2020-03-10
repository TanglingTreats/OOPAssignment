public abstract class Product {

    protected String title;
    protected String link;
    protected String image;
    protected float price;
    protected String desc;
    protected String brand;
    protected String supermarket;
    protected String superMarketImg;


    public Product() {
        //Empty constructor
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getPrice() {
        return this.price;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    public void setSupermarket(String supermarket) {
        this.supermarket = supermarket;
    }

    public String getSupermarket() {
        return supermarket;
    }

    public abstract float getPricePerUnit();
}
