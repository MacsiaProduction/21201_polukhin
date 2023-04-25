package m_polukhin.utils;

public interface ViewListener {
    HexCellInfo getCellState(int y, int x);

    boolean isCellPresent(int y, int x);

    void endTurnButtonClicked();

    void cellClicked(Point cords) throws MoveException;
}

