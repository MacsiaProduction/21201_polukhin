package ru.nsu.fit.nsu.m_polukhin;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.presenter.PlayerPresenter;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelGenerator;

public class Main {
    public final static int NUM_ROWS = 10;
    public final static int NUM_COLUMNS = 10;
    public final static int playerCounter = 2;

    public static void main(String[] args) {
        GameModel gameModel = ModelGenerator.generateModel(NUM_ROWS, NUM_COLUMNS, playerCounter);
        var presenter = new PlayerPresenter(gameModel);
    }
}