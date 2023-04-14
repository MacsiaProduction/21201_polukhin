package m_polukhin.presenter;

import m_polukhin.model.GameModel;
import m_polukhin.model.GameTurnState;
import m_polukhin.utils.*;
import m_polukhin.view.GameView;

public class PlayerPresenter extends Presenter {
    private final GameView view;

    public PlayerPresenter(GameModel model) {
        this.model = model;
        this.view = new GameView(this, model.rows, model.columns);
    }

    @Override
    public void askTurn(GameTurnState state) {
        view.askTurn(state);
    }

    public void updateView() {
        view.updateState();
    }

    public void setAttackInfo(Player player) {
        view.setAttackInfo(player);
    }

    public void setReinforceInfo(Player player, int powerRemain) {
        view.setReinforceInfo(player, powerRemain);
    }

    public void gameOver() {
        view.gameOver();
    }
}