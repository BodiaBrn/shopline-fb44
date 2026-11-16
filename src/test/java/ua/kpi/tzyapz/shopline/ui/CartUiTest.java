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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@DisplayName("UI-тести кошика товарів")
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
        // Очікування завантаження карток товарів з API
        wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='product-name']")));

        // Пошук першої картки товару, яка не має позначки "Out of stock"
        WebElement availableProduct = driver.findElements(By.cssSelector("a.card")).stream()
                .filter(card -> card.findElements(By.cssSelector("[data-test='out-of-stock']")).isEmpty())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Усі товари на першій сторінці розпродані"));

        wait.until(ExpectedConditions.elementToBeClickable(availableProduct)).click();
    }

    @Test
    @DisplayName("UI-05: Додавання товару з детальної сторінки оновлює бейдж кошика")
    void addProductToCartUpdatesCartQuantityBadge() {
        driver.get("https://practicesoftwaretesting.com");

        // Перехід на перший доступний товар
        openFirstAvailableProduct();

        // Клік по кнопці «Add to cart»
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']")));
        addToCartBtn.click();

        // Перевірка лічильника кошика в шапці
        WebElement cartQuantity = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']")));
        assertEquals("1", cartQuantity.getText().trim(), "Кількість товарів у шапці кошика має дорівнювати 1");
    }

    @Test
    @DisplayName("UI-06: Видалення позиції з кошика очищає список")
    void removeItemFromCartClearsCartTable() {
        driver.get("https://practicesoftwaretesting.com");

        // Додавання першого доступного товару
        openFirstAvailableProduct();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();

        // Перехід у кошик
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

        // Видалення товару з кошика
        WebElement deleteBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".btn-danger, [data-test='delete-product']")));
        deleteBtn.click();

        // Перевірка порожнього кошика
        boolean isCartEmpty = wait.until(ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-test='product-title']")));
        assertTrue(isCartEmpty, "Після видалення елемента таблиця кошика має бути порожньою");
    }
}
