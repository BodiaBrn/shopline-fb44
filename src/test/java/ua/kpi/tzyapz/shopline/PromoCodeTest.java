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

        assertAll("Перевірка властивостей промокоду (record)",
            () -> assertEquals("SALE2026", promo.code()),
            () -> assertEquals(PromoCode.Type.PERCENT, promo.type()),
            () -> assertEquals(new BigDecimal("15"), promo.value()),
            () -> assertFalse(promo.used())
        );
    }

    @Test
    @DisplayName("BL-70: Створення фіксованого промокоду")
    void validFixedPromoCodeCreation() {
        PromoCode promo = new PromoCode("FIXED100", PromoCode.Type.FIXED, new BigDecimal("100.00"), today.plusDays(5), true, false);

        assertAll("Перевірка фіксованого промокоду (record)",
            () -> assertEquals(PromoCode.Type.FIXED, promo.type()),
            () -> assertEquals(new BigDecimal("100.00"), promo.value()),
            () -> assertTrue(promo.singleUse())
        );
    }
}
