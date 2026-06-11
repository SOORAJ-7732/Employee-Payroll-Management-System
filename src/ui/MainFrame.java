package ui;

import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MainFrame extends JFrame {
    // Theme Colors
    public static final Color COLOR_PRIMARY = new Color(59, 130, 246);      // #3b82f6 (Bright Blue)
    public static final Color COLOR_PRIMARY_HOVER = new Color(37, 99, 235); // #2563eb (Darker Blue)
    public static final Color COLOR_SIDEBAR = new Color(30, 41, 59);       // #1e293b (Dark Slate)
    public static final Color COLOR_SIDEBAR_ACTIVE = new Color(51, 65, 85); // #334155 (Lighter Slate)
    public static final Color COLOR_BACKGROUND = new Color(248, 250, 252);  // #f8fafc (Very Light Gray)
    public static final Color COLOR_CARD_BG = Color.WHITE;
    public static final Color COLOR_BORDER = new Color(226, 232, 240);      // #e2e8f0 (Soft Gray)
    public static final Color COLOR_TEXT_DARK = new Color(15, 23, 42);      // #0f172a (Almost Black)
    public static final Color COLOR_TEXT_MUTED = new Color(100, 116, 139);  // #64748b (Medium Gray)
    public static final Color COLOR_TEXT_LIGHT = new Color(248, 250, 252);  // #f8fafc (Off-white)
    public static final Color COLOR_SUCCESS = new Color(16, 185, 129);      // #10b981 (Green)
    public static final Color COLOR_DANGER = new Color(239, 68, 68);        // #ef4444 (Red)

    // Standard Fonts
    public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    public static final Font FONT_SUBTITLE = new Font("Segoe UI", Font.BOLD, 16);
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 14);
    public static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 13);
    public static final Font FONT_BODY_BOLD = new Font("Segoe UI", Font.BOLD, 13);
    public static final Font FONT_SMALL = new Font("Segoe UI", Font.PLAIN, 11);

    private DataRepository repository;
    private CardLayout rootCardLayout;
    private JPanel rootCardPanel;

    // Inner Navigation Panel (displayed after login)
    private JPanel mainAppPanel;
    private CardLayout contentCardLayout;
    private JPanel contentCardPanel;

    // Sidebar navigation buttons
    private JButton btnDashboard;
    private JButton btnEmployees;
    private JButton btnSalary;
    private JButton btnLogout;

    // View panels
    private DashboardPanel dashboardPanel;
    private EmployeePanel employeePanel;
    private SalaryPanel salaryPanel;
    private PayslipPanel payslipPanel;

    public MainFrame() {
        this.repository = new DataRepository();
        
        setTitle("Employee Payroll Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 700);
        setMinimumSize(new Dimension(950, 600));
        setLocationRelativeTo(null); // Center on screen

        rootCardLayout = new CardLayout();
        rootCardPanel = new JPanel(rootCardLayout);

        // 1. Login Panel
        LoginPanel loginPanel = new LoginPanel(this, repository);
        rootCardPanel.add(loginPanel, "LOGIN");

        // 2. Main App Panel (Sidebar + Content CardLayout)
        buildMainAppPanel();
        rootCardPanel.add(mainAppPanel, "MAIN_APP");

        add(rootCardPanel);

        // Start at Login Screen
        showLogin();
    }

    private void buildMainAppPanel() {
        mainAppPanel = new JPanel(new BorderLayout());
        mainAppPanel.setBackground(COLOR_BACKGROUND);

        // Sidebar Panel
        JPanel sidebar = new JPanel();
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(220, 0));
        sidebar.setBackground(COLOR_SIDEBAR);
        sidebar.setBorder(new EmptyBorder(20, 15, 20, 15));

        // Sidebar Title
        JLabel lblLogo = new JLabel("PAYROLL PRO");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblLogo.setForeground(COLOR_TEXT_LIGHT);
        lblLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblLogo);

        JLabel lblSubLogo = new JLabel("Admin Portal");
        lblSubLogo.setFont(FONT_SMALL);
        lblSubLogo.setForeground(COLOR_TEXT_MUTED);
        lblSubLogo.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(lblSubLogo);

        sidebar.add(Box.createRigidArea(new Dimension(0, 40)));

        // Navigation Buttons
        btnDashboard = createSidebarButton("Dashboard", "DASHBOARD");
        btnEmployees = createSidebarButton("Manage Employees", "EMPLOYEES");
        btnSalary = createSidebarButton("Calculate Salary", "SALARY");
        btnLogout = createSidebarButton("Log Out", "LOGOUT");

        sidebar.add(btnDashboard);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnEmployees);
        sidebar.add(Box.createRigidArea(new Dimension(0, 10)));
        sidebar.add(btnSalary);
        
        // Push logout to the bottom
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(btnLogout);

        mainAppPanel.add(sidebar, BorderLayout.WEST);

        // Main Content Card Panel
        contentCardLayout = new CardLayout();
        contentCardPanel = new JPanel(contentCardLayout);
        contentCardPanel.setBackground(COLOR_BACKGROUND);
        contentCardPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Instantiate content panels
        dashboardPanel = new DashboardPanel(this, repository);
        employeePanel = new EmployeePanel(this, repository);
        salaryPanel = new SalaryPanel(this, repository);
        payslipPanel = new PayslipPanel(this, repository);

        contentCardPanel.add(dashboardPanel, "DASHBOARD");
        contentCardPanel.add(employeePanel, "EMPLOYEES");
        contentCardPanel.add(salaryPanel, "SALARY");
        contentCardPanel.add(payslipPanel, "PAYSLIP");

        mainAppPanel.add(contentCardPanel, BorderLayout.CENTER);
    }

    private JButton createSidebarButton(String text, String cardName) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_HEADER);
        btn.setForeground(COLOR_TEXT_LIGHT);
        btn.setBackground(COLOR_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setContentAreaFilled(false);
        btn.setOpaque(true);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(200, 40));
        btn.setPreferredSize(new Dimension(200, 40));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setBorder(new EmptyBorder(0, 15, 0, 15));

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (!btn.getBackground().equals(COLOR_SIDEBAR_ACTIVE)) {
                    btn.setBackground(new Color(45, 55, 72));
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                if (!btn.getBackground().equals(COLOR_SIDEBAR_ACTIVE)) {
                    btn.setBackground(COLOR_SIDEBAR);
                }
            }
        });

        btn.addActionListener(e -> {
            if (cardName.equals("LOGOUT")) {
                int confirm = JOptionPane.showConfirmDialog(MainFrame.this,
                        "Are you sure you want to log out?", "Confirm Logout",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (confirm == JOptionPane.YES_OPTION) {
                    logout();
                }
            } else {
                showContentPanel(cardName);
            }
        });

        return btn;
    }

    public void showLogin() {
        rootCardLayout.show(rootCardPanel, "LOGIN");
    }

    public void showMainApp() {
        // Refresh dashboard statistics when switching to the main app
        dashboardPanel.refreshStats();
        employeePanel.refreshTable();
        salaryPanel.refreshEmployeeList();
        
        rootCardLayout.show(rootCardPanel, "MAIN_APP");
        showContentPanel("DASHBOARD");
    }

    public void logout() {
        showLogin();
    }

    public void showContentPanel(String cardName) {
        // Highlight active sidebar button
        btnDashboard.setBackground(cardName.equals("DASHBOARD") ? COLOR_SIDEBAR_ACTIVE : COLOR_SIDEBAR);
        btnEmployees.setBackground(cardName.equals("EMPLOYEES") ? COLOR_SIDEBAR_ACTIVE : COLOR_SIDEBAR);
        btnSalary.setBackground(cardName.equals("SALARY") || cardName.equals("PAYSLIP") ? COLOR_SIDEBAR_ACTIVE : COLOR_SIDEBAR);

        // Refresh panel data if needed
        if (cardName.equals("DASHBOARD")) {
            dashboardPanel.refreshStats();
        } else if (cardName.equals("EMPLOYEES")) {
            employeePanel.refreshTable();
        } else if (cardName.equals("SALARY")) {
            salaryPanel.refreshEmployeeList();
        }

        contentCardLayout.show(contentCardPanel, cardName);
    }

    public void displayPayslip(model.Payslip payslip) {
        payslipPanel.setPayslip(payslip);
        showContentPanel("PAYSLIP");
    }

    // Modern styled button helper for child panels
    public static JButton createStyledButton(String text, Color bg, Color textCol) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BODY_BOLD);
        btn.setBackground(bg);
        btn.setForeground(textCol);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setOpaque(true);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(bg, 1),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));

        // Hover animation/effect
        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(bg.darker());
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(bg);
            }
        });

        return btn;
    }
}
