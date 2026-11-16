# Сценарії UI-автоматизації Selenium WebDriver (Спринт 4)

**Продукт:** «ШопЛайн» (https://practicesoftwaretesting.com)  
**Відповідальний за опис:** Богдан Барановський (Тест-аналітик)  
**Автоматизатори:** Вадим Костюк, Богдан Алферов (Інженери з тестування)  

---

## Зведена таблиця простежуваності UI-тестів

| ID | Тест-клас та метод | Вимога SRS | Кейс Спринту 3 | Назва сценарію (`@DisplayName`) |
|---|---|---|---|---|
| **UI-01** | `SearchUiTest.searchHammerReturnsResults` | `F-60`, `GUI-20` | `TC-016` | Пошук інструмента `'Hammer'` повертає відповідні картки товару |
| **UI-02** | `SearchUiTest.searchNonExistingProductReturnsEmptyList` | `F-60`, `F-50` | `TC-017` | Пошук неіснуючого товару повертає порожній список |
| **UI-03** | `LoginUiTest.loginWithValidCredentialsSuccess` | `F-20`, `GUI-60` | `TC-005` | Успішна авторизація з валідними обліковими даними |
| **UI-04** | `LoginUiTest.loginWithInvalidPasswordShowsError` | `F-20`, `GUI-60` | `TC-006` | Спроба авторизації з невірним паролем відображає помилку |
| **UI-05** | `CartUiTest.addProductToCartUpdatesCartQuantityBadge` | `F-80`, `F-90` | `TC-026` | Додавання товару з детальної сторінки оновлює бейдж кошика |
| **UI-06** | `CartUiTest.removeItemFromCartClearsCartTable` | `F-100`, `GUI-70` | `TC-031` | Видалення позиції з кошика очищає список |
| **UI-07** | `CheckoutUiTest.proceedToCheckoutRedirectsToLoginIfNotAuthenticated` | `F-100`, `F-110`, `S-20` | `TC-032` | Перехід до кроків чекауту вимагає авторизації |
| **UI-08** | `CheckoutUiTest.emptyContactFormDisplaysValidationError` | `F-140`, `GUI-50`, `GUI-90` | `TC-009` | Відправка порожньої контактної форми відображає помилки валідації |

---

## Детальний опис сценаріїв

### UI-01 (`SearchUiTest.searchHammerReturnsResults`): Пошук існуючого товару у каталозі
* **Вимога SRS:** `F-60`, `GUI-20` | **Кейс Спринту 3:** `TC-016`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку каталогу.
  2. Ввести `"Hammer"` у поле пошуку `By.cssSelector("[data-test='search-query']")`.
  3. Натиснути кнопку пошуку `By.cssSelector("[data-test='search-submit']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']"))`
* **Перевірка:** Заголовок результатів `[data-test='search-caption']` містить `"Hammer"`, а список карток товарів `By.cssSelector("[data-test='product-name']")` непорожній (`size() > 0`).

---

### UI-02 (`SearchUiTest.searchNonExistingProductReturnsEmptyList`): Пошук неіснуючого товару
* **Вимога SRS:** `F-60`, `F-50` | **Кейс Спринту 3:** `TC-017`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку каталогу.
  2. Ввести `"NonExistingItem9999"` у поле пошуку `By.cssSelector("[data-test='search-query']")`.
  3. Натиснути кнопку пошуку `By.cssSelector("[data-test='search-submit']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))`
  * `ExpectedConditions.numberOfElementsToBe(By.cssSelector("[data-test='product-name']"), 0)`
* **Перевірка:** Кількість карток товарів `By.cssSelector("[data-test='product-name']")` дорівнює `0`.

---

### UI-03 (`LoginUiTest.loginWithValidCredentialsSuccess`): Успішна авторизація покупця
* **Вимога SRS:** `F-20`, `GUI-60` | **Кейс Спринту 3:** `TC-005`
* **URL:** `https://practicesoftwaretesting.com/auth/login`
* **Кроки:**
  1. Перейти на сторінку входу.
  2. Ввести email `customer@practicesoftwaretesting.com` у `By.cssSelector("[data-test='email']")`.
  3. Ввести пароль `welcome01` у `By.cssSelector("[data-test='password']")`.
  4. Натиснути кнопку `By.cssSelector("[data-test='login-submit']")`.
* **Очікування (`WebDriverWait`, 15 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='page-title']"))`
* **Перевірка:** Заголовок сторінки `[data-test='page-title']` містить `"My account"`.

---

### UI-04 (`LoginUiTest.loginWithInvalidPasswordShowsError`): Авторизація з невірним паролем
* **Вимога SRS:** `F-20`, `GUI-60` | **Кейс Спринту 3:** `TC-006`
* **URL:** `https://practicesoftwaretesting.com/auth/login`
* **Кроки:**
  1. Перейти на сторінку входу.
  2. Ввести email `customer@practice.com` у `By.cssSelector("[data-test='email']")`.
  3. Ввести невірний пароль `wrong_password_123` у `By.cssSelector("[data-test='password']")`.
  4. Натиснути кнопку `By.cssSelector("[data-test='login-submit']")`.
* **Очікування (`WebDriverWait`, 15 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='email']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='login-error']"))`
* **Перевірка:** Блок помилки `[data-test='login-error']` містить текст `"Invalid email or password"`.

---

### UI-05 (`CartUiTest.addProductToCartUpdatesCartQuantityBadge`): Додавання товару до кошика
* **Вимога SRS:** `F-80`, `F-90` | **Кейс Спринту 3:** `TC-026`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку каталогу та натиснути на назву першого товару `By.cssSelector("[data-test='product-name']")`.
  2. На детальній сторінці товару натиснути кнопку `By.cssSelector("[data-test='add-to-cart']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='product-name']"))`
  * `ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='add-to-cart']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))`
* **Перевірка:** Бейдж кількості товарів у шапці `[data-test='cart-quantity']` відображає `"1"`.

---

### UI-06 (`CartUiTest.removeItemFromCartClearsCartTable`): Видалення позиції з кошика
* **Вимога SRS:** `F-100`, `GUI-70` | **Кейс Спринту 3:** `TC-031`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку, перейти на картку товару `By.cssSelector("[data-test='product-name']")` та додати його до кошика `By.cssSelector("[data-test='add-to-cart']")`.
  2. Перейти в кошик через `By.cssSelector("[data-test='nav-cart']")`.
  3. Натиснути кнопку видалення `By.cssSelector(".btn-danger, [data-test='delete-product']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.elementToBeClickable(...)` для навігації, додавання та кнопки видалення.
  * `ExpectedConditions.invisibilityOfElementLocated(By.cssSelector("[data-test='product-title']"))`
* **Перевірка:** Рядок товару `By.cssSelector("[data-test='product-title']")` стає невидимим (таблиця кошика очищена).

---

### UI-07 (`CheckoutUiTest.proceedToCheckoutRedirectsToLoginIfNotAuthenticated`): Перенаправлення на вхід при переході до чекауту
* **Вимога SRS:** `F-100`, `F-110`, `S-20` | **Кейс Спринту 3:** `TC-032`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку, додати товар до кошика (`[data-test='product-name']` → `[data-test='add-to-cart']`) та перейти в кошик `By.cssSelector("[data-test='nav-cart']")`.
  2. Натиснути кнопку продовження оформлення `By.cssSelector("[data-test='proceed-1']")`.
* **Очікування (`WebDriverWait`, 15 с):**
  * `ExpectedConditions.elementToBeClickable(...)` для дій додавання та переходу.
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("h3, [data-test='page-title']"))`
* **Перевірка:** Відбувається перехід на форму входу, заголовок `By.cssSelector("h3, [data-test='page-title']")` містить `"Login"`.

---

### UI-08 (`CheckoutUiTest.emptyContactFormDisplaysValidationError`): Валідація порожньої контактної форми
* **Вимога SRS:** `F-140`, `GUI-50`, `GUI-90` | **Кейс Спринту 3:** `TC-009`
* **URL:** `https://practicesoftwaretesting.com/contact`
* **Кроки:**
  1. Перейти на сторінку контактів `https://practicesoftwaretesting.com/contact`.
  2. Натиснути кнопку відправки форми `By.cssSelector("[data-test='contact-submit']")` без заповнення обов'язкових полів.
* **Очікування (`WebDriverWait`, 15 с):**
  * `ExpectedConditions.elementToBeClickable(By.cssSelector("[data-test='contact-submit']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector(".alert-danger"))`
* **Перевірка:** На сторінці відображається блок із повідомленням про помилку валідації `By.cssSelector(".alert-danger")`.
