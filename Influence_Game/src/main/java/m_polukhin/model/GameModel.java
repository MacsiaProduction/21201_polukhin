package m_polukhin.model;

import m_polukhin.presenter.Presenter;
import m_polukhin.utils.*;

import java.rmi.AccessException;
import java.util.*;

public class GameModel {

    // CR: getter
    public final int rows;

    public final int columns;

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
        list.add(new Point(cords.y+1, cords.x));
        list.add(new Point(cords.y-1, cords.x));
        list.add(new Point(cords.y, cords.x+1));
        list.add(new Point(cords.y, cords.x-1));
        list.add(new Point(cords.y+1, cords.x+1-2*(cords.y%2)));
        list.add(new Point(cords.y-1, cords.x+1-2*(cords.y%2)));
        list.removeIf(point -> !areValidCords(y_max, x_max, point));
        return list;
    }

    // CR: copy paste
    public List<Point> getPossibleNeighbors(Point cords) {
        List<Point> list = new ArrayList<>();
        list.add(new Point(cords.y+1, cords.x));
        list.add(new Point(cords.y-1, cords.x));
        list.add(new Point(cords.y, cords.x+1));
        list.add(new Point(cords.y, cords.x-1));
        list.add(new Point(cords.y+1, cords.x+1-2*(cords.y%2)));
        list.add(new Point(cords.y-1, cords.x+1-2*(cords.y%2)));
        list.removeIf(point -> !areValidCords(point));
        return list;
    }

    public List<HexCellInfo> getNeighbors(Point cords) {
        if(board[cords.y][cords.x]==null){
            throw new IllegalArgumentException("Empty Cell");
        }
        var initialPointList = getPossibleNeighbors(cords);
        List<HexCellInfo> list = new ArrayList<>();
        for (Point point : initialPointList) {
            if(board[point.y][point.x]!=null) {
                try {
                    list.add(getCellInfo(point));
                } catch (AccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }

    private boolean areNeighbors(HexCellInfo cell1, HexCellInfo cell2) {
        return getNeighbors(cell1.position()).contains(cell2);
    }

    public static boolean areValidCords(int y_max, int x_max, Point cords) {
        return cords.y >= 0 && cords.y < y_max && cords.x >= 0 && cords.x < x_max;
    }

    // CR: copy paste
    public boolean areValidCords(Point cords) {
        return cords.y >= 0 && cords.y < columns && cords.x >= 0 && cords.x < rows;
    }

    public boolean isCellPresent(Point cords) {
        return areValidCords(cords) && board[cords.y][cords.x]!=null;
    }

    public HexCellInfo getCellInfo(Point cords) throws AccessException {
        if(!areValidCords(cords))
            throw new IllegalArgumentException("invalid cords");
        if(board[cords.y][cords.x]==null)
//          CR: IllegalArgumentException
            throw new AccessException("no such cell");
        return board[cords.y][cords.x].getInfo();
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

    private void reinforce(Player player, HexCell cell) throws MoveException {
        if(cell.getOwner() != player)
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
        existingCells.forEach(cords -> board[cords.y][cords.x] = new HexCell(cords));
        startingCells.forEach(cords -> {
            Player player = new Player();
            board[cords.y][cords.x].setOwner(player);
            player.addCell();
            board[cords.y][cords.x].setPower(2);
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
        if(currentPlayer!= player) throw new MoveException("not current player trying to make a turn");
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
        playerList.forEach(p->p.getListener().updateView());
    }

    //todo can use digital signatures for purposes of Player field in the multiplayer
    public void cellClicked(Player player, Point cords) throws MoveException {
//        assert areValidCords(cords) : cords;
//        if (selected == null) {
//            HexCell cell = board[cords.y][cords.x];
//            if (isPlayerCell(cell)) {
//                selected = cell;
//                listener.fieldUpdated();
//            }
//            return;
//        }
//        if (turnState == GameTurnState.ATTACK) {
//            if (!isPlayerCell(cell)) {
//                if (attack()) {
//                    listener.fieldUpdated();
//                }
//            }
//        } else {
//            HexCell newSelected = board[cords.y][cords.x];
//            if (newSelected != selected) {
//                selected = newSelected;
//            } else {
//                reinforce();
//            }
//            listener.fieldUpdated();
//        }

        if (player != currentPlayer)
            throw new MoveException("not current player trying to make a turn");
        if (cords == null || !isCellPresent(cords)) {
            selected = null;
            return;
        }
        HexCell newSelected = board[cords.y][cords.x];
        if (newSelected==null) {
            throw new MoveException("Impossible action");
        } else if (selected == null) {
            selected = newSelected;
            System.out.println("selected "+cords.y+" "+cords.x);
        } else try {
            if (turnState == GameTurnState.ATTACK) {
                attack(selected, newSelected);
                currentPlayer.getListener().setAttackInfo();
            } else if (turnState == GameTurnState.REINFORCE) {
                reinforce(currentPlayer, newSelected);
                currentPlayer.getListener().setReinforceInfo(reinforcePoints);
            } else {
                throw new UnsupportedOperationException("GameTurnState don't implemented");
            }
        } finally {
            currentPlayer.getListener().updateView();
            selected = null;
        }
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

