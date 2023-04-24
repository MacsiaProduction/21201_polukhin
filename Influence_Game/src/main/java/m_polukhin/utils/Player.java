package m_polukhin.utils;

public class Player {
    private ModelListener listener;

    public ModelListener getListener() {
        return listener;
    }

    public final int number;
    private int numberOfCells = 0;
    private static int playerCount = 1;
    public Player() {
        this.number = playerCount++;
    }
    public void setPresenter(ModelListener listener) {
        if(this.listener != null) throw new UnsupportedOperationException("already inited");
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