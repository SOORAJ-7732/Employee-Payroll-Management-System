package ui;

import model.Employee;
import model.Payslip;
import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class DashboardPanel extends JPanel {
    private MainFrame mainFrame;
    private DataRepository repository;

    private JLabel lblTotalEmployeesVal;
    private JLabel lblTotalPayrollVal;
    private JLabel lblPayslipsCountVal;

    private DefaultTableModel tableModel;
    private JTable tableRecentPayslips;

    public DashboardPanel(MainFrame mainFrame, DataRepository repository) {
        this.mainFrame = mainFrame;
        this.repository = repository;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_BACKGROUND);

        // --- Header Panel ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainFrame.COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        JLabel lblTitle = new JLabel("System Dashboard");
        lblTitle.setFont(MainFrame.FONT_TITLE);
        lblTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblDate = new JLabel("Welcome, Administrator");
        lblDate.setFont(MainFrame.FONT_SUBTITLE);
        lblDate.setForeground(MainFrame.COLOR_TEXT_MUTED);
        headerPanel.add(lblDate, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Center Panel (Stats cards + Recent activity table) ---
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(MainFrame.COLOR_BACKGROUND);

        // Stats Cards Row
        JPanel statsRow = new JPanel(new GridLayout(1, 3, 20, 0));
        statsRow.setBackground(MainFrame.COLOR_BACKGROUND);
        statsRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JPanel cardEmployees = createStatCard("TOTAL EMPLOYEES", "Active registered staff members");
        lblTotalEmployeesVal = (JLabel) cardEmployees.getClientProperty("valueLabel");
        
        JPanel cardPayroll = createStatCard("TOTAL PAYROLL PAID", "Cumulative net salary issued");
        lblTotalPayrollVal = (JLabel) cardPayroll.getClientProperty("valueLabel");

        JPanel cardPayslips = createStatCard("PAYSLIPS ISSUED", "Total payroll records generated");
        lblPayslipsCountVal = (JLabel) cardPayslips.getClientProperty("valueLabel");

        statsRow.add(cardEmployees);
        statsRow.add(cardPayroll);
        statsRow.add(cardPayslips);

        centerPanel.add(statsRow);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Recent Activity / Quick Actions Split Layout
        JPanel lowerSplitPanel = new JPanel(new BorderLayout(20, 0));
        lowerSplitPanel.setBackground(MainFrame.COLOR_BACKGROUND);

        // Recent Payslips (Left side of lower panel)
        JPanel recentPanel = new JPanel(new BorderLayout());
        recentPanel.setBackground(MainFrame.COLOR_CARD_BG);
        recentPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblRecentTitle = new JLabel("Recent Payslip Generations");
        lblRecentTitle.setFont(MainFrame.FONT_SUBTITLE);
        lblRecentTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblRecentTitle.setBorder(new EmptyBorder(0, 0, 10, 0));
        recentPanel.add(lblRecentTitle, BorderLayout.NORTH);

        // Table setup
        String[] columns = {"Payslip ID", "Employee ID", "Employee Name", "Month/Year", "Net Salary"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only
            }
        };
        tableRecentPayslips = new JTable(tableModel);
        tableRecentPayslips.setFont(MainFrame.FONT_BODY);
        tableRecentPayslips.setRowHeight(30);
        tableRecentPayslips.setGridColor(MainFrame.COLOR_BORDER);
        tableRecentPayslips.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Header styling
        JTableHeader header = tableRecentPayslips.getTableHeader();
        header.setFont(MainFrame.FONT_BODY_BOLD);
        header.setBackground(MainFrame.COLOR_SIDEBAR_ACTIVE);
        header.setForeground(Color.WHITE);
        header.setReorderingAllowed(false);

        JScrollPane scrollPane = new JScrollPane(tableRecentPayslips);
        scrollPane.setBorder(BorderFactory.createLineBorder(MainFrame.COLOR_BORDER));
        recentPanel.add(scrollPane, BorderLayout.CENTER);

        lowerSplitPanel.add(recentPanel, BorderLayout.CENTER);

        // Quick Actions (Right side of lower panel)
        JPanel actionsPanel = new JPanel();
        actionsPanel.setLayout(new BoxLayout(actionsPanel, BoxLayout.Y_AXIS));
        actionsPanel.setBackground(MainFrame.COLOR_CARD_BG);
        actionsPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));
        actionsPanel.setPreferredSize(new Dimension(250, 0));

        JLabel lblActionsTitle = new JLabel("Quick Actions");
        lblActionsTitle.setFont(MainFrame.FONT_SUBTITLE);
        lblActionsTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblActionsTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        actionsPanel.add(lblActionsTitle);

        actionsPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnAddEmp = MainFrame.createStyledButton("Add New Employee", MainFrame.COLOR_PRIMARY, Color.WHITE);
        btnAddEmp.setMaximumSize(new Dimension(210, 40));
        btnAddEmp.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnAddEmp.addActionListener(e -> mainFrame.showContentPanel("EMPLOYEES"));
        actionsPanel.add(btnAddEmp);

        actionsPanel.add(Box.createRigidArea(new Dimension(0, 12)));

        JButton btnCalcSal = MainFrame.createStyledButton("Calculate Salary", MainFrame.COLOR_PRIMARY, Color.WHITE);
        btnCalcSal.setMaximumSize(new Dimension(210, 40));
        btnCalcSal.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnCalcSal.addActionListener(e -> mainFrame.showContentPanel("SALARY"));
        actionsPanel.add(btnCalcSal);

        lowerSplitPanel.add(actionsPanel, BorderLayout.EAST);

        centerPanel.add(lowerSplitPanel);

        add(centerPanel, BorderLayout.CENTER);
    }

    private JPanel createStatCard(String title, String description) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(MainFrame.COLOR_CARD_BG);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(15, 15, 15, 15)
        ));

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(MainFrame.FONT_SMALL);
        lblTitle.setForeground(MainFrame.COLOR_TEXT_MUTED);
        lblTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblTitle);

        card.add(Box.createRigidArea(new Dimension(0, 6)));

        JLabel lblVal = new JLabel("0");
        lblVal.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblVal.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblVal.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblVal);

        card.add(Box.createRigidArea(new Dimension(0, 4)));

        JLabel lblDesc = new JLabel(description);
        lblDesc.setFont(MainFrame.FONT_SMALL);
        lblDesc.setForeground(MainFrame.COLOR_TEXT_MUTED);
        lblDesc.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(lblDesc);

        // Put a client property so we can easily retrieve and update the value label
        card.putClientProperty("valueLabel", lblVal);

        return card;
    }

    public void refreshStats() {
        List<Employee> employees = repository.getAllEmployees();
        List<Payslip> payslips = repository.getAllPayslips();

        // Count employees
        lblTotalEmployeesVal.setText(String.valueOf(employees.size()));

        // Count payslips
        lblPayslipsCountVal.setText(String.valueOf(payslips.size()));

        // Sum net salaries
        double totalPayroll = 0;
        for (Payslip ps : payslips) {
            if (ps.getSalaryDetails() != null) {
                totalPayroll += ps.getSalaryDetails().getNetSalary();
            }
        }
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        lblTotalPayrollVal.setText(currencyFormat.format(totalPayroll));

        // Refresh table
        tableModel.setRowCount(0);
        
        // Show recent payslips (up to last 10, newest first)
        int start = Math.max(0, payslips.size() - 10);
        for (int i = payslips.size() - 1; i >= start; i--) {
            Payslip ps = payslips.get(i);
            Employee emp = repository.getEmployeeById(ps.getEmployeeId());
            String empName = (emp != null) ? emp.getName() : "Unknown Employee";
            double netSalary = (ps.getSalaryDetails() != null) ? ps.getSalaryDetails().getNetSalary() : 0.0;

            tableModel.addRow(new Object[]{
                    ps.getPayslipId(),
                    ps.getEmployeeId(),
                    empName,
                    ps.getMonthYear(),
                    currencyFormat.format(netSalary)
            });
        }
    }
}
