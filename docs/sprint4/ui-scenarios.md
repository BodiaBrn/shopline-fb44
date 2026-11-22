# Сценарії UI-автоматизації Selenium WebDriver (Спринт 4)

**Продукт:** «ШопЛайн» (https://practicesoftwaretesting.com)  
**Відповідальний за опис:** Богдан Барановський (Тест-аналітик)  
**Автоматизатори:** Вадим Костюк, Богдан Алферов (Інженери з тестування)  

---

## Зведена таблиця простежуваності UI-тестів

| ID | Тест-клас та метод | Вимога SRS | Кейс Спринту 3 | Назва сценарію (`@DisplayName`) | Рецензент |
|---|---|---|---|---|---|
| **UI-01** | `SearchUiTest.searchPliersCaseInsensitiveReturnsResults` | `F-60`, `GUI-20` | `TC-016` | Пошук товару без урахування регістру (`'PLIERS'`) повертає відповідні картки | Шандра О. |
| **UI-02** | `SearchUiTest.searchNonExistingProductReturnsEmptyList` | `F-60`, `F-50` | `TC-017` | Пошук неіснуючого товару повертає порожній список | Шандра О. |
| **UI-03** | `LoginUiTest.loginWithValidCredentialsSuccess` | `F-20`, `GUI-60` | `TC-005` | Успішна авторизація з валідними обліковими даними | Шандра О. |
| **UI-04** | `LoginUiTest.loginWithInvalidPasswordShowsError` | `F-20`, `GUI-60` | `TC-006` | Спроба авторизації зареєстрованого користувача з невірним паролем відображає помилку | Шандра О. |
| **UI-05** | `CartUiTest.addProductToCartUpdatesCartQuantityBadge` | `F-80`, `F-90` | `TC-026` | Додавання товару з детальної сторінки оновлює бейдж кошика | Шандра О. |
| **UI-06** | `CartUiTest.changeItemQuantityInCartRecalculatesTotal` | `F-100`, `GUI-70` | `TC-031` | Зміна кількості товару в кошику перераховує вартість рядка та підсумок | Шандра О. |
| **UI-07** | `CheckoutUiTest.authenticatedCheckoutCompletesSuccessfully` | `F-100`, `F-110`, `F-120` | `TC-032` | Успішне оформлення замовлення авторизованим покупцем | Шандра О. |
| **UI-08** | `CheckoutUiTest.contactFormWith49CharMessageShowsValidationError` | `F-140`, `GUI-50`, `GUI-90` | `TC-009` | Повідомлення контактної форми довжиною 49 символів відхиляється валідацією | Шандра О. |

---

## Детальний опис сценаріїв

### UI-01 (`SearchUiTest.searchPliersCaseInsensitiveReturnsResults`): Регістронезалежний пошук товару у каталозі
* **Вимога SRS:** `F-60`, `GUI-20` | **Кейс Спринту 3:** `TC-016`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку каталогу `https://practicesoftwaretesting.com`.
  2. Ввести `"PLIERS"` у поле пошуку `By.cssSelector("[data-test='search-query']")`.
  3. Натиснути кнопку пошуку `By.cssSelector("[data-test='search-submit']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-query']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']"))`
  * `ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("[data-test='product-name']"), 0)`
* **Перевірка:** `Assertions.assertAll`: текст заголовка результатів `[data-test='search-caption']` у нижньому регістрі (`toLowerCase()`) містить `"pliers"`, а серед карток `By.cssSelector("[data-test='product-name']")` є принаймні один елемент, текст якого у нижньому регістрі містить `"pliers"`.

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

### UI-04 (`LoginUiTest.loginWithInvalidPasswordShowsError`): Авторизація зареєстрованого користувача з невірним паролем
* **Вимога SRS:** `F-20`, `GUI-60` | **Кейс Спринту 3:** `TC-006`
* **URL:** `https://practicesoftwaretesting.com/auth/login`
* **Кроки:**
  1. Перейти на сторінку входу `https://practicesoftwaretesting.com/auth/login`.
  2. Ввести email `customer@practicesoftwaretesting.com` у `By.cssSelector("[data-test='email']")`.
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
  1. Відкрити головну сторінку каталогу `https://practicesoftwaretesting.com`, обрати першу картку товару `By.cssSelector("a.card")`, що не містить `By.cssSelector("[data-test='out-of-stock']")` (`openFirstAvailableProduct()`), і відкрити її.
  2. На детальній сторінці товару натиснути кнопку `By.cssSelector("[data-test='add-to-cart']")`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='product-name']"))`
  * `ExpectedConditions.elementToBeClickable(...)` для картки товару `a.card` та кнопки `[data-test='add-to-cart']`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='cart-quantity']"))`
* **Перевірка:** Бейдж кількості товарів у шапці `[data-test='cart-quantity']` відображає `"1"`.

---

### UI-06 (`CartUiTest.changeItemQuantityInCartRecalculatesTotal`): Зміна кількості товару в кошику та перерахунок підсумку
* **Вимога SRS:** `F-100`, `GUI-70` | **Кейс Спринту 3:** `TC-031`
* **URL:** `https://practicesoftwaretesting.com`
* **Кроки:**
  1. Відкрити головну сторінку каталогу `https://practicesoftwaretesting.com`, обрати першу картку товару `By.cssSelector("a.card")`, що не містить `By.cssSelector("[data-test='out-of-stock']")` (`openFirstAvailableProduct()`), і відкрити її.
  2. На детальній сторінці товару натиснути кнопку `By.cssSelector("[data-test='add-to-cart']")` та перейти до кошика через `By.cssSelector("[data-test='nav-cart']")`.
  3. Зчитати ціну за одиницю товару з `By.cssSelector("[data-test='product-price']")` (обчислити очікувану суму `unitPrice × 2` з округленням `RoundingMode.HALF_UP` до 2 знаків) та поточний текст загального підсумку з `By.cssSelector("[data-test='cart-total']")`.
  4. У полі кількості `By.cssSelector("[data-test='product-quantity']")` очистити поточне значення (послідовним надсиланням `Keys.BACK_SPACE` × 3 та `Keys.DELETE` × 3), ввести `"2"` і натиснути `Keys.ENTER`.
* **Очікування (`WebDriverWait`, 10 с):**
  * `ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='product-name']"))`
  * `ExpectedConditions.elementToBeClickable(...)` для картки товару `a.card`, кнопок `[data-test='add-to-cart']`, `[data-test='nav-cart']` та поля `[data-test='product-quantity']`
  * `ExpectedConditions.visibilityOfElementLocated(...)` для `[data-test='cart-quantity']` та `[data-test='product-price']`
  * Очікування зміни тексту `By.cssSelector("[data-test='cart-total']")` порівняно з попереднім значенням (`!d.findElement(By.cssSelector("[data-test='cart-total']")).getText().trim().equals(oldTotalText)`)
* **Перевірка:** `Assertions.assertAll`: вартість рядка `By.cssSelector("[data-test='line-price']")` та загальний підсумок кошика `By.cssSelector("[data-test='cart-total']")` дорівнюють очікуваній сумі (`unitPrice × 2`, масштаб 2 знаки після коми, `RoundingMode.HALF_UP`).

---

### UI-07 (`CheckoutUiTest.authenticatedCheckoutCompletesSuccessfully`): Успішне оформлення замовлення авторизованим покупцем
* **Вимога SRS:** `F-100`, `F-110`, `F-120` | **Кейс Спринту 3:** `TC-032`
* **URL:** `https://practicesoftwaretesting.com/auth/login` → `https://practicesoftwaretesting.com`
* **Кроки:**
  1. **Авторизація (Arrange):** перейти на `https://practicesoftwaretesting.com/auth/login`, ввести `customer@practicesoftwaretesting.com` у `By.cssSelector("[data-test='email']")`, `welcome01` у `By.cssSelector("[data-test='password']")` та натиснути `By.cssSelector("[data-test='login-submit']")`.
  2. **Додавання товару та перехід у кошик:** відкрити `https://practicesoftwaretesting.com`, обрати першу доступну картку `By.cssSelector("a.card")` без `By.cssSelector("[data-test='out-of-stock']")` (`openFirstAvailableProduct()`), натиснути `By.cssSelector("[data-test='add-to-cart']")` та перейти в кошик через `By.cssSelector("[data-test='nav-cart']")`.
  3. **Крок 1 чекауту (Cart):** натиснути кнопку `By.cssSelector("[data-test='proceed-1']")`.
  4. **Крок 2 чекауту (Sign in):** натиснути кнопку `By.cssSelector("[data-test='proceed-2']")` (користувач уже авторизований).
  5. **Крок 3 чекауту (Billing Address):** дочекатися підвантаження адреси профілю, за потреби заповнити порожні наявні поля (`fillIfBlank`): `By.cssSelector("[data-test='street']")` (`"Test Street 1"`), `By.cssSelector("[data-test='house_number']")` (`"1"`), `By.cssSelector("[data-test='city']")` (`"Kyiv"`), `By.cssSelector("[data-test='state']")` (`"Kyiv"`), `By.cssSelector("[data-test='country']")` (`"Ukraine"`), `By.cssSelector("[data-test='postal_code']")` (`"01001"`), після чого натиснути `By.cssSelector("[data-test='proceed-3']")`.
  6. **Крок 4 чекауту (Payment):** у випадаючому списку `By.cssSelector("[data-test='payment-method']")` обрати значення `"cash-on-delivery"` (`Select.selectByValue`) і натиснути `By.cssSelector("[data-test='finish']")`.
* **Очікування (`WebDriverWait`, 15 с; додаткове очікування адреси профілю — 5 с):**
  * `ExpectedConditions.visibilityOfElementLocated(...)` для `[data-test='email']`, `[data-test='page-title']`, `[data-test='cart-quantity']`, `[data-test='street']`, `[data-test='payment-method']`, `[data-test='payment-success-message']`
  * `ExpectedConditions.presenceOfElementLocated(By.cssSelector("[data-test='product-name']"))`
  * `ExpectedConditions.elementToBeClickable(...)` для картки товару `a.card`, `[data-test='add-to-cart']`, `[data-test='nav-cart']`, `[data-test='proceed-1']`, `[data-test='proceed-2']`, `[data-test='proceed-3']`, `[data-test='finish']`
  * Додатковий `WebDriverWait` (5 с) на непорожнє значення `value` поля `[data-test='street']` (з ігноруванням `TimeoutException`).
* **Перевірка:** Блок підтвердження `By.cssSelector("[data-test='payment-success-message']")` містить текст `"Payment was successful"`.

---

### UI-08 (`CheckoutUiTest.contactFormWith49CharMessageShowsValidationError`): Валідація мінімальної довжини повідомлення (49 символів) у контактній формі
* **Вимога SRS:** `F-140`, `GUI-50`, `GUI-90` | **Кейс Спринту 3:** `TC-009`
* **URL:** `https://practicesoftwaretesting.com/contact`
* **Кроки:**
  1. Перейти на сторінку контактів `https://practicesoftwaretesting.com/contact`.
  2. Ввести `"Vadym"` у `By.cssSelector("[data-test='first-name']")`.
  3. Ввести `"Kostiuk"` у `By.cssSelector("[data-test='last-name']")`.
  4. Ввести `"vadym.kostiuk@example.com"` у `By.cssSelector("[data-test='email']")`.
  5. У випадаючому списку `By.cssSelector("[data-test='subject']")` обрати значення `"customer-service"` (`Select.selectByValue`).
  6. Ввести повідомлення довжиною 49 символів (`"a".repeat(49)`) у поле `By.cssSelector("[data-test='message']")`.
  7. Натиснути кнопку відправки форми `By.cssSelector("[data-test='contact-submit']")`.
* **Очікування (`WebDriverWait`, 15 с):**
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='first-name']"))`
  * `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='message-error'], .alert-danger"))`
* **Перевірка:** Блок помилки `By.cssSelector("[data-test='message-error'], .alert-danger")` відображається (`isDisplayed()`) і містить текст `"50"`.
