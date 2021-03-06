/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chatprogramdemo;

/**
 *
 * @author death
 */
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.net.Socket;
import java.net.ServerSocket;
import java.io.*;
import javax.swing.*;

public class Manager extends javax.swing.JFrame implements Runnable {

    ServerSocket serverSocket = null;
    BufferedReader buff = null;
    Thread t;

    /**
     * Creates new form Manager
     */
    public Manager() {
        initComponents();
        this.setSize(700, 500);
        int serverPort = 0;
        boolean reinputPort = false;
        do {
            try {
                this.txtManagerPort.setEditable(true);
                String port = JOptionPane.showInputDialog("Input port");
                if (port==null || port!=null&&"".equals(port)) {
                    System.exit(0);
                }
                txtManagerPort.setText(port);
                serverPort = Integer.parseInt(txtManagerPort.getText());
                if (serverPort < 0 || serverPort > 65000) {
                    JOptionPane.showMessageDialog(null, "Port must between 0 and 65000");
                    reinputPort = true;
                    continue;
                }
                this.txtManagerPort.setEditable(false);
                reinputPort = false;
            } catch (Exception e) {
                reinputPort = true;
            }
        } while (reinputPort);
        try {
            serverSocket = new ServerSocket(serverPort);
            this.lblManagerPort.setText("Manager's server is running at the port ");
        } catch (Exception e) {
//            e.printStackTrace();
        }
        t = new Thread(this);
        t.start();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblManagerPort = new javax.swing.JLabel();
        txtManagerPort = new javax.swing.JTextField();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setLayout(new java.awt.GridLayout(1, 0));

        lblManagerPort.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lblManagerPort.setText("Manager Port: ");
        jPanel1.add(lblManagerPort);

        txtManagerPort.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel1.add(txtManagerPort);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 540, Short.MAX_VALUE)
            .addComponent(jTabbedPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
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
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Manager.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Manager().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel lblManagerPort;
    private javax.swing.JTextField txtManagerPort;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        while (true) {
            try {
                Socket aStaffSocket = serverSocket.accept();
                if (aStaffSocket != null) {
                    /**
                     * Get staff name. When a staff init a connection, he/she
                     * sends his/her name first
                     */
                    buff = new BufferedReader(new InputStreamReader(aStaffSocket.getInputStream()));
                    String title = buff.readLine();
                    //Create a tab for this connection
                    final ChatPanel panel = new ChatPanel(aStaffSocket, "Manager", title);
                    jTabbedPane1.add(title, panel);
                    //Create close button
                    final int index = jTabbedPane1.indexOfTab(title);
                    JButton btnClose = new JButton("x");
                    btnClose.addMouseListener(new java.awt.event.MouseAdapter() {
                        @Override
                        public void mouseClicked(java.awt.event.MouseEvent evt) {
                            panel.setText("Manager has terminated your connection, restart chat to continue");
                            jTabbedPane1.remove(index);
                        }
                    });
                    JLabel lblTitle = new JLabel(title);
                    JPanel panelTab = new JPanel();
                    GridBagConstraints gbc = new GridBagConstraints();
                    gbc.gridx = 0;
                    gbc.gridy = 0;
                    gbc.weightx = 1;
                    panelTab.add(lblTitle, gbc);
                    gbc.gridx = 1;
                    gbc.weightx = 0; //Close button will not get any extra space in tab
                    panelTab.add(btnClose, gbc);
                    jTabbedPane1.setTabComponentAt(index, panelTab);
                    panel.updateUI();
                }
                Thread.sleep(1000);
            } catch (Exception e) {
//                e.printStackTrace();
            }
        }

    }
}
