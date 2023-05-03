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

    private GameTurnState turnState;

    private Player currentPlayer;

    private int reinforcePoints;

    private final List<Player> playerList;

    private final Field field;

    private HexCell selected;

    public GameModel(int y, int x) {
        this.rows = y;
        this.columns = x;
        this.playerList = new ArrayList<>();
        this.field = new Field(y,x);
    }

    public static List<Point> getPossibleNeighbors(int yMax, int xMax, Point cords) {
        List<Point> list = new ArrayList<>();
        list.add(new Point(cords.y()+1, cords.x()));
        list.add(new Point(cords.y()-1, cords.x()));
        list.add(new Point(cords.y(), cords.x()+1));
        list.add(new Point(cords.y(), cords.x()-1));
        list.add(new Point(cords.y()+1, cords.x()+1-2*(cords.y()%2)));
        list.add(new Point(cords.y()-1, cords.x()+1-2*(cords.y()%2)));
        list.removeIf(point -> !areValidCords(yMax, xMax, point));
        return list;
    }

    public List<HexCellInfo> getNeighbors(Point cords) {
        List<HexCellInfo> list = new ArrayList<>();
        for (Point point : getPossibleNeighbors(columns, rows, cords)) {
            if (isCellPresent(point)) {
                list.add(getCellInfo(point));
            }
        }
        return list;
    }

    private boolean areNeighbors(HexCellInfo cell1, HexCellInfo cell2) {
        return getNeighbors(cell1.position()).contains(cell2);
    }

    private static boolean areValidCords(int yMax, int xMax, Point cords) {
        return cords.y() >= 0 && cords.y() < yMax && cords.x() >= 0 && cords.x() < xMax;
    }

    public boolean isCellPresent(Point cords) {
        return areValidCords(columns, rows, cords) && field.getCell(cords)!=null;
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

    private void attack(HexCell cell1, HexCell cell2) throws MoveException {
        if (cell1.getOwner() != currentPlayer) {
            throw new MoveException("You can't attack with not your cell");
        } else if (cell2.getOwner() == currentPlayer) {
            throw new MoveException("You can't attack your own cell");
        } else if (cell1.getPower() <= 1) {
            throw new MoveException("Only cells with >=2 power can attack");
        } else if (!areNeighbors(cell1.getInfo(),cell2.getInfo())) {
            throw new MoveException("Only neighbor cells can attack");
        } else if (cell1.getOwner() == cell2.getOwner()) {
            throw new MoveException("Can not attack your own cells");
        } else while(cell1.getPower() > 1) {
            cell1.setPower(cell1.getPower() - 1);
            int attackPower = new Random().nextInt(3) + 1;
            cell2.setPower(cell2.getPower() - attackPower);
            if (cell2.getPower() <= 0) {
                if (cell2.getOwner()!=null) cell2.getOwner().deleteCell(cell2);
                cell2.setOwner(cell1.getOwner());
                cell1.getOwner().addCell(cell2);
                cell2.setPower(cell1.getPower());
                cell1.setPower(1);
                return;
            }
        }

    }

    private void reinforce(HexCell cell) throws MoveException {
        if (cell.getOwner() != currentPlayer)
            throw new MoveException("You can only reinforce your cells");
        if (reinforcePoints == 0) return;
        cell.setPower(cell.getPower() + 1);
        reinforcePoints--;
    }

    private void setFirstPlayer(Player player) {
        if (currentPlayer!=null)
            throw new UnsupportedOperationException("Trying set first player when there already is one");
        currentPlayer = player;
        turnState = GameTurnState.ATTACK;
    }

    public void initModel(List<Point> existingCells, List<Point> startingCells, List<ModelListener> presenters) {
        if (startingCells.size() != presenters.size()) throw new IllegalArgumentException();
        existingCells.forEach(field::initCell);
        startingCells.forEach(cords -> {
            Player player = new Player();
            field.getCell(cords).setOwner(player);
            player.addCell(field.getCell(cords));
            field.getCell(cords).setPower(2);
            playerList.add(player);
        });
        for(int i = 0; i < presenters.size(); i++) {
            presenters.get(i).init(playerList.get(i).getId(), this);
            playerList.get(i).setListener(presenters.get(i));
        }
        setFirstPlayer(playerList.get(0));
        currentPlayer.getListener().setAttackInfo();
        currentPlayer.getListener().updateView();
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void nextTurn(int playerId) {
        assert playerId == currentPlayer.getId() : currentPlayer;

        playerList.forEach(p->p.getListener().updateView());
        TurnCheck();
        selected = null;
        if (turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = currentPlayer.getNumberOfCells();
            currentPlayer.getListener().setReinforceInfo(reinforcePoints);
        } else {
            turnState = GameTurnState.ATTACK;
            currentPlayer = playerList.get((playerList.indexOf(currentPlayer) + 1) % playerList.size());
            currentPlayer.getListener().setAttackInfo();
        }
        currentPlayer.getListener().askTurn(turnState);
    }

    //todo digital signatures for purposes of playerId field in the multiplayer
    public void cellClicked(int playerId, Point cords) throws MoveException {
        if (cords == null || !isCellPresent(cords)) {
            selected = null;
            return;
        }

        assert areValidCords(columns, rows, cords) : cords;
        assert playerId == currentPlayer.getId() : currentPlayer;

        HexCell newSelected = field.getCell(cords);

        if (selected == null) {
            selected = newSelected;
            return;
        }

        if (turnState == GameTurnState.ATTACK) {
            attack(selected, newSelected);
            currentPlayer.getListener().setAttackInfo();
        } else if (turnState == GameTurnState.REINFORCE) {
            reinforce(newSelected);
            currentPlayer.getListener().setReinforceInfo(reinforcePoints);
        } else {
            throw new UnsupportedOperationException("GameTurnState don't implemented");
        }
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

