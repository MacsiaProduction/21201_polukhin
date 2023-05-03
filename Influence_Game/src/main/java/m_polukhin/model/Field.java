package m_polukhin.model;

import m_polukhin.utils.Point;

import java.util.Arrays;

class Field {
    private final HexCell[][] board;

    Field(int y_max, int x_max) {
        board = new HexCell[y_max][x_max];
        for (int i = 0; i < y_max; i++) {
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
