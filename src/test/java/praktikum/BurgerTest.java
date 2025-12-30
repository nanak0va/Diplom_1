package praktikum;

import static org.junit.Assert.*;

import java.util.List;
import java.util.stream.IntStream;
import org.junit.Before;
import org.junit.Test;

public class BurgerTest extends BaseTest {

    Burger burger;
    List<Ingredient> expectedIngredients;

    @Before
    public void setUp() {
        burger = new Burger();

        expectedIngredients = List.of(
                mockIngredient(IngredientType.FILLING, "very cheap filling", -999999.001f),
                mockIngredient(IngredientType.SAUCE, "cheap sauce", -0.01f),
                mockIngredient(IngredientType.FILLING, "free filling", 0.0f),
                mockIngredient(IngredientType.SAUCE, "low cost sauce", 0.01f),
                mockIngredient(IngredientType.FILLING, "expensive filling", 99999.99f),
                mockIngredient(IngredientType.SAUCE, "astronomical sauce", 10000000.0f));

        expectedIngredients.forEach(burger::addIngredient);
    }

    @Test
    public void removeIngredientFromEmptyListShouldThrowException() {
        assertThrows(
                "Попытка удаления из пустого списка должна выбросить IndexOutOfBoundsException",
                IndexOutOfBoundsException.class,
                () -> new Burger().removeIngredient(0));
    }

    @Test
    public void moveIngredientRightShouldChangePosition() {
        int indexFirst = 0;
        int indexLast = burger.ingredients.size() - 1;
        Ingredient movedIngredient = burger.ingredients.get(indexFirst);

        burger.moveIngredient(indexFirst, indexLast);
        int newIndex = burger.ingredients.indexOf(movedIngredient);

        assertEquals("Ингредиент должен переместиться на новую позицию", indexLast, newIndex);
    }

    @Test
    public void moveIngredientLeftShouldChangePosition() {
        int indexFirst = 0;
        int indexLast = burger.ingredients.size() - 1;
        Ingredient movedIngredient = burger.ingredients.get(indexLast);

        burger.moveIngredient(indexLast, indexFirst);
        int newIndex = burger.ingredients.indexOf(movedIngredient);

        assertEquals("Ингредиент должен переместиться на новую позицию", indexFirst, newIndex);
    }

    @Test
    public void moveIngredientShouldShiftAll() {
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
    public void moveIngredientShouldThrowExceptionWhenIndexFromOutOfBounds() {
        int invalidIndex = burger.ingredients.size() + 1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при некорректном fromIndex",
                IndexOutOfBoundsException.class,
                () -> burger.moveIngredient(invalidIndex, 0));
    }

    @Test
    public void moveIngredientShouldThrowExceptionWhenIndexToOutOfBounds() {
        int invalidIndex = burger.ingredients.size() + 1;
        assertThrows(
                "Должно выбросить IndexOutOfBoundsException при некорректном toIndex",
                IndexOutOfBoundsException.class,
                () -> burger.moveIngredient(0, invalidIndex));
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
    public void moveIngredientShouldNotChangeListWhenIndicesAreEqual() {
        List<Ingredient> original = List.copyOf(burger.ingredients);
        burger.moveIngredient(0, 0);

        assertEquals("Список не должен измениться, если fromIndex == toIndex", original, burger.ingredients);
    }
}
