package m_polukhin.model;

import m_polukhin.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class Player {
    GameTurnState turnState = GameTurnState.ATTACK;
    private static int playerCount = 1;

    private final int number;

    private int reinforcePoints;

    public int getId() {
        return number;
    }

    private ModelListener listener;

    private final Field field;
    private final List<HexCell> hexCells;

    public Player(Field field) {
        this.field = field;
        this.number = playerCount++;
        hexCells = new ArrayList<>();
    }

    public ModelListener getListener() {
        return listener;
    }

    public void setListener(ModelListener listener) {
        if (this.listener != null) throw new UnsupportedOperationException("already inited");
        this.listener = listener;
    }

    public void addCell(HexCell cell) {
        hexCells.add(cell);
    }

    public void deleteCell(HexCell cell) {
        hexCells.remove(cell);
    }

    public int getNumberOfCells() {
        return hexCells.size();
    }

    public List<HexCellInfo> getPlayerCellList() {
        List<HexCellInfo> cellList = new ArrayList<>();
        hexCells.forEach(cell -> cellList.add(cell.getInfo()));
        return cellList;
    }

    private void attack(HexCell cell1, HexCell cell2) throws MoveException {
        if (cell1.getOwner() != this) {
            throw new MoveException("You can't attack with not your cell");
        } else if (cell2.getOwner() == this) {
            throw new MoveException("You can't attack your own cell");
        } else if (cell1.getPower() <= 1) {
            throw new MoveException("Only cells with >=2 power can attack");
        } else if (!field.areNeighbors(cell1.getPosition(),cell2.getPosition())) {
            throw new MoveException("Only neighbor cells can attack");
        } else if (cell1.getOwner() == cell2.getOwner()) {
            throw new MoveException("Can not attack your own cells");
        } else while(cell1.getPower() > 1) {
            cell1.setPower(cell1.getPower() - 1);
            int attackPower = new Random().nextInt(3) + 1;
            cell2.setPower(cell2.getPower() - attackPower);
            if (cell2.getPower() <= 0) {
                if (cell2.getOwner()!=null) cell2.getOwner().deleteCell(cell2);
                cell2.setOwner(cell1.getOwner());
                cell1.getOwner().addCell(cell2);
                cell2.setPower(cell1.getPower());
                cell1.setPower(1);
                return;
            }
        }

    }

    private void reinforce(HexCell cell) throws MoveException {
        if (cell.getOwner() != this)
            throw new MoveException("You can only reinforce your cells");
        if (reinforcePoints == 0) return;
        cell.setPower(cell.getPower() + 1);
        reinforcePoints--;
    }

    public void move(Point cords1, Point cords2) throws MoveException {
        if (turnState == GameTurnState.ATTACK) {
            attack(field.getCell(cords1), field.getCell(cords2));
        } else {
            assert cords1 == cords2 : cords1;
            reinforce(field.getCell(cords1));
            getListener().setReinforceInfo(reinforcePoints);
        }
    }

    public boolean nextState() {
        // returns false if turn is done
        if (turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = getNumberOfCells();
            getListener().setReinforceInfo(reinforcePoints);
            return true;
        } else {
            turnState = GameTurnState.ATTACK;
            getListener().setAttackInfo();
            return false;
        }
    }
}
