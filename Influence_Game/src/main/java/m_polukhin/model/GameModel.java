package m_polukhin.model;


import m_polukhin.presenter.GamePresenter;

import java.awt.*;
import java.rmi.AccessException;
import java.util.*;
import java.util.List;

public class GameModel {
    GameTurnState turnState;
    private Player currentPlayer;
    private int currentPlayerListPos;
    private int reinforcePoints;
    private final List<Player> playerList = new ArrayList<>();
    private final Optional<HexCell>[][] board;
    private final GamePresenter presenter;
    private HexCell selected;
    public GameModel(GamePresenter presenter) {
        this.presenter = presenter;
        board = (Optional<HexCell>[][]) new Optional[presenter.NUM_ROWS][presenter.NUM_COLUMNS];
        for (int i = 0; i < presenter.NUM_ROWS; i++) {
            for (int j = 0; j < presenter.NUM_COLUMNS; j++) {
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
    public void initCell(Point cords) {
        if(!areValidCords(cords.y, cords.x))
            throw new IllegalArgumentException("illegal coordinates of cell");
        board[cords.y][cords.x] = Optional.of(new HexCell(cords));
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
    public List<HexCell> getNeighbors(HexCell cell) {
        if(board[cell.getPosition().y][cell.getPosition().x].isEmpty()){
            throw new IllegalArgumentException("Empty Cell");
        }
        var initialPointList = getPossibleNeighbors(cell.getPosition());
        List<HexCell> list = new ArrayList<>();
        initialPointList.forEach(point -> board[point.y][point.x].ifPresent(list::add));
        return list;
    }
    private boolean areNeighbors(HexCell cell1, HexCell cell2) {
        return getNeighbors(cell1).contains(cell2);
    }
    public boolean areValidCords(int y, int x) {
        if (y < 0 || y >= presenter.NUM_ROWS || x < 0 || x >= presenter.NUM_COLUMNS) {
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
        System.out.print(cell1.getInfo());
        System.out.println(" attacks ");
        System.out.println(cell2.getInfo());
        if(cell1.getOwner()!=currentPlayer) {
            throw new MoveException("You can't attack with not your cell");
        } else if(cell2.getOwner()==currentPlayer) {
            throw new MoveException("You can't attack your own cell");
        } else if (cell1.getPower()<=1) {
            throw new MoveException("Only cells with >=2 power can attack");
        } else if (!areNeighbors(cell1,cell2)) {
            throw new MoveException("Only neighbor cells can attack");
        } else if(cell1.getOwner() == cell2.getOwner()) {
            throw new MoveException("Can not attack your own cells");
        } else while(cell1.getPower() > 1) {
            if(cell2.getPower() <= 0) {
                if(cell2.getOwner()!=null) cell2.getOwner().deleteCell();
                cell2.setOwner(cell1.getOwner());
                cell1.getOwner().addCell();
                cell2.setPower(cell1.getPower() - 1);
                cell1.setPower(1);
                return;
            }
            cell1.setPower(cell1.getPower() - 1);
            int attackPower = new Random().nextInt(3) + 1;
            cell2.setPower(cell2.getPower() - attackPower);
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
    }
    public void nextTurn() {
        TurnCheck();
        if(turnState == GameTurnState.ATTACK) {
            turnState = GameTurnState.REINFORCE;
            reinforcePoints = currentPlayer.getNumberOfCells();
            presenter.setReinforceInfo(currentPlayer, reinforcePoints);
        } else {
            turnState = GameTurnState.ATTACK;
            currentPlayerListPos = (currentPlayerListPos + 1) % playerList.size();
            currentPlayer = playerList.get(currentPlayerListPos);
            presenter.setAttackInfo(currentPlayer);
        }
    }
    public void initBoard() {
        //board
        Point cords = new Point(0,0);
        initCell(cords);
        for(int i = 0; i < 30; i++) {
            System.out.println("cell inited" + cords);
            var neighbours = getPossibleNeighbors(cords);
            int randomNum = new Random().nextInt(neighbours.size());
            cords = neighbours.get(randomNum);
            if (isCellPresent(cords.y, cords.x)) continue;
            initCell(cords);
        }
        //players
        addPlayer(new Player());
        addPlayer(new Player());
        setFirstPlayer(playerList.get(0));
        List<HexCell> list = new ArrayList<>();
        for (Optional<HexCell>[] optionals : board) {
            for (Optional<HexCell> optional : optionals) {
                optional.ifPresent(list::add);
            }
        }
        assert(playerList.size() < list.size()); //wtf
        Random rand = new Random();
        HashSet<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < playerList.size()) {
            int randomNum = rand.nextInt(list.size());
            uniqueNumbers.add(randomNum);
        }
        var iterator = uniqueNumbers.iterator();
        playerList.forEach(player -> {
            HexCell cell = list.get(iterator.next());
            cell.setPower(2);
            cell.setOwner(player);
            player.addCell();
        });
        //GameState
        turnState = GameTurnState.ATTACK;
    }
    public void cellClicked(HexCellInfo cell) throws MoveException {
        if (cell == null) {
            selected = null;
        } else {
            Optional<HexCell> newSelected = board[cell.position().y][cell.position().x];
            if (newSelected.isEmpty()) {
                throw new MoveException("Impossible action");
            } else if (selected == null) {
                selected = newSelected.get();
                System.out.println("selected"+cell);
            } else {
                if (turnState == GameTurnState.ATTACK) {
                    attack(selected,  newSelected.get());
                    presenter.setAttackInfo(currentPlayer);
                } else if (turnState == GameTurnState.REINFORCE) {
                    reinforce(currentPlayer, newSelected.get());
                    presenter.setReinforceInfo(currentPlayer, reinforcePoints);
                } else {
                    throw new UnsupportedOperationException("GameTurnState don't implemented");
                }
                presenter.updateView();
                selected = null;
            }
        }
    }
    public void TurnCheck() {
        if (currentPlayer.getNumberOfCells()==0) {
            removePlayer(currentPlayer);
            currentPlayerListPos%=playerList.size();
        }
        if (playerList.size() == 1) {
            gameOver();
        }
    }

    private void gameOver() {
        presenter.gameOver();
    }
}
