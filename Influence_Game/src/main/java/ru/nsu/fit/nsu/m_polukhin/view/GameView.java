package ru.nsu.fit.nsu.m_polukhin.view;

import ru.nsu.fit.nsu.m_polukhin.utils.ViewListener;

import javax.swing.*;
import java.awt.*;

public class GameView {
    private final JButton nextTurnButton;
    private final GamePlane gamePanel;
    private final JTextArea infoTable;
    public GameView(ViewListener presenter, int y, int x) {
        JFrame frame = new JFrame("Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // set the layout manager to BorderLayout

        // Define the colors
        Color cyan = new Color(129, 223, 223);
        Color lightPurple = new Color(158, 129, 190);
        Color darkBlueGray = new Color(121, 142, 178);
        Color darkBlue = new Color(51, 73, 113);
        Color whiteTransparent = new Color (255, 255, 255, 175);

        // Create the buttons for next turn and end game
        Font font = new Font("Square 721", Font.BOLD, 18);

        nextTurnButton = getButton("Next Turn", font, whiteTransparent, darkBlue, lightPurple);
        nextTurnButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                nextTurnButton.setBackground(cyan);
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                nextTurnButton.setBackground(darkBlue);
            }
        });

        nextTurnButton.addActionListener(e -> presenter.endTurnButtonClicked());

        JButton endGameButton = getButton("Multiplayer", font, whiteTransparent, darkBlue, lightPurple);
        endGameButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt)
            {
                endGameButton.setBackground(lightPurple);
            }

            public void mouseExited(java.awt.event.MouseEvent evt)
            {
                endGameButton.setBackground(darkBlue);
            }
        });

        endGameButton.addActionListener(e -> JOptionPane.showMessageDialog(null, "In future)"));

        // Create the info table
        infoTable = new InfoTable(2,11,font,darkBlueGray, lightPurple);
        infoTable.setText("Cur_State: Chill\n");

        // Create the button panel and set its background color
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(infoTable);
        buttonPanel.add(nextTurnButton);
        buttonPanel.add(endGameButton);
        buttonPanel.setBackground(darkBlue);

        // Add the button panel to the main panel
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create the game panel and add it to the center of the main panel
        gamePanel = new GamePlane(presenter, y, x);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(600, 650);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setAttackInfo() {
        infoTable.setText("State:  Attack\n");
    }

    public void setReinforceInfo(long powerRemain) {
        infoTable.setText("State:  Reinforce\n" + "Points Remain: "+powerRemain);
    }

    public void updateState() {
        gamePanel.invalidate();
        gamePanel.repaint();
    }

    public void gameOver() {
        while (true)
            JOptionPane.showMessageDialog(null, "Game Over!");
    }

    public void askTurn() {
        JOptionPane.showMessageDialog(null, "It's your turn");
    }

    private JButton getButton(String text, Font font, Color foreground, Color background, Color boarder) {
        JButton button = new JButton(text);
        button.setFont(font);
        button.setFocusable(false);
        button.setPreferredSize(new Dimension(120, 50)); // set button size
        button.setForeground(foreground); // set font color
        button.setBackground(background); // set background color
        button.setBorderPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setBorder(BorderFactory.createLineBorder(boarder, 3));
        return button;
    }
}



