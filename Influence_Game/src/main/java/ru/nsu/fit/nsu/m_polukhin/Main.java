package ru.nsu.fit.nsu.m_polukhin;

import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.presenter.AIPresenter;
import ru.nsu.fit.nsu.m_polukhin.presenter.PlayerPresenter;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelGenerator;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public final static int NUM_ROWS = 10;
    public final static int NUM_COLUMNS = 10;
    public final static int playerCounter = 2;

    public static void main(String[] args) {
        GameModel gameModel = ModelGenerator.generateModel(NUM_ROWS, NUM_COLUMNS, playerCounter);
        List<ModelListener> list = new ArrayList<>(List.of(new PlayerPresenter()));
        list.addAll(Collections.nCopies(playerCounter-1, new AIPresenter()));
        gameModel.setPresenters(list);
    }
}