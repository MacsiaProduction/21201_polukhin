package m_polukhin;

import m_polukhin.model.GameModel;
import m_polukhin.presenter.PlayerPresenter;
import m_polukhin.utils.Point;

public class Main {
    public final static int NUM_ROWS = 10;
    public final static int NUM_COLUMNS = 10;

    public static void main(String[] args) {
        var model = new GameModel(NUM_ROWS, NUM_COLUMNS);
        var presenter = new PlayerPresenter(model);
        model.setPresenter(presenter);
        presenter.newGame();
    }
}
