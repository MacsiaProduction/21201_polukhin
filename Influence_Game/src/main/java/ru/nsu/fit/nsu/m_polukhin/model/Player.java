package ru.nsu.fit.nsu.m_polukhin.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;
import ru.nsu.fit.nsu.m_polukhin.utils.Move;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;

import java.util.Random;

class Player {
    private GameTurnState turnState = GameTurnState.ATTACK;

    private long reinforcePoints;

    private static int playerCount = 1;

    @TestOnly
    static void resetCounter() {
        playerCount = 1;
    }

    private final int number;

    public int getId() {
        return number;
    }

    public final Field field;

    private ModelListener listener;

    public Player(Field field) {
        this.field = field;
        this.number = playerCount++;
    }

    public GameTurnState getTurnState() {
        return this.turnState;
    }

    public long getReinforcePoints() {
        return reinforcePoints;
    }

    public ModelListener getListener() {
        if (this.listener == null) throw new UnsupportedOperationException("listener haven't been inited");
        return listener;
    }

    public void setListener(ModelListener listener) {
        if (this.listener != null) throw new UnsupportedOperationException("listener have been already inited");
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
            while(field.getCellInfo(move.start()).power() > 1) {
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
        if (reinforcePoints == 0) throw new MoveException("You can only reinforce your cells <number of your cells> times");
        field.setPower(cell.position(), cell.power()+1);
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
