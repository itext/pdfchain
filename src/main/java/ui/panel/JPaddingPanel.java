package ui.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joris Schellekens on 5/3/2017.
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
