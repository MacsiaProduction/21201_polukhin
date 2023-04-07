package m_polukhin.presenter;

import m_polukhin.model.GameModel;
import m_polukhin.model.GameTurnState;
import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.ModelListener;
import m_polukhin.utils.MoveException;
import m_polukhin.utils.Player;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

public class AI implements ModelListener {
    private Player owner;
    private final GameModel model;
    public AI(GameModel model) {
        this.model = model;
    }

    @Override
    public void setOwner(Player owner) {
        if(this.owner != null) throw new UnsupportedOperationException();
        this.owner = owner;
    }

    @Override
    public void askTurn(GameTurnState state) {
        try {
            generateTurn(state);
        } catch (MoveException e) {
            throw new RuntimeException(e+"\nwrong turn made by ai");
        }
    }

    private List<HexCellInfo> getPlayerCellList() {
        List<HexCellInfo> cellList= new ArrayList<>();
        for(int i =0; i< model.rows; i++) {
            for(int j = 0; j< model.columns; j++){
                if(model.isCellPresent(i,j)) {
                    try {
                        var tmp = model.getCellInfo(i,j);
                        if (tmp.owner() == owner)
                            cellList.add(tmp);
                    } catch (AccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return cellList;
    }
    private void generateTurn(GameTurnState state) throws MoveException {
        List<HexCellInfo> cellList = getPlayerCellList();
        if(state==GameTurnState.ATTACK) {
            for (var attacker : cellList) {
                if (attacker.power() < 2) continue;
                var neighbours = model.getNeighbors(attacker);
                for (var victim : neighbours) {
                    if (victim.owner() != attacker.owner()) {
                        //model.cellClicked(owner,null);
                        model.cellClicked(owner,attacker);
                        model.cellClicked(owner,victim);
                        break;
                    }
                }
            }
            model.nextTurn();
        } else if(state==GameTurnState.REINFORCE) {
            for (var cell : cellList) {
                model.cellClicked(owner,cell);
                model.cellClicked(owner,cell);
            }
            model.nextTurn();
        } else throw new UnsupportedOperationException("new GameTurnState not implemented");
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
