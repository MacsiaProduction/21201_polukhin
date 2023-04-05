

package m_polukhin.view;

import m_polukhin.utils.Player;
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

        // Create the buttons for next turn and end game
        Font font = new Font("Square 721", Font.BOLD, 18);

        nextTurnButton = new JButton("Next Turn");
        nextTurnButton.setFont(font);
        nextTurnButton.setFocusable(false);
        nextTurnButton.setPreferredSize(new Dimension(120, 50)); // set button size
        nextTurnButton.setForeground(Color.WHITE); // set font color
        nextTurnButton.setBackground(cyan); // set background color

        nextTurnButton.addActionListener(e -> presenter.endTurnButtonClicked());

        JButton endGameButton = new JButton("New Game");
        endGameButton.setFont(font);
        endGameButton.setFocusable(false);
        endGameButton.setPreferredSize(new Dimension(120, 50)); // set button size
        endGameButton.setForeground(Color.WHITE); // set font color
        endGameButton.setBackground(lightPurple); // set background color

        endGameButton.addActionListener(e -> presenter.newGameButton());

        // Create the info table
        infoTable = new JTextArea(3, 20);
        infoTable.setEditable(false);
        infoTable.setLineWrap(true);
        infoTable.setWrapStyleWord(true);
        infoTable.setFont(font);
        infoTable.setBackground(darkBlueGray);
        infoTable.setText("Cur_Player: \nCur_State: Chill\n");

        // Create the button panel and set its background color
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3));
        buttonPanel.add(infoTable);
        buttonPanel.add(nextTurnButton);
        buttonPanel.add(endGameButton);

        // Add the button panel to the main panel
        frame.add(buttonPanel, BorderLayout.SOUTH);

        // Create the game panel and add it to the center of the main panel
        gamePanel = new GamePlane(presenter, y, x);
        frame.add(gamePanel, BorderLayout.CENTER);

        frame.pack();
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public void setAttackInfo(Player player) {
        nextTurnButton.setBackground(player.color);
        infoTable.setText("Cur_State:  Attack\n");
    }

    public void setReinforceInfo(Player player, int powerRemain) {
        infoTable.setText("Cur_State:  Reinforce\n" + "Points Remain: "+powerRemain+"\n");
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
}



