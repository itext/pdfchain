package ui.filechooser;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class JPdfFileChooser extends JFileChooser {

    public JPdfFileChooser() {
        setCurrentDirectory(new File(System.getProperty("user.home")));
        setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                if (f.isDirectory())
                    return true;
                String name = f.getName().toLowerCase();
                return name.endsWith(".pdf");
            }

            @Override
            public String getDescription() {
                return "Portable Document Format (.pdf)";
            }
        });
        setMultiSelectionEnabled(false);
    }
}
