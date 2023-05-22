package ru.nsu.fit.nsu.m_polukhin.presenter;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.view.GameView;

public class PlayerPresenter extends Presenter {
    private final GameView view;

    public PlayerPresenter(GameModel model) {
        this.model = model;
        this.view = new GameView(this, model.rows(), model.columns());
    }

    @Override
    public void askTurn() {
        view.askTurn();
    }

    public void updateView() {
        view.updateState();
    }

    public void setAttackInfo() {
        view.setAttackInfo();
    }

    public void setReinforceInfo(long powerRemain) {
        view.setReinforceInfo(powerRemain);
    }

    public void gameOver() {
        view.gameOver();
    }
}