package m_polukhin.model;

import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.MoveException;

import java.util.List;

public class AI {
    private final GameModel model;
    private final int ownerId;

    public AI(GameModel model, int ownerId) {
        this.model = model;
        this.ownerId = ownerId;
    }

    public void generateTurn() throws MoveException {
        List<HexCellInfo> cellList = model.getPlayerCellList(ownerId);
        for (var attacker : cellList) {
            if (attacker.power() < 2) continue;
            var neighbours = model.getNeighbors(attacker.position());
            for (var victim : neighbours) {
                if (victim.ownerId() != attacker.ownerId()) {
                    model.cellClicked(ownerId,attacker.position());
                    model.cellClicked(ownerId,victim.position());
                    break;
                }
            }
        }
        model.nextState(ownerId);
        for (var cell : cellList) {
            model.cellClicked(ownerId,cell.position());
            model.cellClicked(ownerId,cell.position());
        }
        model.nextState(ownerId);
    }

}
