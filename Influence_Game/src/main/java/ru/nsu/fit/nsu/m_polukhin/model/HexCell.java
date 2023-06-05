package ru.nsu.fit.nsu.m_polukhin.model;

import ru.nsu.fit.nsu.m_polukhin.utils.HexCellInfo;
import ru.nsu.fit.nsu.m_polukhin.utils.Point;

class HexCell {
    private final Point position;
    private Player owner = null;
    private Integer power = 0;

    public HexCell(Point position) {
        this.position = position;
    }

    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getPower() {
        return power;
    }
    public void setPower(int power) {
        this.power = power;
    }

    public Point getPosition() {
        return position;
    }

    public HexCellInfo getInfo() {
        if (getOwner() == null) return new HexCellInfo(getPower(), -1, getPosition());
        else return new HexCellInfo(getPower(), getOwner().getId(), getPosition());
    }
}
