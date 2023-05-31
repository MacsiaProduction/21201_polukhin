package ru.nsu.fit.nsu.m_polukhin.model;

import org.testng.annotations.Test;
import ru.nsu.fit.nsu.m_polukhin.model.core.StabPresenter;
import ru.nsu.fit.nsu.m_polukhin.utils.HexCellInfo;
import ru.nsu.fit.nsu.m_polukhin.utils.Move;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingTest {
    private GameModel createTestModel(List<Point> existing, Point... starts) {
        Player.resetCounter();
        return new GameModel(4,4, existing, Arrays.stream(starts).toList());
    }

    @Test
    public void testInit() {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        // current state validated in DataTest
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        assertTrue(model.isCellPresent(cords1));
        assertTrue(model.isCellPresent(cords2));
        assertTrue(model.isCellPresent(cords3));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,1, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(0,0, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
    }

    @Test
    public void testBadMove() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords1, cords3)));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords2, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords1, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords3, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords3, cords2)));
        model.makeMove(1, new Move(cords1, cords2));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords2, cords3)));
    }

    @Test
    public void testOkMove() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        // current state validated in testInit
        model.makeMove(1, new Move(cords1, cords2));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(1,1, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
    }

    @Test
    public void testNextState() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.makeMove(1, new Move(cords1, cords2));
        // current state validated in testOkMove
        model.nextState(1);
        assertTrue(model.isCellPresent(cords1));
        assertTrue(model.isCellPresent(cords2));
        assertTrue(model.isCellPresent(cords3));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(1,1, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
    }

    @Test
    public void testReinforce() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.makeMove(1, new Move(cords1, cords2));
        model.nextState(1);
        // current state validated in testNextState
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords3, cords3)));
        model.makeMove(1, new Move(cords1, cords1));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,1, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
        model.makeMove(1, new Move(cords2, cords2));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,1, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(2,1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
        assertThrows(MoveException.class, () -> model.makeMove(1, new Move(cords1, cords1)));
    }

    @Test
    public void testNextPlayerTurn() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.makeMove(1, new Move(cords1, cords2));
        model.nextState(1);
        model.makeMove(1, new Move(cords1, cords1));
        model.makeMove(1, new Move(cords2, cords2));
        // current state validated in testReinforce
        model.nextState(1);
        model.makeMove(2, new Move(cords3, cords2));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,1, cords1));
        assertTrue(model.getCellInfo(cords2).equals(new HexCellInfo(1, 1, cords2)) ||
                model.getCellInfo(cords2).equals(new HexCellInfo(1, 2, cords2)));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(1,2, cords3));
        assertThrows(MoveException.class, () -> model.makeMove(2, new Move(cords2, cords1)));
    }

    @Test
    public void testSecondReinforces() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.makeMove(1, new Move(cords1, cords2));
        model.nextState(1);
        model.makeMove(1, new Move(cords1, cords1));
        model.makeMove(1, new Move(cords2, cords2));
        model.nextState(1);
        model.makeMove(2, new Move(cords3, cords2));
        // current state validated in testNextPlayerTurn
        model.nextState(2);
        model.makeMove(2, new Move(cords3,cords3));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,1, cords1));
        assertTrue(model.getCellInfo(cords2).equals(new HexCellInfo(1, 1, cords2)) ||
                model.getCellInfo(cords2).equals(new HexCellInfo(1, 2, cords2)));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
    }

    @Test
    public void testPlayerCycle() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.makeMove(1, new Move(cords1, cords2));
        model.nextState(1);
        model.makeMove(1, new Move(cords1, cords1));
        model.makeMove(1, new Move(cords2, cords2));
        model.nextState(1);
        model.makeMove(2, new Move(cords3, cords2));
        model.nextState(2);
        model.makeMove(2, new Move(cords3,cords3));
        // current state validated in testSecondReinforces
        model.nextState(2);
        model.nextState(1);
        model.makeMove(1, new Move(cords1, cords1));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(3,1, cords1));
        assertTrue(model.getCellInfo(cords2).equals(new HexCellInfo(1, 1, cords2)) ||
                model.getCellInfo(cords2).equals(new HexCellInfo(1, 2, cords2)));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,2, cords3));
    }
}
