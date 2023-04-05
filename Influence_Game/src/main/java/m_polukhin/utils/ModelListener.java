package m_polukhin.utils;

public interface ModelListener {
    void updateView();
    void setAttackInfo(Player player);
    void setReinforceInfo(Player player, int powerRemain);
    void gameOver();
}

