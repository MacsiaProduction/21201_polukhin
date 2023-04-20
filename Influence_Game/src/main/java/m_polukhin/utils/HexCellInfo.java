package m_polukhin.utils;

import java.awt.Color;

// CR: move color to view
public record HexCellInfo(int power, Player owner, Point position, Color color) {}
