package m_polukhin.experimental;

import java.util.ArrayList;
import java.util.List;

public class Model {

    private int curPlayerPos;
    private final List<Player> players;

    private final ModelListener modelListener;

    public Model(List<Player> players, ModelListener modelListener) {
        this.players = players;
        this.modelListener = modelListener;
    }

    public void move(int x, int y) {
        Player player = players.get(curPlayerPos);
        player.move(x, y);

        PlayerState playerState = player.getPlayerState();
        if (playerState == PlayerState.ATTACK) {
            curPlayerPos++;
            moveAI();
        }
    }

    private void moveAI() {
        Player nextPlayer = players.get(curPlayerPos);
        while (nextPlayer instanceof AI ai) {
            ai.move();
            modelListener.update();
            curPlayerPos++;
        }
    }

}

class ModelGenerator {

    Model generateModel(int nPlayers) {
        Field field = generateField(nPlayers);
        List<Player> players = generatePlayers(nPlayers);
    }

    private List<Player> generatePlayers(int nPlayers, Field field) {
        assert nPlayers >= 2;
        List<Player> players = new ArrayList<>(nPlayers);
        Player user = new Player(field, 1);
        players.add(user);
        for (int i = 2; i <= nPlayers; i++) {
            AI ai = new AI(field, i);
            players.add(ai);
        }
        return players;
    }

}

class Field {

    private final HexCell[][] cells;

    Field(HexCell[][] cells) {
        this.cells = cells;
    }
}

class HexCell {

    private int marker;
    private int force;

    void update(int marker, int force) {
        this.marker = marker;
        this.force = force;
    }
}

enum PlayerState {
    REINFORCE,
    ATTACK
}

class Player {

    private PlayerState playerState = PlayerState.ATTACK;

    private final int marker;
    private final Field field;

    Player(Field field, int marker) {
        this.field = field;
        this.marker = marker;
    }

    public boolean move(int x, int y) {
        if (!canMove(x, y)) {
            return false;
        }
        // TODO: update field state
        return true;
    }

    public PlayerState getPlayerState() {
        return playerState;
    }
}

class AI extends Player {

    AI(Field field, int marker) {
        super(field, marker);
    }

    void move() {
        Position position = generateTurn();
        super.move(position.x, position.y);
    }

    private Position generateTurn() {
        // TODO
        return new Position(0, 0);
    }

    private record Position(int x, int y) {}
}