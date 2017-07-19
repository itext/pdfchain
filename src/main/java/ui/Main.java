/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui;

import blockchain.IBlockChain;
import blockchain.MultiChain;

import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import javax.swing.*;

import pdfchain.PdfChain;
import ui.filechooser.JPdfFileChooser;

/**
 *
 * @author Joris Schellekens
 */
public class Main {
    
    public static void main(String[] args)
    {
        // set look and feel
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            // If Nimbus is not available, you can set the GUI to another look and feel.
        }        
        
        JFrame frame = new ITextBlockchainFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

}
