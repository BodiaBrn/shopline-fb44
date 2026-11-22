# Матриця покриття вимог бізнес-логіки (Спринт 4)

**Продукт:** П1 «ШопЛайн» (ShopLine) | **Стенд:** https://practicesoftwaretesting.com
**Відповідальний:** Богдан Барановський (Тест-аналітик)
**Рецензент:** Олександр Шандра (Scrum Master / Reviewer)

## Покриття правил бізнес-логіки юніт-тестами

| ID вимоги | Правило бізнес-логіки | Граничні значення / Умови | Назва тест-класу та методу | Автор тесту | Рецензент |
|---|---|---|---|---|---|
| BL-10 | Додавання валідної позиції збільшує підсумок і лічильник | 250.00 × 2 → 500.00 | `CartTest.addItemIncreasesSubtotalAndCount` | Костюк | Шандра |
| BL-10 | Кількість 0 → `IllegalArgumentException` | qty = 0 | `CartTest.addInvalidQuantityThrowsException` | Костюк | Шандра |
| BL-10 | Ціна строго > 0 | 0.00; −0.01 (параметризований) | `CartTest.addItemWithNonPositivePriceThrowsException` | Костюк | Шандра |
| BL-10 | Порожній / null `sku` → `IllegalArgumentException` | null; "   " | `CartTest.addItemWithBlankOrNullSkuThrowsException` | Костюк | Шандра |
| BL-10 | Максимальна допустима кількість | qty = 99 | `CartTest.addItemMaxQuantitySuccess` | Барановський | Шандра |
| BL-10 | Кількість понад максимум → `IllegalArgumentException` (BUG-MOD-005, #17) | qty = 100 | `CartTest.addQuantityAbove99ThrowsException` | Шандра | Алферов |
| BL-10 | Порожня / null назва → `IllegalArgumentException` (BUG-MOD-007, #19) | null; "   " | `CartTest.addItemWithBlankOrNullNameThrowsException` | Шандра | Алферов |
| BL-20 | Повторне додавання того самого `sku` збільшує кількість рядка (BUG-MOD-006, #18) | 2 + 3 → 5, підсумок 500.00 | `CartTest.addSameSkuTwiceIncreasesQuantityOfExistingLine` | Шандра | Алферов |
| BL-40 | Порожній кошик: subtotal 0.00, itemCount 0 | новий кошик | `CartTest.newCartIsEmpty` | Костюк | Шандра |
| BL-40 | Видалення / зміна кількості неіснуючого `sku` → `NoSuchElementException` | "UNKNOWN-SKU" | `CartTest.operationsOnMissingSkuThrowNoSuchElementException` | Костюк | Шандра |
| BL-40 | Межі `changeQty` | 0 → виняток; 1 і 99 дозволені | `CartTest.changeQtyBoundaryValues` | Костюк | Шандра |
| BL-40 | Видалення позиції зменшує subtotal та itemCount | наявний sku | `CartTest.removeItemSuccess` | Алферов | Шандра |
| BL-40 | Зміна кількості перераховує підсумок | 1 → 3, 300.00 | `CartTest.changeQtyUpdatesSubtotal` | Алферов | Шандра |
| BL-50 | 0 % нижче першого порогу | 1999.99 → 0 % | `DiscountCalculatorTest.volumeDiscountBelowFirstTierReturnsZeroPercent` | Барановський | Шандра |
| BL-50 | 5 % від 2000 включно | 2000.00 → 5 % | `DiscountCalculatorTest.volumeDiscountFivePercentTier` | Шандра | Алферов |
| BL-50 | Округлення суми знижки HALF_UP (BUG-MOD-001, #13) | 2333.33 → 116.67 | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | Шандра | Алферов |
| BL-50 | Нижня сторона порогу 5000 | 4999.99 → 5 % | `DiscountCalculatorTest.volumeDiscountJustBelowTenPercentTierReturnsFivePercent` | Шандра | Алферов |
| BL-50 | 10 % від 5000 включно (BUG-MOD-002, #14) | 5000.00 → 10 % | `DiscountCalculatorTest.volumeDiscountTenPercentExactBoundary` | Шандра | Алферов |
| BL-50 | Нижня сторона порогу 10 000 | 9999.99 → 10 % | `DiscountCalculatorTest.discountPercent_JustBelowHighestTier_ReturnsTenPercent` | Мулявка | Шандра |
| BL-50 | 15 % від 10 000, знижки не сумуються | 10000.00 → 1500.00 | `DiscountCalculatorTest.volumeDiscount_HighestTier_DoesNotSumWithLowerTiers` | Мулявка | Шандра |
| BL-50 | Від'ємна сума → `IllegalArgumentException` | −0.01 | `DiscountCalculatorTest.volumeDiscount_NegativeSubtotal_ThrowsException` | Мулявка | Шандра |
| BL-60 | Доставка 120.00, безкоштовна від 1500 включно | 1499.99 → 120.00; 1500.00 → 0.00 | `DiscountCalculatorTest.shippingFeeCalculation` | Шандра | Алферов |
| BL-60 | Від'ємна сума → `IllegalArgumentException` | −0.01 | `DiscountCalculatorTest.shippingFeeNegativeSubtotalThrowsException` | Шандра | Алферов |
| BL-70 | Довжина коду поза 6–10 → невалідний | "SHORT" (5); "ABCDEFGHIJK" (11) (параметризований) | `PromoCodeValidatorTest.invalidCodeFormatFailsValidation` | Шандра | Алферов |
| BL-70 | Граничні довжини 6 і 10 валідні | "SALE10" (6); "SUMMER2026" (10) | `PromoCodeValidatorTest.validCodeLengthBoundaries6And10PassValidation` | Костюк | Шандра |
| BL-70 | Прострочений промокод невалідний | expiresOn = сьогодні − 1 | `PromoCodeValidatorTest.expiredPromoCodeFailsValidation` | Шандра | Алферов |
| BL-70 | Промокод діє в день завершення включно (BUG-MOD-003, #15) | expiresOn = сьогодні | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | Алферов | Шандра |
| BL-70 | Використаний одноразовий код невалідний (BUG-MOD-004, #16) | singleUse + used, expiresOn = сьогодні + 5 | `PromoCodeValidatorTest.usedSingleUsePromoCodeWithFutureDateReturnsInvalid` | Шандра | Алферов |
| BL-70 | Межі відсотка 1 % і 50 % валідні | 1; 50 | `PromoCodeValidatorTest.validPercentRangeBoundaries1And50PassValidation` | Костюк | Шандра |
| BL-70 | Відсоток поза 1–50 невалідний | 0; 51 (параметризований) | `PromoCodeValidatorTest.outOfRangePercentFailsValidation` | Костюк | Шандра |
| BL-80 | Застосування PERCENT, HALF_UP | 10 % від 1000.00 → 100.00 | `PromoCodeValidatorTest.applyPercentPromoCodeSuccess` | Алферов | Шандра |
| BL-80 | Застосування FIXED номіналом | 100.00 при 500.00 | `PromoCodeValidatorTest.applyFixedPromoCodeSuccess` | Барановський | Шандра |
| BL-80 | FIXED більше за підсумок → `IllegalArgumentException` | 500.00 при 300.00 | `PromoCodeValidatorTest.fixedPromoExceedingSubtotalThrowsException` | Шандра | Алферов |
| BL-80 | Застосування використаного одноразового коду → `IllegalArgumentException` (BUG-MOD-004, #16) | singleUse + used | `PromoCodeValidatorTest.applyUsedSingleUsePromoCodeThrowsException` | Шандра | Алферов |
| BL-70 | Створення моделі `PromoCode` (PERCENT, FIXED, межове значення) | валідні дані | `PromoCodeTest.validPercentPromoCodeCreation`, `validFixedPromoCodeCreation`, `boundaryDiscountValueCreation` | Костюк | Шандра |

**Разом:** 34 тестові методи (37 прогонів у Surefire з урахуванням 3 параметризованих тестів × 2 набори). Внесок: Шандра 14, Костюк 10, Алферов 4, Барановський 3, Мулявка 3. Усі правила BL-10…BL-80 SRS 1.1 (крім вилученої BL-30) покрито; рецензент кожного тесту — не його автор.
