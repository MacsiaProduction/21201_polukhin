package ru.nsu.fit.nsu.m_polukhin.presenter;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.utils.*;

public abstract class Presenter implements ViewListener, ModelListener {
    protected GameModel model;
    protected int ownerId;

    @Override
    public HexCellInfo getCellState(int y, int x) {
        return model.getCellInfo(new Point(y,x));
    }

    @Override
    public boolean isCellPresent(int y, int x) {
        return model.isCellPresent(new Point(y,x));
    }

    @Override
    public void endTurnButtonClicked() {
        model.nextState(ownerId);
    }

    @Override
    public void cellClicked(Point cords) throws MoveException {
        model.cellClicked(ownerId, cords);
    }
}
