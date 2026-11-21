package ua.kpi.tzyapz.shopline;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Cart — операції з кошиком товарів (BL-10, BL-40)")
class CartTest {

    private Cart cart;

    @BeforeEach
    void setUp() {
        cart = new Cart();
    }

    @Test
    @DisplayName("BL-40: Порожній кошик після ініціалізації має subtotal 0.00 та 0 товарів [Вадим Костюк]")
    void newCartIsEmpty() {
        // Arrange & Act & Assert
        assertAll("Перевірка початкового стану кошика",
            () -> assertEquals(new BigDecimal("0.00"), cart.subtotal(), "Сума нового кошика має бути 0.00 зі scale=2"),
            () -> assertEquals(0, cart.itemCount(), "Кількість товарів у новому кошику має бути 0")
        );
    }

    @Test
    @DisplayName("BL-10: Додавання валідного товару збільшує subtotal та кількість позицій [Вадим Костюк]")
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
    @DisplayName("BL-10: Додавання товару з нульовою кількістю викликає IllegalArgumentException [Вадим Костюк]")
    void addInvalidQuantityThrowsException() {
        // Arrange
        BigDecimal price = new BigDecimal("150.00");

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addItem("DRL-002", "Свердло по металу", price, 0),
                "Кількість товару <= 0 має викликати IllegalArgumentException");
    }

    @ParameterizedTest(name = "BL-10: Неприпустима ціна {0} викликає IllegalArgumentException [Вадим Костюк]")
    @ValueSource(strings = {"0.00", "-0.01"})
    void addItemWithNonPositivePriceThrowsException(String invalidPrice) {
        // Arrange
        BigDecimal price = new BigDecimal(invalidPrice);

        // Act & Assert
        assertThrows(IllegalArgumentException.class,
                () -> cart.addItem("HAM-010", "Молоток", price, 1),
                "Ціна товару має бути строго більшою за 0");
    }

    @Test
    @DisplayName("BL-10: Порожній або null артикул sku викликає IllegalArgumentException [Вадим Костюк]")
    void addItemWithBlankOrNullSkuThrowsException() {
        // Arrange
        BigDecimal price = new BigDecimal("100.00");

        // Act & Assert
        assertAll("Перевірка некоректного SKU",
            () -> assertThrows(IllegalArgumentException.class, () -> cart.addItem(null, "Молоток", price, 1)),
            () -> assertThrows(IllegalArgumentException.class, () -> cart.addItem("   ", "Молоток", price, 1))
        );
    }

    @Test
    @DisplayName("BL-40: Видалення або зміна кількості неіснуючого sku викликає NoSuchElementException [Вадим Костюк]")
    void operationsOnMissingSkuThrowNoSuchElementException() {
        // Arrange, Act & Assert
        assertAll("Перевірка звернення до відсутнього артикула",
            () -> assertThrows(NoSuchElementException.class, () -> cart.removeItem("UNKNOWN-SKU")),
            () -> assertThrows(NoSuchElementException.class, () -> cart.changeQty("UNKNOWN-SKU", 2))
        );
    }

    @Test
    @DisplayName("BL-40: Граничні значення changeQty (qty = 0 викликає виняток, qty = 1 та 99 дозволені) [Вадим Костюк]")
    void changeQtyBoundaryValues() {
        // Arrange
        cart.addItem("WRN-001", "Ключ гайковий", new BigDecimal("10.00"), 5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> cart.changeQty("WRN-001", 0));

        cart.changeQty("WRN-001", 1);
        assertEquals(1, cart.qtyOf("WRN-001"));

        cart.changeQty("WRN-001", 99);
        assertEquals(99, cart.qtyOf("WRN-001"));
    }

    @Test
    @DisplayName("BL-40: Видалення товару зменшує subtotal та itemCount [Богдан Алферов]")
    void removeItemSuccess() {
        cart.addItem("WRN-001", "Ключ гайковий", new BigDecimal("100.00"), 2);
        cart.removeItem("WRN-001");
        assertAll("Перевірка після видалення",
            () -> assertEquals(new BigDecimal("0.00"), cart.subtotal()),
            () -> assertEquals(0, cart.itemCount())
        );
    }

    @Test
    @DisplayName("BL-40: Зміна кількості товару оновлює subtotal [Богдан Алферов]")
    void changeQtyUpdatesSubtotal() {
        cart.addItem("WRN-001", "Ключ гайковий", new BigDecimal("100.00"), 1);
        cart.changeQty("WRN-001", 3);
        assertEquals(new BigDecimal("300.00"), cart.subtotal());
    }

    @Test
    @DisplayName("BL-10: Додавання товару з максимально допустимою кількістю 99 [Богдан Барановський]")
    void addItemMaxQuantitySuccess() {
        // Arrange & Act
        cart.addItem("WRN-001", "Ключ гайковий", new BigDecimal("100.00"), 99);
        // Assert
        assertEquals(99, cart.itemCount(), "Кількість елементів у кошику має дорівнювати 99");
    }

    @Test
    @DisplayName("BL-10: Додавання 100 одиниць товару викликає IllegalArgumentException (#17 / BUG-MOD-005) [Олександр Шандра]")
    void addQuantityAbove99ThrowsException() {
        // Дефект v1: GitHub Issue #17 (BUG-MOD-005) — MAX_QTY = 100 замість 99
        BigDecimal price = new BigDecimal("50.00");

        assertThrows(IllegalArgumentException.class,
                () -> cart.addItem("WRN-001", "Ключ гайковий", price, 100),
                "Максимальна дозволена кількість у рядку — 99 одиниць");
    }
        @Test
    @DisplayName("BL-20: Повторне додавання того самого sku збільшує кількість наявного рядка (BUG-MOD-006) [Олександр Шандра]")
    void addSameSkuTwiceIncreasesQuantityOfExistingLine() {
        // Дефект v1: BUG-MOD-006
        // Arrange
        BigDecimal price = new BigDecimal("100.00");
        cart.addItem("WRN-001", "Ключ гайковий", price, 2);

        // Act
        cart.addItem("WRN-001", "Ключ гайковий", price, 3);

        // Assert
        assertAll("Перевірка злиття позицій з однаковим sku",
            () -> assertEquals(1, cart.items().size(), "У кошику має залишитися один рядок"),
            () -> assertEquals(5, cart.qtyOf("WRN-001"), "Кількість має дорівнювати 2 + 3 = 5"),
            () -> assertEquals(new BigDecimal("500.00"), cart.subtotal(), "Підсумок має дорівнювати 500.00 грн")
        );
    }

    @Test
    @DisplayName("BL-10: Порожня або null назва товару викликає IllegalArgumentException (BUG-MOD-007) [Олександр Шандра]")
    void addItemWithBlankOrNullNameThrowsException() {
        // Дефект v1: BUG-MOD-007
        // Arrange
        BigDecimal price = new BigDecimal("100.00");

        // Act & Assert
        assertAll("Перевірка некоректної назви товару",
            () -> assertThrows(IllegalArgumentException.class, () -> cart.addItem("HAM-010", null, price, 1)),
            () -> assertThrows(IllegalArgumentException.class, () -> cart.addItem("HAM-011", "   ", price, 1))
        );
    }
}
