package ui;

import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class LoginPanel extends JPanel {
    private MainFrame mainFrame;
    private DataRepository repository;

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JLabel lblError;

    public LoginPanel(MainFrame mainFrame, DataRepository repository) {
        this.mainFrame = mainFrame;
        this.repository = repository;

        setLayout(new GridBagLayout());

        // Center card container
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(MainFrame.COLOR_CARD_BG);
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(40, 40, 40, 40)
        ));
        cardPanel.setPreferredSize(new Dimension(380, 420));
        cardPanel.setMaximumSize(new Dimension(380, 420));

        // Header Title
        JLabel lblTitle = new JLabel("Welcome Back");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblTitle);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 8)));

        // Header Subtitle
        JLabel lblSub = new JLabel("Log in to access your payroll portal");
        lblSub.setFont(MainFrame.FONT_BODY);
        lblSub.setForeground(MainFrame.COLOR_TEXT_MUTED);
        lblSub.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(lblSub);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Username Label & Textfield
        JLabel lblUser = new JLabel("Username");
        lblUser.setFont(MainFrame.FONT_BODY_BOLD);
        lblUser.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblUser.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(lblUser);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        txtUsername = new JTextField(20);
        txtUsername.setFont(MainFrame.FONT_BODY);
        txtUsername.setMaximumSize(new Dimension(300, 35));
        txtUsername.setPreferredSize(new Dimension(300, 35));
        txtUsername.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(txtUsername);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Password Label & Textfield
        JLabel lblPass = new JLabel("Password");
        lblPass.setFont(MainFrame.FONT_BODY_BOLD);
        lblPass.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(lblPass);
        cardPanel.add(Box.createRigidArea(new Dimension(0, 6)));

        txtPassword = new JPasswordField(20);
        txtPassword.setFont(MainFrame.FONT_BODY);
        txtPassword.setMaximumSize(new Dimension(300, 35));
        txtPassword.setPreferredSize(new Dimension(300, 35));
        txtPassword.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(txtPassword);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Error message label
        lblError = new JLabel(" ");
        lblError.setFont(MainFrame.FONT_SMALL);
        lblError.setForeground(MainFrame.COLOR_DANGER);
        lblError.setAlignmentX(Component.LEFT_ALIGNMENT);
        cardPanel.add(lblError);

        cardPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Submit button
        JButton btnLogin = MainFrame.createStyledButton("Log In", MainFrame.COLOR_PRIMARY, MainFrame.COLOR_TEXT_LIGHT);
        btnLogin.setMaximumSize(new Dimension(300, 40));
        btnLogin.setPreferredSize(new Dimension(300, 40));
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);
        cardPanel.add(btnLogin);

        // Add action listeners
        btnLogin.addActionListener(e -> attemptLogin());

        // Press Enter to submit
        KeyAdapter enterKeyAdapter = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    attemptLogin();
                }
            }
        };
        txtUsername.addKeyListener(enterKeyAdapter);
        txtPassword.addKeyListener(enterKeyAdapter);

        // Center card inside parent
        add(cardPanel, new GridBagConstraints());
    }

    private void attemptLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Please fill out all fields.");
            return;
        }

        if (repository.authenticate(username, password)) {
            lblError.setText(" ");
            txtUsername.setText("");
            txtPassword.setText("");
            mainFrame.showMainApp();
        } else {
            lblError.setText("Invalid username or password.");
        }
    }

    // Paint beautiful dark slate gradient background
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        int w = getWidth();
        int h = getHeight();
        Color color1 = new Color(15, 23, 42); // slate-900
        Color color2 = new Color(30, 41, 59); // slate-800
        GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, w, h);
    }
}
