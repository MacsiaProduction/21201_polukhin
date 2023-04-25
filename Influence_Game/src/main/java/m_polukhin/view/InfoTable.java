package m_polukhin.view;

import javax.swing.*;
import java.awt.*;

public class InfoTable extends JTextArea {
    InfoTable(int rows, int columns, Font font, Color background, Color boarder) {
        super(rows,columns);
        this.setEditable(false);
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setFont(font);
        this.setBackground(background);
        this.setBorder(BorderFactory.createLineBorder(boarder, 3));
    }
}
