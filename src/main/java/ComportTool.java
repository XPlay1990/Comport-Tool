
import GUI.Frame;
import javax.swing.BoxLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author jan.Adamczyk
 */
public class ComportTool {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
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

                Frame frame = new Frame();
                frame.setMinimumSize(frame.getMinimumSize());
                frame.setMaximumSize(frame.getPreferredSize());
                frame.setResizable(false);
                frame.setTitle("ComportTool v5.2");
//                frame.allChannelOff();
                frame.setVisible(true);
//                EventHandler connectDisconnectHandler = new EventHandler(frame);
            });
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
