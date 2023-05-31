package ru.nsu.fit.nsu.m_polukhin.model;

import org.testng.annotations.Test;
import ru.nsu.fit.nsu.m_polukhin.model.core.StabPresenter;
import ru.nsu.fit.nsu.m_polukhin.utils.Move;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ModelGenerateMoveTest {
    private GameModel createTestModel(List<Point> existing, Point... starts) {
        Player.resetCounter();
        return new GameModel(4,4, existing, Arrays.stream(starts).toList());
    }
    @Test
    public void testGenerateMoveWhenNoMovesAvailable() {
        var cords1 = new Point(0,0);
        var model = createTestModel(List.of(cords1),cords1);
        model.setPresenters(List.of(new StabPresenter()));
        assertNull(model.generateMove(1));
    }

    @Test
    public void testGenerateMoveToEnemyCell() {
        var cords1 = new Point(0,0);
        var cords2 = new Point(0,1);
        var model = createTestModel(List.of(cords1,cords2),cords1,cords2);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        assertEquals(model.generateMove(1), new Move(cords1, cords2));
        assertEquals(model.generateMove(2), new Move(cords2, cords1));
    }

    @Test
    public void testGenerateReinforceMove() {
        var cords1 = new Point(0,0);
        var cords2 = new Point(0,1);
        var model = createTestModel(List.of(cords1,cords2),cords1,cords2);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        model.nextState(1);
        assertEquals(model.generateMove(1), new Move(cords1, cords1));
    }

    @Test
    public void testGenerateMoveToNeutralCell() {
        var cords1 = new Point(0,0);
        var cords2 = new Point(0,1);
        var cords3 = new Point(0,2);
        var model = createTestModel(List.of(cords1,cords2,cords3),cords1,cords3);
        model.setPresenters(List.of(new StabPresenter(), new StabPresenter()));
        assertEquals(model.generateMove(1), new Move(cords1, cords2));
        assertEquals(model.generateMove(2), new Move(cords3, cords2));
    }
}
