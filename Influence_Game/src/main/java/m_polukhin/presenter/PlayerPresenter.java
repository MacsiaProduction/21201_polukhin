package m_polukhin.presenter;

import m_polukhin.model.GameModel;
import m_polukhin.utils.GameTurnState;
import m_polukhin.view.GameView;

public class PlayerPresenter extends Presenter {
    private final GameView view;

    public PlayerPresenter(GameModel model) {
        this.model = model;
        this.view = new GameView(this, model.rows(), model.columns());
    }

    @Override
    public void askTurn(GameTurnState state) {
        view.askTurn(state);
    }

    public void updateView() {
        view.updateState();
    }

    public void setAttackInfo() {
        view.setAttackInfo();
    }

    public void setReinforceInfo(int powerRemain) {
        view.setReinforceInfo(powerRemain);
    }

    public void gameOver() {
        view.gameOver();
    }
}