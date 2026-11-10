import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Alert;
import org.openqa.selenium.Keys;
import java.util.*;
import java.net.MalformedURLException;
import java.net.URL;

public class SearchHammerTest {
  private WebDriver driver;
  private Map<String, Object> vars;
  JavascriptExecutor js;
  @Before
  public void setUp() {
    driver = new ChromeDriver();
    js = (JavascriptExecutor) driver;
    vars = new HashMap<String, Object>();
  }
  @After
  public void tearDown() {
    driver.quit();
  }
  @Test
  public void searchHammer() {
    driver.get("https://practicesoftwaretesting.com/");
    driver.manage().window().setSize(new Dimension(1920, 1080));
    {
      WebDriverWait wait = new WebDriverWait(driver, 10);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']")));
    }
    driver.findElement(By.cssSelector("[data-test='search-query']")).sendKeys("Hammer");
    driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
    {
      WebDriverWait wait = new WebDriverWait(driver, 10);
      wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']")));
    }
    {
      List<WebElement> elements = driver.findElements(By.cssSelector("[data-test='product-name']"));
      assert(elements.size() > 0);
    }
    assertThat(driver.findElement(By.cssSelector("[data-test='search-caption']")).getText(), is("Searched for: Hammer"));
  }
}
