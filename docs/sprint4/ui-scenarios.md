# Сценарії UI-автоматизації Selenium WebDriver (Спринт 4)

**Продукт:** «ШопЛайн» (practicesoftwaretesting.com)  
**Відповідальний за опис:** Богдан Барановський (Тест-аналітик)  
**Автоматизатор:** Вадим Костюк (Інженер з тестування)  

---

### UI-01 (LoginUiTest): Авторизація покупця
* **Вимога SRS:** F-10, GUI-60
* **URL:** `https://practicesoftwaretesting.com/auth/login`
* **Кроки:**
  1. Перейти на сторінку входу.
  2. Ввести email `customer@practicesoftwaretesting.com` у `By.cssSelector("[data-test='email']")`.
  3. Ввести пароль `welcome01` у `By.cssSelector("[data-test='password']")`.
  4. Натиснути кнопку `By.cssSelector("[data-test='login-submit']")`.
* **Очікування (WebDriverWait):** `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='page-title']"))`
* **Перевірка:** Заголовок сторінки містить `"My account"`.
* **Альтернативний сценарій (невірний пароль):** При введенні `wrong_password_123` відображається помилка у `By.cssSelector("[data-test='login-error']")`.

### UI-02 (SearchUiTest): Пошук товару у каталозі
* **Вимога SRS:** F-40, GUI-20
* **URL:** `https://practicesoftwaretesting.com/`
* **Кроки:**
  1. Ввести `"Hammer"` у поле пошуку `By.cssSelector("[data-test='search-query']")`.
  2. Натиснути кнопку `By.cssSelector("[data-test='search-submit']")`.
* **Очікування:** `ExpectedConditions.visibilityOfElementLocated(By.cssSelector("[data-test='search-caption']"))`
* **Перевірка:** Заголовок результатів містить `"Hammer"`, а список `By.cssSelector("[data-test='product-name']")` непорожній.
* **Альтернативний сценарій (неіснуючий товар):** Запит `"NonExistingItem9999"` повертає 0 елементів.

### UI-03 (CartUiTest): Взаємодія з кошиком
* **Вимога SRS:** F-80, GUI-30
* **URL:** `https://practicesoftwaretesting.com/`
* **Кроки (Додавання товару):**
  1. Клікнути на назву товару `By.cssSelector("[data-test='product-name']")`.
  2. Натиснути кнопку `By.cssSelector("[data-test='add-to-cart']")`.
* **Перевірка:** Бейдж кошика `By.cssSelector("[data-test='cart-quantity']")` відображає `"1"`.
* **Кроки (Видалення товару):**
  1. Перейти в кошик `By.cssSelector("[data-test='nav-cart']`)`.
  2. Натиснути кнопку видалення `By.cssSelector(".btn-danger, [data-test='delete-product']")`.
* **Перевірка:** Елемент `By.cssSelector("[data-test='product-title']")` стає невидимим.

### UI-04 (CheckoutUiTest): Оформлення замовлення та валідація форм
* **Вимога SRS:** F-90, GUI-40
* **URL:** `https://practicesoftwaretesting.com/`
* **Кроки (Перенаправлення неавторизованого користувача):**
  1. Додати товар до кошика та перейти в кошик `By.cssSelector("[data-test='nav-cart']")`.
  2. Натиснути кнопку продовження `By.cssSelector("[data-test='proceed-1']")`.
* **Перевірка:** Відбувається редирект, заголовок `By.cssSelector("h3, [data-test='page-title']")` містить `"Login"`.
* **Кроки (Валідація порожньої контактної форми):**
  1. Перейти на сторінку контактів `https://practicesoftwaretesting.com/contact`.
  2. Натиснути кнопку відправки форми `By.cssSelector("[data-test='contact-submit']")` без заповнення обов'язкових полів.
* **Перевірка:** Відображається блок із повідомленням про помилку валідації `By.cssSelector(".alert-danger")`.
