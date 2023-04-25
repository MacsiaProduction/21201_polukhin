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

    private int currentPlayerListPos;

    private int reinforcePoints;

    private final List<Player> playerList = new ArrayList<>();

    private final HexCell[][] board;

    private HexCell selected;

    public GameModel(int y, int x) {
        this.rows = y;
        this.columns = x;
        board = new HexCell[y][x];
        for (int i = 0; i < rows; i++) {
            Arrays.fill(board[i], null);
        }
    }

    public static List<Point> getPossibleNeighbors(int y_max, int x_max, Point cords) {
        List<Point> list = new ArrayList<>();
        list.add(new Point(cords.y()+1, cords.x()));
        list.add(new Point(cords.y()-1, cords.x()));
        list.add(new Point(cords.y(), cords.x()+1));
        list.add(new Point(cords.y(), cords.x()-1));
        list.add(new Point(cords.y()+1, cords.x()+1-2*(cords.y()%2)));
        list.add(new Point(cords.y()-1, cords.x()+1-2*(cords.y()%2)));
        list.removeIf(point -> !areValidCords(y_max, x_max, point));
        return list;
    }

    public List<Point> getPossibleNeighbors(Point cords) {
        return getPossibleNeighbors(columns, rows, cords);
    }

    public List<HexCellInfo> getNeighbors(Point cords) {
        if(board[cords.y()][cords.x()]==null){
            throw new IllegalArgumentException("Empty Cell");
        }
        var initialPointList = getPossibleNeighbors(cords);
        List<HexCellInfo> list = new ArrayList<>();
        for (Point point : initialPointList) {
            if(board[point.y()][point.x()]!=null) {
                list.add(getCellInfo(point));
            }
        }
        return list;
    }

    private boolean areNeighbors(HexCellInfo cell1, HexCellInfo cell2) {
        return getNeighbors(cell1.position()).contains(cell2);
    }

    public static boolean areValidCords(int y_max, int x_max, Point cords) {
        return cords.y() >= 0 && cords.y() < y_max && cords.x() >= 0 && cords.x() < x_max;
    }

    public boolean areValidCords(Point cords) {
        return areValidCords(columns, rows, cords);
    }

    public boolean isCellPresent(Point cords) {
        return areValidCords(cords) && board[cords.y()][cords.x()]!=null;
    }

    public HexCellInfo getCellInfo(Point cords) {
        if(!isCellPresent(cords))
            throw new IllegalArgumentException("invalid cords");
        return board[cords.y()][cords.x()].getInfo();
    }

    public List<HexCellInfo> getPlayerCellList(Player owner) {
        List<HexCellInfo> cellList= new ArrayList<>();
        for(int i =0; i < rows; i++) {
            for(int j = 0; j < columns; j++){
                var point = new Point(i,j);
                if(isCellPresent(point)) {
                    var tmp = getCellInfo(point);
                    if (tmp.owner() == owner)
                        cellList.add(tmp);
                }
            }
        }
        return cellList;
    }

    private void attack(HexCell cell1, HexCell cell2) throws MoveException {
        if(cell1.getOwner()!=currentPlayer) {
            throw new MoveException("You can't attack with not your cell");
        } else if(cell2.getOwner()==currentPlayer) {
            throw new MoveException("You can't attack your own cell");
        } else if (cell1.getPower()<=1) {
            throw new MoveException("Only cells with >=2 power can attack");
        } else if (!areNeighbors(cell1.getInfo(),cell2.getInfo())) {
            throw new MoveException("Only neighbor cells can attack");
        } else if(cell1.getOwner() == cell2.getOwner()) {
            throw new MoveException("Can not attack your own cells");
        } else while(cell1.getPower() > 1) {
            cell1.setPower(cell1.getPower() - 1);
            int attackPower = new Random().nextInt(3) + 1;
            cell2.setPower(cell2.getPower() - attackPower);
            if(cell2.getPower() <= 0) {
                if(cell2.getOwner()!=null) cell2.getOwner().deleteCell();
                cell2.setOwner(cell1.getOwner());
                cell1.getOwner().addCell();
                cell2.setPower(cell1.getPower());
                cell1.setPower(1);
                return;
            }
        }

    }

    private void reinforce(HexCell cell) throws MoveException {
        if(cell.getOwner() != currentPlayer)
            throw new MoveException("You can only reinforce your cells");
        if(reinforcePoints==0) return;
        cell.setPower(cell.getPower() + 1);
        reinforcePoints--;
    }

    private void setFirstPlayer(Player player) {
        if(currentPlayer!=null)
            throw new UnsupportedOperationException("Trying set first player when there already is one");
        currentPlayer = player;
        currentPlayerListPos = playerList.indexOf(player);
        turnState = GameTurnState.ATTACK;
    }

    public void initModel(List<Point> existingCells, List<Point> startingCells, List<ModelListener> presenters) {
        if (startingCells.size() != presenters.size()) throw new IllegalArgumentException();
        existingCells.forEach(cords -> board[cords.y()][cords.x()] = new HexCell(cords));
        startingCells.forEach(cords -> {
            Player player = new Player();
            board[cords.y()][cords.x()].setOwner(player);
            player.addCell();
            board[cords.y()][cords.x()].setPower(2);
            playerList.add(player);
        });
        for(int i = 0; i < presenters.size(); i++) {
            presenters.get(i).init(playerList.get(i), this);
            playerList.get(i).setPresenter(presenters.get(i));
        }
        setFirstPlayer(playerList.get(0));
        currentPlayer.getListener().setAttackInfo();
        currentPlayer.getListener().updateView();
    }

    //todo can use digital signatures for purposes of Player field in the multiplayer
    public void nextTurn(Player player) throws MoveException {
        assert player == currentPlayer : player;

        playerList.forEach(p->p.getListener().updateView());
        TurnCheck();
        selected = null;
        if(turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = currentPlayer.getNumberOfCells();
            currentPlayer.getListener().setReinforceInfo(reinforcePoints);
        } else {
            turnState = GameTurnState.ATTACK;
            currentPlayerListPos = (currentPlayerListPos + 1) % playerList.size();
            currentPlayer = playerList.get(currentPlayerListPos);
            currentPlayer.getListener().setAttackInfo();
        }
        currentPlayer.getListener().askTurn(turnState);
    }

    //todo can use digital signatures for purposes of Player field in the multiplayer
    public void cellClicked(Player player, Point cords) throws MoveException {
        if (cords == null || !isCellPresent(cords)) {
            selected = null;
            return;
        }

        assert areValidCords(cords) : cords;
        assert player == currentPlayer : player;

        HexCell newSelected = board[cords.y()][cords.x()];

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
        playerList.removeIf(p-> {
            if(p.getNumberOfCells() == 0)
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

