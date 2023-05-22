package ru.nsu.fit.nsu.m_polukhin.model;

import ru.nsu.fit.nsu.m_polukhin.utils.HexCellInfo;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Field {
    private final HexCell[][] board;
    private final int rows;
    private final int columns;

    Field(int y, int x) {
        this.rows = y;
        this.columns = x;
        board = new HexCell[y][x];
        for (int i = 0; i < y; i++) {
            Arrays.fill(board[i], null);
        }
    }

    public HexCell getCell(Point cords) {
        return board[cords.y()][cords.x()];
    }

    public void initCell(Point cords) {
        board[cords.y()][cords.x()] = new HexCell(cords);
    }

    public boolean areNeighbors(Point cords1, Point cords2) {
        return getNeighbors(cords1).contains(getCell(cords2).getInfo());
    }

    public static List<Point> getPossibleNeighbors(int rows, int columns, Point cords) {
        List<Point> list = new ArrayList<>();
        list.add(new Point(cords.y()+1, cords.x()));
        list.add(new Point(cords.y()-1, cords.x()));
        list.add(new Point(cords.y(), cords.x()+1));
        list.add(new Point(cords.y(), cords.x()-1));
        list.add(new Point(cords.y()+1, cords.x()+1-2*(cords.y()%2)));
        list.add(new Point(cords.y()-1, cords.x()+1-2*(cords.y()%2)));
        list.removeIf(point -> !areValidCords(rows, columns, point));
        return list;
    }

    public List<HexCellInfo> getNeighbors(Point cords) {
        List<HexCellInfo> list = new ArrayList<>();
        for (Point point : getPossibleNeighbors(rows, columns, cords)) {
            if (isCellPresent(point)) {
                list.add(getCell(point).getInfo());
            }
        }
        return list;
    }

    public static boolean areValidCords(int rows, int columns, Point cords) {
        return cords.y() >= 0 && cords.y() < rows && cords.x() >= 0 && cords.x() < columns;
    }

    public boolean isCellPresent(Point cords) {
        return areValidCords(rows, columns, cords) && getCell(cords)!=null;
    }

    public long getNumberOfCells(int playerId) {
        return getPlayerCells(playerId).size();
    }

    public List<HexCell> getPlayerCells(int playerId) {
        return Arrays.stream(board).flatMap(Arrays::stream).filter(hexCell -> hexCell!=null && hexCell.getInfo().ownerId() == playerId).toList();
    }
}
