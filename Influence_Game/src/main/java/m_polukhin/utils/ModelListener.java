package m_polukhin.utils;

import m_polukhin.model.GameModel;
import m_polukhin.model.GameTurnState;

public interface ModelListener {
    void init(Player owner, GameModel model);
    void askTurn(GameTurnState state);
    void updateView();
    void setAttackInfo();
    void setReinforceInfo(int powerRemain);
    void gameOver();
}

