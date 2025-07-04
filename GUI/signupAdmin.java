package joption;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

/**
 *
 * @author My PC
 */
public class signupAdmin extends javax.swing.JFrame {
    private Connection con;

    /**
     * Creates new form signupAdmin
     */
    public signupAdmin() {
        initComponents();
        setSize(800, 640);
        setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\JLibraryLogo.png");
setIconImage(icon.getImage());
    }
    
    public void Connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/joption","root","");
            
        } catch(ClassNotFoundException ex) {
            Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static int generateAdminCode(Random random) {
        return 100000 + random.nextInt(900000);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        signUpAdminPanel = new javax.swing.JPanel();
        createAdminAccountLabel = new javax.swing.JLabel();
        passwordLabel = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        passwordCheckBox = new javax.swing.JCheckBox();
        confirmPasswordLabel = new javax.swing.JLabel();
        confirmPasswordField = new javax.swing.JPasswordField();
        confirmPasswordCheckBox = new javax.swing.JCheckBox();
        signUpButton = new javax.swing.JButton();
        alreadyAUserLabel = new javax.swing.JLabel();
        backLogInButton = new javax.swing.JButton();
        signUpIcon = new javax.swing.JLabel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("SIGN UP ADMIN");
        setMinimumSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        signUpAdminPanel.setBackground(new java.awt.Color(111, 79, 55));
        signUpAdminPanel.setPreferredSize(new java.awt.Dimension(800, 600));
        signUpAdminPanel.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        createAdminAccountLabel.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        createAdminAccountLabel.setForeground(new java.awt.Color(255, 255, 255));
        createAdminAccountLabel.setText("*CREATE ADMIN ACCOUNT");
        signUpAdminPanel.add(createAdminAccountLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(53, 41, -1, -1));

        passwordLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        passwordLabel.setForeground(new java.awt.Color(255, 255, 255));
        passwordLabel.setText("Password:");
        signUpAdminPanel.add(passwordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 250, 216, -1));

        passwordField.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        passwordField.setPreferredSize(new java.awt.Dimension(64, 29));
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(passwordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 230, 360, 46));

        passwordCheckBox.setBackground(new java.awt.Color(111, 79, 55));
        passwordCheckBox.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        passwordCheckBox.setForeground(new java.awt.Color(255, 255, 255));
        passwordCheckBox.setText("See Password");
        passwordCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordCheckBoxActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(passwordCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 280, -1, -1));

        confirmPasswordLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        confirmPasswordLabel.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswordLabel.setText("Confirm Password:");
        signUpAdminPanel.add(confirmPasswordLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, -1, -1));

        confirmPasswordField.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        confirmPasswordField.setPreferredSize(new java.awt.Dimension(64, 29));
        confirmPasswordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPasswordFieldActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(confirmPasswordField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 320, 362, 44));

        confirmPasswordCheckBox.setBackground(new java.awt.Color(111, 79, 55));
        confirmPasswordCheckBox.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        confirmPasswordCheckBox.setForeground(new java.awt.Color(255, 255, 255));
        confirmPasswordCheckBox.setText("See Password");
        confirmPasswordCheckBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                confirmPasswordCheckBoxActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(confirmPasswordCheckBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 370, -1, -1));

        signUpButton.setFont(new java.awt.Font("Bahnschrift", 1, 36)); // NOI18N
        signUpButton.setForeground(new java.awt.Color(111, 79, 55));
        signUpButton.setText("Sign Up");
        signUpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                signUpButtonActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(signUpButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 420, 360, -1));

        alreadyAUserLabel.setFont(new java.awt.Font("Bahnschrift", 2, 24)); // NOI18N
        alreadyAUserLabel.setForeground(new java.awt.Color(255, 255, 255));
        alreadyAUserLabel.setText("Already a user?");
        signUpAdminPanel.add(alreadyAUserLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, 189, -1));

        backLogInButton.setBackground(new java.awt.Color(111, 79, 55));
        backLogInButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        backLogInButton.setForeground(new java.awt.Color(255, 255, 255));
        backLogInButton.setText("Back");
        backLogInButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                backLogInButtonActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(backLogInButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 550, 137, -1));

        signUpIcon.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\signUpImage.png")); // NOI18N
        signUpAdminPanel.add(signUpIcon, new org.netbeans.lib.awtextra.AbsoluteConstraints(530, 320, 270, 280));

        nameLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        nameLabel.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel.setText("Name:");
        signUpAdminPanel.add(nameLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 170, 216, -1));

        nameField.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        nameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameFieldActionPerformed(evt);
            }
        });
        signUpAdminPanel.add(nameField, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 150, 360, 50));

        getContentPane().add(signUpAdminPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void backLogInButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_backLogInButtonActionPerformed
   if (!passwordField.getText().isEmpty() || 
        !confirmPasswordField.getText().isEmpty()) {
        
        int response = JOptionPane.showConfirmDialog(this, 
            "You have unsaved changes. Are you sure you want to go back?", 
            "Confirm", 
            JOptionPane.YES_NO_OPTION, 
            JOptionPane.QUESTION_MESSAGE);
        
        if (response == JOptionPane.YES_OPTION) {
            loginAdmin loginAdmin = new loginAdmin();
            loginAdmin.setVisible(true);
            this.dispose();
        }
    } else {
       loginAdmin loginAdmin = new loginAdmin();
       loginAdmin.setVisible(true);
       this.dispose();
    }
    }//GEN-LAST:event_backLogInButtonActionPerformed

    private void signUpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_signUpButtonActionPerformed
        String name = nameField.getText();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        Random random = new Random();
        int adminCode = generateAdminCode(random);

    // Validate if fields are empty
    if (password.isEmpty() || confirmPassword.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Validate password criteria
    if (password.length() < 8 || !password.matches(".*[A-Z].*") || !password.matches(".*[0-9].*") || !password.matches(".*[!@#$%^&*()].*")) {
        JOptionPane.showMessageDialog(this, "Password must be at least 8 characters long and include an uppercase letter, a number, and a special character.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Check if passwords match
    if (!password.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(this, "Passwords do not match.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Save to database
    Connect(); // Ensure database connection
    try {
        String query = "INSERT INTO admin (name, adminCode, password) VALUES (?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, name);
        pst.setInt(2, adminCode);
        pst.setString(3, password);

        int rowCount = pst.executeUpdate();
        if (rowCount > 0) {
            JOptionPane.showMessageDialog(this, "User registered successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Redirect to login
            loginUser loginUser = new loginUser();
            loginUser.setVisible(true);
            this.dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Failed to register user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }//GEN-LAST:event_signUpButtonActionPerformed

    private void passwordCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordCheckBoxActionPerformed
        if (passwordCheckBox.isSelected()) {
            passwordField.setEchoChar((char) 0);
        } else {
            passwordField.setEchoChar('*');
        }
    }//GEN-LAST:event_passwordCheckBoxActionPerformed

    private void confirmPasswordCheckBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPasswordCheckBoxActionPerformed
        if (confirmPasswordCheckBox.isSelected()) {
            confirmPasswordField.setEchoChar((char) 0);
        } else {
            confirmPasswordField.setEchoChar('*');
        }
    }//GEN-LAST:event_confirmPasswordCheckBoxActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void confirmPasswordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_confirmPasswordFieldActionPerformed
     
    }//GEN-LAST:event_confirmPasswordFieldActionPerformed

    private void nameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameFieldActionPerformed

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
            java.util.logging.Logger.getLogger(signupAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(signupAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(signupAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(signupAdmin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new signupAdmin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel alreadyAUserLabel;
    private javax.swing.JButton backLogInButton;
    private javax.swing.JCheckBox confirmPasswordCheckBox;
    private javax.swing.JPasswordField confirmPasswordField;
    private javax.swing.JLabel confirmPasswordLabel;
    private javax.swing.JLabel createAdminAccountLabel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JCheckBox passwordCheckBox;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JLabel passwordLabel;
    private javax.swing.JPanel signUpAdminPanel;
    private javax.swing.JButton signUpButton;
    private javax.swing.JLabel signUpIcon;
    // End of variables declaration//GEN-END:variables
}
