package m_polukhin.utils;

import java.rmi.AccessException;

public interface ViewListener {
    HexCellInfo getCellState(int y, int x) throws AccessException;

    boolean areValidCords(int y, int x);

    boolean isCellPresent(int y, int x);

    void endTurnButtonClicked();

    void cellClicked(Point cords) throws MoveException;
}

