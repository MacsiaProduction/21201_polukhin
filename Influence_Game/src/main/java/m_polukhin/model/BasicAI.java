package m_polukhin.model;

import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.MoveException;

import java.util.List;

class BasicAI extends AI{
    public BasicAI(Field field) {
        super(field);
    }
    @Override
    public void generateTurn() throws MoveException {
        List<HexCellInfo> cellList = getPlayerCellList();
        for (var attacker : cellList) {
            if (attacker.power() < 2) continue;
            var neighbours = field.getNeighbors(attacker.position());
            for (var victim : neighbours) {
                if (victim.ownerId() != attacker.ownerId()) {
                    move(attacker.position(), victim.position());
                    break;
                }
            }
        }
        nextState();
        for (var cell : cellList) {
            move(cell.position(), cell.position());
        }
        nextState();
    }
}
