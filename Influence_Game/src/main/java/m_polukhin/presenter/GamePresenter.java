package m_polukhin.presenter;

import m_polukhin.model.*;
import m_polukhin.utils.*;
import m_polukhin.view.GameView;

import java.rmi.AccessException;

public class GamePresenter implements ModelListener, ViewListener {
    public final int NUM_ROWS = 10;
    public final int NUM_COLUMNS = 10;
    private final GameModel model;
    private final GameView view;
    private Player owner;
    public GamePresenter() {
        this.model = new GameModel(this,NUM_ROWS,NUM_COLUMNS);
        this.view  =  new GameView(this,NUM_ROWS,NUM_COLUMNS);
        newGameButton();
    }
    @Override
    public void setOwner(Player owner) {
        if(this.owner != null) throw new UnsupportedOperationException();
        this.owner = owner;
    }
    public HexCellInfo GetCellState(int y, int x) throws AccessException {
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
        model.initBoard(new BoardGenerator());
        view.updateState();
    }
    public void cellClicked(HexCellInfo cell) throws MoveException {
        model.cellClicked(owner, cell);
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