# Протокол виконання автотестів (Surefire Summary) – Спринт 4

- **Проєкт:** `shopline-module` (`ua.kpi.tzyapz.shopline`, модуль `v1`)
- **Дата та час запуску:** 16 листопада 2026, 11:45
- **Середовище:** Linux Mint (JDK 17, Maven 3.9, JUnit 5, Selenium 4.21, Surefire 3.2.5)
- **Відповідальний (Test Lead):** Алферов Б.

---

## 1. Прогін модульних тестів (Unit Tests)

- **Команда запуску:** `mvn clean test`
- **Підсумок Surefire:** `Tests run: 28, Failures: 5, Errors: 0, Skipped: 0` (час виконання: `2.931 s`)

| Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `CartTest` | 7 | 6 | 1 | 0 | 0 | 0.037 | FAIL |
| `DiscountCalculatorTest` | 8 | 6 | 2 | 0 | 0 | 0.027 | FAIL |
| `PromoCodeTest` | 3 | 3 | 0 | 0 | 0 | 0.026 | PASS |
| `PromoCodeValidatorTest` | 10 | 8 | 2 | 0 | 0 | 0.173 | FAIL |
| **Разом (Unit)** | **28** | **23** | **5** | **0** | **0** | **0.263** | **FAIL** |

> **Примітка:** 26 унікальних тестових методів формують **28 прогонів** у звіті Surefire за рахунок параметризованого тесту (`@ParameterizedTest` на 3 набори даних у `PromoCodeValidatorTest`).

---

## 2. Прогін UI-тестів (Selenium WebDriver)

- **Команда запуску:** `mvn test -Dgroups=ui -DexcludedGroups=""`
- **Підсумок Surefire:** `Tests run: 8, Failures: 0, Errors: 0, Skipped: 0` (час виконання: `30.131 s`, `BUILD SUCCESS`)

| UI Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `ui.CheckoutUiTest` | 2 | 2 | 0 | 0 | 0 | 7.381 | PASS |
| `ui.SearchUiTest` | 2 | 2 | 0 | 0 | 0 | 5.705 | PASS |
| `ui.LoginUiTest` | 2 | 2 | 0 | 0 | 0 | 6.760 | PASS |
| `ui.CartUiTest` | 2 | 2 | 0 | 0 | 0 | 7.984 | PASS |
| **Разом (UI)** | **8** | **8** | **0** | **0** | **0** | **27.830** | **PASS** |

---

## 3. Список `failed` тестів та виявлених дефектів модуля `v1` (мітка `module`)

| ID дефекту | Тестовий клас та метод | Рядок | Вимога BL | Очікуваний / фактичний результат |
|---|---|---|---|---|
| **BUG-M1** | `CartTest.addQuantityAbove99ThrowsException` | `:95` | BL-10 | Очікувався `IllegalArgumentException` при `qty = 100`, але виняток не викинуто (у коді `MAX_QTY = 100` замість `99`). |
| **BUG-M2 (округлення)** | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | `:44` | BL-50 | `expected: <116.67> but was: <116.66>` – сума знижки `116.6665` (5% від `2333.33 грн`) округлена вниз (`RoundingMode.DOWN`) замість `HALF_UP`. |
| **BUG-M2 (поріг 10%)** | `DiscountCalculatorTest.volumeDiscountTenPercentExactBoundary` | `:118` | BL-50 | `expected: <10> but was: <5>` – для суми рівно `5000.00 грн` повертається 5% замість 10% (строга нерівність `> 5000` замість `>= 5000`). |
| **BUG-M3** | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | `:84` | BL-70 | `expected: <true> but was: <false>` – промокод відхиляється у день закінчення терміну дії (`today == expiresOn`). |
| **BUG-M4** | `PromoCodeValidatorTest.usedSingleUsePromoCodeWithFutureDateReturnsInvalid` | `:108` | BL-70 | `expected: <false> but was: <true>` – уже використаний одноразовий промокод (`singleUse = true, used = true`) проходить валідацію. |
