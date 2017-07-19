package ui.panel;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Joris Schellekens on 5/3/2017.
 */
public class JCenterContentPanel extends JPanel {

    public JCenterContentPanel(Component c)
    {
        setOpaque(false);
        setLayout(new GridBagLayout());
        add(c);
    }
}
