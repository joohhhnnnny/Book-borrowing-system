package joption;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author My PC
 */
public final class AdminDashboard extends javax.swing.JFrame {
     private DefaultTableModel bookTableModel; 
    private ArrayList<String[]> bookList;
    private Connection con;
    int totalAvailableBooks;
    int totalUser;
    int totalBorrowedBooks;
    /**
     * Creates new form AdminDashboard
     */
    public AdminDashboard() {
    initComponents();
    setSize(800, 640);
    setLocationRelativeTo(null);
    bookList = new ArrayList<>();
    
    ImageIcon icon = new ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\JLibraryLogo.png");
setIconImage(icon.getImage());


    // Set up the table model with non-editable behavior
    bookTableModel = new DefaultTableModel(new Object[]{"Title", "Author", "Genre", "Stock"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false; // Make all cells non-editable
        }
    };

    // Apply the custom model to the JTable
    addBooksTable.setModel(bookTableModel);

    loadBooksData();
    loadUserData();
    loadHistoryData();
    
     // Get total available books and set it in the text field (e.g., totalAvailableBooksField)
    totalAvailableBooks = getTotalAvailableBooks();
    totalAvailableBooksField.setText(String.valueOf(totalAvailableBooks));
    
    totalUser = getUserCount();
    totalUsersField.setText(String.valueOf(totalUser));
    
    totalBorrowedBooks = getTotalBorrowedBooks();
    totalBorrowedBooksField.setText(String.valueOf(totalBorrowedBooks));
    
    // Center the text in the totalAvailableBooksField
    totalAvailableBooksField.setHorizontalAlignment(JTextField.CENTER);
    totalUsersField.setHorizontalAlignment(JTextField.CENTER);
    totalBorrowedBooksField.setHorizontalAlignment(JTextField.CENTER);
    
}
    
    public void loadDataAfterInit() {
    loadHistoryData();  // Now that the object is fully initialized
}

    
   public void Connect() {
    try {
        Class.forName("com.mysql.cj.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://localhost:3306/joption", "root", "");

        System.out.println("Connection established: " + con);  // Add this to confirm the connection

    } catch (ClassNotFoundException ex) {
        Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
    } catch (SQLException ex) {
        Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
    }
}

    
    public final void loadBooksData() {
    System.out.println("Checking connection state in loadData: " + con);
    String query = "SELECT title, author, genre, stock FROM books"; // Select only the desired columns

    try {
        Connect(); // Ensure the connection is established

        // Execute the query
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Prepare the table model
            DefaultTableModel model = (DefaultTableModel) addBooksTable.getModel();
            model.setRowCount(0); // Clear existing data

            // Populate the table model with data
            while (rs.next()) {
                Object[] rowData = {
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("stock")
                };
                model.addRow(rowData);
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
    }
}
    
    public final void loadUserData() {
    System.out.println("Checking connection state in loadData: " + con);
    String query = "SELECT userID, name, email FROM user"; // Select only the desired columns

    try {
        Connect(); // Ensure the connection is established

        // Execute the query
        try (Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Prepare the table model
            DefaultTableModel model = (DefaultTableModel) usersTable.getModel();
            model.setRowCount(0); // Clear existing data

            // Populate the table model with data
            while (rs.next()) {
                Object[] rowData = {
                    rs.getInt("userID"),
                    rs.getString("name"),
                    rs.getString("email")
                };
                model.addRow(rowData);
            }
        }

    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error loading data: " + ex.getMessage());
    }
}
    
    
    private int getTotalAvailableBooks() {
    String query = "SELECT COUNT(*) AS totalAvailable FROM books WHERE stock > 0"; // Assuming 'stock' is the column indicating availability
    int totalAvailableBooks = 0;

    try {
        Connect(); // Ensure you have a valid connection

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve the total number of available books from the query result
                totalAvailableBooks = rs.getInt("totalAvailable");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error fetching available books: " + ex.getMessage());
    }

    return totalAvailableBooks;
}

    private int getUserCount() {
    String query = "SELECT COUNT(*) AS userCount FROM user"; // Assuming 'user' is the table and it has a column like 'id' or 'username'
    int userCount = 0;

    try {
        Connect(); // Ensure you have a valid connection

        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve the total number of users from the query result
                userCount = rs.getInt("userCount");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error fetching user count: " + ex.getMessage());
    }

    return userCount;
}
    
    private int getTotalBorrowedBooks() {
    String query = "SELECT COUNT(*) AS borrowedCount FROM bookborrow";
    int borrowedCount = 0;

    try {
        Connect(); // Ensure this properly initializes the 'con' variable

        try (PreparedStatement pstmt = con.prepareStatement(query);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                // Retrieve the total number of borrowed books
                borrowedCount = rs.getInt("borrowedCount");
            }
        }
    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, 
            "Error fetching borrowed books count: " + ex.getMessage());
    }

    return borrowedCount;
}

    
      // Helper method to get bookID by title
private int getBookIDByTitle(String title) {
    String query = "SELECT booksID FROM books WHERE title = ?";  // Adjusted column name to 'bookID'
    try {
        Connect(); // Ensure the database connection is established
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.setString(1, title);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return rs.getInt("booksID");  // Adjusted column name to 'booksID'
            }
        }
    } catch (SQLException e) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, e);
    }
    return -1; // Return -1 if bookID is not found
}

public void loadHistoryData() {
    String query = "SELECT activity, userID, booksID, date FROM history";
    DefaultTableModel tableModel = (DefaultTableModel) borrowSellTable.getModel();
    tableModel.setRowCount(0);  // Clear any existing rows in the table

    Statement stmt = null;
    ResultSet rs = null;

    try {
        Connect();  // Ensure database connection is established

        // Use the instance-level 'con' variable, which is set in Connect()
        stmt = con.createStatement();
        System.out.println("Executing query: " + query);

        rs = stmt.executeQuery(query);

        while (rs.next()) {
            String activity = rs.getString("activity");
            int userID = rs.getInt("userID");
            int booksID = rs.getInt("booksID");
            Date date = rs.getDate("date");

            System.out.println("Activity: " + activity + ", UserID: " + userID + ", BooksID: " + booksID + ", Date: " + date);

            tableModel.addRow(new Object[]{activity, userID, booksID, date});
        }

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error loading History data from database: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    } finally {
        try {
            if (rs != null) rs.close();
            if (stmt != null) stmt.close();
            // Do not close 'con' here if it's a shared connection across methods
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
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

        menu = new javax.swing.JPanel();
        companyLogoLabel = new javax.swing.JLabel();
        greetingLabel = new javax.swing.JLabel();
        adminDashboardButton = new javax.swing.JButton();
        adminBooksButton = new javax.swing.JButton();
        adminUsersButton = new javax.swing.JButton();
        adminBorrowSellButton = new javax.swing.JButton();
        adminLogoutButton = new javax.swing.JButton();
        Content = new javax.swing.JPanel();
        Dashboard = new javax.swing.JPanel();
        totalAvailableBooksPanel = new javax.swing.JPanel();
        totalAvailableBooksLabel = new javax.swing.JLabel();
        totalAvailableBooksField = new javax.swing.JTextField();
        totalUsersPanel = new javax.swing.JPanel();
        totalUsersLabel = new javax.swing.JLabel();
        totalUsersField = new javax.swing.JTextField();
        totalBorrowedBooksPanel = new javax.swing.JPanel();
        totalBorrowedBooksLabel = new javax.swing.JLabel();
        totalBorrowedBooksField = new javax.swing.JTextField();
        Books = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        titleField = new javax.swing.JTextField();
        authorLabel = new javax.swing.JLabel();
        authorField = new javax.swing.JTextField();
        genreLabel = new javax.swing.JLabel();
        genreField = new javax.swing.JTextField();
        deleteBookButton = new javax.swing.JButton();
        updateBookButton = new javax.swing.JButton();
        addBooksScrollPane = new javax.swing.JScrollPane();
        addBooksTable = new javax.swing.JTable();
        addBookBtn = new javax.swing.JButton();
        stockLabel = new javax.swing.JLabel();
        stockField = new javax.swing.JTextField();
        searchLabel = new javax.swing.JLabel();
        searchField = new javax.swing.JTextField();
        Users = new javax.swing.JPanel();
        fullNameLabel = new javax.swing.JLabel();
        usersScrollPane = new javax.swing.JScrollPane();
        usersTable = new javax.swing.JTable();
        clearUserButton = new javax.swing.JButton();
        BorrowSell = new javax.swing.JPanel();
        borrowSellScrollPane = new javax.swing.JScrollPane();
        borrowSellTable = new javax.swing.JTable();
        clearRecordsButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("ADMIN MAIN FRAME");
        setMinimumSize(new java.awt.Dimension(800, 600));
        setResizable(false);
        setSize(new java.awt.Dimension(800, 600));
        getContentPane().setLayout(null);

        menu.setBackground(new java.awt.Color(111, 79, 55));

        companyLogoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        companyLogoLabel.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\companyLogoWhiteVersion.png")); // NOI18N
        companyLogoLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEADING);
        companyLogoLabel.setIconTextGap(0);

        greetingLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        greetingLabel.setForeground(new java.awt.Color(255, 255, 255));
        greetingLabel.setText("Hello, [user]!");

        adminDashboardButton.setBackground(new java.awt.Color(242, 242, 242));
        adminDashboardButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        adminDashboardButton.setForeground(new java.awt.Color(111, 79, 55));
        adminDashboardButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\dashboard.png")); // NOI18N
        adminDashboardButton.setText("DASHBOARD");
        adminDashboardButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminDashboardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminDashboardButtonActionPerformed(evt);
            }
        });

        adminBooksButton.setBackground(new java.awt.Color(242, 242, 242));
        adminBooksButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        adminBooksButton.setForeground(new java.awt.Color(111, 79, 55));
        adminBooksButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\bookManagementIcon.png")); // NOI18N
        adminBooksButton.setText("BOOKS");
        adminBooksButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminBooksButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminBooksButtonActionPerformed(evt);
            }
        });

        adminUsersButton.setBackground(new java.awt.Color(242, 242, 242));
        adminUsersButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        adminUsersButton.setForeground(new java.awt.Color(111, 79, 55));
        adminUsersButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\addUser.png")); // NOI18N
        adminUsersButton.setText("USERS");
        adminUsersButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminUsersButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminUsersButtonActionPerformed(evt);
            }
        });

        adminBorrowSellButton.setBackground(new java.awt.Color(242, 242, 242));
        adminBorrowSellButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        adminBorrowSellButton.setForeground(new java.awt.Color(111, 79, 55));
        adminBorrowSellButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\borrowBook.png")); // NOI18N
        adminBorrowSellButton.setText("BORROW/SELL");
        adminBorrowSellButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminBorrowSellButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminBorrowSellButtonActionPerformed(evt);
            }
        });

        adminLogoutButton.setBackground(new java.awt.Color(242, 242, 242));
        adminLogoutButton.setFont(new java.awt.Font("Bahnschrift", 1, 20)); // NOI18N
        adminLogoutButton.setForeground(new java.awt.Color(111, 79, 55));
        adminLogoutButton.setIcon(new javax.swing.ImageIcon("C:\\Users\\Administrator\\Documents\\NetBeansProjects\\BookBorrowingSystem\\src\\resources\\Images\\logout.png")); // NOI18N
        adminLogoutButton.setText("LOGOUT");
        adminLogoutButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        adminLogoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                adminLogoutButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout menuLayout = new javax.swing.GroupLayout(menu);
        menu.setLayout(menuLayout);
        menuLayout.setHorizontalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(adminUsersButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(adminDashboardButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuLayout.createSequentialGroup()
                .addGap(41, 41, 41)
                .addComponent(greetingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 156, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(adminBorrowSellButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
            .addComponent(adminLogoutButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(adminBooksButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(menuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(companyLogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );
        menuLayout.setVerticalGroup(
            menuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(menuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(companyLogoLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(greetingLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(adminDashboardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(adminBooksButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(adminUsersButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(adminBorrowSellButton, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66)
                .addComponent(adminLogoutButton, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(38, Short.MAX_VALUE))
        );

        getContentPane().add(menu);
        menu.setBounds(0, 0, 230, 600);

        Content.setBackground(new java.awt.Color(228, 197, 158));
        Content.setLayout(new java.awt.CardLayout());

        Dashboard.setBackground(new java.awt.Color(228, 197, 158));

        totalAvailableBooksPanel.setBackground(new java.awt.Color(222, 172, 128));

        totalAvailableBooksLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        totalAvailableBooksLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalAvailableBooksLabel.setText("Total Available Books");

        totalAvailableBooksField.setEditable(false);
        totalAvailableBooksField.setFont(new java.awt.Font("Bahnschrift", 1, 48)); // NOI18N
        totalAvailableBooksField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalAvailableBooksFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout totalAvailableBooksPanelLayout = new javax.swing.GroupLayout(totalAvailableBooksPanel);
        totalAvailableBooksPanel.setLayout(totalAvailableBooksPanelLayout);
        totalAvailableBooksPanelLayout.setHorizontalGroup(
            totalAvailableBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalAvailableBooksPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalAvailableBooksField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
            .addGroup(totalAvailableBooksPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(totalAvailableBooksLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        totalAvailableBooksPanelLayout.setVerticalGroup(
            totalAvailableBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalAvailableBooksPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(totalAvailableBooksLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalAvailableBooksField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        totalUsersPanel.setBackground(new java.awt.Color(222, 172, 128));

        totalUsersLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        totalUsersLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalUsersLabel.setText("Total Users");

        totalUsersField.setEditable(false);
        totalUsersField.setFont(new java.awt.Font("Bahnschrift", 1, 48)); // NOI18N

        javax.swing.GroupLayout totalUsersPanelLayout = new javax.swing.GroupLayout(totalUsersPanel);
        totalUsersPanel.setLayout(totalUsersPanelLayout);
        totalUsersPanelLayout.setHorizontalGroup(
            totalUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalUsersPanelLayout.createSequentialGroup()
                .addContainerGap(72, Short.MAX_VALUE)
                .addGroup(totalUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalUsersPanelLayout.createSequentialGroup()
                        .addComponent(totalUsersField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(69, 69, 69))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalUsersPanelLayout.createSequentialGroup()
                        .addComponent(totalUsersLabel)
                        .addGap(78, 78, 78))))
        );
        totalUsersPanelLayout.setVerticalGroup(
            totalUsersPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalUsersPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(totalUsersLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalUsersField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        totalBorrowedBooksPanel.setBackground(new java.awt.Color(222, 172, 128));

        totalBorrowedBooksLabel.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        totalBorrowedBooksLabel.setForeground(new java.awt.Color(255, 255, 255));
        totalBorrowedBooksLabel.setText("Total Borrowed Books");

        totalBorrowedBooksField.setEditable(false);
        totalBorrowedBooksField.setFont(new java.awt.Font("Bahnschrift", 1, 48)); // NOI18N
        totalBorrowedBooksField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                totalBorrowedBooksFieldActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout totalBorrowedBooksPanelLayout = new javax.swing.GroupLayout(totalBorrowedBooksPanel);
        totalBorrowedBooksPanel.setLayout(totalBorrowedBooksPanelLayout);
        totalBorrowedBooksPanelLayout.setHorizontalGroup(
            totalBorrowedBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, totalBorrowedBooksPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(totalBorrowedBooksField, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(69, 69, 69))
            .addGroup(totalBorrowedBooksPanelLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(totalBorrowedBooksLabel)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        totalBorrowedBooksPanelLayout.setVerticalGroup(
            totalBorrowedBooksPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(totalBorrowedBooksPanelLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(totalBorrowedBooksLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(totalBorrowedBooksField, javax.swing.GroupLayout.PREFERRED_SIZE, 62, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(40, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout DashboardLayout = new javax.swing.GroupLayout(Dashboard);
        Dashboard.setLayout(DashboardLayout);
        DashboardLayout.setHorizontalGroup(
            DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardLayout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addGroup(DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(totalBorrowedBooksPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalAvailableBooksPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(totalUsersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(130, Short.MAX_VALUE))
        );
        DashboardLayout.setVerticalGroup(
            DashboardLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DashboardLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(totalAvailableBooksPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(totalUsersPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(totalBorrowedBooksPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(39, Short.MAX_VALUE))
        );

        Content.add(Dashboard, "card2");

        Books.setBackground(new java.awt.Color(222, 172, 128));
        Books.setPreferredSize(new java.awt.Dimension(570, 600));
        Books.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        titleLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        titleLabel.setForeground(new java.awt.Color(255, 255, 255));
        titleLabel.setText("Title:");
        Books.add(titleLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 20, 83, -1));

        titleField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Books.add(titleField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 10, 401, 40));

        authorLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        authorLabel.setForeground(new java.awt.Color(255, 255, 255));
        authorLabel.setText("Author:");
        Books.add(authorLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 70, 90, -1));

        authorField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Books.add(authorField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 60, 401, 40));

        genreLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        genreLabel.setForeground(new java.awt.Color(255, 255, 255));
        genreLabel.setText("Genre:");
        Books.add(genreLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 120, -1, -1));

        genreField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        genreField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                genreFieldActionPerformed(evt);
            }
        });
        Books.add(genreField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 110, 401, 40));

        deleteBookButton.setBackground(new java.awt.Color(242, 242, 242));
        deleteBookButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        deleteBookButton.setForeground(new java.awt.Color(111, 79, 55));
        deleteBookButton.setText("DELETE");
        deleteBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteBookButtonActionPerformed(evt);
            }
        });
        Books.add(deleteBookButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 230, 150, 46));

        updateBookButton.setBackground(new java.awt.Color(242, 242, 242));
        updateBookButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        updateBookButton.setForeground(new java.awt.Color(111, 79, 55));
        updateBookButton.setText("UPDATE");
        updateBookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateBookButtonActionPerformed(evt);
            }
        });
        Books.add(updateBookButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 230, 144, 46));

        addBooksTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        addBooksTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "TITLE", "AUTHOR", "GENRE", "STOCK"
            }
        ));
        addBooksScrollPane.setViewportView(addBooksTable);

        Books.add(addBooksScrollPane, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 290, 514, 247));

        addBookBtn.setBackground(new java.awt.Color(242, 242, 242));
        addBookBtn.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        addBookBtn.setForeground(new java.awt.Color(111, 79, 55));
        addBookBtn.setText("ADD");
        addBookBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addBookBtnActionPerformed(evt);
            }
        });
        Books.add(addBookBtn, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 230, 144, 46));

        stockLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        stockLabel.setForeground(new java.awt.Color(255, 255, 255));
        stockLabel.setText("Stock:");
        Books.add(stockLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 170, -1, -1));

        stockField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        Books.add(stockField, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 160, 401, 40));

        searchLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        searchLabel.setForeground(new java.awt.Color(255, 255, 255));
        searchLabel.setText("Search:");
        Books.add(searchLabel, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 560, -1, -1));

        searchField.setFont(new java.awt.Font("Bahnschrift", 0, 18)); // NOI18N
        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });
        Books.add(searchField, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 550, 290, 40));

        Content.add(Books, "card3");

        Users.setBackground(new java.awt.Color(222, 172, 128));

        fullNameLabel.setFont(new java.awt.Font("Bahnschrift", 1, 18)); // NOI18N
        fullNameLabel.setForeground(new java.awt.Color(255, 255, 255));
        fullNameLabel.setText("Users:");

        usersTable.setFont(new java.awt.Font("Bahnschrift", 0, 12)); // NOI18N
        usersTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "NAME", "EMAIL"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        usersScrollPane.setViewportView(usersTable);

        clearUserButton.setBackground(new java.awt.Color(242, 242, 242));
        clearUserButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        clearUserButton.setForeground(new java.awt.Color(111, 79, 55));
        clearUserButton.setText("Clear Records");
        clearUserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearUserButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout UsersLayout = new javax.swing.GroupLayout(Users);
        Users.setLayout(UsersLayout);
        UsersLayout.setHorizontalGroup(
            UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(clearUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(fullNameLabel)
                        .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 529, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );
        UsersLayout.setVerticalGroup(
            UsersLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(UsersLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(fullNameLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(usersScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 428, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(55, Short.MAX_VALUE))
        );

        Content.add(Users, "card4");

        BorrowSell.setBackground(new java.awt.Color(222, 172, 128));

        borrowSellTable.setFont(new java.awt.Font("Bahnschrift", 0, 14)); // NOI18N
        borrowSellTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ACTIVITY", "USER ID", "BOOK ID", "DATE"
            }
        ));
        borrowSellScrollPane.setViewportView(borrowSellTable);

        clearRecordsButton.setBackground(new java.awt.Color(242, 242, 242));
        clearRecordsButton.setFont(new java.awt.Font("Bahnschrift", 1, 24)); // NOI18N
        clearRecordsButton.setForeground(new java.awt.Color(111, 79, 55));
        clearRecordsButton.setText("Clear Records");
        clearRecordsButton.setFocusable(false);
        clearRecordsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearRecordsButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout BorrowSellLayout = new javax.swing.GroupLayout(BorrowSell);
        BorrowSell.setLayout(BorrowSellLayout);
        BorrowSellLayout.setHorizontalGroup(
            BorrowSellLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, BorrowSellLayout.createSequentialGroup()
                .addContainerGap(23, Short.MAX_VALUE)
                .addGroup(BorrowSellLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(borrowSellScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 525, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(clearRecordsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(22, 22, 22))
        );
        BorrowSellLayout.setVerticalGroup(
            BorrowSellLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(BorrowSellLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addComponent(borrowSellScrollPane, javax.swing.GroupLayout.PREFERRED_SIZE, 460, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(clearRecordsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(53, Short.MAX_VALUE))
        );

        Content.add(BorrowSell, "card5");

        getContentPane().add(Content);
        Content.setBounds(230, 0, 570, 600);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void adminDashboardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminDashboardButtonActionPerformed
        Content.removeAll();
        Content.add(Dashboard);
        Content.repaint();
        Content.revalidate();
    }//GEN-LAST:event_adminDashboardButtonActionPerformed

    private void adminUsersButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminUsersButtonActionPerformed
        Content.removeAll();
        Content.add(Users);
        Content.repaint();
        Content.revalidate();
    }//GEN-LAST:event_adminUsersButtonActionPerformed

    private void adminBooksButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminBooksButtonActionPerformed
        Content.removeAll();
        Content.add(Books);
        Content.repaint();
        Content.revalidate();
    }//GEN-LAST:event_adminBooksButtonActionPerformed

    private void adminBorrowSellButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminBorrowSellButtonActionPerformed
        Content.removeAll();
        Content.add(BorrowSell);
        Content.repaint();
        Content.revalidate();
    }//GEN-LAST:event_adminBorrowSellButtonActionPerformed

    private void adminLogoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_adminLogoutButtonActionPerformed
        int response = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Confirm", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (response == JOptionPane.YES_OPTION) {
        loginAdmin loginAdmin  = new loginAdmin ();
        loginAdmin .setVisible(true);
        this.dispose();
    }
    }//GEN-LAST:event_adminLogoutButtonActionPerformed

    private void updateBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateBookButtonActionPerformed
        // Get the selected row index
    int selectedRow = addBooksTable.getSelectedRow();

    if (selectedRow == -1) {
        // No row is selected, show a warning
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a book to update.");
        return;
    }

    // Get the visible values (not the bookID) from the selected row
    String currentTitle = (String) addBooksTable.getValueAt(selectedRow, 0); // Title column
    String currentAuthor = (String) addBooksTable.getValueAt(selectedRow, 1); // Author column
    String currentGenre = (String) addBooksTable.getValueAt(selectedRow, 2); // Genre column
    int currentStock = (int) addBooksTable.getValueAt(selectedRow, 3); // Stock column

    // You can use the title (or any other unique field) to fetch the bookID
    int bookID = getBookIDByTitle(currentTitle); // Assume this method queries the database

    if (bookID == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Book not found.");
        return;
    }

    // Show a dialog for updating book details
    JTextField titleField = new JTextField(currentTitle);
    JTextField authorField = new JTextField(currentAuthor);
    JTextField genreField = new JTextField(currentGenre);
    JTextField stockField = new JTextField(String.valueOf(currentStock));

    Object[] updateForm = {
        "Title:", titleField,
        "Author:", authorField,
        "Genre:", genreField,
        "Stock:", stockField
    };

    int result = javax.swing.JOptionPane.showConfirmDialog(this, updateForm, "Update Book", javax.swing.JOptionPane.OK_CANCEL_OPTION);

    if (result == javax.swing.JOptionPane.OK_OPTION) {
        // Retrieve updated values
        String updatedTitle = titleField.getText().trim();
        String updatedAuthor = authorField.getText().trim();
        String updatedGenre = genreField.getText().trim();
        int updatedStock;

        try {
            updatedStock = Integer.parseInt(stockField.getText().trim());
        } catch (NumberFormatException e) {
            javax.swing.JOptionPane.showMessageDialog(this, "Invalid stock value. Please enter a valid number.");
            return;
        }

        // Execute the UPDATE query
        String query = "UPDATE books SET title = ?, author = ?, genre = ?, stock = ? WHERE booksID = ?";
        try {
            Connect(); // Ensure the database connection is established
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setString(1, updatedTitle);
                pstmt.setString(2, updatedAuthor);
                pstmt.setString(3, updatedGenre);
                pstmt.setInt(4, updatedStock);
                pstmt.setInt(5, bookID); // Use the bookID retrieved based on the title

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Book updated successfully.");
                    System.out.println("Calling loadBooksData() after update...");
                    loadBooksData();
                    System.out.println("loadBooksData() executed.");

                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No book was updated. Please try again.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Error updating book: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_updateBookButtonActionPerformed

    private void addBookBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addBookBtnActionPerformed
        double ratings = 0;
        
        String title = titleField.getText().trim(); 
        String author = authorField.getText().trim(); 
        String genre = genreField.getText().trim();
        String stock = stockField.getText().trim();

        if (title.isEmpty() || author.isEmpty() || genre.isEmpty() || stock.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return; 
        }
        
        // Save to database
    Connect(); // Ensure database connection
    try {
        String query = "INSERT INTO books (title, author, genre, stock, ratings) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement pst = con.prepareStatement(query);
        pst.setString(1, title);
        pst.setString(2, author);
        pst.setString(3, genre);
        pst.setString(4, stock); // Storing plain text passwords is not secure; consider hashing.
        pst.setDouble(5, ratings);

        int rowCount = pst.executeUpdate();
        if (rowCount > 0) {
            JOptionPane.showMessageDialog(this, "Book Added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);

            String[] bookEntry = {title, author, genre, stock};
            bookList.add(bookEntry);
            bookTableModel.addRow(bookEntry);

            BookManager.getInstance().addBook(title);

            loadBooksData();

            // Update the total available books count
            totalAvailableBooks = getTotalAvailableBooks();
            totalAvailableBooksField.setText(String.valueOf(totalAvailableBooks));
            titleField.setText("");
            authorField.setText("");
            genreField.setText("");
            stockField.setText("");
            getTotalAvailableBooks();

        } else {
            JOptionPane.showMessageDialog(this, "Failed to add book.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        Logger.getLogger(signupUser.class.getName()).log(Level.SEVERE, null, ex);
    }
    }//GEN-LAST:event_addBookBtnActionPerformed

    private void deleteBookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteBookButtonActionPerformed
        // Fetch the selected row
    int selectedRow = addBooksTable.getSelectedRow();

    if (selectedRow == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Please select a book to delete.");
        return;
    }

    // Get the book title from the selected row (assuming book title is in the first column)
    Object titleObject = addBooksTable.getValueAt(selectedRow, 0); // Column 0 for book title in the table
    if (titleObject == null) {
        javax.swing.JOptionPane.showMessageDialog(this, "Book title not found for the selected row.");
        return;
    }

    String title = titleObject.toString();

    // Query the database to get the bookID by title
    int bookID = getBookIDByTitle(title); // Implement this method to fetch the book ID by title
    if (bookID == -1) {
        javax.swing.JOptionPane.showMessageDialog(this, "Book ID not found for the selected book title.");
        return;
    }

    // Confirm deletion
    int confirmation = javax.swing.JOptionPane.showConfirmDialog(this,
        "Are you sure you want to delete the book with title: '" + title + "'?",
        "Confirm Deletion",
        javax.swing.JOptionPane.YES_NO_OPTION);

    if (confirmation == javax.swing.JOptionPane.YES_OPTION) {
        // Perform the delete query
        String query = "DELETE FROM books WHERE booksID = ?";
        try {
            Connect(); // Ensure the database connection is established
            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                pstmt.setInt(1, bookID); // Set the book ID in the query
                int rowsAffected = pstmt.executeUpdate(); // Execute the delete query

                if (rowsAffected > 0) {
                    javax.swing.JOptionPane.showMessageDialog(this, "Book deleted successfully.");
                    loadBooksData(); // Refresh the table data
                } else {
                    javax.swing.JOptionPane.showMessageDialog(this, "No book was deleted. Please try again.");
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Error deleting book: " + ex.getMessage());
        }
    }
    }//GEN-LAST:event_deleteBookButtonActionPerformed


    private void clearRecordsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearRecordsButtonActionPerformed
        // Get the table model of borrowSellTable
    DefaultTableModel bst = (DefaultTableModel) borrowSellTable.getModel();

    // Check if the table is empty
    if (bst.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "The history table is already empty.");
        return; // Exit the method without performing the clear action
    }

    String query = "TRUNCATE TABLE history";

    try {
        Connect(); // Ensure this method properly sets up the connection
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.executeUpdate();

            // Clear the rows in the table model
            bst.setRowCount(0);
        }
        javax.swing.JOptionPane.showMessageDialog(this, "History cleared successfully.");
    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error clearing history: " + ex.getMessage());
    }
    }//GEN-LAST:event_clearRecordsButtonActionPerformed

    private void genreFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_genreFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_genreFieldActionPerformed

    private void totalAvailableBooksFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalAvailableBooksFieldActionPerformed
        // Query to get the total number of available books
        String query = "SELECT COUNT(*) AS totalAvailable FROM books WHERE stock > 0"; // Assuming 'stock' is the column indicating availability

        try {
            Connect(); // Ensure you have a valid connection

            try (PreparedStatement pstmt = con.prepareStatement(query)) {
                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    // Retrieve the total number of available books from the query result
                    int totalAvailableBooks = rs.getInt("totalAvailable");

                    // Debug: print the value fetched
                    System.out.println("Total Available Books: " + totalAvailableBooks);

                    // Display the total available books in the totalAvailableBooksField
                    totalAvailableBooksField.setText(String.valueOf(totalAvailableBooks));
                } else {
                    // Debug: Print if no rows were found
                    System.out.println("No data found for available books.");
                }

            }
        } catch (SQLException ex) {
            Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
            javax.swing.JOptionPane.showMessageDialog(this, "Error fetching available books: " + ex.getMessage());
        }
    }//GEN-LAST:event_totalAvailableBooksFieldActionPerformed

    private void clearUserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearUserButtonActionPerformed
        // Get the table model of usersTable
    DefaultTableModel bst = (DefaultTableModel) usersTable.getModel();

    // Check if the table is empty
    if (bst.getRowCount() == 0) {
        javax.swing.JOptionPane.showMessageDialog(this, "The user table is already empty.");
        return; // Exit the method without performing the clear action
    }

    String query = "TRUNCATE TABLE user";

    try {
        Connect(); // Ensure this method properly sets up the connection
        try (PreparedStatement pstmt = con.prepareStatement(query)) {
            pstmt.executeUpdate();

            // Clear the rows in the table model
            bst.setRowCount(0);
        }
        javax.swing.JOptionPane.showMessageDialog(this, "Users cleared successfully.");
    } catch (SQLException ex) {
        Logger.getLogger(AdminDashboard.class.getName()).log(Level.SEVERE, null, ex);
        javax.swing.JOptionPane.showMessageDialog(this, "Error clearing users: " + ex.getMessage());
    }
    }//GEN-LAST:event_clearUserButtonActionPerformed

    private void totalBorrowedBooksFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_totalBorrowedBooksFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_totalBorrowedBooksFieldActionPerformed

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
        DefaultTableModel model = (DefaultTableModel) addBooksTable.getModel();
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
                if ("Flatlaf".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AdminDashboard.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                AdminDashboard adminDashboard = new AdminDashboard();  // Create instance of AdminDashboard
                adminDashboard.setVisible(true);  // Show the UI
                adminDashboard.loadHistoryData();  // Call the method after the UI is initialized
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Books;
    private javax.swing.JPanel BorrowSell;
    private javax.swing.JPanel Content;
    private javax.swing.JPanel Dashboard;
    private javax.swing.JPanel Users;
    private javax.swing.JButton addBookBtn;
    private javax.swing.JScrollPane addBooksScrollPane;
    private javax.swing.JTable addBooksTable;
    private javax.swing.JButton adminBooksButton;
    private javax.swing.JButton adminBorrowSellButton;
    private javax.swing.JButton adminDashboardButton;
    private javax.swing.JButton adminLogoutButton;
    private javax.swing.JButton adminUsersButton;
    private javax.swing.JTextField authorField;
    private javax.swing.JLabel authorLabel;
    private javax.swing.JScrollPane borrowSellScrollPane;
    private javax.swing.JTable borrowSellTable;
    private javax.swing.JButton clearRecordsButton;
    private javax.swing.JButton clearUserButton;
    private javax.swing.JLabel companyLogoLabel;
    private javax.swing.JButton deleteBookButton;
    private javax.swing.JLabel fullNameLabel;
    private javax.swing.JTextField genreField;
    private javax.swing.JLabel genreLabel;
    private javax.swing.JLabel greetingLabel;
    private javax.swing.JPanel menu;
    private javax.swing.JTextField searchField;
    private javax.swing.JLabel searchLabel;
    private javax.swing.JTextField stockField;
    private javax.swing.JLabel stockLabel;
    private javax.swing.JTextField titleField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JTextField totalAvailableBooksField;
    private javax.swing.JLabel totalAvailableBooksLabel;
    private javax.swing.JPanel totalAvailableBooksPanel;
    private javax.swing.JTextField totalBorrowedBooksField;
    private javax.swing.JLabel totalBorrowedBooksLabel;
    private javax.swing.JPanel totalBorrowedBooksPanel;
    private javax.swing.JTextField totalUsersField;
    private javax.swing.JLabel totalUsersLabel;
    private javax.swing.JPanel totalUsersPanel;
    private javax.swing.JButton updateBookButton;
    private javax.swing.JScrollPane usersScrollPane;
    private javax.swing.JTable usersTable;
    // End of variables declaration//GEN-END:variables
}
