package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("PromoCodeValidator — перевірка та застосування промокодів (BL-70, BL-80) [Олександр Шандра]")
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
        // Arrange
        PromoCode promo = new PromoCode(invalidCode, PromoCode.Type.PERCENT, new BigDecimal("10"), today, false, false);
        
        // Act & Assert
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));
        assertFalse(result.valid(), "Промокод некоректного формату повинен повертати valid() == false");
    }

    @Test
    @DisplayName("BL-70: Промокод із закінченим терміном дії не проходить валідацію")
    void expiredPromoCodeFailsValidation() {
        // Arrange
        LocalDate expiredDate = today.minusDays(1);
        PromoCode promo = new PromoCode("EXPIRED1", PromoCode.Type.PERCENT, new BigDecimal("10"), expiredDate, false, false);
        
        // Act & Assert
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));
        assertFalse(result.valid(), "Прострочений промокод повинен бути невалідним");
    }

    @Test
    @DisplayName("BL-70: Повторне використання одноразового промокоду не проходить валідацію")
    void usedOneTimePromoCodeFailsValidation() {
        // Arrange
        PromoCode promo = new PromoCode("ONETIME1", PromoCode.Type.PERCENT, new BigDecimal("10"), today, true, true);
        
        // Act & Assert
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));
        assertFalse(result.valid(), "Використаний одноразовий промокод не повинен проходити валідацію");
    }

    @Test
    @DisplayName("BL-80: Фіксований промокод FIXED перевищує суму замовлення")
    void fixedPromoExceedingSubtotalThrowsException() {
        // Arrange
        PromoCode promo = new PromoCode("FIXED500", PromoCode.Type.FIXED, new BigDecimal("500.00"), today, false, false);
        
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> validator.apply(promo, today, new BigDecimal("300.00")));
    }

    @Test
    @DisplayName("BL-80: Успішне застосування відсоткового промокоду повертає правильну суму [Богдан Алферов]")
    void applyPercentPromoCodeSuccess() {
        PromoCode promo = new PromoCode("SALE10", PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(1), false, false);
        BigDecimal discount = validator.apply(promo, today, new BigDecimal("1000.00"));
        assertEquals(new BigDecimal("100.00"), discount);
    }

    @Test
    @DisplayName("BL-70: Промокод у день закінчення терміну дії має бути валідним (BUG-M3) [Богдан Алферов]")
    void promoCodeValidOnExpirationDay() {
        // Промокод діє до 'today' включно
        PromoCode promo = new PromoCode("TODAY10", PromoCode.Type.PERCENT, new BigDecimal("10"), today, false, false);
        PromoCodeValidator.ValidationResult result = validator.validate(promo, today, new BigDecimal("1000.00"));
        assertTrue(result.valid(), "Промокод має бути валідним у день закінчення терміну");
    }

    @Test
    @DisplayName("BL-80: Успішне застосування фіксованого промокоду FIXED [Богдан Барановський]")
    void applyFixedPromoCodeSuccess() {
        // Arrange
        PromoCode promo = new PromoCode("FIXED100", PromoCode.Type.FIXED, new BigDecimal("100.00"), today.plusDays(1), false, false);
        // Act
        BigDecimal discount = validator.apply(promo, today, new BigDecimal("500.00"));
        // Assert
        assertEquals(new BigDecimal("100.00"), discount, "Фіксована знижка має повертати точну суму 100.00 грн");
    }
}
