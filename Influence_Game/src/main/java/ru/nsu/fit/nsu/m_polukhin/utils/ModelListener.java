package ru.nsu.fit.nsu.m_polukhin.utils;

public interface ModelListener {
//    void init(int ownerId, GameModel model);
    void askTurn();
    void updateView();
    void setAttackInfo();
    void setReinforceInfo(long powerRemain);
    void gameOver();
}

