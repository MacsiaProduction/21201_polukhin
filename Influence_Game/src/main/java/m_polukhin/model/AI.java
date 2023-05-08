package m_polukhin.model;

import m_polukhin.utils.MoveException;

// All AIs should extend this class
abstract class AI extends Player{
    public AI(Field field) {
        super(field);
    }

    public abstract void generateTurn() throws MoveException;

    public void move() {
        try {
            generateTurn();
        } catch (MoveException e) {
            throw new IllegalStateException(e);
        }
    }
}
