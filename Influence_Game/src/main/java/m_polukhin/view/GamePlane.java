package m_polukhin.view;

import m_polukhin.utils.MoveException;
import m_polukhin.utils.Player;
import m_polukhin.utils.ViewListener;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GamePlane extends JPanel {
    final int columns;
    final int rows;
    private final ViewListener presenter;
    private final List<Pair> HexShapes = new ArrayList<>();
    private Pair highlighted;

    public GamePlane(ViewListener presenter, int y, int x) {
        this.presenter = presenter;
        this.rows = y;
        this.columns = x;
        addMouseListener(new MouseAdapter() {
            private void updateHighlighted(MouseEvent e) {
                highlighted = null;
                for (Pair HexShape : HexShapes) {
                    if (HexShape.shape().contains(e.getPoint())) {
                        highlighted = HexShape;
                        repaint();
                        break;
                    }
                }
            }
            @Override
            public void mouseClicked(MouseEvent e) {
                updateHighlighted(e);
                try {
                    if(highlighted==null) {
                        presenter.cellClicked(null);
                    } else {
                        presenter.cellClicked(highlighted.info().position());
                    }
                } catch (MoveException ex) {
                    System.out.println("wrong action"+ex);
                    //todo show tip
                }
            }
        });
    }

    @Override
    public void invalidate() {
        updateHoneyComb();
    }

    protected void updateHoneyComb() {
        GeneralPath path = new GeneralPath();

        double rowHeight = ((getHeight() * 1.14f) / 3f);
        double colWidth = (float)getWidth() / columns;

        double size = Math.min(rowHeight, colWidth) / 2d;

        double centerX = size / 2d;
        double centerY = size / 2d;

        double width = Math.sqrt(3d) * size;
        double height = size * 2;

        //init hexagon shape
        for (float i = 0; i < 6; i++) {
            float angleDegrees = (60f * i) - 30f;
            float angleRad = ((float) Math.PI / 180.0f) * angleDegrees;

            double x = centerX + (size * Math.cos(angleRad));
            double y = centerY + (size * Math.sin(angleRad));

            if (i == 0) {
                path.moveTo(x, y);
            } else {
                path.lineTo(x, y);
            }
        }
        path.closePath();

        HexShapes.clear();

        // filling HexShapes
        double yPos = size / 2d;
        for (int row = 0; row < rows; row++) {
            double offset = (width / 2d);
            if (row % 2 == 1) offset = 0;
            double xPos = offset;
            for (int col = 0; col < columns; col++) {
                if(!presenter.isCellPresent(row,col)) {
                    xPos += width;
                    continue;
                }
                AffineTransform at = AffineTransform.getTranslateInstance(xPos + (size * 0.38), yPos);
                Area area = new Area(path);
                area = area.createTransformedArea(at);
                HexShapes.add(new Pair(area, presenter.getCellState(row,col)));
                xPos += width;
            }
            yPos += height * 0.75;
        }
    }

    private void updateBackgroundImage(Graphics2D g2d) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/main/resources/background2.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        // Draw the image
        if (img != null) {
            g2d.drawImage(img, 0, 0, getWidth(), getHeight(), null);
        }
    }

    private void drawHexShape(Graphics2D g2d, Pair coloredShape) {
        // drawing cell
        g2d.setColor(getPlayerColor(coloredShape.info().owner())); // set the filling color

        // Create a gradient paint
        GradientPaint gradient = new GradientPaint(
                coloredShape.shape().getBounds().x,
                coloredShape.shape().getBounds().y,
                Color.WHITE,
                coloredShape.shape().getBounds().x + coloredShape.shape().getBounds().width,
                coloredShape.shape().getBounds().y + coloredShape.shape().getBounds().height,
                getPlayerColor(coloredShape.info().owner()));

        // Set the gradient paint
        g2d.setPaint(gradient);

        g2d.fill(coloredShape.shape()); // fill the hexagon with the gradient color

        // Create a new graphics object for the shadow effect
        Graphics2D shadow = (Graphics2D) g2d.create();
        shadow.translate(3, 3); // translate the shadow
        shadow.setColor(new Color(0, 0, 0, 50)); // set the shadow color with alpha
        shadow.fill(coloredShape.shape()); // fill the shadow shape
        shadow.dispose(); // dispose the shadow graphics object

        // draw the outline
        g2d.setColor(Color.BLACK); // set the outline color
        g2d.draw(coloredShape.shape()); // draw the outline

        // get the center of the hexagon
        Rectangle bounds = coloredShape.shape().getBounds();
        int x = bounds.x + (bounds.width / 2);
        int y = bounds.y + (bounds.height / 2);

        // set the font and color for the number
        Font font = new Font("Square 721", Font.BOLD, 18);
        g2d.setFont(font);
        g2d.setColor(new Color(211, 236, 255));

        // draw the number in the center of the hexagon
        String number = Integer.toString(coloredShape.info().power());
        FontMetrics fm = g2d.getFontMetrics();

        int textWidth = fm.stringWidth(number);
        int textHeight = fm.getHeight();

        g2d.drawString(number, x - (textWidth / 2), y + (textHeight / 4));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        updateBackgroundImage(g2d);

        HexShapes.forEach(coloredShape -> drawHexShape(g2d, coloredShape));

        if (highlighted != null) {
            //todo: shape should glow translucently
            g2d.setColor(new Color(148, 171, 103, 132)); // set the outline color with alpha
            g2d.fill(highlighted.shape()); // fill the highlighted shape with the translucent color
            g2d.draw(highlighted.shape()); // draw the outline
        }
        g2d.dispose();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(200, 500);
    }

    Color getPlayerColor(Player owner) {
        if (owner == null) return Color.CYAN;
        float hue = (float) owner.number / 10.0f;
        float saturation = 0.8f;
        float brightness = 0.8f;
        return Color.getHSBColor(hue, saturation, brightness);
    }
}

