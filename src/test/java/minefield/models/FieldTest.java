package minefield.models;

import minefield.exceptions.ExplosionException;
import minefield.models.Field;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FieldTest {

    private Field field;

    @BeforeEach
    void init() {
        field = new Field(3, 3);
    }

    @Test
    void testNeighborDistance1() {
        Field neighbor = new Field(3, 2);
        boolean result = field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testNeighborDistance2() {
        Field neighbor = new Field(2, 2);
        boolean result = field.addNeighbor(neighbor);

        assertTrue(result);
    }

    @Test
    void testIfNotNeighbor() {
        Field neighbor = new Field(3, 5);
        boolean result = field.addNeighbor(neighbor);

        assertFalse(result);
    }

    @Test
    void testMarkedFieldDefaultValue() {
        assertFalse(field.isMarked());
    }

    @Test
    void testToggleFieldMarkup() {
        field.toggleFieldMarkup();

        assertTrue(field.isMarked());
    }

    @Test
    void testToggleFieldMarkupTwoTimes() {
        field.toggleFieldMarkup();
        field.toggleFieldMarkup();

        assertFalse(field.isMarked());
    }

    @Test
    void testOpenNonMinedFieldAndNonMarkedField() {
        assertTrue(field.openField());
    }

    @Test
    void testOpenNonMinedFieldAndMarkedField() {
        field.toggleFieldMarkup();
        assertFalse(field.openField());
    }

    @Test
    void testOpenMinedFieldAndMarkedField() {
        field.toggleFieldMarkup();
        field.undermine();
        assertFalse(field.openField());
    }

    @Test
    void testOpenMinedFieldAndNonMarkedField() {
        field.undermine();

        assertThrows(ExplosionException.class, () -> {
            assertFalse(field.openField());
        });
    }

    @Test
    void testOpenFieldsAndNeighbors() {
        Field field11 = new Field(1, 1);
        Field field22 = new Field(2, 2);

        field22.addNeighbor(field11);

        field.addNeighbor(field22);
        field.openField();

        assertTrue(field11.isOpen() && field22.isOpen());
    }

    @Test
    void testOpenFieldsAndNeighborsWithMinedField() {
        Field field11 = new Field(1, 1);
        Field field12 = new Field(1, 2);
        field12.undermine();

        Field field22 = new Field(2, 2);
        field22.addNeighbor(field11);
        field22.addNeighbor(field12);

        field.addNeighbor(field22);
        field.openField();

        assertTrue(field22.isOpen() && field11.isClosed());
    }

}
