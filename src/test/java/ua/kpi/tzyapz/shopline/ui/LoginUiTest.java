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
@DisplayName("UI-тести авторизації користувача [Богдан Алферов]")
class LoginUiTest {

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

    @Test
    @DisplayName("UI-03: Успішна авторизація з валідними обліковими даними")
    void loginWithValidCredentialsSuccess() {
        driver.get("https://practicesoftwaretesting.com/auth/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']")));
        emailInput.sendKeys("customer@practicesoftwaretesting.com");

        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys("welcome01");
        driver.findElement(By.cssSelector("[data-test='login-submit']")).click();

        WebElement pageTitle = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='page-title']")));
        assertTrue(pageTitle.getText().contains("My account"), "Після успішного входу має відкритися сторінка кабінету");
    }

    @Test
    @DisplayName("UI-04: Спроба авторизації з невірним паролем відображає помилку")
    void loginWithInvalidPasswordShowsError() {
        driver.get("https://practicesoftwaretesting.com/auth/login");

        WebElement emailInput = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']")));
        emailInput.sendKeys("customer@practice.com");

        driver.findElement(By.cssSelector("[data-test='password']")).sendKeys("wrong_password_123");
        driver.findElement(By.cssSelector("[data-test='login-submit']")).click();

        WebElement errorMessage = wait.until(ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='login-error']")));
        assertTrue(errorMessage.getText().contains("Invalid email or password"), "Має відображатися повідомлення про невірний логін/пароль");
    }
}
