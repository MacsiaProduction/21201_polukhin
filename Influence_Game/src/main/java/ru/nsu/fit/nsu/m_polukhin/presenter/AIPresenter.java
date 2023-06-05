package ru.nsu.fit.nsu.m_polukhin.presenter;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;
import ru.nsu.fit.nsu.m_polukhin.utils.Move;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;

public class AIPresenter implements ModelListener {

    private GameModel model;
    private int ownerId;

    @Override
    public void init(GameModel model, int ownerId) {
        this.model = model;
        this.ownerId = ownerId;
    }

    @Override
    public void askMove() {
        try {
            Move move = model.generateMove(ownerId);
            if (move == null) {
                model.nextState(ownerId);
                return;
            }
            model.makeMove(ownerId, move);
        } catch (MoveException e) {
            throw new IllegalStateException("Illegal move made by AI");
        }
    }

    @Override
    public void startOfTurn() {}

    @Override
    public void setAttackInfo() {}

    @Override
    public void setReinforceInfo(long powerRemain) {}

    @Override
    public void gameOver() {}
}
