package m_polukhin.utils;

import m_polukhin.model.GameTurnState;

public interface ModelListener {
    void setOwner(Player owner);
    void askTurn(GameTurnState state);
    void updateView();
    void setAttackInfo(Player player);
    void setReinforceInfo(Player player, int powerRemain);
    void gameOver();
}

