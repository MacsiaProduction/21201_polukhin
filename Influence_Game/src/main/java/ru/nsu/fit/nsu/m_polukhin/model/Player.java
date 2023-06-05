package ru.nsu.fit.nsu.m_polukhin.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;
import ru.nsu.fit.nsu.m_polukhin.utils.Move;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;

import java.util.Random;

class Player {

    public final Field field;
    private final int number;

    private GameTurnState turnState = GameTurnState.ATTACK;
    private long reinforcePoints;
    private ModelListener listener;

    public Player(Field field, int number) {
        this.field = field;
        this.number = number;
    }

    public int getId() {
        return number;
    }

    public GameTurnState getTurnState() {
        return this.turnState;
    }

    public long getReinforcePoints() {
        return reinforcePoints;
    }

    public ModelListener getListener() {
        if (this.listener == null) throw new UnsupportedOperationException("listener hasn't been initialized");
        return listener;
    }

    public void setListener(ModelListener listener) {
        if (this.listener != null) throw new UnsupportedOperationException("listener has been already initialized");
        this.listener = listener;
    }

    /**
     * @return true if the turn is not done, false otherwise
     */
    public boolean nextState() {
        if (turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = field.getNumberOfCells(getId());
            getListener().setReinforceInfo(reinforcePoints);
            return true;
        } else {
            turnState = GameTurnState.ATTACK;
            getListener().setAttackInfo();
            return false;
        }
    }

    private void attack(@NotNull Move move) throws MoveException {
        var cell1 = field.getCellInfo(move.start());
        var cell2 = field.getCellInfo(move.end());

        if (cell1.ownerId() != getId()) {
            throw new MoveException("You can't attack with not your cell");
        } else if (cell2.ownerId() == getId()) {
            throw new MoveException("You can't attack your own cell");
        } else if (cell1.power() <= 1) {
            throw new MoveException("Only cells with >=2 power can attack");
        } else if (!field.areNeighbors(move.start(), move.end())) {
            throw new MoveException("Only neighbor cells can attack");
        } else if (cell1.ownerId() == cell2.ownerId()) {
            throw new MoveException("Can not attack your own cells");
        } else {
            while (field.getCellInfo(move.start()).power() > 1) {
                field.setPower(cell1.position(), field.getCellInfo(move.start()).power() - 1);
                int attackPower = new Random().nextInt(3) + 1;
                field.setPower(cell2.position(), field.getCellInfo(move.end()).power() - attackPower);
                if (field.getCellInfo(move.end()).power() <= 0) {
                    field.conquer(cell1, cell2);
                    field.setPower(cell2.position(), field.getCellInfo(move.start()).power());
                    field.setPower(cell1.position(), 1);
                    return;
                }
            }
        }
    }

    private void reinforce(@NotNull Move move) throws MoveException {
        assert move.start() == move.end();
        var cell = field.getCellInfo(move.start());
        if (cell.ownerId() != getId())
            throw new MoveException("You can only reinforce your cells");
        if (reinforcePoints == 0)
            throw new MoveException("You can only reinforce your cells <number of your cells> times");
        field.setPower(cell.position(), cell.power() + 1);
        reinforcePoints--;
    }

    public void move(Move move) throws MoveException {
        if (turnState == GameTurnState.ATTACK) {
            attack(move);
        } else {
            reinforce(move);
            getListener().setReinforceInfo(reinforcePoints);
        }
    }
}
