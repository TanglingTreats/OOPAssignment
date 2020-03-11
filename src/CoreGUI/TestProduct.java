package CoreGUI;

public class TestProduct extends Product {

    public TestProduct() {
        setImage("https://cdn.onebauer.media/one/empire-tmdb/people/1356758/profiles/cjh0YIKEDNpQkmaZtSg6K7EQNas.jpg?" +
                "quality=50&width=1000&ratio=1-1&resizeStyle=aspectfit&format=jpg");
        setTitle("Thomasin McKenzie");
    }

    public TestProduct(String title, String desc, String imageURL) {
        setTitle(title);
        setDesc(desc);
        setImage(imageURL);
    }

    @Override
    public float getPricePerUnit() {
        return 0;
    }
}
