package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

@Tag("ui")
@DisplayName("UI-тести пошуку товарів у ShopLine Toolshop [Вадим Костюк]")
class SearchUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("UI-01 [TC-016]: Пошук товару без урахування регістру ('PLIERS') повертає відповідні картки")
    void searchPliersCaseInsensitiveReturnsResults() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com");

        // Act
        WebElement searchInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))
        );
        searchInput.sendKeys("PLIERS");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();

        // Assert
        WebElement searchCaption = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']"))
        );
        List<WebElement> products = wait.until(
            ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("[data-test='product-name']"), 0)
        );

        Assertions.assertAll("Перевірка результатів регістронезалежного пошуку 'PLIERS'",
            () -> Assertions.assertTrue(searchCaption.getText().toLowerCase().contains("pliers"),
                "Заголовок результатів має містити запит 'PLIERS'"),
            () -> Assertions.assertTrue(
                products.stream().anyMatch(el -> el.getText().toLowerCase().contains("pliers")),
                "Серед знайдених карток повинні бути товари з назвою 'Pliers'")
        );
    }

    @Test
    @DisplayName("UI-02 [TC-017]: Пошук неіснуючого товару повертає порожній список")
    void searchNonExistingProductReturnsEmptyList() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com");

        // Act
        WebElement searchInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))
        );
        searchInput.sendKeys("NonExistingItem9999");
        driver.findElement(By.cssSelector("[data-test='search-submit']")).click();

        // Assert
        wait.until(ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-test='product-name']"), 0));
        Assertions.assertEquals(0, driver.findElements(By.cssSelector("[data-test='product-name']")).size(),
            "Для неіснуючого запиту картки товарів не повинні відображатися");
    }
}
