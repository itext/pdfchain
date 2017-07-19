/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.filechooser;

import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 *
 * @author Joris Schellekens
 */
public class JPdfFileChooser extends JFileChooser{
    
    public JPdfFileChooser()
    {
        setCurrentDirectory(new File(System.getProperty("user.home")));
        setFileFilter(new FileFilter(){
            @Override
            public boolean accept(File f) {
                if(f.isDirectory())
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
