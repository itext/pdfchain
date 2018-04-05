package ui.panel;

import javax.swing.*;
import java.awt.*;

/**
 *
 */
public class JCenterContentPanel extends JPanel {

    public JCenterContentPanel(Component c)
    {
        setOpaque(false);
        setLayout(new GridBagLayout());
        add(c);
    }
}
