package m_polukhin.utils;

import java.awt.Color;

public class Player {
    private ModelListener listener;

    public ModelListener getListener() {
        return listener;
    }

    public final int number;
    public final Color color;
    private int numberOfCells = 0;
    private static int playerCount = 1;
    static public void reset() {
        playerCount = 1;
    }
    public Player() {
        this.number = playerCount++;
        color = numberToColor(number);
    }
    public void setPresenter(ModelListener listener) {
        if(this.listener != null) throw new UnsupportedOperationException("already inited");
        this.listener = listener;
    }
    private static Color numberToColor(int x) {
        float hue = (float) x / 10.0f;
        float saturation = 0.8f;
        float brightness = 0.8f;
        return Color.getHSBColor(hue, saturation, brightness);
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