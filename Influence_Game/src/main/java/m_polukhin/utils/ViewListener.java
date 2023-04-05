package m_polukhin.utils;

import java.rmi.AccessException;

public interface ViewListener {
    HexCellInfo GetCellState(int y, int x) throws AccessException;

    boolean areValidCords(int y, int x);

    boolean isCellPresent(int y, int x);

    void endTurnButtonClicked();

    void newGameButton();

    void cellClicked(HexCellInfo cell) throws MoveException;
}

