package ui.filechooser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 *
 */
public class JKeystoreFileChooser extends JFileChooser {
    public JKeystoreFileChooser() {
        setCurrentDirectory(new File(System.getProperty("user.home")));
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                String name = f.getName().toLowerCase();
                return name.endsWith("ks");
            }

            @Override
            public String getDescription() {
                return "Keystore (ks)";
            }
        });
        setMultiSelectionEnabled(false);
    }
}
