package assessment;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import io.github.bonigarcia.wdm.WebDriverManager;

public class Speer_test {

	private static WebDriver driver;

//1.) Accepts a Wikipedia link - return/throw an error if the link is not a valid wiki link
	public static void isValidURL(String url) {
		try {
			(new java.net.URL(url)).openStream().close();

		} catch (IOException ex) {
			System.out.println(ex + " is not valid url");
			Assert.assertTrue(false);
		}
	}

	public static void main(String[] args) {
		
		Scanner link = new Scanner(System.in);
		System.out.println("Enter Link :");
		String url = link.nextLine();
		isValidURL(url);
		

//2) Accepts a valid integer between 1 to 20 - call it n
		Scanner validInt = new Scanner(System.in);
		System.out.println("Enter number betwwen 1 to 20 :");
		Integer n = validInt.nextInt();
		validInt.close();
		link.close();

//3) Scrape the link provided in Step 1, for all wiki links embedded in the page and store them in a data structure of your choice.
		WebDriverManager.chromedriver().setup();
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
		driver.get(url);
		List<String> embededURLS = getPageEmbadedLinks();
		driver.close();

//4) Repeat Step 3 for all newly found links and store them in the same data structure.		
		List<String> allLinks = new ArrayList<String>();

		for (int i = 0; i < n; i++) { // 5) This process should terminate after n cycles.
			String currentURL = embededURLS.get(i);

			driver = new ChromeDriver();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
			driver.get(currentURL);

			List<String> newlyFoundedLinks = getPageEmbadedLinks();
			allLinks.addAll(newlyFoundedLinks);
			
			driver.close();
			driver.quit();
		}
		embededURLS.addAll(allLinks); // storing in same data structure
		
		for (String FoundLinks : embededURLS) {
			System.out.println(FoundLinks);
		}
		System.out.println("Total Links Count: " + embededURLS.size());
		Set<String> uniqueLinks = new HashSet<String>(embededURLS);
		System.out.println("Unique Links Count: " + uniqueLinks.size());

	}

	public static List<String> getPageEmbadedLinks() {

		List<String> newlyFoundedLinks = new ArrayList<String>();

		List<WebElement> allLinks = driver.findElements(By.tagName("a"));

		for (WebElement element : allLinks) {
			String newlyUrl = element.getAttribute("href");

			if (newlyUrl != null) {
				newlyFoundedLinks.add(newlyUrl);
			}
		}
		return newlyFoundedLinks;
	}

}
