

package m_polukhin.view;

import m_polukhin.model.GameTurnState;
import m_polukhin.utils.MoveException;
import m_polukhin.utils.ViewListener;

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
        //todo unite in mybuttton class
        nextTurnButton = new JButton("Next Turn");
        nextTurnButton.setFont(font);
        nextTurnButton.setFocusable(false);
        nextTurnButton.setPreferredSize(new Dimension(120, 50)); // set button size
        nextTurnButton.setForeground(whiteTransparent); // set font color
        nextTurnButton.setBackground(darkBlue); // set background color
        nextTurnButton.setBorderPainted(false);
        nextTurnButton.setOpaque(true);
        nextTurnButton.setContentAreaFilled(true);
        nextTurnButton.setBorder(BorderFactory.createLineBorder(lightPurple, 3));
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
        nextTurnButton.addActionListener(e -> {
            try {
                presenter.endTurnButtonClicked();
            } catch (MoveException ex) {
                //todo show tip
            }
        });

        JButton endGameButton = new JButton("Multiplayer");
        endGameButton.setFont(font);
        endGameButton.setFocusable(false);
        endGameButton.setPreferredSize(new Dimension(120, 50)); // set button size
        endGameButton.setForeground(whiteTransparent); // set font color
        endGameButton.setBackground(darkBlue); // set background color
        endGameButton.setBorderPainted(false);
        endGameButton.setOpaque(true);
        endGameButton.setContentAreaFilled(true);
        endGameButton.setBorder(BorderFactory.createLineBorder(lightPurple, 3));
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
        infoTable = new JTextArea(3, 20);
        infoTable.setEditable(false);
        infoTable.setLineWrap(true);
        infoTable.setWrapStyleWord(true);
        infoTable.setFont(font);
        infoTable.setBackground(darkBlueGray);
        infoTable.setText("Cur_Player: \nCur_State: Chill\n");
        infoTable.setBorder(BorderFactory.createLineBorder(lightPurple, 3));

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

    public void setReinforceInfo(int powerRemain) {
        infoTable.setText("State:  Reinforce\n" + "Points Remain: "+powerRemain+"\n");
    }

    public void updateState() {
        gamePanel.invalidate();
        gamePanel.repaint();
    }

    public void gameOver() {
        while (true) {
            JOptionPane.showMessageDialog(null, "Game Over!");
        }
    }

    public void askTurn(GameTurnState state) {
        JOptionPane.showMessageDialog(null, "It's your turn to "+ state.name());
    }
}



