package ru.nsu.fit.nsu.m_polukhin.model;

import org.testng.annotations.Test;
import ru.nsu.fit.nsu.m_polukhin.utils.HexCellInfo;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelDataTest {
    private GameModel createTestModel(Point... cords) {
        return new GameModel(4,4, Arrays.stream(cords).toList(), Collections.emptyList());
    }
    @Test
    public void testIsCellPresent() {
        Point cords1 = new Point(0, 0);
        Point cords2 = new Point(3, 3);
        Point cords3 = new Point(2, 2);
        var model = createTestModel(cords1, cords2);
        assertTrue(model.isCellPresent(cords1));
        assertTrue(model.isCellPresent(cords2));
        assertFalse(model.isCellPresent(cords3));
    }

    @Test
    public void testInvalidCell() {
        Point cords = new Point(5, 5);
        assertThrows(Exception.class,() -> createTestModel(cords));
    }

    @Test
    public void testGetCellInfo() {
        Point cords1 = new Point(0, 0);
        Point cords2 = new Point(3, 3);
        Point cords3 = new Point(2, 2);
        var model = createTestModel(cords1, cords2);
        HexCellInfo info1 = new HexCellInfo(0, -1, cords1);
        HexCellInfo info2 = new HexCellInfo(0, -1, cords2);
        assertEquals(info1, model.getCellInfo(cords1));
        assertEquals(info2, model.getCellInfo(cords2));
        assertThrows(Exception.class, ()->model.getCellInfo(cords3));
    }

    @Test
    public void testGetPossibleNeighbors1() {
        Point cords = new Point(3, 2);
        int rows = 5;
        int columns = 6;
        List<Point> expectedResult = new ArrayList<>();
        expectedResult.add(new Point(4, 2));
        expectedResult.add(new Point(2, 2));
        expectedResult.add(new Point(3, 3));
        expectedResult.add(new Point(3, 1));
        expectedResult.add(new Point(4, 1));
        expectedResult.add(new Point(2, 1));

        assertEquals(GameModel.getPossibleNeighbors(rows, columns, cords), expectedResult);
    }

    @Test
    public void testGetPossibleNeighbors2() {
        Point cords = new Point(1, 4);
        int rows = 5;
        int columns = 6;
        List<Point> expectedResult = new ArrayList<>();
        expectedResult.add(new Point(2, 4));
        expectedResult.add(new Point(0, 4));
        expectedResult.add(new Point(1, 5));
        expectedResult.add(new Point(1, 3));
        expectedResult.add(new Point(2, 3));
        expectedResult.add(new Point(0, 3));

        assertEquals(GameModel.getPossibleNeighbors(rows, columns, cords), expectedResult);
    }
}
