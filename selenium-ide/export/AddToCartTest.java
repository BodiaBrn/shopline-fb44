import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import static org.junit.Assert.*;

public class AddToCartTest {
  @Test
  public void addToCart() {
    WebDriver driver = new ChromeDriver();
    WebDriverWait wait = new WebDriverWait(driver, 10);
    driver.get("https://practicesoftwaretesting.com/");
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='product-name']")));
    driver.findElement(By.cssSelector("[data-test='product-name']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='add-to-cart']")));
    driver.findElement(By.cssSelector("[data-test='add-to-cart']")).click();
    wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']")));
    assertEquals("1", driver.findElement(By.cssSelector("[data-test='cart-quantity']")).getText());
    driver.quit();
  }
}