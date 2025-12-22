package praktikum;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BurgerTest extends BaseTest {

    @Parameterized.Parameter
    public String testName;

    @Parameterized.Parameter(1)
    public Bun expectedBun;

    @Parameterized.Parameter(2)
    public List<Ingredient> expectedIngredients;

    @Parameterized.Parameter(3)
    public float expectedTotalPrice;

    private Burger burger;

    @Parameterized.Parameters(name = "Тестовые данные: [name:\"{0}\"]")
    public static Object[][] getData() {
        return new Object[][] {
            {"Бесплатная булочка, без ингредиентов", mockBun("free bun", 0.0f), List.of(), 0.0f},
            {
                "Дешёвая булочка с дешёвым соусом",
                mockBun("white bun", 0.01f),
                List.of(mockIngredient(IngredientType.SAUCE, "sauce", 0.01f)),
                0.03f
            },
            {
                "Обычная булочка с начинкой",
                mockBun("red bun", 150.0f),
                List.of(mockIngredient(IngredientType.FILLING, "meat", 100.0f)),
                400.0f
            },
            {
                "Булочка с соусом и начинкой",
                mockBun("brown bun", 80.99f),
                List.of(
                        mockIngredient(IngredientType.SAUCE, "catchup", 60.01f),
                        mockIngredient(IngredientType.FILLING, "chicken", 200.01f)),
                422.0f
            },
            {
                "Булочка с большим количеством ингредиентов",
                mockBun("black bun", 100.0f),
                List.of(
                        mockIngredient(IngredientType.FILLING, "very cheap filling", -999999.001f),
                        mockIngredient(IngredientType.SAUCE, "cheap sauce", -0.01f),
                        mockIngredient(IngredientType.FILLING, "free filling", 0.0f),
                        mockIngredient(IngredientType.SAUCE, "low cost sauce", 0.01f),
                        mockIngredient(IngredientType.FILLING, "expensive filling", 99999.99f),
                        mockIngredient(IngredientType.SAUCE, "astronomical sauce", 10000000.0f)),
                9100200.989f
            }
        };
    }

    @Before
    public void setUp() {
        burger = new Burger();
        burger.setBuns(expectedBun);
        expectedIngredients.forEach(burger::addIngredient);
    }

    @Test
    public void setBunsShouldAssignBun() {
        assertSame("Метод setBuns должен устанавливать булочку", expectedBun, burger.bun);
    }

    @Test
    public void addIngredientShouldAddAllIngredients() {
        assertEquals(
                "Список ингредиентов должен содержать все добавленные элементы в правильном порядке",
                expectedIngredients,
                burger.ingredients);
    }

    @Test
    public void removeIngredientShouldRemoveByIndex() {
        if (burger.ingredients.isEmpty()) return;

        int index = burger.ingredients.size() - 1;
        Ingredient expectedRemoved = burger.ingredients.get(index);

        burger.removeIngredient(index);

        assertFalse("Удалённый ингредиент не должен быть в списке", burger.ingredients.contains(expectedRemoved));
    }

    @Test
    public void removeIngredientShouldThrowExceptionWhenIndexOutOfBounds() {
        int invalidIndex = burger.ingredients.size() + 1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при индексе >= size",
                IndexOutOfBoundsException.class,
                () -> burger.removeIngredient(invalidIndex));
    }

    @Test
    public void removeIngredientShouldThrowExceptionWhenIndexNegative() {
        int invalidIndex = -1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при отрицательном индексе",
                IndexOutOfBoundsException.class,
                () -> burger.removeIngredient(invalidIndex));
    }

    @Test
    public void removeIngredientFromEmptyListShouldThrowException() {
        if (!burger.ingredients.isEmpty()) return;
        assertThrows(
                "Попытка удаления из пустого списка должна выбросить IndexOutOfBoundsException",
                IndexOutOfBoundsException.class,
                () -> burger.removeIngredient(0));
    }

    @Test
    public void moveIngredientRightShouldChangePosition() {
        if (burger.ingredients.size() < 2) {
            return;
        }

        int indexFirst = 0;
        int indexLast = burger.ingredients.size() - 1;
        Ingredient movedIngredient = burger.ingredients.get(indexFirst);

        burger.moveIngredient(indexFirst, indexLast);
        int newIndex = burger.ingredients.indexOf(movedIngredient);

        assertEquals("Ингредиент должен переместиться на новую позицию", indexLast, newIndex);
    }

    @Test
    public void moveIngredientLeftShouldChangePosition() {
        if (burger.ingredients.size() < 2) {
            return;
        }

        int indexFirst = 0;
        int indexLast = burger.ingredients.size() - 1;
        Ingredient movedIngredient = burger.ingredients.get(indexLast);

        burger.moveIngredient(indexLast, indexFirst);
        int newIndex = burger.ingredients.indexOf(movedIngredient);

        assertEquals("Ингредиент должен переместиться на новую позицию", indexFirst, newIndex);
    }

    @Test
    public void moveIngredientShouldShiftAll() {
        if (expectedIngredients.size() < 2) {
            return;
        }

        List<Ingredient> original = List.copyOf(burger.ingredients);
        burger.moveIngredient(0, burger.ingredients.size() - 1);

        boolean allShiftedCorrectly = IntStream.range(1, original.size())
                .allMatch(i -> burger.ingredients.get(i - 1).equals(original.get(i)));

        boolean firstMovedToEnd =
                burger.ingredients.get(burger.ingredients.size() - 1).equals(original.get(0));

        assertTrue(
                "Все элементы должны сдвинуться влево, а первый — переместиться в конец",
                allShiftedCorrectly && firstMovedToEnd);
    }

    @Test
    public void moveIngredientShouldNotChangeListWhenIndicesAreEqual() {
        if (burger.ingredients.isEmpty()) return;

        List<Ingredient> original = List.copyOf(burger.ingredients);

        burger.moveIngredient(0, 0);

        assertEquals("Список не должен измениться, если fromIndex == toIndex", original, burger.ingredients);
    }

    @Test
    public void moveIngredientShouldThrowExceptionWhenIndexFromOutOfBounds() {
        if (burger.ingredients.isEmpty()) return;

        int invalidIndex = burger.ingredients.size() + 1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при некорректном fromIndex",
                IndexOutOfBoundsException.class,
                () -> burger.moveIngredient(invalidIndex, 0));
    }

    @Test
    public void moveIngredientShouldThrowExceptionWhenIndexToOutOfBounds() {
        if (burger.ingredients.isEmpty()) return;

        int invalidIndex = burger.ingredients.size() + 1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при некорректном toIndex",
                IndexOutOfBoundsException.class,
                () -> burger.moveIngredient(0, invalidIndex));
    }

    @Test
    public void getPriceShouldReturnCorrectTotalPrice() {
        float actualPrice = burger.getPrice();
        assertEquals("Цена должна быть корректной с учётом всех ингредиентов", expectedTotalPrice, actualPrice, DELTA);
    }

    // ------------------------------------------------------------------------------
    // Чек
    // ------------------------------------------------------------------------------

    @Test
    public void getReceiptShouldIncludeBunName() {
        String receipt = burger.getReceipt();
        assertTrue(
                "Чек должен содержать имя булочки: " + expectedBun.getName(), receipt.contains(expectedBun.getName()));
    }

    @Test
    public void getReceiptShouldIncludeAllIngredientNames() {
        if (expectedIngredients.isEmpty()) return;

        String receipt = burger.getReceipt();

        for (Ingredient ingredient : expectedIngredients) {

            String expectedIngredientsName =
                    String.format("= %s %s =%n", ingredient.getType().toString().toLowerCase(), ingredient.getName());
            assertTrue(
                    "Имя и тип ингредиента должно присутствовать в чеке: " + expectedIngredientsName,
                    receipt.contains(expectedIngredientsName));
        }
    }

    @Test
    public void getReceiptShouldContainTotalPrice() {
        String receipt = burger.getReceipt();

        assertTrue(
                "Чек должен содержать итоговую цену",
                receipt.contains(String.format("%nPrice: %f%n", expectedTotalPrice)));
    }

    @Test
    public void getReceiptsShouldBeEqualReceiptsTemplate() {
        assertEquals("Чек должен совпадать с шаблоном", getReceiptsTemplate(), burger.getReceipt());
    }

    public String getReceiptsTemplate() {
        StringBuilder receipt = new StringBuilder(String.format("(==== %s ====)%n", expectedBun.getName()));

        for (Ingredient ingredient : expectedIngredients) {
            receipt.append(
                    String.format("= %s %s =%n", ingredient.getType().toString().toLowerCase(), ingredient.getName()));
        }

        receipt.append(String.format("(==== %s ====)%n", expectedBun.getName()));
        receipt.append(String.format("%nPrice: %f%n", expectedTotalPrice));

        return receipt.toString();
    }
}
