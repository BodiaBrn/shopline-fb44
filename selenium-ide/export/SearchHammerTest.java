import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.*;

public class SearchHammerTest {
  @Test
  public void searchHammer() {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, 10);
    driver.get("https://practicesoftwaretesting.com/");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']")));
    driver.findElement(By.cssSelector("[data-test='search-query']")).sendKeys("Hammer");
    driver.findElement(By.cssSelector("[data-test='search-submit']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']")));
    assertTrue(driver.findElements(By.cssSelector("[data-test='product-name']")).size() > 0);
    assertEquals("Searched for: Hammer", driver.findElement(By.cssSelector("[data-test='search-caption']")).getText());
    driver.quit();
  }
}
