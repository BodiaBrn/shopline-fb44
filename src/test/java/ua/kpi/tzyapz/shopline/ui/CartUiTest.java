package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("ui")
@DisplayName("UI-тести кошика товарів [Вадим Костюк]")
class CartUiTest {

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

    private void openFirstAvailableProduct() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='product-name']")));

        WebElement availableProduct = driver.findElements(By.cssSelector("a.card")).stream()
                .filter(card -> card.findElements(By.cssSelector("[data-test='out-of-stock']")).isEmpty())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Усі товари на першій сторінці розпродані"));

        wait.until(ExpectedConditions.elementToBeClickable(availableProduct)).click();
    }

    private BigDecimal parsePrice(String priceText) {
        String cleaned = priceText.replaceAll("[^0-9.]", "");
        return new BigDecimal(cleaned).setScale(2, RoundingMode.HALF_UP);
    }

    @Test
    @DisplayName("UI-05 [TC-026]: Додавання товару з детальної сторінки оновлює бейдж кошика")
    void addProductToCartUpdatesCartQuantityBadge() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com");

        // Act
        openFirstAvailableProduct();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();

        // Assert
        WebElement cartQuantity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']")));
        assertEquals("1", cartQuantity.getText().trim(), "Кількість товарів у шапці кошика має дорівнювати 1");
    }

    @Test
    @DisplayName("UI-06 [TC-031]: Зміна кількості товару в кошику перераховує вартість рядка та підсумок")
    void changeItemQuantityInCartRecalculatesTotal() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com");
        openFirstAvailableProduct();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

        WebElement unitPriceEl = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='product-price']")));
        BigDecimal unitPrice = parsePrice(unitPriceEl.getText());
        BigDecimal expectedTotal = unitPrice.multiply(BigDecimal.valueOf(2)).setScale(2, RoundingMode.HALF_UP);
        String oldTotalText = driver.findElement(By.cssSelector("[data-test='cart-total']")).getText().trim();

        // Act: зміна кількості з 1 на 2
        WebElement qtyInput = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-quantity']")));
        // Очищення поля без Ctrl+A: у number-input під headless Chrome виділення не спрацьовує,
        // і «2» дописувалося до наявної «1» (отримували 12 замість 2)
        qtyInput.sendKeys(Keys.BACK_SPACE, Keys.BACK_SPACE, Keys.BACK_SPACE,
                          Keys.DELETE, Keys.DELETE, Keys.DELETE);
        qtyInput.sendKeys("2", Keys.ENTER);

        // Assert: очікування оновлення суми кошика та перевірка перерахунку
        wait.until(d -> !d.findElement(By.cssSelector("[data-test='cart-total']")).getText().trim().equals(oldTotalText));
        BigDecimal actualLineTotal = parsePrice(driver.findElement(By.cssSelector("[data-test='line-price']")).getText());
        BigDecimal actualCartTotal = parsePrice(driver.findElement(By.cssSelector("[data-test='cart-total']")).getText());

        Assertions.assertAll("Перевірка перерахунку суми після зміни кількості на 2",
            () -> assertEquals(expectedTotal, actualLineTotal, "Вартість рядка має дорівнювати ціні × 2"),
            () -> assertEquals(expectedTotal, actualCartTotal, "Загальний підсумок кошика має дорівнювати ціні × 2")
        );
    }
}
