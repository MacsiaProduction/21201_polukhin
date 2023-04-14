package m_polukhin.presenter;

import m_polukhin.model.AI;
import m_polukhin.model.GameModel;
import m_polukhin.model.GameTurnState;
import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.MoveException;
import m_polukhin.utils.Player;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;


public class AIPresenter extends Presenter {
    private final AI ai;
    public AIPresenter(AI ai) {
        this.ai = ai;
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
