package ScraperApp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import org.json.JSONArray;

public class Scraper {

    public Scraper() {
        // Empty constructor
    }

    public void getProduct(String url) {
        WebDriver driver = new FirefoxDriver();

        driver.get(url);

        Document doc = Jsoup.parse(driver.getPageSource());

        driver.close();

        String title = doc.title();


        Elements containers = doc.getElementsByClass("product-container");

        for (Element container : containers) {
            Element link = container.select("a").first();
            String relHref = link.attr("href"); // == "/"
            String absHref = link.attr("abs:href");
            System.out.println(relHref);
            String linkText = container.text();
            linkText = removeWord(linkText, "favourite");
            System.out.println("link is: " + linkText);

        }
    }

    public String removeWord(String string, String word) {
        if(string.contains(word)) {
            String tempWord = word + " ";
            string = string.replaceAll(tempWord, "");

            tempWord = " " + word;
            string = string.replaceAll(tempWord, "");
        }

        return string;
    }

}
