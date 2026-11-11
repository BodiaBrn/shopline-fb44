# Сценарії UI-автоматизації Selenium WebDriver (Спринт 4)

**Продукт:** «ШопЛайн» (practicesoftwaretesting.com)  
**Відповідальний за опис:** Богдан Барановський (Тест-аналітик)  
**Автоматизатор:** Вадим Костюк (Інженер з тестування)  

---

### UI-01 (TC-001): Авторизація покупця
* **Вимога SRS:** F-10, GUI-60
* **URL:** `https://practicesoftwaretesting.com/#/auth/login`
* **Кроки:**
  1. Перейти на сторінку входу.
  2. Ввести email `customer@practicesoftwaretesting.com` у `By.cssSelector("[data-test='email']")`.
  3. Ввести пароль `welcome01` у `By.cssSelector("[data-test='password']")`.
  4. Натиснути кнопку `By.cssSelector("[data-test='login-submit']")`.
* **Очікування (WebDriverWait):** `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='page-title']"))`
* **Перевірка:** Заголовок сторінки містить `"My account"`.

### UI-02 (TC-010): Пошук товару за назвою у каталозі
* **Вимога SRS:** F-40, GUI-20
* **URL:** `https://practicesoftwaretesting.com/#/`
* **Кроки:**
  1. Ввести `"Pliers"` у поле пошуку `By.cssSelector("[data-test='search-query']")`.
  2. Натиснути `By.cssSelector("[data-test='search-submit']")`.
* **Очікування:** `wait.until(d -> d.findElements(By.cssSelector("[data-test='product-name']")).size() > 0)`
* **Перевірка:** Список результатів непорожній і всі назви містять `"Pliers"`.

### UI-03 (TC-024): Додавання товару до кошика з картки
* **Вимога SRS:** F-80, GUI-30
* **URL:** `https://practicesoftwaretesting.com/#/`
* **Кроки:**
  1. Клікнути на першу картку товару `By.cssSelector("[data-test^='product-']")`.
  2. Натиснути кнопку `By.cssSelector("[data-test='add-to-cart']")`.
* **Очікування:** `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))`
* **Перевірка:** Значення лічильника кошика дорівнює `"1"`.

### UI-04 (TC-030): Оформлення замовлення (Checkout)
* **Вимога SRS:** F-90, GUI-40
* **URL:** `https://practicesoftwaretesting.com/#/checkout`
* **Кроки:**
  1. Перейшовши до кошика, натиснути кнопку `By.cssSelector("[data-test='proceed-to-checkout']")`.
  2. Заповнити покрокові форми адреси доставки та оплати (`By.cssSelector("[data-test='state']")`, `By.cssSelector("[data-test='postal_code']")`).
  3. Підтвердити замовлення натисканням кнопки `By.cssSelector("[data-test='finish']")`.
* **Очікування:** `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='order-confirmation']"))`
* **Перевірка:** На екрані відображається повідомлення про успішне оформлення замовлення та номер замовлення.
