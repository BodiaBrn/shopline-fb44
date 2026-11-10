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
    @DisplayName("BL-70: Створення коректного відсоткового промокоду та перевірка accessor-методів record")
    void validPercentPromoCodeCreation() {
        // Arrange & Act
        PromoCode promo = new PromoCode("SALE2026", PromoCode.Type.PERCENT, new BigDecimal("15"), today.plusDays(10), false, false);

        // Assert
        assertAll("Перевірка полів record PromoCode",
            () -> assertEquals("SALE2026", promo.code(), "Код має збігатися"),
            () -> assertEquals(PromoCode.Type.PERCENT, promo.type(), "Тип має бути PERCENT"),
            () -> assertEquals(new BigDecimal("15"), promo.value(), "Значення знижки має збігатися"),
            () -> assertEquals(today.plusDays(10), promo.expiryDate(), "Термін дії має збігатися"),
            () -> assertFalse(promo.singleUse(), "Ознака одноразовості має бути false"),
            () -> assertFalse(promo.used(), "Ознака використання має бути false")
        );
    }

    @Test
    @DisplayName("BL-70: Створення фіксованого одноразового промокоду")
    void validFixedPromoCodeCreation() {
        // Arrange & Act
        PromoCode promo = new PromoCode("FIXED100", PromoCode.Type.FIXED, new BigDecimal("100.00"), today.plusDays(5), true, false);

        // Assert
        assertAll("Перевірка фіксованого промокоду",
            () -> assertEquals(PromoCode.Type.FIXED, promo.type()),
            () -> assertEquals(new BigDecimal("100.00"), promo.value()),
            () -> assertTrue(promo.singleUse(), "singleUse має повертати true"),
            () -> assertFalse(promo.used(), "used має повертати false")
        );
    }

    @Test
    @DisplayName("BL-70: Граничне значення знижки 100% коректно фіксується в моделі")
    void boundaryDiscountValueCreation() {
        // Arrange & Act
        PromoCode promo = new PromoCode("MAX100", PromoCode.Type.PERCENT, new BigDecimal("100.00"), today.plusDays(1), false, false);

        // Assert
        assertEquals(new BigDecimal("100.00"), promo.value(), "Значення 100.00 має коректно зберігатися");
    }
}
