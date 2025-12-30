package praktikum;

import static org.junit.Assert.*;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class BurgerNotEmptyIngredientsTest extends BaseTest {

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
    public void removeIngredientShouldRemoveByIndex() {
        int index = burger.ingredients.size() - 1;
        Ingredient expectedRemoved = burger.ingredients.get(index);
        burger.removeIngredient(index);

        assertFalse("Удалённый ингредиент не должен быть в списке", burger.ingredients.contains(expectedRemoved));
    }

    @Test
    public void getReceiptShouldIncludeAllIngredientNames() {
        String receipt = burger.getReceipt();

        for (Ingredient ingredient : expectedIngredients) {
            String expectedIngredientsName =
                    String.format("= %s %s =%n", ingredient.getType().toString().toLowerCase(), ingredient.getName());
            assertTrue(
                    "Имя и тип ингредиента должно присутствовать в чеке: " + expectedIngredientsName,
                    receipt.contains(expectedIngredientsName));
        }
    }
}
