package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Юніт-тести кошика покупок Cart (Вадим Костюк)")
class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    @DisplayName("BL-11: Порожній кошик після ініціалізації має нульову суму та нуль позицій")
    void testNewCartIsEmpty() {
        // Arrange & Act
        double total = cart.getTotalPrice();
        int itemCount = cart.getItemCount();

        // Assert
        assertAll("Перевірка початкового стану кошика",
            () -> assertEquals(0.0, total, 0.001, "Сума порожнього кошика має дорівнювати 0.0"),
            () -> assertEquals(0, itemCount, "Кількість товарів у новому кошику має бути 0")
        );
    }

    @Test
    @DisplayName("BL-12: Додавання товару коректно збільшує підсумкову вартість кошика")
    void testAddItemIncreasesTotalPrice() {
        // Arrange
        double itemPrice = 250.0;
        int quantity = 2;
        double expectedTotal = 500.0;

        // Act
        cart.addItem("ITEM-001", itemPrice, quantity);

        // Assert
        assertEquals(expectedTotal, cart.getTotalPrice(), 0.001, 
                "Підсумкова вартість має дорівнювати добутку ціни на кількість");
    }

    @Test
    @DisplayName("BL-13: Додавання товару з нульовою або від'ємною кількістю викликає помилку")
    void testAddInvalidQuantityThrowsException() {
        // Arrange
        String itemId = "ITEM-002";
        double price = 100.0;
        int invalidQuantity = -1;

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            cart.addItem(itemId, price, invalidQuantity);
        }, "Додавання від'ємної кількості одиниць товару неприпустиме");
    }
}
