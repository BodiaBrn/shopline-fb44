package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart — операції з кошиком товарів (BL-40) [Вадим Костюк]")
class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    @DisplayName("BL-40: Порожній кошик після ініціалізації має нульовий subtotal та 0 товарів")
    void newCartIsEmpty() {
        // Arrange & Act & Assert
        assertAll("Перевірка початкового стану кошика",
            () -> assertEquals(BigDecimal.ZERO, cart.subtotal(), "Сума нового кошика має бути 0"),
            () -> assertEquals(0, cart.itemCount(), "Кількість товарів у новому кошику має бути 0")
        );
    }

    @Test
    @DisplayName("BL-40: Додавання товару збільшує subtotal та кількість позицій")
    void addItemIncreasesSubtotalAndCount() {
        // Arrange
        BigDecimal price = new BigDecimal("250.00");
        int quantity = 2;

        // Act
        cart.addItem("WRN-001", "Ключ гайковий", price, quantity);

        // Assert
        assertAll("Перевірка після додавання товару",
            () -> assertEquals(new BigDecimal("500.00"), cart.subtotal(), "Підсумок має бути 500.00 грн"),
            () -> assertEquals(2, cart.itemCount(), "Загальна кількість товарів має бути 2")
        );
    }

    @Test
    @DisplayName("BL-40: Додавання товару з нульовою або від'ємною кількістю викликає виняток")
    void addInvalidQuantityThrowsException() {
        // Arrange
        BigDecimal price = new BigDecimal("150.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem("DRL-002", "Свердло по металу", price, 0);
        }, "Кількість товару <= 0 має викликати IllegalArgumentException");
    }
}
