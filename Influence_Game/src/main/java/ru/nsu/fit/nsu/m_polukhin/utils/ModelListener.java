package ru.nsu.fit.nsu.m_polukhin.utils;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;

public interface ModelListener {
    void init(GameModel model, int ownerId);
    void startOfTurn();
    void askMove();
    void setAttackInfo();
    void setReinforceInfo(long powerRemain);
    void gameOver();
}

