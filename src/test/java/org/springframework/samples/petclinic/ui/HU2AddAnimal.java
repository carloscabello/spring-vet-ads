
package org.springframework.samples.petclinic.ui;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU2AddAnimal {

	@LocalServerPort
	private int				port;

	private String			username;
	private WebDriver		driver;
	private String			baseUrl;
	private boolean			acceptNextAlert		= true;
	private StringBuffer	verificationErrors	= new StringBuffer();


	@BeforeEach
	public void setUp() throws Exception {
		if (System.getProperty("os.name").toLowerCase().contains("windows")) {
			System.setProperty("webdriver.gecko.driver", System.getenv("webdriver.gecko.driver"));
		}
		this.driver = new FirefoxDriver();
		this.baseUrl = "https://www.google.com/";
		this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testHU2Positive() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario1");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[contains(@href, '/veterinario/explotacion-ganadera/list')]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Vacuno']")).click();
		this.driver.findElement(By.xpath("//input[@value='Añade un nuevo animal']")).click();
		this.driver.findElement(By.id("identificadorAnimal")).click();
		this.driver.findElement(By.id("identificadorAnimal")).clear();
		this.driver.findElement(By.id("identificadorAnimal")).sendKeys("VAC09808");
		this.driver.findElement(By.id("fechaIdentificacion")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.driver.findElement(By.linkText("15")).click();
		this.driver.findElement(By.id("fechaNacimiento")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.driver.findElement(By.xpath("//div[@id='ui-datepicker-div']/div/a/span")).click();
		this.driver.findElement(By.linkText("28")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("VAC09808", this.driver.findElement(By.linkText("VAC09808")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("Vacuno", this.driver.findElement(By.xpath("//table[@id='animalTable']/tbody/tr/td[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("2020-03-28", this.driver.findElement(By.xpath("//table[@id='animalTable']/tbody/tr/td[3]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.xpath("//a[contains(text(),'Logout')]")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Test
	public void testHU2Negative() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("veterinario1");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("veterinario1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Vacuno']")).click();
		this.driver.findElement(By.xpath("//input[@value='Añade un nuevo animal']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-animal-form']/div/div/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//form[@id='add-animal-form']/div/div[2]/div/span[2]")).click();
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-animal-form']/div/div[2]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//form[@id='add-animal-form']/div/div[3]/div/span[2]")).click();
		try {
			Assertions.assertEquals("no puede ser null", this.driver.findElement(By.xpath("//form[@id='add-animal-form']/div/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		this.driver.findElement(By.xpath("//img[@alt='Vacuno']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	}

	@AfterEach
	public void tearDown() throws Exception {
		this.driver.quit();
		String verificationErrorString = this.verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			Assertions.fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(final By by) {
		try {
			this.driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			this.driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = this.driver.switchTo().alert();
			String alertText = alert.getText();
			if (this.acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			this.acceptNextAlert = true;
		}
	}

}
