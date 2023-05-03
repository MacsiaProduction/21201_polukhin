package m_polukhin.presenter;

import m_polukhin.model.GameModel;
import m_polukhin.utils.*;

public abstract class Presenter implements ViewListener, ModelListener {
    protected GameModel model;
    protected int ownerId;

    public void init(int ownerId, GameModel model) {
        this.ownerId = ownerId;
        this.model = model;
    }

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
        model.nextTurn(ownerId);
    }

    @Override
    public void cellClicked(Point cords) throws MoveException {
        model.cellClicked(ownerId, cords);
    }
}
