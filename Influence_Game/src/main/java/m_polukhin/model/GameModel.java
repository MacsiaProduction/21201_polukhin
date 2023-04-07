package m_polukhin.model;


import m_polukhin.utils.BoardGenerator;
import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.ModelListener;
import m_polukhin.utils.MoveException;
import m_polukhin.presenter.GamePresenter;
import m_polukhin.utils.Player;

import java.awt.*;
import java.rmi.AccessException;
import java.util.*;
import java.util.List;

public class GameModel {
    public final int rows;

    public final int columns;

    GameTurnState turnState;

    private Player currentPlayer;

    private int currentPlayerListPos;

    private int reinforcePoints;

    private final List<Player> playerList = new ArrayList<>();

    private final Optional<HexCell>[][] board;

    private final ModelListener presenter;

    private HexCell selected;

    public GameModel(ModelListener presenter, int y, int x) {
        this.presenter = presenter;
        this.rows = y;
        this.columns = x;
        board = (Optional<HexCell>[][]) new Optional[rows][columns];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                    board[i][j] = Optional.empty();
            }
        }
    }

    public void addPlayer(Player p) {
        playerList.add(p);
    }

    public void removePlayer(Player p) {
        playerList.remove(p);
    }

    public HexCell initCell(Point cords) {
        if(isCellPresent(cords.y,cords.x)) {
            throw new IllegalArgumentException("cell already presents");
        }
        if(!areValidCords(cords.y, cords.x))
            throw new IllegalArgumentException("illegal coordinates of cell");
        HexCell tmp = new HexCell(cords);
        board[cords.y][cords.x] = Optional.of(tmp);
        return tmp;
    }

    public List<Point> getPossibleNeighbors(Point position) {
        List<Point> list = new ArrayList<>();
        if(areValidCords(position.y-1, position.x-1)) {
            list.add(new Point(position.x-1,position.y-1));
        }
        if(areValidCords(position.y+1, position.x-1)) {
            list.add(new Point(position.x-1,position.y+1));
        }
        if(areValidCords(position.y-1, position.x+1)) {
            list.add(new Point(position.x+1,position.y-1));
        }
        if(areValidCords(position.y+1, position.x+1)) {
            list.add(new Point(position.x+1,position.y+1));
        }
        if(areValidCords(position.y, position.x-2)) {
            list.add(new Point(position.x-2, position.y));
        }
        if(areValidCords(position.y, position.x+2)) {
            list.add(new Point(position.x+2, position.y));
        }
        return list;
    }

    public List<HexCellInfo> getNeighbors(HexCellInfo cell) {
        if(board[cell.position().y][cell.position().x].isEmpty()){
            throw new IllegalArgumentException("Empty Cell");
        }
        var initialPointList = getPossibleNeighbors(cell.position());
        List<HexCellInfo> list = new ArrayList<>();
        for (Point point : initialPointList) {
            if(board[point.y][point.x].isPresent()) {
                try {
                    list.add(getCellInfo(point.y,point.x));
                } catch (AccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return list;
    }

    private boolean areNeighbors(HexCellInfo cell1, HexCellInfo cell2) {
        return getNeighbors(cell1).contains(cell2);
    }

    public boolean areValidCords(int y, int x) {
        if (y < 0 || y >= rows || x < 0 || x >= columns) {
            return false;
        } else return (x + y) % 2 == 0;
    }

    public boolean isCellPresent(int y, int x) {
        return areValidCords(y,x) && board[y][x].isPresent();
    }

    public HexCellInfo getCellInfo(int y, int x) throws AccessException {
        if(!areValidCords(y,x))
            throw new IllegalArgumentException("invalid cords");
        if(board[y][x].isEmpty())
            throw new AccessException("no such cell");
        return board[y][x].get().getInfo();
    }

    private void attack(HexCell cell1, HexCell cell2) throws MoveException {
        System.out.println(cell1.getInfo());
        System.out.print(cell1.getInfo());
        System.out.println(" attacks ");
        System.out.println(cell2.getInfo());
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

    public void setFirstPlayer(Player firstPlayer) {
        if(currentPlayer!=null)
            throw new UnsupportedOperationException("Trying set first player when there already is one");
        currentPlayer = firstPlayer;
        currentPlayerListPos = playerList.indexOf(firstPlayer);
        turnState = GameTurnState.ATTACK;
    }

    public void initBoard(BoardGenerator generator) {
        generator.init(this, presenter);
        presenter.setAttackInfo(currentPlayer);
    }

    public void nextTurn() {
        TurnCheck();
        selected = null;
        if(turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = currentPlayer.getNumberOfCells();
            currentPlayer.getListener().setReinforceInfo(currentPlayer, reinforcePoints);
        } else {
            turnState = GameTurnState.ATTACK;
            currentPlayerListPos = (currentPlayerListPos + 1) % playerList.size();
            currentPlayer = playerList.get(currentPlayerListPos);
            currentPlayer.getListener().setAttackInfo(currentPlayer);
        }
        currentPlayer.getListener().askTurn(turnState);
    }

    //todo can use digital signatures for purposes of Player field in the multiplayer
    public void cellClicked(Player player, HexCellInfo cell) throws MoveException {
        if (player != currentPlayer)
            throw new MoveException("not current player trying to make a turn");
        if (cell == null) {
            selected = null;
            return;
        }
        Optional<HexCell> newSelected = board[cell.position().y][cell.position().x];
        if (newSelected.isEmpty()) {
            throw new MoveException("Impossible action");
        } else if (selected == null) {
            selected = newSelected.get();
            System.out.println("selected"+cell);
        } else try {
            if (turnState == GameTurnState.ATTACK) {
                attack(selected, newSelected.get());
                currentPlayer.getListener().setAttackInfo(currentPlayer);
            } else if (turnState == GameTurnState.REINFORCE) {
                reinforce(currentPlayer, newSelected.get());
                currentPlayer.getListener().setReinforceInfo(currentPlayer, reinforcePoints);
            } else {
                throw new UnsupportedOperationException("GameTurnState don't implemented");
            }
        } finally {
            presenter.updateView();
            selected = null;
        }
    }

    private void TurnCheck() {
        for(var player: playerList) {
            if (player.getNumberOfCells() == 0) {
                removePlayer(player);
            }
        }
        if (playerList.size() <= 1) {
            gameOver();
        }
    }

    private void gameOver() {
        presenter.gameOver();
    }
}

