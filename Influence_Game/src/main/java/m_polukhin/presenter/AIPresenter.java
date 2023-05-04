package m_polukhin.presenter;

import m_polukhin.model.*;
import m_polukhin.utils.*;

public class AIPresenter extends Presenter {
    private AI ai;

    @Override
    public void init(int ownerId, GameModel model) {
        this.ownerId = ownerId;
        this.model = model;
        this.ai = new AI(model, ownerId);
    }

    @Override
    public void askTurn() {
        try {
            ai.generateTurn();
        } catch (MoveException e) {
            throw new IllegalStateException(e+"\nWrong turn made by ai");
        }
    }

    @Override
    public void updateView() {}

    @Override
    public void setAttackInfo() {}

    @Override
    public void setReinforceInfo(int powerRemain) {}

    @Override
    public void gameOver() {}
}
