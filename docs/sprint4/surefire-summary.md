# Протокол виконання автотестів (Surefire Summary) — Спринт 4

* **Проєкт:** `shopline-module` (`ua.kpi.tzyapz.shopline`, модуль `v1`)
* **Дата та час запуску:** 12 листопада 2026, 20:00
* **Середовище:** JDK 17 (Temurin), Maven 3.9, JUnit 5.10, Selenium 4.21, Surefire 3.2.5
* **Виконавець:** Барановський Б. (Тест-аналітик)

---

## 1. Прогін модульних тестів (Unit Tests)

* **Команда запуску:** `mvn clean test`
* **Підсумок Surefire:** `Tests run: 25, Failures: 2, Errors: 0, Skipped: 0`

| Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `CartTest` | 6 | 6 | 0 | 0 | 0 | 0.062 | PASS |
| `DiscountCalculatorTest` | 7 | 6 | 1 | 0 | 0 | 0.028 | FAIL |
| `PromoCodeTest` | 3 | 3 | 0 | 0 | 0 | 0.021 | PASS |
| `PromoCodeValidatorTest` | 9 | 8 | 1 | 0 | 0 | 0.101 | FAIL |
| **Разом (Unit)** | **25** | **23** | **2** | **0** | **0** | **0.212** | **FAIL** |

> **Примітка:** 23 унікальні тестові методи з `coverage-matrix.md` формують **25 прогонів** у звіті Surefire за рахунок розгортання наборів даних у параметризованих тестах (`@ParameterizedTest`).

---

## 2. Повний прогін разом із UI-тестами (Unit + Selenium WebDriver)

* **Команда запуску:** `mvn test -DexcludedGroups=none`
* **Підсумок Surefire:** `Tests run: 33, Failures: 2, Errors: 0, Skipped: 0` (загальний час: `26.573 s`)

| UI Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `ui.CartUiTest` | 2 | 2 | 0 | 0 | 0 | 7.695 | PASS |
| `ui.CheckoutUiTest` | 2 | 2 | 0 | 0 | 0 | 5.289 | PASS |
| `ui.LoginUiTest` | 2 | 2 | 0 | 0 | 0 | 4.791 | PASS |
| `ui.SearchUiTest` | 2 | 2 | 0 | 0 | 0 | 4.252 | PASS |
| **Разом (UI)** | **8** | **8** | **0** | **0** | **0** | **22.027** | **PASS** |

---

## 3. Список `failed` тестів та пов'язаних дефектів (мітка `module`)

| ID дефекту | Тестовий клас та метод | Рядок | Вимога BL | Очікуваний / фактичний результат |
|---|---|---|---|---|
| **BUG-S4-01** | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | `:44` | BL-30 | `expected: <116.67> but was: <116.66>` — сума знижки `116.6665` (5% від `2333.33 грн`) округлена вниз замість `HALF_UP`. |
| **BUG-S4-02** | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | `:84` | BL-70 | `expected: <true> but was: <false>` — промокод відхиляється у день закінчення терміну дії (`today == expiresOn`). |