package m_polukhin.model;

import m_polukhin.utils.Point;

import java.util.Arrays;

class Field {
    private final HexCell[][] board;

    Field(int yMax, int xMax) {
        board = new HexCell[yMax][xMax];
        for (int i = 0; i < yMax; i++) {
            Arrays.fill(board[i], null);
        }
    }

    public HexCell getCell(Point p) {
        return board[p.y()][p.x()];
    }

    public void initCell(Point cords) {
        board[cords.y()][cords.x()] = new HexCell(cords);
    }
}
