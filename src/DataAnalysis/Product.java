package DataAnalysis;

public abstract class Product
{

	protected String title;
	protected String link;
	protected String image;
	protected double price;
	protected long volume;
	protected String desc;
	protected String brand;
	protected String supermarket;
	protected String superMarketImg;


	public Product()
	{
		title = "Empty";
	}

	public void setPrice(Double price)
	{
		this.price = price;
	}

	public double getPrice()
	{
		return this.price;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public String getTitle()
	{
		return this.title;
	}

	public void setDesc(String desc)
	{
		this.desc = desc;
	}

	public String getDesc()
	{
		return this.desc;
	}

	public void setBrand(String brand)
	{
		this.brand = brand;
	}

	public String getBrand()
	{
		return brand;
	}

	public void setSupermarket(String supermarket)
	{
		this.supermarket = supermarket;
	}

	public String getSupermarket()
	{
		return supermarket;
	}

	public void setSupermarketImg(String superMarketImg)
	{
		this.superMarketImg = superMarketImg;
	}

	public String getSupermarketImg()
	{
		return superMarketImg;
	}

	public void setLink(String link)
	{
		this.link = link;
	}

	public String getLink()
	{
		return link;
	}

	public void setImage(String image)
	{
		this.image = image;
	}

	public String getImage()
	{
		return image;
	}

	public abstract double getPricePerUnit();

	public long getVolume()
	{
		return volume;
	}

	public void setVolume(long volume)
	{
		this.volume = volume;
	}

	public boolean isNull()
	{
		return title.equals("Empty");
	}
}
