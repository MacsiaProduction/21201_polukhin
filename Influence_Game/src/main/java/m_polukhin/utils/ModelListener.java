package m_polukhin.utils;

import m_polukhin.model.GameModel;

public interface ModelListener {
    void init(int ownerId, GameModel model);
    void askTurn();
    void updateView();
    void setAttackInfo();
    void setReinforceInfo(int powerRemain);
    void gameOver();
}

