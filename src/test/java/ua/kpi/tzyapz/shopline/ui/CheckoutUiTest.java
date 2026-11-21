package ua.kpi.tzyapz.shopline.ui;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Tag("ui")
@DisplayName("UI-тести оформлення замовлення та контактної форми [Вадим Костюк]")
class CheckoutUiTest {

    private WebDriver driver;
    private WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new", "--window-size=1920,1080", "--no-sandbox", "--disable-dev-shm-usage");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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

    /**
     * Заповнює поле, лише якщо воно є на сторінці і порожнє.
     * Для випадаючого списку обирає перший непорожній варіант.
     */
    private void fillIfBlank(By locator, String fallbackValue) {
        List<WebElement> found = driver.findElements(locator);
        if (found.isEmpty()) {
            return; // поля немає в поточній версії форми
        }
        WebElement field = found.get(0);
        String current = field.getAttribute("value");
        if (current != null && !current.isBlank()) {
            return;
        }
        if ("select".equalsIgnoreCase(field.getTagName())) {
            Select select = new Select(field);
            select.getOptions().stream()
                    .map(option -> option.getAttribute("value"))
                    .filter(value -> value != null && !value.isBlank())
                    .findFirst()
                    .ifPresent(select::selectByValue);
        } else {
            field.sendKeys(fallbackValue);
        }
    }

    /** Стан полів форми для повідомлення про помилку, якщо кнопка переходу лишилась неактивною. */
    private String describeFormFields() {
        return driver.findElements(By.cssSelector("input, select")).stream()
                .map(e -> e.getAttribute("data-test") + "='" + e.getAttribute("value") + "'"
                        + (String.valueOf(e.getAttribute("class")).contains("ng-invalid") ? " [INVALID]" : ""))
                .collect(Collectors.joining("; "));
    }

    @Test
    @DisplayName("UI-07 [TC-032]: Успішне оформлення замовлення авторизованим покупцем")
    void authenticatedCheckoutCompletesSuccessfully() {
        // Arrange: авторизація покупця
        driver.get("https://practicesoftwaretesting.com/auth/login");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']")))
                .sendKeys("customer@practicesoftwaretesting.com");
        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys("welcome01");
        driver.findElement(By.cssSelector("[data-test='login-submit']")).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='page-title']")));

        // Act: додавання товару в кошик та проходження 4 кроків чекауту
        driver.get("https://practicesoftwaretesting.com");
        openFirstAvailableProduct();
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))).click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']")));
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='nav-cart']"))).click();

        // Крок 1: Cart
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-1']"))).click();
        // Крок 2: Sign in (користувач уже авторизований)
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-2']"))).click();

        // Крок 3: Billing Address
        WebElement street = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='street']")));
        try {
            // адреса з профілю підвантажується асинхронно — даємо їй до 5 с
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(d -> !String.valueOf(street.getAttribute("value")).isBlank());
        } catch (TimeoutException ignored) {
            // адреси в профілі немає — порожні поля заповнимо нижче
        }
        fillIfBlank(By.cssSelector("[data-test='street']"), "Test Street 1");
        fillIfBlank(By.cssSelector("[data-test='house_number']"), "1");
        fillIfBlank(By.cssSelector("[data-test='city']"), "Kyiv");
        fillIfBlank(By.cssSelector("[data-test='state']"), "Kyiv");
        fillIfBlank(By.cssSelector("[data-test='country']"), "Ukraine");
        fillIfBlank(By.cssSelector("[data-test='postal_code']"), "01001");

        new WebDriverWait(driver, Duration.ofSeconds(15))
                .withMessage(() -> "Кнопка proceed-3 неактивна. Поля форми: " + describeFormFields())
                .until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='proceed-3']")))
                .click();

        // Крок 4: Payment (Cash on Delivery)
        WebElement paymentSelect = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='payment-method']")));
        new Select(paymentSelect).selectByValue("cash-on-delivery");
        wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='finish']"))).click();

        // Assert: підтвердження успішної оплати
        WebElement successMsg = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='payment-success-message']")));
        assertTrue(successMsg.getText().contains("Payment was successful"),
            "Після підтвердження замовлення має відображатися повідомлення про успішну оплату");
    }

    @Test
    @DisplayName("UI-08 [TC-009]: Повідомлення контактної форми довжиною 49 символів відхиляється валідацією")
    void contactFormWith49CharMessageShowsValidationError() {
        // Arrange
        driver.get("https://practicesoftwaretesting.com/contact");
        String message49Chars = "a".repeat(49);

        // Act
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='first-name']"))).sendKeys("Vadym");
        driver.findElement(By.cssSelector("[data-test='last-name']")).sendKeys("Kostiuk");
        driver.findElement(By.cssSelector("[data-test='email']")).sendKeys("vadym.kostiuk@example.com");
        new Select(driver.findElement(By.cssSelector("[data-test='subject']"))).selectByValue("customer-service");
        driver.findElement(By.cssSelector("[data-test='message']")).sendKeys(message49Chars);
        driver.findElement(By.cssSelector("[data-test='contact-submit']")).click();

        // Assert
        WebElement messageError = wait.until(
            ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='message-error'], .alert-danger"))
        );
        assertTrue(messageError.isDisplayed() && messageError.getText().contains("50"),
            "Для повідомлення на 49 символів має з'явитися помилка мінімальної довжини (50 символів)");
    }
}
