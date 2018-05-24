/*
 *  Copyright (C) Jan Adamczyk (j_adamczyk@hotmail.com) 2017
 */
package Main;

import GUI.QD_GUI;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jan.adamczyk
 */
public class FrameCreator {

    QD_GUI frame;

    /**
     *
     */
    public void createFrame() {
        try {
            JPanel container = new JPanel();
            container.setLayout(new BoxLayout(container, BoxLayout.X_AXIS));

            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
             */
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        break;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {

            }
            //</editor-fold>

            //</editor-fold>

            /* Create and display the form */
            java.awt.EventQueue.invokeLater(() -> {

                frame = new QD_GUI();
                frame.setMinimumSize(frame.getMinimumSize());
                frame.setMaximumSize(frame.getPreferredSize());
                frame.setResizable(false);
                frame.setTitle("ComportTool v6.5");
//                frame.allChannelOff();
                frame.setVisible(true);
//                EventHandler connectDisconnectHandler = new EventHandler(frame);
            });

            KeyboardFocusManager manager
                    = KeyboardFocusManager.getCurrentKeyboardFocusManager();
            manager.addKeyEventDispatcher(new KeyDispatcher());
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    class KeyDispatcher implements KeyEventDispatcher {

        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                switch (e.getExtendedKeyCode()) {
                    case KeyEvent.VK_F1:
                        frame.klickPausePlay();
                        break;
                    case KeyEvent.VK_F2:
                        frame.klickAllOnOff();
                        break;
                    default:
                        break;
                }
            }
            return false;
        }
    }
}
