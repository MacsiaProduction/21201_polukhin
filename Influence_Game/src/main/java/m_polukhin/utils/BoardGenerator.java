package m_polukhin.utils;

import m_polukhin.model.*;
import m_polukhin.presenter.AIPresenter;
import m_polukhin.presenter.Presenter;

import java.util.*;

public interface BoardGenerator {
    static List<Point> generateBoard(int y_max, int x_max) {
        List<Point> points = new ArrayList<>();
        Point cords = new Point(0,0);
        points.add(cords);
        for(int i = 0; i < 50; i++) {
            var neighbours = GameModel.getPossibleNeighbors(y_max, x_max, cords);
            int randomNum = new Random().nextInt(neighbours.size());
            cords = neighbours.get(randomNum);
            if (!points.contains(cords)) points.add(cords);
        }
        return points;
    }

    static List<Point> generateStarts(List<Point> cells, int size) {
        if (size > cells.size())
            throw new IllegalArgumentException("number of starting cells is more then amount of cells");
        Random rand = new Random();
        HashSet<Integer> uniqueNumbers = new HashSet<>();
        while (uniqueNumbers.size() < size) {
            int randomNum = rand.nextInt(cells.size());
            uniqueNumbers.add(randomNum);
        }
        var list = new ArrayList<Point>(size);
        uniqueNumbers.forEach(e -> list.add(cells.get(e)));
        return list;
    }

    static List<ModelListener> generatePlayers(int size, Presenter host) {
        List<ModelListener> presenters = new ArrayList<>();
        presenters.add(host);
        for(int i = 1;  i< size; i++) {
            presenters.add(new AIPresenter());
        }
        return presenters;
    }
}
