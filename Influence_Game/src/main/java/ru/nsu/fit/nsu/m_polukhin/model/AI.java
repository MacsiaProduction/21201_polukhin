package ru.nsu.fit.nsu.m_polukhin.model;

import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;

// All AIs should extend this class
abstract class AI extends Player {
    public AI(Field field) {
        super(field);
    }

    @Override
    public ModelListener getListener() {
        throw new UnsupportedOperationException("AI don't have presenter");
    }

    @Override
    public void setListener(ModelListener listener) {
        throw new UnsupportedOperationException("AI don't have presenter");
    }

    public abstract void generateTurn() throws MoveException;

    Coords generateTurn() {
        return new Coords();
    }



    /**
     * must generate all turn by once.
     * nextState will be called exactly two times.
     */
    public void move() {
        try {
            generateTurn();
        } catch (MoveException e) {
            throw new IllegalStateException(e);
        }
    }
}
