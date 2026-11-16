# Матриця покриття вимог бізнес-логіки (Спринт 4)

**Продукт:** П1 «ШопЛайн» (ShopLine) | **Стенд:** https://practicesoftwaretesting.com
**Відповідальний:** Богдан Барановський (Тест-аналітик)
**Рецензент:** Олександр Шандра (Scrum Master / Reviewer)

## Покриття правил бізнес-логіки юніт-тестами

| ID вимоги | Правило бізнес-логіки | Граничні значення / Умови | Назва тест-класу та методу | Автор тесту | Рецензент |
|---|---|---|---|---|---|
| BL-10 | Додавання позиції: кількість 1..99 | qty = 99 (прийнятно) | `CartTest.addItemMaxQuantitySuccess` | Барановський | Шандра |
| BL-10 | Кількість понад максимум → `IllegalArgumentException` | qty = 100 | `CartTest.addQuantityAbove99ThrowsException` | Шандра | Алферов |
| BL-10 | Некоректна кількість → `IllegalArgumentException` | qty ≤ 0 | `CartTest.addInvalidQuantityThrowsException` | Костюк | Шандра |
| BL-10 | Додана позиція збільшує підсумок і лічильник | валідна позиція | `CartTest.addItemIncreasesSubtotalAndCount` | Костюк | Шандра |
| BL-10 | Новий кошик порожній | — | `CartTest.newCartIsEmpty` | Костюк | Шандра |
| BL-40 | Видалення позиції | наявний sku | `CartTest.removeItemSuccess` | Алферов | Шандра |
| BL-40 | Зміна кількості перераховує підсумок | qty 1..99 | `CartTest.changeQtyUpdatesSubtotal` | Алферов | Шандра |
| BL-50 | Знижка 0 % нижче першого порогу | 1999.99 → 0 % | `DiscountCalculatorTest.volumeDiscountBelowFirstTierReturnsZeroPercent` | Барановський | Шандра |
| BL-50 | 5 % від 2000 (включно) | 2000.00 → 5 % | `DiscountCalculatorTest.volumeDiscountFivePercentTier` | Шандра | Алферов |
| BL-50 | Округлення HALF_UP | 2333.33 → 116.67 | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | Шандра | Алферов |
| BL-50 | 10 % від 5000 (включно) | 5000.00 → 10 % | `DiscountCalculatorTest.volumeDiscountTenPercentExactBoundary` | Шандра | Алферов |
| BL-50 | Верхня межа нижче 10 000 | 9999.99 → 10 % | `DiscountCalculatorTest.discountPercent_JustBelowHighestTier_ReturnsTenPercent` | Мулявка | Шандра |
| BL-50 | 15 % від 10 000, знижки не сумуються | 10000.00 → 1500.00 | `DiscountCalculatorTest.volumeDiscount_HighestTier_DoesNotSumWithLowerTiers` | Мулявка | Шандра |
| BL-50 | Від'ємна сума → `IllegalArgumentException` | −0.01 | `DiscountCalculatorTest.volumeDiscount_NegativeSubtotal_ThrowsException` | Мулявка | Шандра |
| BL-60 | Доставка 120.00, безкоштовна від 1500 | 1499.99 → 120.00; 1500.00 → 0.00 | `DiscountCalculatorTest.shippingFeeCalculation` | Шандра | Алферов |
| BL-70 | Невалідний формат коду (довжина 6–10, символи) | `SHORT`, `TOOLONGPROMOCODE123`, `BAD@CHAR` | `PromoCodeValidatorTest.invalidCodeFormatFailsValidation` | Шандра | Алферов |
| BL-70 | Прострочений промокод | expiresOn = сьогодні − 1 | `PromoCodeValidatorTest.expiredPromoCodeFailsValidation` | Шандра | Алферов |
| BL-70 | Промокод діє в день завершення (включно) | expiresOn = сьогодні | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | Алферов | Шандра |
| BL-70 | Використаний одноразовий код невалідний | singleUse + used, expiresOn = сьогодні | `PromoCodeValidatorTest.usedOneTimePromoCodeFailsValidation` | Шандра | Алферов |
| BL-70 | Використаний одноразовий код невалідний при майбутній даті | singleUse + used, expiresOn = сьогодні + 5 | `PromoCodeValidatorTest.usedSingleUsePromoCodeWithFutureDateReturnsInvalid` | Шандра | Алферов |
| BL-70 | FIXED-знижка не перевищує підсумок кошика | FIXED 500 при 300 → `IllegalArgumentException` | `PromoCodeValidatorTest.fixedPromoExceedingSubtotalThrowsException` | Шандра | Алферов |
| BL-80 | Застосування PERCENT-промокоду, HALF_UP | 10 % від 1000 → 100.00 | `PromoCodeValidatorTest.applyPercentPromoCodeSuccess` | Алферов | Шандра |
| BL-80 | Застосування FIXED-промокоду номіналом | 100 при 500 | `PromoCodeValidatorTest.applyFixedPromoCodeSuccess` | Барановський | Шандра |
| BL-70 | Створення моделі `PromoCode` (PERCENT, FIXED, межове значення) | валідні дані | `PromoCodeTest.validPercentPromoCodeCreation`, `validFixedPromoCodeCreation`, `boundaryDiscountValueCreation` | Костюк | Шандра |
