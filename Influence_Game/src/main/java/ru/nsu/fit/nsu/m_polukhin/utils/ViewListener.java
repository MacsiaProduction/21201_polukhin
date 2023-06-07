package ru.nsu.fit.nsu.m_polukhin.utils;

public interface ViewListener {

    HexCellInfo getCellState(int y, int x);

    boolean isCellPresent(int y, int x);

    void endTurnButtonClicked();

    void makeMove(Move move) throws MoveException;
}

