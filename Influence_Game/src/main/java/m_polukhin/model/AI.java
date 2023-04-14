package m_polukhin.model;

import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.MoveException;
import m_polukhin.utils.Player;
import m_polukhin.utils.Point;

import java.rmi.AccessException;
import java.util.ArrayList;
import java.util.List;

public class AI {
    private final GameModel model;
    private final Player owner;

    public AI(GameModel model, Player owner) {
        this.model = model;
        this.owner = owner;
    }


    private List<HexCellInfo> getPlayerCellList() {
        List<HexCellInfo> cellList= new ArrayList<>();
        for(int i =0; i< model.rows; i++) {
            for(int j = 0; j< model.columns; j++){
                var point = new Point(i,j);
                if(model.isCellPresent(point)) {
                    try {
                        var tmp = model.getCellInfo(point);
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
    public void generateTurn(GameTurnState state) throws MoveException {
        List<HexCellInfo> cellList = getPlayerCellList();
        if(state==GameTurnState.ATTACK) {
            for (var attacker : cellList) {
                if (attacker.power() < 2) continue;
                var neighbours = model.getNeighbors(attacker.position());
                for (var victim : neighbours) {
                    if (victim.owner() != attacker.owner()) {
                        //model.cellClicked(owner,null);
                        model.cellClicked(owner,attacker.position());
                        model.cellClicked(owner,victim.position());
                        break;
                    }
                }
            }
            model.nextTurn();
        } else if(state==GameTurnState.REINFORCE) {
            for (var cell : cellList) {
                model.cellClicked(owner,cell.position());
                model.cellClicked(owner,cell.position());
            }
            model.nextTurn();
        } else throw new UnsupportedOperationException("new GameTurnState not implemented");
    }

}
