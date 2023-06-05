package ru.nsu.fit.nsu.m_polukhin.model.core;

import org.jetbrains.annotations.TestOnly;
import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;

@TestOnly
public class StabPresenter implements ModelListener {
    private int askMoveCounter = 0;
    private int startOfTurnCounter = 0;

    private int gameOverCounter = 0;

    public int getStartOfTurnCounter() {
        return startOfTurnCounter;
    }

    public int getAskMoveCounter() {
        return askMoveCounter;
    }

    public int getGameOverCounter() {
        return gameOverCounter;
    }


    @Override
    public void init(GameModel model, int ownerId) {}

    @Override
    public void askMove() {askMoveCounter++;}

    @Override
    public void startOfTurn() {startOfTurnCounter++;}

    @Override
    public void setAttackInfo() {}

    @Override
    public void setReinforceInfo(long powerRemain) {}

    @Override
    public void gameOver() {gameOverCounter++;}
}
