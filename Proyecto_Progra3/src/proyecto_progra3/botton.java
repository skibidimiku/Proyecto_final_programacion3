package proyecto_progra3;

import java.awt.*;
import javax.swing.*;

/**
 *
 * @author MSI
 */

public class botton extends JButton {

    public botton(String texto) {
        super(texto);

        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);

        setPreferredSize(new Dimension(90, 90));

        setFont(new Font("Arial", Font.BOLD, 28));
        setHorizontalAlignment(SwingConstants.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {

        Graphics2D g2 = (Graphics2D) g.create();

        g2.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        g2.setColor(Color.WHITE);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setColor(Color.BLACK);
        g2.drawRoundRect(
            0, 0,
            getWidth() - 1,
            getHeight() - 1,
            20, 20
        );

        g2.dispose();

        super.paintComponent(g);
    }
}