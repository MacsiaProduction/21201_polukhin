package m_polukhin.model;

import m_polukhin.utils.HexCellInfo;
import m_polukhin.utils.ModelListener;

import java.util.ArrayList;
import java.util.List;

class Player {
    private static int playerCount = 1;

    public final int number;

    private ModelListener listener;

    private final List<HexCell> hexCells;

    public Player() {
        this.number = playerCount++;
        hexCells = new ArrayList<>();
    }

    public ModelListener getListener() {
        return listener;
    }

    public void setListener(ModelListener listener) {
        if (this.listener != null) throw new UnsupportedOperationException("already inited");
        this.listener = listener;
    }

    public boolean addCell(HexCell cell) {
        return hexCells.add(cell);
    }

    public boolean deleteCell(HexCell cell) {
        return hexCells.remove(cell);
    }

    public int getNumberOfCells() {
        return hexCells.size();
    }

    public List<HexCellInfo> getPlayerCellList() {
        List<HexCellInfo> cellList = new ArrayList<>();
        hexCells.forEach(cell -> cellList.add(cell.getInfo()));
        return cellList;
    }
}
