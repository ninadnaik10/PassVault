
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.sql.*;
import javax.swing.table.*;
import java.security.Key;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64.Encoder;
import java.util.Base64.Decoder;
import javax.swing.JOptionPane;

public class Dashboard extends javax.swing.JFrame implements ActionListener {

    public String user;
    public String userPassword;
    public String selectUsername;
    public String selectPassword;
    public String selectName;
    StringSelection stringSelection;
    Clipboard clipboard;
    SHA1 s1 = new SHA1();

    /**
     * Creates new form Dashboard
     */
    public Dashboard(String username, String password) {
        user = username;
        String hashUsername = s1.hashMethod(user);
        userPassword = password;
        System.out.print(user);
        initComponents();
        this.setTitle("Passvault - Password Manager");
        loadData();
        actionEvent();
        refreshData();
        this.setResizable(false);
        this.setFont(new Font("Inter", Font.PLAIN, 14));
        AESEncryption aes = new AESEncryption(userPassword);
    }

    public void loadData() {
        this.userLabel.setText("Hello, " + user);
    }

    public void actionEvent() {
        addItem.addActionListener(this);
        refreshBtn.addActionListener(this);
        copyPasswordBtn.addActionListener(this);
        copyUsernameBtn.addActionListener(this);
        deleteBtn.addActionListener(this);
//        signinBtn.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addItem) {
            CreateEntry ce = new CreateEntry(user, userPassword);
            ce.show();
        }
        if (e.getSource() == refreshBtn) {
            DefaultTableModel model = (DefaultTableModel) entryTable.getModel();
            model.setRowCount(0);
            refreshData();
        }
        if (e.getSource() == copyPasswordBtn) {
            stringSelection = new StringSelection(selectPassword);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        if (e.getSource() == copyUsernameBtn) {
            stringSelection = new StringSelection(selectUsername);
            clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        }
        if (e.getSource() == deleteBtn) {
            String hashUsername = s1.hashMethod(user);
            try {
                Class.forName("org.sqlite.JDBC");
            } catch (Exception e1) {
                e1.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error occurred");
            }
            try {
                System.out.println(selectName);
                Connection connection = DriverManager.getConnection("jdbc:sqlite:userid.db");

                PreparedStatement PCreatestatement = connection.prepareStatement("delete from " + hashUsername + " where webName = '" + selectName + "' and webUsername = '" + selectUsername + "';");
                PCreatestatement.executeUpdate();
                DefaultTableModel model = (DefaultTableModel) entryTable.getModel();
                model.setRowCount(0);
                refreshData();
                System.out.println("Deleted successfully");
            } catch (SQLException e1) {
                e1.printStackTrace();

            }
        }
    }

    public void refreshData() {
        String hashUsername = s1.hashMethod(user);
        AESEncryption aes = new AESEncryption(userPassword);
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (Exception e1) {
            e1.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred");
        }
        try {
            Connection connection = DriverManager.getConnection("jdbc:sqlite:userid.db");

            Statement st = connection.createStatement();
            String query = "select webName, webUsername, webPassword from '" + hashUsername + "';";
            ResultSet rs = st.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            DefaultTableModel model = (DefaultTableModel) entryTable.getModel();
            /* int cols = rsmd.getColumnCount();
            String [] colName = new String[cols];
            for(int i=0; i<cols; i++){
                colName[i] = rsmd.getColumnName(i+1);
            }
            model.setColumnIdentifiers(colName); */
            while (rs.next()) {
                try {
                    String pw = aes.decrypt(rs.getString(3));
                    String us = aes.decrypt(rs.getString(2));
                    String[] row = {rs.getString(1), us, pw};
                    model.addRow(row);
                } catch (Exception ex) {
                    System.out.println(ex);
                }

//                model.getColumn
            }
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        addItem = new javax.swing.JButton();
        userLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        entryTable = new javax.swing.JTable();
        refreshBtn = new javax.swing.JButton();
        copyUsernameBtn = new javax.swing.JButton();
        copyPasswordBtn = new javax.swing.JButton();
        deleteBtn = new javax.swing.JButton();

        jButton2.setText("Copy username");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(227, 242, 253));
        jPanel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jPanel1.setFont(new java.awt.Font("Inter", 0, 18)); // NOI18N
        jPanel1.setPreferredSize(new java.awt.Dimension(720, 600));

        addItem.setBackground(new java.awt.Color(38, 127, 255));
        addItem.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        addItem.setForeground(new java.awt.Color(255, 255, 255));
        addItem.setText("Add Item");
        addItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addItemActionPerformed(evt);
            }
        });

        userLabel.setFont(new java.awt.Font("Segoe Script", 1, 36)); // NOI18N

        jScrollPane2.setBorder(null);
        jScrollPane2.setForeground(new java.awt.Color(38, 127, 255));
        jScrollPane2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jScrollPane2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jScrollPane2MouseClicked(evt);
            }
        });

        entryTable.setBackground(new java.awt.Color(204, 204, 255));
        entryTable.setFont(new java.awt.Font("Inter", 0, 14)); // NOI18N
        entryTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Website Name", "Username", "Password"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        entryTable.setRowHeight(30);
        entryTable.setSelectionBackground(new java.awt.Color(153, 153, 153));
        entryTable.getTableHeader().setReorderingAllowed(false);
        entryTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                entryTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(entryTable);
        if (entryTable.getColumnModel().getColumnCount() > 0) {
            entryTable.getColumnModel().getColumn(0).setPreferredWidth(20);
        }

        refreshBtn.setBackground(new java.awt.Color(38, 127, 255));
        refreshBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        refreshBtn.setForeground(new java.awt.Color(255, 255, 255));
        refreshBtn.setText("Refresh");
        refreshBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                refreshBtnActionPerformed(evt);
            }
        });

        copyUsernameBtn.setBackground(new java.awt.Color(38, 127, 255));
        copyUsernameBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        copyUsernameBtn.setForeground(new java.awt.Color(255, 255, 255));
        copyUsernameBtn.setText("Copy Username");
        copyUsernameBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyUsernameBtnActionPerformed(evt);
            }
        });

        copyPasswordBtn.setBackground(new java.awt.Color(38, 127, 255));
        copyPasswordBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        copyPasswordBtn.setForeground(new java.awt.Color(255, 255, 255));
        copyPasswordBtn.setText("Copy Password");
        copyPasswordBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copyPasswordBtnActionPerformed(evt);
            }
        });

        deleteBtn.setBackground(new java.awt.Color(38, 127, 255));
        deleteBtn.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        deleteBtn.setForeground(new java.awt.Color(255, 255, 255));
        deleteBtn.setText("Delete");
        deleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 500, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(33, 33, 33)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(copyPasswordBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(copyUsernameBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(deleteBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)))
                    .addComponent(userLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(110, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 51, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(addItem, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(deleteBtn)
                            .addComponent(copyUsernameBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(copyPasswordBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(refreshBtn, javax.swing.GroupLayout.Alignment.TRAILING))))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 380, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton2ActionPerformed

    private void deleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_deleteBtnActionPerformed

    private void copyPasswordBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyPasswordBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_copyPasswordBtnActionPerformed

    private void copyUsernameBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copyUsernameBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_copyUsernameBtnActionPerformed

    private void refreshBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_refreshBtnActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_refreshBtnActionPerformed

    private void jScrollPane2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jScrollPane2MouseClicked

    }//GEN-LAST:event_jScrollPane2MouseClicked

    private void entryTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_entryTableMouseClicked
        int i = entryTable.getSelectedRow();
        TableModel model = entryTable.getModel();
        selectName = model.getValueAt(i, 0).toString();
        selectUsername = model.getValueAt(i, 1).toString();
        selectPassword = model.getValueAt(i, 2).toString();
    }//GEN-LAST:event_entryTableMouseClicked

    private void addItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_addItemActionPerformed

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
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Dashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Dashboard(null, null).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addItem;
    private javax.swing.JButton copyPasswordBtn;
    private javax.swing.JButton copyUsernameBtn;
    private javax.swing.JButton deleteBtn;
    private javax.swing.JTable entryTable;
    private javax.swing.JButton jButton2;
    public javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JButton refreshBtn;
    private javax.swing.JLabel userLabel;
    // End of variables declaration//GEN-END:variables
}

class AESEncryption {

    private static final String ALGO = "AES";
    private byte[] keyValue;
    Base64.Encoder encoder = Base64.getEncoder();
    Base64.Decoder decoder = Base64.getDecoder();

    public AESEncryption(String key) {
        keyValue = key.getBytes();
    }

    public String encrypt(String Data) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(Data.getBytes());
        String encryptedValue = encoder.encodeToString(encVal);
        return encryptedValue;
    }

    public String decrypt(String encryptedData) throws Exception {
        Key key = generateKey();
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decordedValue = decoder.decode(encryptedData); //Decoder.decode(encryptedData);
        byte[] decValue = c.doFinal(decordedValue);
        String decryptedValue = new String(decValue);
        return decryptedValue;
    }

    private Key generateKey() throws Exception {
        Key key = new SecretKeySpec(keyValue, ALGO);
        return key;
    }
}
