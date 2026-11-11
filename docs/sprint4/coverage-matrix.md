# Матриця покриття вимог бізнес-логіки (Спринт 4)

**Продукт:** П1 «ШопЛайн» (ShopLine) | **Стенд:** https://practicesoftwaretesting.com  
**Відповідальний:** Богдан Барановський (Тест-аналітик)  
**Рецензент:** Олександр Шандра (Scrum Master / Reviewer)  

---

## Покриття правил бізнес-логіки юніт-тестами

| ID вимоги | Правило бізнес-логіки | Граничні значення / Умови | Назва тест-класу та методу | Автор тесту | Рецензент |
|---|---|---|---|---|---|
| **BL-40** | Початковий стан кошика | subtotal == 0.00 (scale 2), itemCount == 0 | `CartTest.newCartIsEmpty` | Костюк В. | Шандра О. |
| **BL-40** | Додавання товару до кошика | Розрахунок subtotal та збільшення itemCount | `CartTest.addItemIncreasesSubtotalAndCount` | Костюк В. | Шандра О. |
| **BL-40** | Валідація кількості товару | qty <= 0 -> IllegalArgumentException | `CartTest.addInvalidQuantityThrowsException` | Костюк В. | Шандра О. |
| **BL-40** | Видалення позиції з кошика | Обнулення subtotal та itemCount після видалення | `CartTest.removeItemSuccess` | Алферов Б. | Шандра О. |
| **BL-40** | Зміна кількості товару | Оновлення subtotal при зміні кількості | `CartTest.changeQtyUpdatesSubtotal` | Алферов Б. | Шандра О. |
| **BL-30** | Поріг знижки 5% (від 2000 грн) | subtotal = 2000.00 грн -> 5% | `DiscountCalculatorTest.volumeDiscountFivePercentTier` | Шандра О. | Барановський Б. |
| **BL-30** | Межа знижки 10% (9999.99 грн) | subtotal = 9999.99 грн -> 10% (15% від 10000.00) | `DiscountCalculatorTest.discountPercent_JustBelowHighestTier_ReturnsTenPercent` | Шандра О. | Барановський Б. |
| **BL-30** | Максимальний поріг знижки 15% | subtotal = 10000.00 грн -> 1500.00 грн (без сумування порогів) | `DiscountCalculatorTest.volumeDiscount_HighestTier_DoesNotSumWithLowerTiers` | Шандра О. | Барановський Б. |
| **BL-30** | Округлення суми знижки HALF_UP | subtotal = 2333.33 грн -> 116.67 грн | `DiscountCalculatorTest.volumeDiscountRoundsHalfUp` | Шандра О. | Барановський Б. |
| **BL-30** | Від'ємна сума замовлення | subtotal < 0 -> IllegalArgumentException | `DiscountCalculatorTest.volumeDiscount_NegativeSubtotal_ThrowsException` | Шандра О. | Барановський Б. |
| **BL-60** | Порогова вартість доставки (1500 грн) | 1499.99 (120.00 грн), 1500.00 (0.00 грн) | `DiscountCalculatorTest.shippingFeeCalculation` | Шандра О. | Барановський Б. |
| **BL-70** | Створення відсоткового промокоду | Перевірка полів record PromoCode (PERCENT, value, expiryDate) | `PromoCodeTest.validPercentPromoCodeCreation` | Костюк В. | Шандра О. |
| **BL-70** | Створення фіксованого промокоду | singleUse == true, used == false, FIXED | `PromoCodeTest.validFixedPromoCodeCreation` | Костюк В. | Шандра О. |
| **BL-70** | Межа значення знижки (100%) | value = 100.00 | `PromoCodeTest.boundaryDiscountValueCreation` | Костюк В. | Шандра О. |
| **BL-70** | Неприпустимий формат промокоду | Маски SHORT, TOOLONGPROMOCODE123, BAD@CHAR -> isValid() == false | `PromoCodeValidatorTest.invalidCodeFormatFailsValidation` | Шандра О. | Барановський Б. |
| **BL-70** | Прострочений промокод | expiryDate = today - 1 -> isValid() == false | `PromoCodeValidatorTest.expiredPromoCodeFailsValidation` | Шандра О. | Барановський Б. |
| **BL-70** | Одноразовий промокод | used == true -> isValid() == false | `PromoCodeValidatorTest.usedOneTimePromoCodeFailsValidation` | Шандра О. | Барановський Б. |
| **BL-70** | Термін дії промокоду (включно) | expiryDate == today -> valid() == true | `PromoCodeValidatorTest.promoCodeValidOnExpirationDay` | Алферов Б. | Шандра О. |
| **BL-80** | Перевищення суми фіксованим промокодом | FIXED value > subtotal -> IllegalArgumentException | `PromoCodeValidatorTest.fixedPromoExceedingSubtotalThrowsException` | Шандра О. | Барановський Б. |
| **BL-80** | Застосування відсоткового промокоду | PERCENT 10% від 1000.00 грн = 100.00 грн discount | `PromoCodeValidatorTest.applyPercentPromoCodeSuccess` | Алферов Б. | Шандра О. |
