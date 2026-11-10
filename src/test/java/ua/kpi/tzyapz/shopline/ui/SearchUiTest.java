package ua.kpi.tzyapz.shopline.ui;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

@Tag("ui")
@DisplayName("UI-тести пошуку товарів у ShopLine Toolshop (Вадим Костюк)")
class SearchUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        WebDriverManager.chromedriver().setup();
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
    @DisplayName("UI-01: Пошук інструмента 'Hammer' повертає відповідні картки товару")
    void searchHammerReturnsResults() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com");

        // Act: очікування поля пошуку та введення запиту
        WebElement searchInput = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))
        );
        searchInput.sendKeys("Hammer");

        WebElement searchButton = driver.findElement(By.cssSelector("[data-test='search-submit']"));
        searchButton.click();

        // Assert: перевірка заголовка результатів та наявності товарів
        WebElement searchCaption = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']"))
        );

        Assertions.assertTrue(searchCaption.getText().contains("Hammer"), "Заголовок результатів повинен містити 'Hammer'");
        Assertions.assertTrue(driver.findElements(By.cssSelector("[data-test='product-name']")).size() > 0, "Картки товарів мають бути присутніми у вибірці");
    }
}
