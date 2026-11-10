package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PromoCode — модель промокоду (BL-70) [Вадим Костюк]")
class PromoCodeTest {

    private final LocalDate today = LocalDate.of(2026, 11, 10);

    @Test
    @DisplayName("BL-70: Створення коректного відсоткового промокоду та перевірка полів")
    void validPercentPromoCodeCreation() {
        PromoCode promo = new PromoCode("SALE2026", PromoCode.Type.PERCENT, new BigDecimal("15"), today.plusDays(10), false, false);

        assertAll("Перевірка властивостей промокоду",
            () -> assertEquals("SALE2026", promo.getCode()),
            () -> assertEquals(PromoCode.Type.PERCENT, promo.getType()),
            () -> assertEquals(new BigDecimal("15"), promo.getDiscountValue()),
            () -> assertFalse(promo.isUsed())
        );
    }

    @Test
    @DisplayName("BL-70: Створення фіксованого промокоду")
    void validFixedPromoCodeCreation() {
        PromoCode promo = new PromoCode("FIXED100", PromoCode.Type.FIXED, new BigDecimal("100.00"), today.plusDays(5), true, false);

        assertAll("Перевірка фіксованого промокоду",
            () -> assertEquals(PromoCode.Type.FIXED, promo.getType()),
            () -> assertEquals(new BigDecimal("100.00"), promo.getDiscountValue()),
            () -> assertTrue(promo.isOneTime())
        );
    }

    @Test
    @DisplayName("BL-70: Від'ємне значення знижки викликає IllegalArgumentException")
    void negativeDiscountValueThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            new PromoCode("INVALID", PromoCode.Type.PERCENT, new BigDecimal("-5.00"), today.plusDays(1), false, false);
        });
    }
}
