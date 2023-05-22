package ru.nsu.fit.nsu.m_polukhin.model;

import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;

import java.util.List;

class BasicAI extends AI{
    public BasicAI(Field field) {
        super(field);
    }
    @Override
    public void generateTurn() throws MoveException {
        List<HexCell> cellList = field.getPlayerCells(getId());
        for (var attacker : cellList) {
            if (attacker.getPower() < 2) continue;
            var neighbours = field.getNeighbors(attacker.getPosition());
            for (var victim : neighbours) {
                if (victim.ownerId() != attacker.getOwner().getId()) {
                    move(attacker.getPosition(), victim.position());
                    break;
                }
            }
        }
        nextState();
        for (var cell : cellList) {
            move(cell.getPosition(), cell.getPosition());
        }
        nextState();
    }
}
