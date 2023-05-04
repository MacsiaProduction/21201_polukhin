package m_polukhin.model;

import m_polukhin.utils.*;

import java.util.*;

public class GameModel {
    private final int rows;

    public int rows() {
        return rows;
    }

    private final int columns;

    public int columns() {
        return columns;
    }

    private Player currentPlayer;

    private final List<Player> playerList;

    private final Field field;

    private HexCell selected;

    public GameModel(int rows, int columns) {
        this.rows = rows;
        this.columns = columns;
        this.playerList = new ArrayList<>();
        this.field = new Field(rows,columns);
    }

    public static List<Point> getPossibleNeighbors(int rows, int columns, Point cords) {
        return Field.getPossibleNeighbors(rows, columns, cords);
    }

    public List<HexCellInfo> getNeighbors(Point cords) {
        return field.getNeighbors(cords);
    }

    public boolean isCellPresent(Point cords) {
        return field.isCellPresent(cords);
    }

    public List<HexCellInfo> getPlayerCellList(int playerId) {
        for(var player: playerList) {
            if (player.getId() == playerId) return player.getPlayerCellList();
        }
        throw new IllegalArgumentException("incorrect playerId");
    }

    public HexCellInfo getCellInfo(Point cords) {
        if (!isCellPresent(cords))
            throw new IllegalArgumentException("invalid cords");
        return field.getCell(cords).getInfo();
    }

    public void initModel(List<Point> existingCells, List<Point> startingCells, List<ModelListener> presenters) {
        if (startingCells.size() != presenters.size()) throw new IllegalArgumentException();
        existingCells.forEach(field::initCell);
        startingCells.forEach(cords -> {
            Player player = new Player(field);
            field.getCell(cords).setOwner(player);
            player.addCell(field.getCell(cords));
            field.getCell(cords).setPower(2);
            playerList.add(player);
        });
        for(int i = 0; i < presenters.size(); i++) {
            presenters.get(i).init(playerList.get(i).getId(), this);
            playerList.get(i).setListener(presenters.get(i));
        }
        currentPlayer = playerList.get(0);
        currentPlayer.getListener().setAttackInfo();
        currentPlayer.getListener().updateView();
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void nextState(int playerId) {
        assert playerId == currentPlayer.getId() : currentPlayer;
        TurnCheck();
        selected = null;
        if(!currentPlayer.nextState())
            nextPlayerTurn();
    }

    public void nextPlayerTurn() {
        currentPlayer = playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size());
        currentPlayer.getListener().updateView();
        currentPlayer.getListener().askTurn();
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void cellClicked(int playerId, Point cords) throws MoveException {
        if (cords == null || !isCellPresent(cords)) {
            selected = null;
            return;
        }
        assert Field.areValidCords(rows, columns, cords) : cords;
        assert playerId == currentPlayer.getId() : currentPlayer;

        HexCell newSelected = field.getCell(cords);
        if (selected == null) {
            selected = newSelected;
            return;
        }
        currentPlayer.move(selected.getPosition(), cords);
        currentPlayer.getListener().updateView();
        selected = null;
    }

    private void TurnCheck() {
        playerList.removeIf (p-> {
            if (p.getNumberOfCells() == 0)
                p.getListener().gameOver();
            return p.getNumberOfCells() == 0;
        });
        if (playerList.size() <= 1) {
            gameOver();
        }
    }

    private void gameOver() {
        playerList.forEach(player -> player.getListener().gameOver());
    }
}
