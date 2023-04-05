package m_polukhin.presenter;

import m_polukhin.model.*;
import m_polukhin.view.GameView;

import java.rmi.AccessException;

public class GamePresenter {
    public final int NUM_ROWS = 10;
    public final int NUM_COLUMNS = 10;
    private final GameModel model;
    private final GameView view;

    public GamePresenter() {
        this.model = new GameModel(this);
        this.view  =  new GameView(this);
        newGameButton();
    }
    public HexCellInfo GetCellState(int y,int x) throws AccessException {
        return model.getCellInfo(y,x);
    }
    public boolean areValidCords(int y,int x) {
        return model.areValidCords(y,x);
    }
    public boolean isCellPresent(int y, int x) {
        return model.isCellPresent(y,x);
    }
    public void endTurnButtonClicked() {
        model.nextTurn();
    }
    public void newGameButton() {
        model.initBoard();
        view.updateState();
    }
    public void cellClicked(HexCellInfo cell) throws MoveException {
        model.cellClicked(cell);
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