package ru.nsu.fit.nsu.m_polukhin.utils;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;

public interface ModelListener {
//    void init(int ownerId, GameModel model);
    void askTurn();
    void updateView();
    void setAttackInfo();
    void setReinforceInfo(int powerRemain);
    void gameOver();
}

