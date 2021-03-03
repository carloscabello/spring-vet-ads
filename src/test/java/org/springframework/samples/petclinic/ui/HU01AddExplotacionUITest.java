
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
public class HU01AddExplotacionUITest {

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
	public void testHU1Positive() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("ganadero1");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("ganadero1");
		this.driver.findElement(By.id("password")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[@href='/ganadero/explotacion-ganadera/list']")).click();
		Integer numExplotacionesInicial = this.driver.findElements(By.xpath("//table[@id='expGanaderaTable']/tbody/tr")).size();
		this.driver.findElement(By.xpath("//input[@value='Crear una nueva explotación ganadera']")).click();
		this.driver.findElement(By.id("name")).click();
		this.driver.findElement(By.id("name")).clear();
		this.driver.findElement(By.id("name")).sendKeys("Finca ACME");
		this.driver.findElement(By.id("numeroRegistro")).click();
		this.driver.findElement(By.id("numeroRegistro")).click();
		this.driver.findElement(By.id("numeroRegistro")).clear();
		this.driver.findElement(By.id("numeroRegistro")).sendKeys("800023678");
		this.driver.findElement(By.id("terminoMunicipal")).click();
		this.driver.findElement(By.id("terminoMunicipal")).click();
		this.driver.findElement(By.id("terminoMunicipal")).clear();
		this.driver.findElement(By.id("terminoMunicipal")).sendKeys("La Saucedilla");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();

		try {
			Assertions.assertEquals("Finca ACME", this.driver.findElement(By.xpath("//table[@id='expGanaderaTable']/tbody/tr[" + String.valueOf(numExplotacionesInicial + 1) + "]/td")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("800023678", this.driver.findElement(By.xpath("//table[@id='expGanaderaTable']/tbody/tr[" + String.valueOf(numExplotacionesInicial + 1) + "]/td[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("La Saucedilla", this.driver.findElement(By.xpath("//table[@id='expGanaderaTable']/tbody/tr[" + String.valueOf(numExplotacionesInicial + 1) + "]/td[3]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertTrue(this.driver.findElement(By.xpath("//body/div/div")).getText().contains("¡Tu explotación ganadera ha sido creada con éxito!"));
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Test
	public void testHU2Negative() throws Exception {
		this.driver.get("http://localhost:" + this.port);
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		this.driver.findElement(By.id("password")).clear();
		this.driver.findElement(By.id("password")).sendKeys("ganadero1");
		this.driver.findElement(By.id("username")).clear();
		this.driver.findElement(By.id("username")).sendKeys("ganadero1");
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		this.driver.findElement(By.xpath("//a[@href='/ganadero/explotacion-ganadera/list']")).click();
		Integer numExplotacionesInicial = this.driver.findElements(By.xpath("//table[@id='expGanaderaTable']/tbody/tr")).size();
		this.driver.findElement(By.xpath("//input[@value='Crear una nueva explotación ganadera']")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
		try {
			Assertions.assertEquals("el tamaño tiene que estar entre 3 y 50", this.driver.findElement(By.xpath("//form[@id='add-expGanadera-form']/div/div/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-expGanadera-form']/div/div[2]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		try {
			Assertions.assertEquals("no puede estar vacío", this.driver.findElement(By.xpath("//form[@id='add-expGanadera-form']/div/div[3]/div/span[2]")).getText());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();
		try {
			Assertions.assertEquals(numExplotacionesInicial, this.driver.findElements(By.xpath("//table[@id='expGanaderaTable']/tbody/tr")).size());
		} catch (Error e) {
			this.verificationErrors.append(e.toString());
		}
		this.driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/strong")).click();
		this.driver.findElement(By.linkText("Logout")).click();
		this.driver.findElement(By.xpath("//button[@type='submit']")).click();
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
