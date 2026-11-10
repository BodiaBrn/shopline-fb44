package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiscountCalculator — обчислення знижки за обсяг та вартості доставки (BL-50, BL-60)")
class DiscountCalculatorTest {

    private DiscountCalculator calc;

    @BeforeEach
    void setUp() {
        calc = new DiscountCalculator();
    }

    @Test
    @DisplayName("BL-50: Замовлення від 2000.00 грн отримує знижку 5%")
    void volumeDiscountFivePercentTier() {
        // Arrange
        BigDecimal subtotal = new BigDecimal("2000.00");

        // Act
        int percent = calc.volumeDiscountPercent(subtotal);

        // Assert
        assertEquals(5, percent, "Для суми 2000.00 грн відсоток знижки має дорівнювати 5%");
    }

    @Test
    @DisplayName("BL-50: Сума знижки округлюється до копійок за правилом HALF_UP")
    void volumeDiscountRoundsHalfUp() {
        // Arrange
        BigDecimal subtotal = new BigDecimal("2333.33");

        // Act
        BigDecimal discount = calc.volumeDiscount(subtotal);

        // Assert
        assertEquals(new BigDecimal("116.67"), discount, "Сума знижки повинна округлюватися до 116.67 грн");
    }

    @Test
    @DisplayName("BL-60: Доставка безкоштовна (0.00 грн) при сумі від 1500.00 грн, інакше 120.00 грн")
    void shippingFeeCalculation() {
        // Arrange, Act & Assert
        assertAll("Перевірка порогів вартості доставки",
            () -> assertEquals(new BigDecimal("120.00"), calc.shippingFee(new BigDecimal("1499.99")), "До 1500 грн доставка 120 грн"),
            () -> assertEquals(new BigDecimal("0.00"), calc.shippingFee(new BigDecimal("1500.00")), "Від 1500 грн доставка безкоштовна")
        );
    }
}