package ru.nsu.fit.nsu.m_polukhin.model;

import org.testng.annotations.Test;
import ru.nsu.fit.nsu.m_polukhin.model.core.StabPresenter;
import ru.nsu.fit.nsu.m_polukhin.utils.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PlayingTest {
    private GameModel createTestModel(List<Point> existing, Point... starts) {
        return new GameModel(4,4, existing, Arrays.stream(starts).toList());
    }

    private void checkListener(StabPresenter presenter1, int startOfTurnCounter1, int gameOverCounter1, int askMoveCounter1) {
        assertEquals(presenter1.getStartOfTurnCounter(), startOfTurnCounter1);
        assertEquals(presenter1.getGameOverCounter(), gameOverCounter1);
        assertEquals(presenter1.getAskMoveCounter(), askMoveCounter1);
    }

    @Test
    public void testInit() {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        // current state validated in DataTest
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        assertTrue(model.isCellPresent(cords1));
        assertTrue(model.isCellPresent(cords2));
        assertTrue(model.isCellPresent(cords3));
        checkListener(presenter1,1,0,1);
        checkListener(presenter2,0,0,0);
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(0,-1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,1, cords3));
    }

    @Test
    public void testBadMove() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords1, cords3)));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords2, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords1, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords3, cords1)));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords3, cords2)));
        model.makeMove(0, new Move(cords1, cords2));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords2, cords3)));
    }

    @Test
    public void testOkMove() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        // current state validated in testInit
        model.makeMove(0, new Move(cords1, cords2));
        checkListener(presenter1,1,0,2);
        checkListener(presenter2,0,0,0);
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(1,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,0, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,1, cords3));
    }

    @Test
    public void testNextState() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.makeMove(0, new Move(cords1, cords2));
        // current state validated in testOkMove
        model.nextState(0);
        checkListener(presenter1,1,0,3);
        checkListener(presenter2,0,0,0);
        assertTrue(model.isCellPresent(cords1));
        assertTrue(model.isCellPresent(cords2));
        assertTrue(model.isCellPresent(cords3));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(1,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,0, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,1, cords3));
    }

    @Test
    public void testReinforce() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.makeMove(0, new Move(cords1, cords2));
        model.nextState(0);
        // current state validated in testNextState
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords3, cords3)));
        model.makeMove(0, new Move(cords1, cords1));
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(1,0, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,1, cords3));
        model.makeMove(0, new Move(cords2, cords2));
        checkListener(presenter1,1,0,5);
        checkListener(presenter2,0,0,0);
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(2,0, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(2,1, cords3));
        assertThrows(MoveException.class, () -> model.makeMove(0, new Move(cords1, cords1)));
    }

    @Test
    public void testNextPlayerTurn() {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.nextState(0);
        model.nextState(0);
        checkListener(presenter1,1,0,2);
        checkListener(presenter2,1,0,1);
    }

    @Test
    public void testSecondReinforce() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.nextState(0);
        model.nextState(0);
        model.nextState(1);
        model.makeMove(1, new Move(cords3, cords3));
        checkListener(presenter1,1,0,2);
        checkListener(presenter2,1,0,3);
        assertEquals(model.getCellInfo(cords1), new HexCellInfo(2,0, cords1));
        assertEquals(model.getCellInfo(cords2), new HexCellInfo(0,-1, cords2));
        assertEquals(model.getCellInfo(cords3), new HexCellInfo(3,1, cords3));
    }

    @Test
    public void testPlayerCycle() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.nextState(0);
        model.nextState(0);
        model.nextState(1);
        model.nextState(1);
        model.makeMove(0, new Move(cords1, cords2));
        checkListener(presenter1,2,0,4);
        checkListener(presenter2,1,0,2);
    }

    @Test
    public void testPlayerRemoval() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        Point cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3), cords1, cords2, cords3);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        var presenter3 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2, presenter3));
        model.nextState(0);
        model.makeMove(0, new Move(cords1, cords1));
        model.nextState(0);
        model.nextState(1);
        model.nextState(1);
        model.nextState(2);
        model.nextState(2);
        model.makeMove(0, new Move(cords1, cords2));
        model.nextState(0);
        model.nextState(0);
        checkListener(presenter1,3,0,7);
        checkListener(presenter2,1,1,2);
        checkListener(presenter3,1,0,2);
    }

    @Test
    public void testGameOver() throws MoveException {
        Point cords1 = new Point(0,0);
        Point cords2 = new Point(0,1);
        var model = createTestModel(List.of(cords1,cords2), cords1, cords2);
        var presenter1 = new StabPresenter();
        var presenter2 = new StabPresenter();
        model.setPresenters(List.of(presenter1, presenter2));
        model.makeMove(0, new Move(cords1, cords2));
        model.nextState(0);
        model.nextState(0);
        assertEquals(presenter1.getGameOverCounter(), 1);
        assertEquals(presenter2.getGameOverCounter(), 1);
    }
}
