package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PromoCodeValidator — перевірка та застосування промокодів (BL-70, BL-80)")
class PromoCodeValidatorTest {

    private PromoCodeValidator validator;
    private LocalDate today;

    @BeforeEach
    void setUp() {
        validator = new PromoCodeValidator();
        today = LocalDate.of(2026, 11, 10);
    }

    @ParameterizedTest(name = "BL-70: Неприпустимий код '{0}' не проходить валідацію")
    @ValueSource(strings = {"SHORT", "TOOLONGPROMOCODE123", "BAD@CHAR"})
    void invalidCodeFormatFailsValidation(String invalidCode) {
        PromoCode promo = new PromoCode(invalidCode, PromoCode.Type.PERCENT, new BigDecimal("10"), today, false, false);
        // Виправлено на перевірку булевого статусу (якщо ValidationResult — це record, використайте .valid() замість .isValid())
        assertFalse(validator.validate(promo, today, new BigDecimal("1000.00")).isValid());
    }

    @Test
    @DisplayName("BL-70: Промокод із закінченим терміном дії не проходить валідацію")
    void expiredPromoCodeFailsValidation() {
        LocalDate expiredDate = today.minusDays(1);
        PromoCode promo = new PromoCode("EXPIRED1", PromoCode.Type.PERCENT, new BigDecimal("10"), expiredDate, false, false);
        assertFalse(validator.validate(promo, today, new BigDecimal("1000.00")).isValid());
    }

    @Test
    @DisplayName("BL-70: Повторне використання одноразового промокоду не проходить валідацію")
    void usedOneTimePromoCodeFailsValidation() {
        PromoCode promo = new PromoCode("ONETIME1", PromoCode.Type.PERCENT, new BigDecimal("10"), today, true, true);
        assertFalse(validator.validate(promo, today, new BigDecimal("1000.00")).isValid());
    }

    @Test
    @DisplayName("BL-80: Фіксований промокод FIXED перевищує суму замовлення")
    void fixedPromoExceedingSubtotalThrowsException() {
        PromoCode promo = new PromoCode("FIXED500", PromoCode.Type.FIXED, new BigDecimal("500.00"), today, false, false);
        // Залишаємо assertThrows, оскільки тут викликається apply
        assertThrows(IllegalArgumentException.class, () -> validator.apply(promo, today, new BigDecimal("300.00")));
    }
}
