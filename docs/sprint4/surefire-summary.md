# Протокол виконання автотестів (Surefire Summary) – Спринт 4

- **Проєкт:** `shopline-module` (`ua.kpi.tzyapz.shopline`, модуль `v1`)
- **Дата та час запуску:** 21 листопада 2026, 18:10
- **Середовище:** Linux Mint (JDK 17, Maven 3.9, JUnit 5.10.2, Selenium 4.21.0, Surefire 3.2.5)
- **Відповідальний (Test Lead):** Алферов Б.

---

## 1. Виконання нормативів спринту (План / Факт – склад 5 осіб)

| Показник / Артефакт | План (норматив «5») | Факт | Статус |
|---|---|---|---|
| Модульні тести (JUnit 5) | ≥ 20 методів | 34 методи (37 прогонів у Surefire) | Виконано |
| Внесок кожного учасника | ≥ 3 юніт-тести | ≥ 3 юніт-тести на кожного учасника (згідно з матрицею) | Виконано |
| UI-тести (Selenium WebDriver, `@Tag("ui")`) | ≥ 8 сценаріїв | 8 тест-методів (`UI-01` – `UI-08`) | Виконано |
| Записані сценарії Selenium IDE | 1–2 сценарії | 2 сценарії (`shopline.side` + експорт) | Виконано |
| Виявлені дефекти модуля `v1` (мітка `module`) | ≥ 5 дефектів | 7 дефектів (`BUG-MOD-001` – `BUG-MOD-007`, Issues `#13`–`#19`) | Виконано |

---

## 2. Прогін модульних тестів (Unit Tests)

- **Команда запуску:** `mvn clean test`
- **Підсумок Surefire:** `Tests run: 37, Failures: 8, Errors: 0, Skipped: 0` (загальний час збірки: `3.397 s`)

| Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `PromoCodeValidatorTest` | 13 | 10 | 3 | 0 | 0 | 0.181 | FAIL |
| `CartTest` | 14 | 11 | 3 | 0 | 0 | 0.136 | FAIL |
| `DiscountCalculatorTest` | 10 | 8 | 2 | 0 | 0 | 0.030 | FAIL |
| **Разом (Unit)** | **37** | **29** | **8** | **0** | **0** | **0.347** | **FAIL** |

> **Примітка:** 34 унікальні тестові методи формують **37 прогонів** у звіті Surefire за рахунок трьох параметризованих тестів (`@ParameterizedTest` по 2 набори даних у `CartTest` та `PromoCodeValidatorTest`). 8 падінь підтверджують **7 унікальних дефектів модуля `v1`** (`#13`–`#19`), оскільки дефект `BUG-MOD-004` (`#16`) перевіряється окремо для методів `validate` та `apply`.

---

## 3. Прогін UI-тестів (Selenium WebDriver)

- **Команда запуску:** `mvn clean test -Dgroups=ui -DexcludedGroups=""`
- **Підсумок Surefire:** `Tests run: 8, Failures: 0, Errors: 0, Skipped: 0` (загальний час збірки: `38.643 s`, `BUILD SUCCESS`)

| UI Тестовий клас | Tests run | Passed | Failures | Errors | Skipped | Час (с) | Статус |
|---|---|---|---|---|---|---|---|
| `ui.CheckoutUiTest` | 2 | 2 | 0 | 0 | 0 | 13.390 | PASS |
| `ui.SearchUiTest` | 2 | 2 | 0 | 0 | 0 | 5.497 | PASS |
| `ui.LoginUiTest` | 2 | 2 | 0 | 0 | 0 | 7.216 | PASS |
| `ui.CartUiTest` | 2 | 2 | 0 | 0 | 0 | 8.805 | PASS |
| **Разом (UI)** | **8** | **8** | **0** | **0** | **0** | **34.908** | **PASS** |

---

## 4. Список `failed` тестів та виявлених дефектів модуля `v1` (GitHub Issues `#13`–`#19`)

| ID дефекту (Issue) | Тестовий клас та метод | Рядок | Вимога BL | Severity | Очікуваний / фактичний результат |
|---|---|---|---|---|---|
| **BUG-MOD-001** (`#13`) | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | `:45` | BL-50 | Major | `expected: <116.67> but was: <116.66>` – сума знижки `116.6665` (5 % від `2333.33 грн`) округлена вниз (`RoundingMode.DOWN`) замість `HALF_UP`. |
| **BUG-MOD-002** (`#14`) | `DiscountCalculatorTest.volumeDiscountTenPercentExactBoundary` | `:118` | BL-50 | Major | `expected: <10> but was: <5>` – для суми рівно `5000.00 грн` повертається 5 % замість 10 % (строга нерівність `> 0` замість `>= 0`). |
| **BUG-MOD-003** (`#15`) | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | `:87` | BL-70 | Major | `expected: <true> but was: <false>` – промокод відхиляється у день закінчення терміну дії (`today.equals(expiresOn)`). |
| **BUG-MOD-004** (`#16`) | `PromoCodeValidatorTest.applyUsedSingleUsePromoCodeThrowsException`<br>`PromoCodeValidatorTest.usedSingleUsePromoCodeWithFutureDateReturnsInvalid` | `:57`<br>`:111` | BL-70, BL-80 | Critical | Вже використаний одноразовий промокод (`singleUse = true, used = true`) з актуальною датою проходить валідацію (`valid=true`) та застосовується без винятку `IllegalArgumentException`. |
| **BUG-MOD-005** (`#17`) | `CartTest.addQuantityAbove99ThrowsException` | `:148` | BL-10 | Minor | Очікувався `IllegalArgumentException` при `qty = 100`, але виняток не викинуто (у коді `MAX_QTY = 100` замість `99`). |
| **BUG-MOD-006** (`#18`) | `CartTest.addSameSkuTwiceIncreasesQuantityOfExistingLine` | `:164` | BL-20 | Major | `expected: <5> / <500.00> but was: <3> / <300.00>` – повторне додавання позиції з тим самим `sku` перезаписує наявний рядок (`items.put`) замість підсумовування кількості. |
| **BUG-MOD-007** (`#19`) | `CartTest.addItemWithBlankOrNullNameThrowsException` | `:179` | BL-10 | Minor | Очікувався `IllegalArgumentException` при `name = null` та `name = "   "`, але обидві позиції без назви додаються до кошика без винятку. |

---

## 5. Висновок про стан модуля `v1`

Модуль бізнес-логіки `shopline-module` версії `v1` **не готовий до релізу в продуктивне середовище (Not Ready for Release)**. Під час автоматизованого тестування (34 юніт-методи, 37 прогонів) виявлено та підтверджено **7 дефектів бізнес-логіки** (`BUG-MOD-001` — `BUG-MOD-007`, GitHub Issues `#13`–`#19`), з яких **1 Critical**, **4 Major** та **2 Minor**:

1. **Критичні та фінансові ризики (Critical / Major):**
   - **Неконтрольоване повторне використання промокодів (`BUG-MOD-004`, `#16`, Critical):** відсутність перевірки прапорців `singleUse` та `used` у `PromoCodeValidator` дозволяє багаторазово застосовувати одноразові промокоди, що несе прямі фінансові збитки.
   - **Втрата товарів та заниження підсумку кошика (`BUG-MOD-006`, `#18`, Major):** повторне додавання позиції з тим самим артикулом (`sku`) у `Cart.addItem` перезаписує рядок замість збільшення кількості (`BL-20`), через що покупець втрачає попередньо додані одиниці товару, а контроль ліміту `99` шт. не спрацьовує.
   - **Помилки розрахунку знижок (`BUG-MOD-001`, `#13` та `BUG-MOD-002`, `#14`, Major):** замовлення рівно на `5000.00 грн` отримує 5 % знижки замість 10 % (втрата 250 грн для клієнта), а сума знижки округлюється вниз (`RoundingMode.DOWN`) замість `HALF_UP` (`BL-50`).
2. **Дефекти валідації та граничних умов (Major / Minor):**
   - **Блокування дійсних промокодів (`BUG-MOD-003`, `#15`, Major):** валідний промокод помилково відхиляється у день закінчення терміну дії (`today.equals(expiresOn)`), порушуючи `BL-70`.
   - **Порушення обмежень вхідних даних кошика (`BUG-MOD-005`, `#17` та `BUG-MOD-007`, `#19`, Minor):** метод `Cart.addItem` дозволяє додати `100` одиниць товару в один рядок замість максимуму `99` і приймає товари з `null` або порожньою назвою (`BL-10`).
3. **Умови випуску релізу (`v2`):**
   - Передати дефекти `#13`–`#19` розробникам для усунення у версії модуля `v2`.
   - Повторний реліз можливий лише після повного проходження регресійного набору модульних тестів (`mvn clean test` має завершитися зі статусом `BUILD SUCCESS`, `Failures: 0, Errors: 0`).
