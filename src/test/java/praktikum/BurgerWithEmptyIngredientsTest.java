package praktikum;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BurgerWithEmptyIngredientsTest extends BaseTest {

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
