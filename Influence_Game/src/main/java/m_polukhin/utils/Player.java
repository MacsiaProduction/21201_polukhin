package m_polukhin.utils;

public class Player {
    private static int playerCount = 1;

    public final int number;
    private ModelListener listener;
    private int numberOfCells = 0;

    public Player() {
        this.number = playerCount++;
    }

    public ModelListener getListener() {
        return listener;
    }

    public void setPresenter(ModelListener listener) {
        if (this.listener != null) throw new UnsupportedOperationException("already inited");
        this.listener = listener;
    }

    public void addCell() {
        numberOfCells++;
    }

    public void deleteCell() {
        numberOfCells--;
    }

    public int getNumberOfCells() {
        return numberOfCells;
    }
}