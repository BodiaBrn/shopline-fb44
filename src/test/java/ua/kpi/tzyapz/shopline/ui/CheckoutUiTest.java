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

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@DisplayName("UI-тести оформлення замовлення та промокодів")
class CheckoutUiTest {

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
    @DisplayName("UI-07: Перехід до кроків чекауту вимагає авторизації")
    void proceedToCheckoutRedirectsToLoginIfNotAuthenticated() {
        driver.get("https://practicesoftwaretesting.com");

        // Додаємо товар та переходимо в кошик
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-name']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

        // Перехід до оформлення
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-1']"))).click();

        // Перевірка перенаправлення на сторінку входу
        WebElement loginTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3, [data-test='page-title']")));
        assertTrue(loginTitle.getText().contains("Login"), "Неавторизований користувач має бути перенаправлений на сторінку Login");
    }

    @Test
    @DisplayName("UI-08: Введення неіснуючого промокоду відображає відмову")
    void invalidDiscountCodeDisplaysError() {
        driver.get("https://practicesoftwaretesting.com");

        // Додаємо товар та переходимо в кошик
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-name']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

        // Введення невалідного промокоду
        WebElement discountInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='discount-code']")));
        discountInput.sendKeys("INVALID_PROMO_2026");
        driver.findElement(By.cssSelector("[data-test='apply-discount']")).click();

        // Перевірка повідомлення про помилку
        WebElement alert = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-danger, [data-test='discount-error']")));
        assertTrue(alert.getText().length() > 0, "При некоректному промокоді має відображатися повідомлення про помилку");
    }
}
