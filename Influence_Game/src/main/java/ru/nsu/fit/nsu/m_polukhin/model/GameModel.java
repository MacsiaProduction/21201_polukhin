package ru.nsu.fit.nsu.m_polukhin.model;

import ru.nsu.fit.nsu.m_polukhin.utils.HexCellInfo;
import ru.nsu.fit.nsu.m_polukhin.utils.MoveException;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

import java.util.ArrayList;
import java.util.List;

public class GameModel {

    private final int rows;
    private final int columns;

    private final List<Player> playerList;
    private final Field field;

    private Player host;
    private Player currentPlayer;

    private HexCell selected;

    public GameModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.playerList = new ArrayList<>();
        this.field = new Field(rows, columns);
    }

    public int rows() {
        return rows;
    }

    public int columns() {
        return columns;
    }

    public boolean isCellPresent(Point cords) {
        return field.isCellPresent(cords);
    }

    public HexCellInfo getCellInfo(Point cords) {
        if (!isCellPresent(cords))
            throw new IllegalArgumentException("invalid cords");
        return field.getCell(cords).getInfo();
    }

    public void initModel(List<Point> existingCells, List<Point> startingCells) {
        existingCells.forEach(field::initCell);
        host = new Player(field);
        playerList.add(initPlayer(host, startingCells.get(0)));
        for (int i = 1; i < startingCells.size(); i++) {
            Player player = new BasicAI(field);
            playerList.add(initPlayer(player, startingCells.get(i)));
        }
        host.setListener(presenter);
        host.getListener().setAttackInfo();
        host.getListener().updateView();
        currentPlayer = host;
    }

    private Player initPlayer(Player player, Point startingCell) {
        field.getCell(startingCell).setOwner(player);
        field.getCell(startingCell).setPower(2);
        return player;
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void nextState(int playerId) {
        assert playerId == currentPlayer.getId() : currentPlayer;
        selected = null;
        if (!currentPlayer.nextState())
            nextPlayerTurn();
    }

    private void nextPlayerTurn() {
        currentPlayer = playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size());
        turnCheck();
        if (currentPlayer instanceof AI) {
            ((AI) currentPlayer).move();
            nextPlayerTurn();
        } else {
            currentPlayer.getListener().updateView();
            currentPlayer.getListener().askTurn();
        }
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void cellClicked(int playerId, Point cords) throws MoveException {
        if (cords == null || !isCellPresent(cords)) {
            selected = null;
            return;
        }
        // CR: assert / boolean check, not both
        assert Field.areValidCords(rows, columns, cords) : cords;
        assert playerId == currentPlayer.getId() : currentPlayer;

        HexCell newSelected = field.getCell(cords);
        if (selected == null) {
            selected = newSelected;
            return;
        }
        currentPlayer.move(selected.getPosition(), cords);
        // CR: currentPlayer.getListener().updateView(); -> currentPlayer.updateView();, remove AI check
        if (!(currentPlayer instanceof AI)) currentPlayer.getListener().updateView();
        selected = null;
    }

    private void turnCheck() {
        playerList.removeIf(p -> field.getNumberOfCells(p.getId()) == 0);
        if (playerList.size() <= 1) {
            gameOver();
        }
    }

    private void gameOver() {
        host.getListener().gameOver();
    }

    public static List<Point> getPossibleNeighbors(int rows, int columns, Point cords) {
        return Field.getPossibleNeighbors(rows, columns, cords);
    }
}
