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
    @ValueSource(strings = {"SHORT", "TOOLONGPROMOCODE123"})
    void invalidCodeFormatFailsValidation(String invalidCode) {
        // Arrange
        PromoCode promo = new PromoCode(invalidCode, PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(5), false, false);
        
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
    @DisplayName("BL-70: Повторне використання одноразового промокоду не проходить валідацію (BUG-MOD-004, #16)")
    void usedOneTimePromoCodeFailsValidation() {
        // Arrange
        PromoCode promo = new PromoCode("ONETIME1", PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(5), true, true);
        
        // Act & Assert
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));
        assertFalse(result.valid(), "Використаний одноразовий промокод не повинен проходити валідацію");
    }

    @Test
    @DisplayName("BL-70: Фіксований промокод FIXED перевищує суму замовлення")
    void fixedPromoExceedingSubtotalThrowsException() {
        // Arrange
        PromoCode promo = new PromoCode("FIXED500", PromoCode.Type.FIXED, new BigDecimal("500.00"), today.plusDays(5), false, false);
        
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
    @DisplayName("BL-70: Промокод у день закінчення терміну дії має бути валідним (BUG-MOD-003, #15) [Богдан Алферов]")
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

    @Test
    @DisplayName("BL-70: Вже використаний одноразовий промокод з актуальною датою має повертати valid() == false (BUG-MOD-004, #16) [Олександр Шандра]")
    void usedSingleUsePromoCodeWithFutureDateReturnsInvalid() {
        // Arrange
        PromoCode promo = new PromoCode("USED2026", PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(5), true, true);

        // Act
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));

        // Assert
        assertFalse(result.valid(), "Використаний одноразовий промокод не повинен проходити валідацію");
    }

    @Test
    @DisplayName("BL-70: Граничні довжини коду 6 та 10 символів проходять валідацію [Вадим Костюк]")
    void validCodeLengthBoundaries6And10PassValidation() {
        // Arrange
        PromoCode minLenCode = new PromoCode("SALE10", PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(1), false, false);
        PromoCode maxLenCode = new PromoCode("SUMMER2026", PromoCode.Type.PERCENT, new BigDecimal("10"), today.plusDays(1), false, false);

        // Act & Assert
        assertAll("Перевірка допустимих меж довжини промокоду (6 і 10 символів)",
            () -> assertTrue(validator.validate(minLenCode, today, new BigDecimal("1000.00")).valid(), "Довжина 6 символів має бути валідною"),
            () -> assertTrue(validator.validate(maxLenCode, today, new BigDecimal("1000.00")).valid(), "Довжина 10 символів має бути валідною")
        );
    }

    @Test
    @DisplayName("BL-70: Граничні значення відсоткової знижки 1% та 50% є валідними [Вадим Костюк]")
    void validPercentRangeBoundaries1And50PassValidation() {
        // Arrange
        PromoCode minPercent = new PromoCode("MIN001", PromoCode.Type.PERCENT, new BigDecimal("1"), today.plusDays(1), false, false);
        PromoCode maxPercent = new PromoCode("MAX050", PromoCode.Type.PERCENT, new BigDecimal("50"), today.plusDays(1), false, false);

        // Act & Assert
        assertAll("Перевірка допустимого діапазону 1..50%",
            () -> assertTrue(validator.validate(minPercent, today, new BigDecimal("1000.00")).valid(), "Знижка 1% має бути валідною"),
            () -> assertTrue(validator.validate(maxPercent, today, new BigDecimal("1000.00")).valid(), "Знижка 50% має бути валідною")
        );
    }

    @ParameterizedTest(name = "BL-70: Відсоткова знижка {0}% поза діапазоном 1..50 не проходить валідацію [Вадим Костюк]")
    @ValueSource(strings = {"0", "51"})
    void outOfRangePercentFailsValidation(String invalidPercent) {
        // Arrange
        PromoCode promo = new PromoCode("OUTRNG", PromoCode.Type.PERCENT, new BigDecimal(invalidPercent), today.plusDays(1), false, false);

        // Act
        var result = validator.validate(promo, today, new BigDecimal("1000.00"));

        // Assert
        assertFalse(result.valid(), "Відсоток " + invalidPercent + "% поза межами 1..50 має відхилятися");
    }
}
