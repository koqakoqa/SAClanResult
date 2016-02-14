package main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class Chrome {

	static WebDriver driver;
	static int page = 1;

	@BeforeClass
	public static void Setup() {
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
	}

	@Test
	public static void run() {
		driver = new ChromeDriver();
		driver.get("http://sa.nexon.co.jp/clan/clanrecord/clanrecord_list.aspx?kw=Conflit&tm=201602");

		next();
	}

	public static void next() {
		try{
			File file = new File("log/html" + page + ".txt");
			FileWriter filewriter = new FileWriter(file);

			filewriter.write(driver.getPageSource());

			filewriter.close();
		}catch(IOException e){
			System.out.println(e);
		}
		try {
			new Actions(driver).click(driver.findElement(By.id("RightContent_Pager_LkbNext"))).build().perform();
//			Thread.sleep(5000);
			page++;
			next();
		} catch (Exception e) {
			driver.quit();
		}
	}
}