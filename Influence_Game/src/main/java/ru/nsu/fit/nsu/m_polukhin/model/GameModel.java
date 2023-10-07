package ru.nsu.fit.nsu.m_polukhin.model;

import org.jetbrains.annotations.NotNull;
import ru.nsu.fit.nsu.m_polukhin.utils.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameModel {
    private final int rows;
    private final int columns;

    private final List<Player> playerList;
    private Player currentPlayer;

    private final Field field;

    public GameModel(int rows, int columns, @NotNull List<Point> existingCells, @NotNull List<Point> startingCells) {
        this.rows = rows;
        this.columns = columns;
        this.playerList = new ArrayList<>();
        this.field = new Field(rows, columns);
        for (Point existingCell : existingCells) {
            field.initCell(existingCell);
        }
        for (int i = 0; i < startingCells.size(); i++) {
            Point startingCell = startingCells.get(i);
            playerList.add(initPlayer(new Player(field, i), startingCell));
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
        return field.getCellInfo(cords);
    }

    public void setPresenters(@NotNull List<ModelListener> presenters) {
        assert !playerList.isEmpty() && presenters.size() == playerList.size();
        for (ModelListener presenter : presenters) {
            var player = playerList.get(presenters.indexOf(presenter));
            presenter.init(this, player.getId());
            player.setListener(presenter);
            presenter.setAttackInfo();
        }
        currentPlayer = playerList.get(0);
        currentPlayer.getListener().startOfTurn();
        currentPlayer.getListener().askMove();
    }

    private Player initPlayer(Player player, Point startingCell) {
        field.setOwner(startingCell, player);
        field.setPower(startingCell, 2);
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
        if (!turnCheck()) return;
        currentPlayer.getListener().startOfTurn();
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void makeMove(int playerId, Move move) throws MoveException {
        assert playerId == currentPlayer.getId() : currentPlayer;

        if (move == null || move.start()==null || move.end() == null || !isCellPresent(move.start()) || !isCellPresent(move.end())) return;

        currentPlayer.move(move);
        currentPlayer.getListener().askMove();
    }


    /**
     * @return true if this player is able to move, false otherwise
     */
    private boolean turnCheck() {
        if (field.getNumberOfCells(currentPlayer.getId()) == 0) {
            currentPlayer.getListener().gameOver();
            playerList.remove(currentPlayer);
            if (playerList.size() <= 1) {
                gameOver();
            }
            nextPlayerTurn();
            return false;
        }
        return true;
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
        List<HexCellInfo> cellList = field.getPlayerCells(playerId);
        GameTurnState state = currentPlayer.getTurnState();
        if (state == GameTurnState.ATTACK) {
            for (var attacker : cellList) {
                if (attacker.power() < 2) continue;
                var neighbours = field.getNeighbors(attacker.position());
                for (var victim : neighbours) {
                    if (victim.ownerId() != attacker.ownerId()) {
                        return new Move(attacker.position(), victim.position());
                    }
                }
            }
        }
        else if (state == GameTurnState.REINFORCE && currentPlayer.getReinforcePoints() > 0) {
            var cell = cellList.get(new Random().nextInt(cellList.size()));
            return new Move(cell.position(), cell.position());
        }
        return null;
    }
}