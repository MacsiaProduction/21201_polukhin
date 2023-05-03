package m_polukhin.model;

import m_polukhin.utils.*;

class HexCell {
    private Player owner = null;
    private Integer power = 0;
    private final Point position;
    public HexCell(Point position) {
        this.position = position;
    }
    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public int getPower() {
        return power;
    }
    public Point getPosition() {
        return position;
    }
    public HexCellInfo getInfo() {
        if (getOwner() == null) return new HexCellInfo(getPower(), 0, getPosition());
        else return new HexCellInfo(getPower(), getOwner().number, getPosition());
    }
}
