package m_polukhin;

import m_polukhin.model.GameModel;
import m_polukhin.presenter.PlayerPresenter;
import m_polukhin.presenter.Presenter;
import m_polukhin.utils.BoardGenerator;

import java.util.List;

public class Main {
    public final static int NUM_ROWS = 10;
    public final static int NUM_COLUMNS = 10;
    public final static int playerCounter = 2;

    public static void main(String[] args) {
        var model = new GameModel(NUM_ROWS, NUM_COLUMNS);
        var presenter = new PlayerPresenter(model);
        var cells = BoardGenerator.generateBoard(NUM_ROWS, NUM_COLUMNS);
        var starts = BoardGenerator.generateStarts(cells, playerCounter);
        var players = BoardGenerator.generatePlayers(playerCounter, presenter);
        model.initModel(cells, starts, players);
    }
}
