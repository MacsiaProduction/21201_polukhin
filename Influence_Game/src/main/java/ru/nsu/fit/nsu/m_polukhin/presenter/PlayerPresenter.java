package ru.nsu.fit.nsu.m_polukhin.presenter;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.utils.*;
import ru.nsu.fit.nsu.m_polukhin.view.GameView;

public class PlayerPresenter implements ModelListener, ViewListener {
    private GameView view;
    private GameModel model;
    private int ownerId;

    @Override
    public void init(GameModel model, int ownerId) {
        this.model = model;
        this.ownerId = ownerId;
        this.view = new GameView(this, model.rows(), model.columns());
    }

    @Override
    public void startOfTurn() {
        view.updateState();
        view.askMove();
    }

    @Override
    public void askMove() {
        view.updateState();
    }

    public void setAttackInfo() {
        view.setAttackInfo();
        view.updateState();
    }

    public void setReinforceInfo(long powerRemain) {
        view.setReinforceInfo(powerRemain);
    }

    public void gameOver() {
        view.gameOver();
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
        model.nextState(ownerId);
    }

    @Override
    public void cellClicked(Move move) throws MoveException {
        model.makeMove(ownerId, move);
    }
}