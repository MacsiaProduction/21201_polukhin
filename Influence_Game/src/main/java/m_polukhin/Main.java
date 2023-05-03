package m_polukhin;

import m_polukhin.utils.ModelGenerator;

public class Main {
    public final static int NUM_ROWS = 10;
    public final static int NUM_COLUMNS = 10;
    public final static int playerCounter = 2;

    public static void main(String[] args) {
        var model = ModelGenerator.generateModel(NUM_ROWS, NUM_COLUMNS, playerCounter);
    }
}

// todo player is only model abstraction(don't use it anywhere else)