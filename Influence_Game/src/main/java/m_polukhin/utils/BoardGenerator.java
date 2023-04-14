package m_polukhin.utils;

import m_polukhin.model.*;

import m_polukhin.presenter.AIPresenter;

import java.util.*;

public class BoardGenerator {
    // CR: return result of generation, set to model (in main)
    private final List<HexCell> inited = new ArrayList<>();
    List<Player> playerList = new ArrayList<>();
    private void initBoard(GameModel model) {
        Point cords = new Point(0,0);
        model.initCell(cords);
        for(int i = 0; i < 50; i++) {
            var neighbours = model.getPossibleNeighbors(cords);
            int randomNum = new Random().nextInt(neighbours.size());
            cords = neighbours.get(randomNum);
            if (model.isCellPresent(cords)) continue;
            HexCell added = model.initCell(cords);
            inited.add(added);
        }
    }

    private void initPlayers(GameModel model, ModelListener presenter) {
        Player tmp = new Player();
        tmp.setPresenter(presenter);
        presenter.init(tmp, model);
        playerList.add(tmp);
        model.addPlayer(tmp);
        for (int i = 1; i<2; i++) {
            tmp = new Player();
            AIPresenter ai = new AIPresenter(new AI(model, tmp));
            ai.init(tmp, model);
            tmp.setPresenter(ai);
            playerList.add(tmp);
            model.addPlayer(tmp);
        }
        model.setFirstPlayer(playerList.get(0));
        assert(playerList.size() < inited.size()); //wtf
        Random rand = new Random();
        HashSet<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < playerList.size()) {
            int randomNum = rand.nextInt(inited.size());
            uniqueNumbers.add(randomNum);
        }
        var iterator = uniqueNumbers.iterator();
        playerList.forEach(player -> {
            HexCell cell = inited.get(iterator.next());
            cell.setPower(2);
            cell.setOwner(player);
            player.addCell();
        });
    }

    public void init(GameModel model, ModelListener presenter) {
        initBoard(model);
        initPlayers(model, presenter);
    }
}
