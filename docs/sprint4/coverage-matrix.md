# Матриця покриття вимог бізнес-логіки (Спринт 4)

**Продукт:** П1 «ШопЛайн» (ShopLine) | **Стенд:** https://practicesoftwaretesting.com  
**Відповідальний:** Богдан Барановський (Тест-аналітик)  
**Рецензент:** Олександр Шандра (Scrum Master / Reviewer)  

---

## Покриття правил бізнес-логіки юніт-тестами

| ID вимоги | Правило бізнес-логіки | Граничні значення / Умови | Назва тест-класу та методу | Автор тесту | Рецензент |
|---|---|---|---|---|---|
| **BL-10** | Додавання товару та межі кількості | Qty: 1 (min), 99 (max), 0 та 100 (виняток) | `CartTest.addItemQtyBoundaries` | Костюк В. | Шандра О. |
| **BL-20** | Розрахунок підсумку кошика (subtotal) | Порожній кошик (0.00), сумування lineTotal | `CartTest.subtotalCalculation` | Костюк В. | Шандра О. |
| **BL-30** | Відсоток знижки на межах (2000, 5000, 10000) | 1999.99 (0%), 2000.00 (5%), 4999.99 (5%), 5000.00 (10%), 9999.99 (10%), 10000.00 (15%) | `DiscountCalculatorTest.discountPercentAtBoundaries` | Барановський Б. | Шандра О. |
| **BL-30** | Округлення знижки за HALF_UP | subtotal = 2333.33 (5% = 116.6665 -> 116.67 грн) | `DiscountCalculatorTest.discountAmountIsRoundedHalfUp` | Барановський Б. | Шандра О. |
| **BL-30** | Від'ємна сума замовлення | subtotal < 0 -> IllegalArgumentException | `DiscountCalculatorTest.negativeSubtotalThrowsException` | Барановський Б. | Шандра О. |
| **BL-40** | Порогова вартість доставки (1500 грн) | 1499.99 (120.00 грн), 1500.00 (0.00 грн), 1500.01 (0.00 грн) | `DiscountCalculatorTest.shippingFeeBoundaries` | Алферов Б. | Шандра О. |
| **BL-50** | Валідація форматів промокодів | Маска [A-Z0-9]{6,10}, недійсний формат -> fail | `PromoCodeValidatorTest.invalidFormatFails` | Мулявка Я. | Шандра О. |
| **BL-50** | Термін дії промокоду (включно) | today == expiresOn (Valid), today > expiresOn (Expired) | `PromoCodeValidatorTest.expirationDateInclusive` | Мулявка Я. | Шандра О. |
| **BL-50** | Ініціалізація та валідація об'єкта промокоду | Перевірка створення промокоду з відсотком знижки > 0 | `PromoCodeTest.createValidPromoCode` | Мулявка Я. | Шандра О. |
| **BL-50** | Перевірка розрахунку знижки за об'єктом промокоду | Застосування відсотка промокоду до суми кошика | `PromoCodeTest.applyPromoCodeDiscount` | Мулявка Я. | Шандра О. |
