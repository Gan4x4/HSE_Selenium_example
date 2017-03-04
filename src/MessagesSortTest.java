

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.google.common.collect.Ordering;

public class MessagesSortTest {
  private WebDriver driver;
  private String baseUrl;
  private boolean acceptNextAlert = true;
  private StringBuffer verificationErrors = new StringBuffer();
  
  //Level 1 (UI)
  private static final By THEME_HEADER = By.linkText("Тема");
  private static final By MESSAGES_LINK = By.linkText("Сообщения");
  private static final By TESTING_COURSE = By.partialLinkText("Обеспечение качества и тестирование 2015 ");
  private static final By MY_COURSES_LINK = By.linkText("Мои курсы");
 

  

//=============== Level1 UI ===================================
  private WebElement getLoginField() {
		return driver.findElement(By.name("login"));
	}
  
  private WebElement getLoginButton() {
		return driver.findElement(By.name("btnlogin"));
	}

	private WebElement getPasswordField() {
		return driver.findElement(By.name("password"));
	}
  
	public List<String> getMessagesTitles(){
		List<WebElement> cells = driver.findElements(By.xpath("//table[@id='messagesTable']//tr[position() >1 and position() < last()]/td[position() = 2]"));
		List<String> result = new ArrayList<String>();
		// Copy text from WebElements to list of string
		for(WebElement cell: cells){
			System.out.println("Line: "+cell.getText());
			result.add(cell.getText());
		}
		return result;
	}
	
// =============== Level2 Complex Interaction ==================
	
  private void login() {
		driver.get(baseUrl + "/");
		getLoginField().clear();
		getLoginField().sendKeys("******");
		getPasswordField().clear();
		getPasswordField().sendKeys("*****");
		getLoginButton().click();
	}

  
  private void openMessagePage() {
		login();
	    driver.findElement(MY_COURSES_LINK).click();
	    driver.findElement(TESTING_COURSE).click();    
	    driver.findElement(MESSAGES_LINK).click();
	}

  private void sortMessages() {
		driver.findElement(THEME_HEADER).click();
		// Not work for lms :(
		//(new WebDriverWait(driver,500)).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//td[@class='topTitle']/img")));
		try {
			// Very very bad ....
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
  
  // =============== Tests =======================================
  
  @Before
  public void setUp() throws Exception {
    driver = new FirefoxDriver();
    baseUrl = "http://lms.hse.ru/";
    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    driver.manage().window().maximize();
  }
  
  @Test
  public void testMessagesSort() throws Exception {
	// Setup
    openMessagePage();
    // Exercise
    sortMessages();
    // Verify
    List<String> titles = getMessagesTitles();
    assertTrue(Ordering.natural().isOrdered(titles) ||
    		Ordering.natural().reverse().isOrdered(titles));
    //Cleanup
  }

  @After
  public void tearDown() throws Exception {
    driver.quit();
    String verificationErrorString = verificationErrors.toString();
    if (!"".equals(verificationErrorString)) {
      fail(verificationErrorString);
    }
  }
//================== Utils ================================
  private boolean isElementPresent(By by) {
    try {
      driver.findElement(by);
      return true;
    } catch (NoSuchElementException e) {
      return false;
    }
  }

  private boolean isAlertPresent() {
    try {
      driver.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }

  private String closeAlertAndGetItsText() {
    try {
      Alert alert = driver.switchTo().alert();
      String alertText = alert.getText();
      if (acceptNextAlert) {
        alert.accept();
      } else {
        alert.dismiss();
      }
      return alertText;
    } finally {
      acceptNextAlert = true;
    }
  }
}
