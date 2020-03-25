package selenium.webdriver.basic;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

import java.util.concurrent.TimeUnit;

public class LaunchBrowserTwo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		WebDriver driver = new HtmlUnitDriver();
		driver.manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
		
		driver.navigate().to("https://fdadunslookup.com/");
		driver.manage().window().maximize();
		String title = driver.getTitle();
		
		System.out.println("title of page is = " + title );
		
		//driver.findElement(By.linkText("Member Sign In")).click();
		
		WebElement email = driver.findElement(By.id("MainContent_Txt_EmailID"));
		email.sendKeys("duns@registrarcorp.com");
		WebElement password = driver.findElement(By.id("MainContent_Txt_Password"));
		password.sendKeys("Sabar1sh$$");
		driver.findElement(By.id("MainContent_Btn_Login")).click();
		driver.findElement(By.id("MainContent_Btn_Enter")).click();

	}

}
