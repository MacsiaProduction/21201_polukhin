package ru.nsu.fit.nsu.m_polukhin.model.core;

import org.jetbrains.annotations.TestOnly;
import ru.nsu.fit.nsu.m_polukhin.model.GameModel;
import ru.nsu.fit.nsu.m_polukhin.utils.ModelListener;

@TestOnly
public class StabPresenter implements ModelListener {
    private GameModel model;
    private int ownerId;

    @Override
    public void init(GameModel model, int ownerId) {
        this.model = model;
        this.ownerId = ownerId;
    }

    @Override
    public void askMove() {}

    @Override
    public void startOfTurn() {}

    @Override
    public void updateView() {}

    @Override
    public void setAttackInfo() {}

    @Override
    public void setReinforceInfo(long powerRemain) {}

    @Override
    public void gameOver() {}
}
