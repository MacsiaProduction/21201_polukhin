package ru.nsu.fit.nsu.m_polukhin.utils;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.presenter.PlayerPresenter;

import java.util.*;

public class ModelGenerator {

    static List<Point> generateBoard(int rows, int columns) {
        List<Point> points = new ArrayList<>();
        Point coords = new Point(0,0);
        points.add(coords);
        for(int i = 0; i < 50; i++) {
            var neighbours = GameModel.getPossibleNeighbors(rows, columns, coords);
            int randomNum = new Random().nextInt(neighbours.size());
            coords = neighbours.get(randomNum);
            if (!points.contains(coords)) points.add(coords);
        }
        return points;
    }

    static List<Point> generateStarts(List<Point> cells, int nPlayers) {
        if (nPlayers > cells.size())
            throw new IllegalArgumentException("number of starting cells is more then amount of cells");
        Random rand = new Random();
        HashSet<Point> points = new HashSet<>();
        while (points.size() < nPlayers) {
            int randomNum = rand.nextInt(cells.size());
            points.add(cells.get(randomNum));
        }
        return new ArrayList<>(points);
    }

    public static GameModel generateModel(int rows, int columns, int nPlayers) {
//        var model = new GameModel(rows, columns);
        var cells = generateBoard(rows, columns);
        var starts = generateStarts(cells, nPlayers);
        var model = new GameModel(rows, columns, cells, starts);
//        model.initModel(cells, starts, presenter);
        return model;
    }
}
