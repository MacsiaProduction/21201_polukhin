package m_polukhin.view;

import javax.swing.*;
import java.awt.*;

public class MyButton extends JButton {
    MyButton(String text, Font font, Color foreground, Color background, Color boarder) {
        super(text);
        this.setFont(font);
        this.setFocusable(false);
        this.setPreferredSize(new Dimension(120, 50)); // set button size
        this.setForeground(foreground); // set font color
        this.setBackground(background); // set background color
        this.setBorderPainted(false);
        this.setOpaque(true);
        this.setContentAreaFilled(true);
        this.setBorder(BorderFactory.createLineBorder(boarder, 3));
    }
}
