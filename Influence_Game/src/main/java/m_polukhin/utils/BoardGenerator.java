package m_polukhin.utils;

import m_polukhin.model.GameModel;
import m_polukhin.model.HexCell;

import java.awt.*;
import java.util.*;
import java.util.List;

public class BoardGenerator {
    private List<HexCell> inited = new ArrayList<>();
    List<Player> playerList = new ArrayList<>();
    private void initBoard(GameModel model) {
        Point cords = new Point(0,0);
        model.initCell(cords);
        for(int i = 0; i < 30; i++) {
            System.out.println("cell inited" + cords);
            var neighbours = model.getPossibleNeighbors(cords);
            int randomNum = new Random().nextInt(neighbours.size());
            cords = neighbours.get(randomNum);
            if (model.isCellPresent(cords.y, cords.x)) continue;
            HexCell added = model.initCell(cords);
            inited.add(added);
        }
    }

    private void initPlayers(GameModel model) {
        for (int i = 0; i<2; i++) {
            Player tmp = new Player();
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
    public void init(GameModel model) {
        initBoard(model);
        initPlayers(model);
    }
}
