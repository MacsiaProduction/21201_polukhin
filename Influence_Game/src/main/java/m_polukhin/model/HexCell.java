package m_polukhin.model;

import m_polukhin.utils.*;


public class HexCell {
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
        return new HexCellInfo(getPower(), getOwner(), getPosition());
    }
}
