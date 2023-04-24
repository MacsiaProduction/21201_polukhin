package m_polukhin.model;

import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.MoveException;
import m_polukhin.utils.Player;

import java.util.List;

public class AI {
    private final GameModel model;
    private final Player owner;

    public AI(GameModel model, Player owner) {
        this.model = model;
        this.owner = owner;
    }

    public void generateTurn(GameTurnState state) throws MoveException {
        List<HexCellInfo> cellList = model.getPlayerCellList(owner);
        if(state==GameTurnState.ATTACK) {
            for (var attacker : cellList) {
                if (attacker.power() < 2) continue;
                var neighbours = model.getNeighbors(attacker.position());
                for (var victim : neighbours) {
                    if (victim.owner() != attacker.owner()) {
                        model.cellClicked(owner,attacker.position());
                        model.cellClicked(owner,victim.position());
                        break;
                    }
                }
            }
            model.nextTurn(owner);
        } else if(state==GameTurnState.REINFORCE) {
            for (var cell : cellList) {
                model.cellClicked(owner,cell.position());
                model.cellClicked(owner,cell.position());
            }
            model.nextTurn(owner);
        } else throw new UnsupportedOperationException("new GameTurnState not implemented");
    }

}
