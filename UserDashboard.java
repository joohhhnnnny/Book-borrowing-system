/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package joption;

import java.awt.Color;
import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import static joption.SessionManager.getCurrentUserID;

/**
 *
 * @author My PC
 */

public class UserDashboard extends javax.swing.JFrame {
    private DefaultTableModel tableModel; 
    private ArrayList<String[]> activityRecords;
    private ArrayList<String> availableBooks;
    private Connection con;
    private int userID;
    /**
     * Creates new form UserDashboard
     */
    public UserDashboard(){
        initComponents();
        setSize(800, 640);
        setLocationRelativeTo(null);
        
        ImageIcon icon = new ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\JLibraryLogo.png");
setIconImage(icon.getImage());
            
        // Get the logged-in user ID from SessionManager
        this.userID = SessionManager.getCurrentUserID();

        // Set initial placeholder text and color
        borrowingDateField.setText("YYYY-MM-DD");
        borrowingDateField.setForeground(Color.GRAY);

        dueDateField.setText("YYYY-MM-DD");
        dueDateField.setForeground(Color.GRAY);

        // Add focus listeners for borrowingDateField
borrowingDateField.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent evt) {
        if (borrowingDateField.getText().equals("YYYY-MM-DD")) {
            borrowingDateField.setText("");
            borrowingDateField.setForeground(Color.BLACK); // Reset to default text color
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent evt) {
        if (borrowingDateField.getText().isEmpty()) {
            borrowingDateField.setText("YYYY-MM-DD");
            borrowingDateField.setForeground(Color.GRAY); // Set to placeholder color
        }
    }
});

// Add focus listeners for dueDateField
dueDateField.addFocusListener(new java.awt.event.FocusAdapter() {
    @Override
    public void focusGained(java.awt.event.FocusEvent evt) {
        if (dueDateField.getText().equals("YYYY-MM-DD")) {
            dueDateField.setText("");
            dueDateField.setForeground(Color.BLACK); // Reset to default text color
        }
    }

    @Override
    public void focusLost(java.awt.event.FocusEvent evt) {
        if (dueDateField.getText().isEmpty()) {
            dueDateField.setText("YYYY-MM-DD");
            dueDateField.setForeground(Color.GRAY); // Set to placeholder color
        }
    }
});

        //for acitivity table
        tableModel = (DefaultTableModel) ratingsBookTable.getModel();
        activityRecords = new ArrayList<>();
        availableBooks = new ArrayList<>(); 
        loadBooksData();
        loadRatingsBookData();
        loadBorrowedBooks();
        loadUserProfile();
        loadSoldBooksData();
        
        //Buttons Focusable
        submitRatingsButton.setFocusable(false);
        userRateABookButton.setFocusable(false);
        userBorrowABookButton.setFocusable(false);
        userSellABookButton.setFocusable(false);
        userProfileButton.setFocusable(false);
        userLogoutButton.setFocusable(false);
        
        // Create a cell renderer to center text
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        
        // Apply the renderer to all columns in booksTable
        for (int i = 0; i < booksTable.getColumnCount(); i++) {
            booksTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // Apply the renderer to all columns in ratingsBookTable
        for (int i = 0; i < ratingsBookTable.getColumnCount(); i++) {
            ratingsBookTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // Apply the renderer to all columns in borrowedTable
        for (int i = 0; i < borrowedBooksTable.getColumnCount(); i++) {
            borrowedBooksTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        // Apply the renderer to all columns in soldBooksTable
        for (int i = 0; i < soldBooksTable.getColumnCount(); i++) {
            soldBooksTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
    }
    
    private void resetDateFields() {
    // Reset borrowingDateField to placeholder text and color
    if (borrowingDateField.getText().isEmpty()) {
        borrowingDateField.setText("YYYY-MM-DD");
        borrowingDateField.setForeground(Color.GRAY);
    }
    
    // Reset dueDateField to placeholder text and color
    if (dueDateField.getText().isEmpty()) {
        dueDateField.setText("YYYY-MM-DD");
        dueDateField.setForeground(Color.GRAY);
    }
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
    
    public final void loadBooksData() { 
    String query = "SELECT title, author, genre, ratings FROM books"; // Query for only valid columns

    try {
        Connect(); // Establish database connection
        System.out.println("Checking connection state in loadBooksData: " + con);
        

        // Execute the query
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Prepare the table model
            DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
            model.setRowCount(0); // Clear existing data in the table

            // Iterate through the result set
            while (rs.next()) {
                String bookTitle = rs.getString("title");
                String author = rs.getString("author");
                String genre = rs.getString("genre");
                int ratings = rs.getInt("ratings");

                // Debugging: Print retrieved data
                System.out.println("Book: " + bookTitle + ", Author: " + author + ", Genre: " + genre + ", Ratings: " + ratings);

                // Add the row to the table (Columns: TITLE, AUTHOR, GENRE)
                model.addRow(new Object[]{bookTitle, author, genre, ratings + "/5"});
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error loading book data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}
    
    public final void loadRatingsBookData() {
        System.out.println("Checking connection state in loadBooksData: " + con);
        String query = "SELECT title, author, genre FROM books"; // Query for only valid columns

        try {
            Connect(); // Establish database connection

            // Execute the query
            try (Statement stmt = con.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

                // Prepare the table model
                DefaultTableModel model = (DefaultTableModel) ratingsBookTable.getModel();
                model.setRowCount(0); // Clear existing data in the table

                // Iterate through the result set
                while (rs.next()) {
                    String bookTitle = rs.getString("title");
                    String author = rs.getString("author");
                    String genre = rs.getString("genre");

                    // Debugging: Print retrieved data
                    System.out.println("Book: " + bookTitle + ", Author: " + author + ", Genre: " + genre);

                    // Add the row to the table (Columns: TITLE, AUTHOR, GENRE)
                    model.addRow(new Object[]{bookTitle, author, genre});
                }
            }

        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "Error loading book data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    
    private void loadBorrowedBooks() {
    int userID = getCurrentUserID();
    if (userID == -1) return;

    DefaultTableModel model = (DefaultTableModel) borrowedBooksTable.getModel();
    model.setRowCount(0); // Clear the table

    try {
        String query = "SELECT b.title, br.borrowDate, br.returnDate FROM bookborrow br " +
                       "JOIN books b ON br.booksID = b.booksID WHERE br.userID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setInt(1, userID);
            ResultSet rs = pstmt.executeQuery();

            if (!rs.isBeforeFirst()) { // No data
                //JOptionPane.showMessageDialog(this, "No borrowed books found for this user.", "Info", JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            while (rs.next()) {
                String bookTitle = rs.getString("title");
                String borrowDate = (rs.getDate("borrowDate") != null) ? rs.getDate("borrowDate").toString() : "";
                String returnDate = (rs.getDate("returnDate") != null) ? rs.getDate("returnDate").toString() : "";
                model.addRow(new Object[]{bookTitle, borrowDate, returnDate});
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(UserDashboard.class.getName()).log(Level.SEVERE, "Error loading borrowed books", ex);
        JOptionPane.showMessageDialog(this, "Error loading borrowed books: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}


    private void loadUserProfile() {
    try {
        Connect(); // Ensure database connection

        // Get the current user's ID from SessionManager
        int userID = SessionManager.getCurrentUserID();

        // Query to fetch the user's name and bio from the profile table
        String query = "SELECT name, Bio FROM profile WHERE userID = ?";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userID);

        ResultSet rs = pst.executeQuery();

        if (rs.next()) {
            // Retrieve the name and bio from the ResultSet
            String name = rs.getString("name");
            String bio = rs.getString("Bio");

            // Set the values to the respective fields
            nameField.setText(name);
            bioField.setText(bio);
        } else {
            JOptionPane.showMessageDialog(this, "Profile not found for the logged-in user.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error fetching user profile: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (con != null) {
                con.close(); // Ensure connection is closed after use
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
    
    private void loadSoldBooksData() {
    DefaultTableModel tableModel = (DefaultTableModel) soldBooksTable.getModel();
    tableModel.setRowCount(0); // Clear existing rows

    try {
        Connect(); // Ensure database connection

        int userID = SessionManager.getCurrentUserID(); // Get the logged-in user's ID

        // Check if the user is logged in
        if (userID == -1) {
            JOptionPane.showMessageDialog(this, "No user is logged in. Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Modify the query to filter by the logged-in user ID
        String query = "SELECT b.title, s.quantity, s.totalPrice, s.sellDate " +
                       "FROM sell s " +
                       "JOIN books b ON s.bookID = b.booksID " +
                       "WHERE s.userID = ?"; // Assuming 'userID' column exists in 'sell' table

        PreparedStatement pst = con.prepareStatement(query);
        pst.setInt(1, userID); // Set the logged-in user ID in the query
        ResultSet rs = pst.executeQuery();

        while (rs.next()) {
            String bookTitle = rs.getString("title");
            int quantity = rs.getInt("quantity");
            double totalPrice = rs.getDouble("totalPrice");
            Timestamp sellDate = rs.getTimestamp("sellDate");

            // Format the sell date to a readable format (optional)
            String formattedDate = new SimpleDateFormat("yyyy-MM-dd").format(sellDate);

            // Add row to the table model
            tableModel.addRow(new Object[]{bookTitle, quantity, totalPrice, formattedDate});
        }

    } catch (SQLException ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error loading sold books data: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
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

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        menuUserPanel = new javax.swing.JPanel();
        userCompanyLogo = new javax.swing.JLabel();
        greetingsLabel = new javax.swing.JLabel();
        userRateABookButton = new javax.swing.JButton();
        userBorrowABookButton = new javax.swing.JButton();
        userSellABookButton = new javax.swing.JButton();
        userProfileButton = new javax.swing.JButton();
        userLogoutButton = new javax.swing.JButton();
        userContent = new javax.swing.JPanel();
        userReviewABookPanel = new javax.swing.JPanel();
        userActivityDashboardScrollPane = new javax.swing.JScrollPane();
        ratingsBookTable = new javax.swing.JTable();
        rateLabel = new javax.swing.JLabel();
        ratingsComboBox = new javax.swing.JComboBox<>();
        submitRatingsButton = new javax.swing.JButton();
        starsLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        starsLabel1 = new javax.swing.JLabel();
        userBorrowABookPanel = new javax.swing.JPanel();
        chooseABookLabel = new javax.swing.JLabel();
        borrowingDateLabel = new javax.swing.JLabel();
        borrowingDateField = new javax.swing.JTextField();
        dueDateLabel = new javax.swing.JLabel();
        dueDateField = new javax.swing.JTextField();
        borrowButton = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        booksTable = new javax.swing.JTable();
        jSeparator1 = new javax.swing.JSeparator();
        searchField = new javax.swing.JTextField();
        searchLabel = new javax.swing.JLabel();
        userProfilePanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        nameField = new javax.swing.JTextField();
        bioLabel = new javax.swing.JLabel();
        bioField = new javax.swing.JTextField();
        updateButton = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        borrowedBooksTable = new javax.swing.JTable();
        borrowedLabel = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        returnButton = new javax.swing.JButton();
        userSellABookPanel = new javax.swing.JPanel();
        titleOfTheBookLabel = new javax.swing.JLabel();
        titleOfTheBookField = new javax.swing.JTextField();
        authorOfTheBookLabel = new javax.swing.JLabel();
        authorOfTheBookField = new javax.swing.JTextField();
        conditionLabel = new javax.swing.JLabel();
        sellingPriceLabel = new javax.swing.JLabel();
        sellingPriceField = new javax.swing.JTextField();
        sellButton = new javax.swing.JButton();
        stockField = new javax.swing.JTextField();
        genreComboBox = new javax.swing.JComboBox<>();
        genreLabel = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        soldBooksTable = new javax.swing.JTable();
        jSeparator2 = new javax.swing.JSeparator();
        soldBooksLabel = new javax.swing.JLabel();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("USER MAIN FRAME");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(600, 800));
        getContentPane().setLayout(null);

        menuUserPanel.setBackground(new java.awt.Color(111, 79, 55));

        userCompanyLogo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        userCompanyLogo.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\IM\\Icons\\companyLogoWhiteVersion.png")); // NOI18N
        userCompanyLogo.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        userCompanyLogo.setIconTextGap(0);

        greetingsLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        greetingsLabel.setForeground(new java.awt.Color(255, 255, 255));
        greetingsLabel.setText("Hello, [user]!");

        userRateABookButton.setBackground(new java.awt.Color(242, 242, 242));
        userRateABookButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        userRateABookButton.setForeground(new java.awt.Color(111, 79, 55));
        userRateABookButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\ReviewIcon2.png")); // NOI18N
        userRateABookButton.setText("RATE A BOOK");
        userRateABookButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userRateABookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userRateABookButtonActionPerformed(evt);
            }
        });

        userBorrowABookButton.setBackground(new java.awt.Color(242, 242, 242));
        userBorrowABookButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        userBorrowABookButton.setForeground(new java.awt.Color(111, 79, 55));
        userBorrowABookButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\IM\\Icons\\borrowBook.png")); // NOI18N
        userBorrowABookButton.setText("BORROW A BOOK");
        userBorrowABookButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userBorrowABookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userBorrowABookButtonActionPerformed(evt);
            }
        });

        userSellABookButton.setBackground(new java.awt.Color(242, 242, 242));
        userSellABookButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        userSellABookButton.setForeground(new java.awt.Color(111, 79, 55));
        userSellABookButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\IM\\Icons\\sellBook.png")); // NOI18N
        userSellABookButton.setText("SELL A BOOK");
        userSellABookButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userSellABookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userSellABookButtonActionPerformed(evt);
            }
        });

        userProfileButton.setBackground(new java.awt.Color(242, 242, 242));
        userProfileButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        userProfileButton.setForeground(new java.awt.Color(111, 79, 55));
        userProfileButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\IM\\Icons\\userIcon.png")); // NOI18N
        userProfileButton.setText("PROFILE");
        userProfileButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userProfileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userProfileButtonActionPerformed(evt);
            }
        });

        userLogoutButton.setBackground(new java.awt.Color(242, 242, 242));
        userLogoutButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        userLogoutButton.setForeground(new java.awt.Color(111, 79, 55));
        userLogoutButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\IM\\Icons\\logout.png")); // NOI18N
        userLogoutButton.setText("LOGOUT");
        userLogoutButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        userLogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                userLogoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuUserPanelLayout = new javax.swing.GroupLayout(menuUserPanel);
        menuUserPanel.setLayout(menuUserPanelLayout);
        menuUserPanelLayout.setHorizontalGroup(
            menuUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(userRateABookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(userSellABookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(userBorrowABookButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuUserPanelLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(greetingsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(menuUserPanelLayout.createSequentialGroup()
                .addComponent(userCompanyLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(userProfileButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(userLogoutButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        menuUserPanelLayout.setVerticalGroup(
            menuUserPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuUserPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(userCompanyLogo, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(greetingsLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userRateABookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(userBorrowABookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(userSellABookButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(userProfileButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(userLogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        getContentPane().add(menuUserPanel);
        menuUserPanel.setBounds(0, 0, 230, 600);

        userContent.setBackground(new java.awt.Color(175, 108, 81));
        userContent.setLayout(new java.awt.CardLayout());

        userReviewABookPanel.setBackground(new java.awt.Color(222, 172, 128));

        ratingsBookTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        ratingsBookTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TITLE", "AUTHOR", "GENRE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        userActivityDashboardScrollPane.setViewportView(ratingsBookTable);

        rateLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        rateLabel.setForeground(new java.awt.Color(255, 255, 255));
        rateLabel.setText("Rate:");

        ratingsComboBox.setBackground(new java.awt.Color(242, 242, 242));
        ratingsComboBox.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        ratingsComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "5", "4", "3", "2", "1", " " }));
        ratingsComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ratingsComboBoxActionPerformed(evt);
            }
        });

        submitRatingsButton.setBackground(new java.awt.Color(242, 242, 242));
        submitRatingsButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        submitRatingsButton.setForeground(new java.awt.Color(111, 79, 55));
        submitRatingsButton.setText("                                 SUBMIT RATINGS");
        submitRatingsButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        submitRatingsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                submitRatingsButtonActionPerformed(evt);
            }
        });

        starsLabel.setFont(new java.awt.Font("Bahnschrift", 1, 14)); // NOI18N
        starsLabel.setForeground(new java.awt.Color(255, 255, 255));
        starsLabel.setText("Star/s");

        jLabel1.setFont(new java.awt.Font("Bahnschrift", 1, 12)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("We guarantee the confidentiality of ratings and ensure strict adherence to policy protocols.");

        starsLabel1.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        starsLabel1.setForeground(new java.awt.Color(255, 255, 255));
        starsLabel1.setText("Rate a Book");

        javax.swing.GroupLayout userReviewABookPanelLayout = new javax.swing.GroupLayout(userReviewABookPanel);
        userReviewABookPanel.setLayout(userReviewABookPanelLayout);
        userReviewABookPanelLayout.setHorizontalGroup(
            userReviewABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userReviewABookPanelLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(userReviewABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(starsLabel1)
                    .addComponent(submitRatingsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(userActivityDashboardScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(userReviewABookPanelLayout.createSequentialGroup()
                        .addComponent(rateLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ratingsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(starsLabel))
                    .addGroup(userReviewABookPanelLayout.createSequentialGroup()
                        .addGap(8, 8, 8)
                        .addComponent(jLabel1)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        userReviewABookPanelLayout.setVerticalGroup(
            userReviewABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userReviewABookPanelLayout.createSequentialGroup()
                .addContainerGap(53, Short.MAX_VALUE)
                .addComponent(starsLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(userActivityDashboardScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userReviewABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rateLabel)
                    .addComponent(ratingsComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(starsLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(submitRatingsButton)
                .addGap(97, 97, 97)
                .addComponent(jLabel1)
                .addGap(43, 43, 43))
        );

        userContent.add(userReviewABookPanel, "card4");

        userBorrowABookPanel.setBackground(new java.awt.Color(222, 172, 128));
        userBorrowABookPanel.setPreferredSize(new java.awt.Dimension(570, 600));

        chooseABookLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        chooseABookLabel.setForeground(new java.awt.Color(255, 255, 255));
        chooseABookLabel.setText("Choose a Book:");

        borrowingDateLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        borrowingDateLabel.setForeground(new java.awt.Color(255, 255, 255));
        borrowingDateLabel.setText("Borrowing Date:");

        borrowingDateField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        borrowingDateField.setText("MM-DD-YYYY");
        borrowingDateField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowingDateFieldActionPerformed(evt);
            }
        });

        dueDateLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        dueDateLabel.setForeground(new java.awt.Color(255, 255, 255));
        dueDateLabel.setText("Due Date:");

        dueDateField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        dueDateField.setText("MM-DD-YYYY");
        dueDateField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        borrowButton.setBackground(new java.awt.Color(242, 242, 242));
        borrowButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        borrowButton.setForeground(new java.awt.Color(111, 79, 55));
        borrowButton.setText("BORROW");
        borrowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrowButtonActionPerformed(evt);
            }
        });

        booksTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        booksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TITLE", "AUTHOR", "GENRE", "RATINGS"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane2.setViewportView(booksTable);

        jSeparator1.setForeground(new java.awt.Color(255, 255, 255));

        searchField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        searchLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        searchLabel.setForeground(new java.awt.Color(255, 255, 255));
        searchLabel.setText("Search:");

        javax.swing.GroupLayout userBorrowABookPanelLayout = new javax.swing.GroupLayout(userBorrowABookPanel);
        userBorrowABookPanel.setLayout(userBorrowABookPanelLayout);
        userBorrowABookPanelLayout.setHorizontalGroup(
            userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userBorrowABookPanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userBorrowABookPanelLayout.createSequentialGroup()
                        .addComponent(searchLabel)
                        .addGap(18, 18, 18)
                        .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chooseABookLabel, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(borrowButton, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, userBorrowABookPanelLayout.createSequentialGroup()
                                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(borrowingDateLabel)
                                    .addComponent(dueDateLabel))
                                .addGap(111, 111, 111)
                                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(dueDateField, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(borrowingDateField, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 276, Short.MAX_VALUE))))))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        userBorrowABookPanelLayout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {borrowingDateLabel, dueDateLabel});

        userBorrowABookPanelLayout.setVerticalGroup(
            userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userBorrowABookPanelLayout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchLabel)
                    .addComponent(searchField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addComponent(chooseABookLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(borrowingDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrowingDateLabel))
                .addGap(18, 18, 18)
                .addGroup(userBorrowABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(dueDateField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(dueDateLabel))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(borrowButton)
                .addGap(86, 86, 86))
        );

        userContent.add(userBorrowABookPanel, "card2");

        userProfilePanel.setBackground(new java.awt.Color(222, 172, 128));

        nameLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        nameLabel.setForeground(new java.awt.Color(255, 255, 255));
        nameLabel.setText("Name:");

        nameField.setEditable(false);
        nameField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        bioLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        bioLabel.setForeground(new java.awt.Color(255, 255, 255));
        bioLabel.setText("Bio:");

        bioField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        updateButton.setBackground(new java.awt.Color(242, 242, 242));
        updateButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        updateButton.setForeground(new java.awt.Color(111, 79, 55));
        updateButton.setText("UPDATE BIO");
        updateButton.setPreferredSize(new java.awt.Dimension(135, 37));
        updateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateButtonActionPerformed(evt);
            }
        });

        borrowedBooksTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        borrowedBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BOOK", "BORROW DATE", "RETURN DATE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane5.setViewportView(borrowedBooksTable);

        borrowedLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        borrowedLabel.setForeground(new java.awt.Color(255, 255, 255));
        borrowedLabel.setText("Borrowed books:");

        jSeparator3.setForeground(new java.awt.Color(255, 255, 255));

        jLabel2.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Downloads\\user.png")); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 147, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );

        returnButton.setBackground(new java.awt.Color(242, 242, 242));
        returnButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        returnButton.setForeground(new java.awt.Color(111, 79, 55));
        returnButton.setText("RETURN BOOK");
        returnButton.setPreferredSize(new java.awt.Dimension(135, 37));
        returnButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                returnButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout userProfilePanelLayout = new javax.swing.GroupLayout(userProfilePanel);
        userProfilePanel.setLayout(userProfilePanelLayout);
        userProfilePanelLayout.setHorizontalGroup(
            userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userProfilePanelLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(userProfilePanelLayout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 44, Short.MAX_VALUE)
                        .addGroup(userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bioField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bioLabel)
                            .addComponent(nameLabel)
                            .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31))
                    .addGroup(userProfilePanelLayout.createSequentialGroup()
                        .addGroup(userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jSeparator3)
                                .addComponent(borrowedLabel)
                                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 530, Short.MAX_VALUE)))
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        userProfilePanelLayout.setVerticalGroup(
            userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userProfilePanelLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(userProfilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userProfilePanelLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(47, 47, 47))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userProfilePanelLayout.createSequentialGroup()
                        .addComponent(nameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bioLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bioField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(25, 25, 25)
                        .addComponent(updateButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(17, 17, 17)))
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(borrowedLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(returnButton, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(68, Short.MAX_VALUE))
        );

        userContent.add(userProfilePanel, "card5");

        userSellABookPanel.setBackground(new java.awt.Color(222, 172, 128));

        titleOfTheBookLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        titleOfTheBookLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleOfTheBookLabel.setText("Title of the Book:");

        titleOfTheBookField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        authorOfTheBookLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        authorOfTheBookLabel.setForeground(new java.awt.Color(255, 255, 255));
        authorOfTheBookLabel.setText("Author of the Book:");

        authorOfTheBookField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        conditionLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        conditionLabel.setForeground(new java.awt.Color(255, 255, 255));
        conditionLabel.setText("Stock:");

        sellingPriceLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        sellingPriceLabel.setForeground(new java.awt.Color(255, 255, 255));
        sellingPriceLabel.setText("Selling Price:");

        sellingPriceField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N

        sellButton.setBackground(new java.awt.Color(242, 242, 242));
        sellButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        sellButton.setForeground(new java.awt.Color(111, 79, 55));
        sellButton.setText("SELL");
        sellButton.setMaximumSize(new java.awt.Dimension(135, 37));
        sellButton.setMinimumSize(new java.awt.Dimension(135, 37));
        sellButton.setPreferredSize(new java.awt.Dimension(135, 37));
        sellButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sellButtonActionPerformed(evt);
            }
        });

        stockField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        stockField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stockFieldActionPerformed(evt);
            }
        });

        genreComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Action", "Horror", "Fantasy", "Drama", "Sci-Fi", " ", " " }));
        genreComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genreComboBoxActionPerformed(evt);
            }
        });

        genreLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        genreLabel.setForeground(new java.awt.Color(255, 255, 255));
        genreLabel.setText("Genre:");

        soldBooksTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        soldBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "BOOK", "QUANTITY", "TOTAL PRICE", "DATE"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane4.setViewportView(soldBooksTable);

        jSeparator2.setBackground(new java.awt.Color(255, 255, 255));
        jSeparator2.setForeground(new java.awt.Color(255, 255, 255));

        soldBooksLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        soldBooksLabel.setForeground(new java.awt.Color(255, 255, 255));
        soldBooksLabel.setText("Sold books:");

        javax.swing.GroupLayout userSellABookPanelLayout = new javax.swing.GroupLayout(userSellABookPanel);
        userSellABookPanel.setLayout(userSellABookPanelLayout);
        userSellABookPanelLayout.setHorizontalGroup(
            userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userSellABookPanelLayout.createSequentialGroup()
                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(userSellABookPanelLayout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sellButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, userSellABookPanelLayout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(userSellABookPanelLayout.createSequentialGroup()
                                .addComponent(soldBooksLabel)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(jSeparator2, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                            .addGroup(userSellABookPanelLayout.createSequentialGroup()
                                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(userSellABookPanelLayout.createSequentialGroup()
                                        .addComponent(titleOfTheBookLabel)
                                        .addGap(38, 52, Short.MAX_VALUE))
                                    .addGroup(userSellABookPanelLayout.createSequentialGroup()
                                        .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(userSellABookPanelLayout.createSequentialGroup()
                                                .addComponent(sellingPriceLabel)
                                                .addGap(0, 77, Short.MAX_VALUE))
                                            .addComponent(authorOfTheBookLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(titleOfTheBookField, javax.swing.GroupLayout.DEFAULT_SIZE, 289, Short.MAX_VALUE)
                                    .addComponent(authorOfTheBookField, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(sellingPriceField)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, userSellABookPanelLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(conditionLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stockField, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(genreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(10, 10, 10)))))
                .addGap(45, 45, 45))
        );
        userSellABookPanelLayout.setVerticalGroup(
            userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(userSellABookPanelLayout.createSequentialGroup()
                .addContainerGap(33, Short.MAX_VALUE)
                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(titleOfTheBookField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(titleOfTheBookLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(authorOfTheBookField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(authorOfTheBookLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(genreComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(genreLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(stockField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(conditionLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(userSellABookPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sellingPriceField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sellingPriceLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(sellButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 47, Short.MAX_VALUE)
                .addComponent(soldBooksLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56))
        );

        userSellABookPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {authorOfTheBookField, sellingPriceField, titleOfTheBookField});

        userSellABookPanelLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {authorOfTheBookLabel, conditionLabel, sellingPriceLabel, titleOfTheBookLabel});

        userContent.add(userSellABookPanel, "card3");

        getContentPane().add(userContent);
        userContent.setBounds(230, 0, 570, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void userRateABookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userRateABookButtonActionPerformed
        userContent.removeAll();
        userContent.add(userReviewABookPanel);
        userContent.repaint();
        userContent.revalidate();
    }//GEN-LAST:event_userRateABookButtonActionPerformed

    private void userSellABookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userSellABookButtonActionPerformed
        userContent.removeAll();
        userContent.add(userSellABookPanel);
        userContent.repaint();
        userContent.revalidate();
    }//GEN-LAST:event_userSellABookButtonActionPerformed

    private void userLogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userLogoutButtonActionPerformed
       int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (response == JOptionPane.YES_OPTION) {
        loginUser loginUser  = new loginUser ();
        loginUser .setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_userLogoutButtonActionPerformed

    private void sellButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sellButtonActionPerformed
        // Get input values
    String title = titleOfTheBookField.getText();
    String author = authorOfTheBookField.getText();
    String price = sellingPriceField.getText();
    String genre = genreComboBox.getSelectedItem().toString(); // Assuming genreComboBox is a JComboBox
    String stockString = stockField.getText(); // Stock entered by the user
    
    // Ensure inputs are not empty
    if (title.isEmpty() || author.isEmpty() || price.isEmpty() || genre.isEmpty() || stockString.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Parse stock to an integer
    int stock;
    try {
        stock = Integer.parseInt(stockString);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid stock value. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Calculate total price (assuming price is in string format and needs to be converted to a double)
    double bookPrice;
    try {
        bookPrice = Double.parseDouble(price);
    } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid price value. Please enter a valid price.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    double totalPrice = bookPrice * stock;  // Total price of the sold books

    // Assuming userID is obtained from the session or context
    int userID = SessionManager.getCurrentUserID();  // This should get the logged-in userID

    // Connect to the database and add the book to the books table
    try {
        Connect(); // Ensure connection to the database
        
        // Insert the book into the books table with the new stock value and set default rating to 0
        String insertQuery = "INSERT INTO books (title, author, genre, stock, ratings) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement insertStmt = con.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);
        insertStmt.setString(1, title);
        insertStmt.setString(2, author);
        insertStmt.setString(3, genre);
        insertStmt.setInt(4, stock);  // Add the stock value
        insertStmt.setInt(5, 0);      // Set default ratings to 0
        insertStmt.executeUpdate();
        
        // Get the generated bookID after inserting the new book
        ResultSet generatedKeys = insertStmt.getGeneratedKeys();
        int bookID = 0;
        if (generatedKeys.next()) {
            bookID = generatedKeys.getInt(1);
        }

        // Insert the sale into the 'sell' table
        String sellQuery = "INSERT INTO sell (userID, bookID, quantity, sellDate, totalPrice) VALUES (?, ?, ?, CURRENT_TIMESTAMP, ?)";
        PreparedStatement sellStmt = con.prepareStatement(sellQuery);
        sellStmt.setInt(1, userID);      // Get the userID from the session
        sellStmt.setInt(2, bookID);      // The bookID from the previous insert
        sellStmt.setInt(3, stock);       // The quantity of books sold
        sellStmt.setDouble(4, totalPrice);  // Total price of the books sold
        sellStmt.executeUpdate();

        // Add sale activity to the history table with "SELL" activity
        String addHistoryQuery = "INSERT INTO history (activity, userID, booksID, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement historyStmt = con.prepareStatement(addHistoryQuery)) {
            historyStmt.setString(1, "SELL");  // Activity type is "SELL"
            historyStmt.setInt(2, userID);     // Current userID
            historyStmt.setInt(3, bookID);     // The bookID of the sold book
            historyStmt.setDate(4, new java.sql.Date(System.currentTimeMillis()));  // Current date
            historyStmt.executeUpdate();
        }

        // Log the sale activity (add to table model and/or local records)
        String[] activity = {title, genre, getCurrentDate(), price};
        tableModel.addRow(activity);
        loadSoldBooksData();

        // Clear input fields
        titleOfTheBookField.setText("");
        authorOfTheBookField.setText("");
        sellingPriceField.setText("");
        stockField.setText("");

        JOptionPane.showMessageDialog(this, "Book sold successfully and added to the database!", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException ex) {
        Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error processing sale: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_sellButtonActionPerformed

    private void userBorrowABookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userBorrowABookButtonActionPerformed
        userContent.removeAll();
        userContent.add(userBorrowABookPanel);
        userContent.repaint();
        userContent.revalidate();
    }//GEN-LAST:event_userBorrowABookButtonActionPerformed

    private void userProfileButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_userProfileButtonActionPerformed
        userContent.removeAll();
        userContent.add(userProfilePanel);
        userContent.repaint();
        userContent.revalidate();
    }//GEN-LAST:event_userProfileButtonActionPerformed

    private void borrowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowButtonActionPerformed
        // Get the selected book row
    int selectedRow = booksTable.getSelectedRow();
    
    int userID = SessionManager.getCurrentUserID();

    // Ensure a user is logged in
    if (userID == -1) {
        JOptionPane.showMessageDialog(this, "No user is logged in. Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a book to borrow.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    String borrowDate = borrowingDateField.getText().trim();
    String dueDate = dueDateField.getText().trim();

    if (borrowDate.isEmpty() || dueDate.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Get the selected book title
    String bookTitle = (String) booksTable.getValueAt(selectedRow, 0); // Assuming column 0 is the title

    try {
        Connect(); // Ensure database connection is established

        // Retrieve the bookID based on the bookTitle
        String getBookIDQuery = "SELECT booksID, stock FROM books WHERE title = ?";
        int booksID = -1;
        int stock = 0;

        try (PreparedStatement pstmt = con.prepareStatement(getBookIDQuery)) {
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                booksID = rs.getInt("booksID");
                stock = rs.getInt("stock");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Check if stock is available
        if (stock <= 0) {
            JOptionPane.showMessageDialog(this, "Book is out of stock.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Insert into the borrow table
        String borrowInsertQuery = "INSERT INTO bookborrow (userID, booksID, borrowDate, returnDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(borrowInsertQuery)) {
            pstmt.setInt(1, userID);
            pstmt.setInt(2, booksID);
            pstmt.setDate(3, java.sql.Date.valueOf(borrowDate)); // Convert to SQL Date
            pstmt.setDate(4, java.sql.Date.valueOf(dueDate)); // Convert to SQL Date

            pstmt.executeUpdate();
        }

        // Update book stock
        String updateStockQuery = "UPDATE books SET stock = stock - 1 WHERE booksID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateStockQuery)) {
            pstmt.setInt(1, booksID);
            pstmt.executeUpdate();
        }

        // Insert the activity record into the 'history' table
        String historyInsertQuery = "INSERT INTO history (activity, userID, booksID, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(historyInsertQuery)) {
            pstmt.setString(1, "BORROW");  // Activity type is "BORROW"
            pstmt.setInt(2, userID);       // User ID of the current user
            pstmt.setInt(3, booksID);      // Book ID of the borrowed book
            pstmt.setDate(4, java.sql.Date.valueOf(borrowDate)); // Date of borrowing
            pstmt.executeUpdate();  // Insert the record into the 'history' table
        }

        borrowingDateField.setText("");
        dueDateField.setText("");
        
        // Update the activity record in the UI (activity log)
        String[] activity = {"Borrowed", borrowDate};
        activityRecords.add(activity);
        tableModel.addRow(activity);

        loadBorrowedBooks();
        JOptionPane.showMessageDialog(this, "Book borrowed successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        
        resetDateFields();

    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error borrowing book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_borrowButtonActionPerformed

    private void borrowingDateFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrowingDateFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_borrowingDateFieldActionPerformed

    private void genreComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genreComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genreComboBoxActionPerformed

    private void stockFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stockFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_stockFieldActionPerformed

    private void ratingsComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ratingsComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_ratingsComboBoxActionPerformed

    private void submitRatingsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_submitRatingsButtonActionPerformed
       // Get the selected row index
    int selectedRow = ratingsBookTable.getSelectedRow();

    if (selectedRow == -1) {
        // No row is selected, show a warning
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a book to update.");
        return;
    }

    Object ratings = ratingsComboBox.getSelectedItem();
    System.out.println("Ratings: " + ratings);

    // Get the title from the selected row
    String selectedTitle = ratingsBookTable.getValueAt(selectedRow, 0).toString(); // Assuming title is in column 0
    System.out.println("Selected row: " + selectedRow);
    System.out.println("Selected title: " + selectedTitle);

    int booksID = -1;

    try {
        Connect(); // Ensure the database connection is established

        // Query the booksID using the title
        String fetchQuery = "SELECT booksID FROM books WHERE title = ?";
        try (PreparedStatement pstmt = con.prepareStatement(fetchQuery)) {
            pstmt.setString(1, selectedTitle);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    booksID = rs.getInt("booksID");
                    System.out.println("booksID fetched from database: " + booksID);
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No matching book found for the selected title.");
                    return;
                }
            }
        }

        // Check if the user has already rated the book
        String checkRatingQuery = "SELECT COUNT(*) AS count FROM book_ratings WHERE userID = ? AND booksID = ?";
        System.out.println("Checking if user " + userID + " has rated book " + booksID);
        try (PreparedStatement pstmt = con.prepareStatement(checkRatingQuery)) {
            pstmt.setInt(1, userID); // Set the userID
            pstmt.setInt(2, booksID); // Set the bookID
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next() && rs.getInt("count") > 0) {
                    System.out.println("User " + userID + " has already rated book " + booksID);
                    javax.swing.JOptionPane.showMessageDialog(this, "You have already rated this book.");
                    return; // Exit if the user has already rated this book
                } else {
                    System.out.println("User " + userID + " has not rated book " + booksID);
                }
            }
        }

        // Insert the rating into the book_ratings table (to keep track of all ratings for the book)
        String insertQuery = "INSERT INTO book_ratings (booksID, userID, rating) VALUES (?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(insertQuery)) {
            pstmt.setInt(1, booksID);  // Set the booksID
            pstmt.setInt(2, userID);   // Set the userID
            pstmt.setInt(3, Integer.parseInt(ratings.toString()));  // Set the rating

            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                javax.swing.JOptionPane.showMessageDialog(this, "Rating submitted to book_ratings successfully.");
            } else {
                javax.swing.JOptionPane.showMessageDialog(this, "Failed to submit rating to book_ratings.");
                return;
            }
        }

        // Calculate the average rating for the selected book
        String avgQuery = "SELECT AVG(rating) AS average_rating FROM book_ratings WHERE booksID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(avgQuery)) {
            pstmt.setInt(1, booksID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    double avgRating = rs.getDouble("average_rating");
                    System.out.println("Average rating: " + avgRating);

                    // Update the ratings in the books table with the calculated average
                    String updateQuery = "UPDATE books SET ratings = ? WHERE booksID = ?";
                    try (PreparedStatement updatePstmt = con.prepareStatement(updateQuery)) {
                        updatePstmt.setDouble(1, avgRating); // Set the average rating
                        updatePstmt.setInt(2, booksID);      // Set the booksID for the WHERE condition

                        int updateRowsAffected = updatePstmt.executeUpdate();

                        if (updateRowsAffected > 0) {
                            javax.swing.JOptionPane.showMessageDialog(this, "Book ratings updated successfully.");
                            loadBooksData(); // Refresh the table data
                        } else {
                            javax.swing.JOptionPane.showMessageDialog(this, "No book ratings were updated. Please try again.");
                        }
                    }
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "Error calculating average rating.");
                }
            }
        }

    } catch (SQLException ex) {
        javax.swing.JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        ex.printStackTrace();
    } finally {
        try {
            if (con != null) {
                con.close(); // Ensure the connection is closed after use
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_submitRatingsButtonActionPerformed

    private void updateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateButtonActionPerformed
        String updatedBio = bioField.getText(); // Get the updated bio from the bioField
    int userID = SessionManager.getCurrentUserID(); // Get the logged-in user's ID

    // Check if bioField is empty
    if (updatedBio.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Bio cannot be empty.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    Connect(); // Ensure the database connection is established
    try {
        // Update the bio in the profile table for the logged-in user
        String updateQuery = "UPDATE profile SET Bio = ? WHERE userID = ?";
        PreparedStatement pst = con.prepareStatement(updateQuery);
        pst.setString(1, updatedBio); // Set the updated bio
        pst.setInt(2, userID); // Set the userID for the WHERE condition

        int rowsAffected = pst.executeUpdate();
        if (rowsAffected > 0) {
            JOptionPane.showMessageDialog(this, "Bio updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Failed to update bio. Please try again.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(UserDashboard.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
        try {
            if (con != null) {
                con.close(); // Close the connection after use
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    }//GEN-LAST:event_updateButtonActionPerformed

    private void returnButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_returnButtonActionPerformed
        // Get the selected borrowed book row
    int selectedRow = borrowedBooksTable.getSelectedRow(); // Assuming this table shows borrowed books

    int userID = SessionManager.getCurrentUserID();

    // Ensure a user is logged in
    if (userID == -1) {
        JOptionPane.showMessageDialog(this, "No user is logged in. Please log in first.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Please select a borrowed book to return.", "Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Get the book title (column 0 contains book title)
    String bookTitle = (String) borrowedBooksTable.getValueAt(selectedRow, 0);

    try {
        Connect(); // Ensure database connection is established

        // Retrieve the booksID based on the bookTitle
        String getBookIDQuery = "SELECT booksID FROM books WHERE title = ?";
        int booksID = -1;

        try (PreparedStatement pstmt = con.prepareStatement(getBookIDQuery)) {
            pstmt.setString(1, bookTitle);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                booksID = rs.getInt("booksID");
            } else {
                JOptionPane.showMessageDialog(this, "Book not found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Update book stock (increment stock by 1)
        String updateStockQuery = "UPDATE books SET stock = stock + 1 WHERE booksID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(updateStockQuery)) {
            pstmt.setInt(1, booksID);
            pstmt.executeUpdate();
        }

        // Mark the book as returned in the bookborrow table (delete the record)
        String returnBookQuery = "DELETE FROM bookborrow WHERE booksID = ? AND userID = ?";
        try (PreparedStatement pstmt = con.prepareStatement(returnBookQuery)) {
            pstmt.setInt(1, booksID);
            pstmt.setInt(2, userID); // Assuming userID exists in the bookborrow table to identify the borrower
            pstmt.executeUpdate();
        }

        // Insert a record into the history table (activity = 'RETURN')
        String historyQuery = "INSERT INTO history (activity, userID, booksID, date) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = con.prepareStatement(historyQuery)) {
            pstmt.setString(1, "RETURNED"); // Activity is "RETURN"
            pstmt.setInt(2, userID); // User ID of the person returning the book
            pstmt.setInt(3, booksID); // Book ID of the returned book
            pstmt.setDate(4, new java.sql.Date(System.currentTimeMillis())); // Current date
            pstmt.executeUpdate();
        }

        // Reload borrowed books table
        loadBorrowedBooks();

        JOptionPane.showMessageDialog(this, "Book returned successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        JOptionPane.showMessageDialog(this, "Error returning book: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_returnButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
    // Get the search query from the text field
    String searchQuery = searchField.getText().trim();

    // Define the SQL query for both cases
    String query;
    if (searchQuery.isEmpty()) {
        // If search field is empty, fetch all records
        query = "SELECT title, author, genre, ratings FROM books";
    } else {
        // If search field is not empty, fetch filtered records
        query = "SELECT title, author, genre, ratings FROM books WHERE "
                + "title LIKE ? OR author LIKE ? OR genre LIKE ?";
    }

    try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/joption", "root", "");
         PreparedStatement pstmt = con.prepareStatement(query)) {

        // Set parameters only if searchQuery is not empty
        if (!searchQuery.isEmpty()) {
            pstmt.setString(1, "%" + searchQuery + "%");
            pstmt.setString(2, "%" + searchQuery + "%");
            pstmt.setString(3, "%" + searchQuery + "%");
        }

        // Execute the query and get the result set
        ResultSet rs = pstmt.executeQuery();

        // Clear the table model
        DefaultTableModel model = (DefaultTableModel) booksTable.getModel();
        model.setRowCount(0); // Clear existing rows

        // Add the search results to the table
        while (rs.next()) {
            String title = rs.getString("title");
            String author = rs.getString("author");
            String genre = rs.getString("genre");
            double ratings = rs.getDouble("ratings");

            // Add a row to the table model
            model.addRow(new Object[]{title, author, genre, ratings});
        }

        // Check if no results were found
        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No matching books found.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Search completed successfully.", "Search Results", JOptionPane.INFORMATION_MESSAGE);
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Error retrieving data from the database.", "Database Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_searchFieldActionPerformed


     private String getCurrentDate() {
        return "MM-DD-YYYY"; 
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Flatlaf".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UserDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UserDashboard().setVisible(true);  // Pass userID as parameter
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField authorOfTheBookField;
    private javax.swing.JLabel authorOfTheBookLabel;
    private javax.swing.JTextField bioField;
    private javax.swing.JLabel bioLabel;
    private javax.swing.JTable booksTable;
    private javax.swing.JButton borrowButton;
    private javax.swing.JTable borrowedBooksTable;
    private javax.swing.JLabel borrowedLabel;
    private javax.swing.JTextField borrowingDateField;
    private javax.swing.JLabel borrowingDateLabel;
    private javax.swing.JLabel chooseABookLabel;
    private javax.swing.JLabel conditionLabel;
    private javax.swing.JTextField dueDateField;
    private javax.swing.JLabel dueDateLabel;
    private javax.swing.JComboBox<String> genreComboBox;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JLabel greetingsLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JTable jTable1;
    private javax.swing.JPanel menuUserPanel;
    private javax.swing.JTextField nameField;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JLabel rateLabel;
    private javax.swing.JTable ratingsBookTable;
    private javax.swing.JComboBox<String> ratingsComboBox;
    private javax.swing.JButton returnButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JButton sellButton;
    private javax.swing.JTextField sellingPriceField;
    private javax.swing.JLabel sellingPriceLabel;
    private javax.swing.JLabel soldBooksLabel;
    private javax.swing.JTable soldBooksTable;
    private javax.swing.JLabel starsLabel;
    private javax.swing.JLabel starsLabel1;
    private javax.swing.JTextField stockField;
    private javax.swing.JButton submitRatingsButton;
    private javax.swing.JTextField titleOfTheBookField;
    private javax.swing.JLabel titleOfTheBookLabel;
    private javax.swing.JButton updateButton;
    private javax.swing.JScrollPane userActivityDashboardScrollPane;
    private javax.swing.JButton userBorrowABookButton;
    private javax.swing.JPanel userBorrowABookPanel;
    private javax.swing.JLabel userCompanyLogo;
    private javax.swing.JPanel userContent;
    private javax.swing.JButton userLogoutButton;
    private javax.swing.JButton userProfileButton;
    private javax.swing.JPanel userProfilePanel;
    private javax.swing.JButton userRateABookButton;
    private javax.swing.JPanel userReviewABookPanel;
    private javax.swing.JButton userSellABookButton;
    private javax.swing.JPanel userSellABookPanel;
    // End of variables declaration//GEN-END:variables
}
