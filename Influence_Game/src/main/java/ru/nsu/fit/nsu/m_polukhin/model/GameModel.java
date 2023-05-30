package ru.nsu.fit.nsu.m_polukhin.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.nsu.m_polukhin.utils.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class GameModel {
    private final int rows;
    private final int columns;
    private final List<Player> playerList;
    private final Field field;
    private Player currentPlayer;

    public GameModel(int rows, int columns, @NotNull List<Point> existingCells, @NotNull List<Point> startingCells) {
        this.rows = rows;
        this.columns = columns;
        this.playerList = new ArrayList<>();
        this.field = new Field(rows, columns);
        existingCells.forEach(field::initCell);
        for (Point startingCell : startingCells) {
            playerList.add(initPlayer(new Player(field), startingCell));
        }
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

    public void setPresenters(@NotNull List<ModelListener> presenters) {
        assert presenters.size() == playerList.size();

        for (ModelListener presenter : presenters) {
            var tmp = playerList.get(presenters.indexOf(presenter));
            presenter.init(this, tmp.getId());
            tmp.setListener(presenter);
            tmp.getListener().setAttackInfo();
            tmp.getListener().updateView();
        }
        currentPlayer = playerList.get(0);
        currentPlayer.getListener().startOfTurn();
    }

    private Player initPlayer(Player player, Point startingCell) {
        field.getCell(startingCell).setOwner(player);
        field.getCell(startingCell).setPower(2);
        return player;
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void nextState(int playerId) {
        assert playerId == currentPlayer.getId() : currentPlayer;
        if (!currentPlayer.nextState())
            nextPlayerTurn();
        currentPlayer.getListener().askMove();
    }

    private void nextPlayerTurn() {
        currentPlayer = playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size());
        turnCheck();
        currentPlayer.getListener().updateView();
        currentPlayer.getListener().startOfTurn();
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void makeMove(int playerId, Move move) throws MoveException {
        assert playerId == currentPlayer.getId() : currentPlayer;

        if (move == null || move.start()==null || move.end() == null || !isCellPresent(move.start()) || !isCellPresent(move.end())) return;

        currentPlayer.move(move);
        currentPlayer.getListener().updateView();
        currentPlayer.getListener().askMove();
    }

    private void turnCheck() {
        for (Iterator<Player> iterator = playerList.iterator(); iterator.hasNext(); ) {
            Player player = iterator.next();
            if (field.getNumberOfCells(player.getId()) == 0) {
                player.getListener().gameOver();
                iterator.remove();
            }
        }
        if (playerList.size() <= 1) {
            gameOver();
        }
    }

    private void gameOver() {
        playerList.forEach(player -> player.getListener().gameOver());
    }

    public static @NotNull List<Point> getPossibleNeighbors(int rows, int columns, Point cords) {
        return Field.getPossibleNeighbors(rows, columns, cords);
    }

    /**
     * @param playerId player whose turn is generating
     * @return Move if there is a turn to make null otherwise
     */
    public Move generateMove(int playerId) {
        List<HexCell> cellList = field.getPlayerCells(playerId);
        GameTurnState state = currentPlayer.getTurnState();
        if (state == GameTurnState.ATTACK) {
            for (var attacker : cellList) {
                if (attacker.getPower() < 2) continue;
                var neighbours = field.getNeighbors(attacker.getPosition());
                for (var victim : neighbours) {
                    if (victim.ownerId() != attacker.getInfo().ownerId()) {
                        return new Move(attacker.getPosition(), victim.position());
                    }
                }
            }
        }
        else if (state == GameTurnState.REINFORCE && currentPlayer.getReinforcePoints() > 0) {
            var cell = cellList.get(new Random().nextInt(cellList.size()));
            return new Move(cell.getPosition(), cell.getPosition());
        }
        return null;
    }
}
