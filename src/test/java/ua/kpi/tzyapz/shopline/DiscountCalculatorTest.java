package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("DiscountCalculator — обчислення знижки за обсяг та вартості доставки (BL-50, BL-60) [Олександр Шандра]")
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
    
    @Test
    @DisplayName("BL-30: Замовлення на суму 9999.99 грн отримує знижку 10%, а не 15% (межа третього порогу)")
    void discountPercent_JustBelowHighestTier_ReturnsTenPercent() {
        // Arrange
        BigDecimal subtotal = new BigDecimal("9999.99");
        
        // Act: виправлено виклик на volumeDiscountPercent
        int actualPercent = calc.volumeDiscountPercent(subtotal);
        
        // Assert
        assertEquals(10, actualPercent, "Для суми 9999.99 грн знижка має бути 10%, 15% починається рівно з 10000");
    }

    @Test
    @DisplayName("BL-30: Від'ємна сума замовлення викликає IllegalArgumentException")
    void volumeDiscount_NegativeSubtotal_ThrowsException() {
        // Arrange & Act
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> calc.volumeDiscount(new BigDecimal("-0.01")));
        
        // Assert
        assertTrue(ex.getMessage().contains("невід'ємною"), 
                "Повідомлення про помилку має вказувати, що сума невід'ємна. Фактичне повідомлення: " + ex.getMessage());
    }

    @Test
    @DisplayName("BL-30: При досягненні порогу 10000 грн застосовується лише знижка 15%, знижки не сумуються")
    void volumeDiscount_HighestTier_DoesNotSumWithLowerTiers() {
        // Arrange
        BigDecimal subtotal = new BigDecimal("10000.00");
        // 15% від 10000 = 1500.00
        BigDecimal expectedDiscount = new BigDecimal("1500.00"); 
        
        // Act
        BigDecimal actualDiscount = calc.volumeDiscount(subtotal);
        
        // Assert
        assertEquals(expectedDiscount, actualDiscount, "Знижка має бути рівно 1500.00 (лише 15%), а не 30% (15+10+5)");
    }
    @Test
    @DisplayName("BL-30: Замовлення до 2000.00 грн отримує 0% знижки (граничне значення 1999.99) [Богдан Барановський]")
    void volumeDiscountBelowFirstTierReturnsZeroPercent() {
        // Arrange
        BigDecimal subtotal = new BigDecimal("1999.99");
        // Act
        int percent = calc.volumeDiscountPercent(subtotal);
        // Assert
        assertEquals(0, percent, "Для суми 1999.99 грн знижка повинна дорівнювати 0%");
}
}
