package ru.nsu.fit.nsu.m_polukhin.utils;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.presenter.PlayerPresenter;

import java.util.*;

public class ModelGenerator {
    static List<Point> generateBoard(int rows, int columns) {
        List<Point> points = new ArrayList<>();
        Point cords = new Point(0,0);
        points.add(cords);
        for(int i = 0; i < 50; i++) {
            var neighbours = GameModel.getPossibleNeighbors(rows, columns, cords);
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

    public static GameModel generateModel(int rows, int columns, int playerCounter) {
        var model = new GameModel(rows, columns);
        var presenter = new PlayerPresenter(model);
        var cells = ModelGenerator.generateBoard(rows, columns);
        var starts = ModelGenerator.generateStarts(cells, playerCounter);
        model.initModel(cells, starts, presenter);
        return model;
    }
}
