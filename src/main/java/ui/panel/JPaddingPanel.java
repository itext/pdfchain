package ui.panel;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class JPaddingPanel extends JPanel {

    public JPaddingPanel(Component c, int padding)
    {
        setLayout(new BorderLayout());
        add(c);
        setOpaque(false);
        add(Box.createVerticalStrut(padding), BorderLayout.NORTH);
        add(Box.createHorizontalStrut(padding), BorderLayout.EAST);
        add(Box.createVerticalStrut(padding), BorderLayout.SOUTH);
        add(Box.createHorizontalStrut(padding), BorderLayout.WEST);
    }
}
