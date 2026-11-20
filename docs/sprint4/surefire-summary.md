# Протокол виконання автотестів (Surefire Summary) – Спринт 4

- **Проєкт:** `shopline-module` (`ua.kpi.tzyapz.shopline`, модуль `v1`)
- **Дата та час запуску:** 16 листопада 2026, 11:45
- **Середовище:** Linux Mint (JDK 17, Maven 3.9, JUnit 5, Selenium 4.21, Surefire 3.2.5)
- **Відповідальний (Test Lead):** Алферов Б.

---

## 1. Виконання нормативів спринту (План / Факт – склад 5 осіб)

| Показник / Артефакт | План (норматив «5») | Факт | Статус |
|---|---|---|---|
| Модульні тести (JUnit 5) | ≥ 20 методів | 26 методів (28 прогонів у Surefire) | Виконано |
| Внесок кожного учасника | ≥ 3 юніт-тести | 10 / 6 / 4 / 3 / 3 | Виконано |
| UI-тести (Selenium WebDriver, `@Tag("ui")`) | ≥ 8 сценаріїв | 8 тест-методів (`UI-01` – `UI-08`) | Виконано |
| Записані сценарії Selenium IDE | 1–2 сценарії | 2 сценарії (`shopline.side` + експорт) | Виконано |
| Виявлені дефекти модуля `v1` (мітка `module`) | ≥ 5 дефектів | 5 дефектів (`BUG-MOD-001` – `BUG-MOD-005`) | Виконано |

---

## 2. Прогін модульних тестів (Unit Tests)

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

## 3. Прогін UI-тестів (Selenium WebDriver)

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

## 4. Список `failed` тестів та виявлених дефектів модуля `v1` (GitHub Issues `#13`–`#17`)

| ID дефекту (Issue) | Тестовий клас та метод | Рядок | Вимога BL | Severity | Очікуваний / фактичний результат |
|---|---|---|---|---|---|
| **BUG-MOD-001** (`#13`) | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | `:44` | BL-50 | Major | `expected: <116.67> but was: <116.66>` – сума знижки `116.6665` (5% від `2333.33 грн`) округлена вниз (`RoundingMode.DOWN`) замість `HALF_UP`. |
| **BUG-MOD-002** (`#14`) | `DiscountCalculatorTest.volumeDiscountTenPercentExactBoundary` | `:118` | BL-50 | Major | `expected: <10> but was: <5>` – для суми рівно `5000.00 грн` повертається 5% замість 10% (строга нерівність `> 5000` замість `>= 5000`). |
| **BUG-MOD-003** (`#15`) | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | `:84` | BL-70 | Major | `expected: <true> but was: <false>` – промокод відхиляється у день закінчення терміну дії (`today == expiresOn`). |
| **BUG-MOD-004** (`#16`) | `PromoCodeValidatorTest.usedSingleUsePromoCodeWithFutureDateReturnsInvalid` | `:108` | BL-70 | Critical | `expected: <false> but was: <true>` – уже використаний одноразовий промокод (`singleUse = true, used = true`) проходить валідацію. |
| **BUG-MOD-005** (`#17`) | `CartTest.addQuantityAbove99ThrowsException` | `:95` | BL-10 | Minor | Очікувався `IllegalArgumentException` при `qty = 100`, але виняток не викинуто (у коді `MAX_QTY = 100` замість `99`). |

---

## 5. Висновок про стан модуля `v1`

Модуль бізнес-логіки `shopline-module` версії `v1` **не готовий до релізу в продуктивне середовище (Not Ready for Release)** і потребує виправлення 5 виявлених дефектів (`BUG-MOD-001` — `BUG-MOD-005`, Issues `#13`–`#17`):
- **Фінансові та бізнес-ризики:** Некоректне округлення знижок (`BUG-MOD-001`), помилка граничної умови на порозі 5000 грн (`BUG-MOD-002`) та можливість повторного застосування одноразових промокодів (`BUG-MOD-004`) призводять до прямих фінансових втрат магазину.
- **Логічні обмеження:** Відхилення валідного промокоду в останній день дії (`BUG-MOD-003`) погіршує клієнтський досвід, а перевищення ліміту кількості товару в рядку кошика (`BUG-MOD-005`) порушує специфікацію `BL-10`.
- **Рекомендація:** Передати дефекти `#13`–`#17` на виправлення у версію модуля `v2`, після чого виконати повторний регресійний прогін набору модульних тестів (`mvn clean test` має завершитися зі статусом `BUILD SUCCESS`, `Failures: 0`).
