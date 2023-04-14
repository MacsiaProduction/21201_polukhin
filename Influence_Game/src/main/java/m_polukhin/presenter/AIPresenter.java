package m_polukhin.presenter;

import m_polukhin.model.*;
import m_polukhin.utils.*;

public class AIPresenter extends Presenter {
    private AI ai;

    @Override
    public void init(Player owner, GameModel model) {
        this.owner = owner;
        this.model = model;
        ai = new AI(model, owner);
    }

    @Override
    public void askTurn(GameTurnState state) {
        try {
            ai.generateTurn(state);
        } catch (MoveException e) {
            throw new RuntimeException(e+"\nwrong turn made by ai");
        }
    }
    @Override
    public void updateView() {}
    @Override
    public void setAttackInfo(Player player) {}
    @Override
    public void setReinforceInfo(Player player, int powerRemain) {}
    @Override
    public void gameOver() {}
}
