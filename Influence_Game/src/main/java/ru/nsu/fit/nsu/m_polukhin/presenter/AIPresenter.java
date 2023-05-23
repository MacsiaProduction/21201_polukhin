//package ru.nsu.fit.nsu.m_polukhin.presenter;
//
//import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
//import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;
//
//public class AIPresenter implements ModelListener {
//
//    private final GameModel gameModel;
//
//    @Override
//    public void askTurn() {
//        Coords coords = gameModel.generateMove();
//        gameModel.cellClicked(coords);
//    }
//
//    void updateView() {
//        askTurn();
//    }
//
//    @Override
//    public void setAttackInfo() {
//
//    }
//
//    @Override
//    public void setReinforceInfo(long powerRemain) {
//
//    }
//
//    @Override
//    public void gameOver() {
//
//    }
//}
