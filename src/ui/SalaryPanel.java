package ui;

import model.Employee;
import model.SalaryDetails;
import model.Payslip;
import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class SalaryPanel extends JPanel {
    private MainFrame mainFrame;
    private DataRepository repository;

    private JComboBox<Employee> cbEmployees;
    private JTextField txtMonthYear;

    // Inputs
    private JTextField txtBasicSalary;
    private JTextField txtHra;
    private JTextField txtDa;
    private JTextField txtMedical;
    private JTextField txtBonus;
    private JTextField txtPf;
    private JTextField txtTax;
    private JTextField txtLop;

    // Displays
    private JLabel lblGrossVal;
    private JLabel lblDeductionsVal;
    private JLabel lblNetVal;

    private boolean isUpdatingFields = false;

    public SalaryPanel(MainFrame mainFrame, DataRepository repository) {
        this.mainFrame = mainFrame;
        this.repository = repository;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_BACKGROUND);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainFrame.COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Calculate Salary");
        lblTitle.setFont(MainFrame.FONT_TITLE);
        lblTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Compute employee earnings, allowances, and tax deductions");
        lblSub.setFont(MainFrame.FONT_BODY);
        lblSub.setForeground(MainFrame.COLOR_TEXT_MUTED);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Main Form Panel ---
        JPanel mainCard = new JPanel(new BorderLayout());
        mainCard.setBackground(MainFrame.COLOR_CARD_BG);
        mainCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(25, 25, 25, 25)
        ));

        // Selector Bar (Top of Card)
        JPanel selectorBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        selectorBar.setBackground(MainFrame.COLOR_CARD_BG);
        selectorBar.setBorder(new EmptyBorder(0, 0, 20, 0));

        selectorBar.add(new JLabel("Select Employee:"));
        cbEmployees = new JComboBox<>();
        cbEmployees.setFont(MainFrame.FONT_BODY);
        cbEmployees.setPreferredSize(new Dimension(220, 32));
        cbEmployees.setBackground(Color.WHITE);
        cbEmployees.addActionListener(e -> autoFillDefaults());
        selectorBar.add(cbEmployees);

        selectorBar.add(new JLabel("Pay Period:"));
        txtMonthYear = new JTextField(12);
        txtMonthYear.setFont(MainFrame.FONT_BODY);
        txtMonthYear.setPreferredSize(new Dimension(120, 32));
        // Pre-fill with current month and year (e.g. "June 2026")
        LocalDate now = LocalDate.now();
        String currentMonthYear = now.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + now.getYear();
        txtMonthYear.setText(currentMonthYear);
        selectorBar.add(txtMonthYear);

        mainCard.add(selectorBar, BorderLayout.NORTH);

        // Split Inputs (Left: Allowances, Right: Deductions)
        JPanel inputsPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        inputsPanel.setBackground(MainFrame.COLOR_CARD_BG);

        // Allowances Column (Left)
        JPanel colAllowances = new JPanel(new GridBagLayout());
        colAllowances.setBackground(MainFrame.COLOR_CARD_BG);
        colAllowances.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                " Earnings & Allowances ",
                0, 0, MainFrame.FONT_HEADER, MainFrame.COLOR_PRIMARY
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.weightx = 1.0;

        // Basic Salary
        gbc.gridy = 0;
        colAllowances.add(createFieldLabel("Basic Salary ($)"), gbc);
        gbc.gridy = 1;
        txtBasicSalary = createInputTextField();
        colAllowances.add(txtBasicSalary, gbc);

        // HRA
        gbc.gridy = 2;
        colAllowances.add(createFieldLabel("House Rent Allowance - HRA ($)"), gbc);
        gbc.gridy = 3;
        txtHra = createInputTextField();
        colAllowances.add(txtHra, gbc);

        // DA
        gbc.gridy = 4;
        colAllowances.add(createFieldLabel("Dearness Allowance - DA ($)"), gbc);
        gbc.gridy = 5;
        txtDa = createInputTextField();
        colAllowances.add(txtDa, gbc);

        // Medical
        gbc.gridy = 6;
        colAllowances.add(createFieldLabel("Medical Allowance ($)"), gbc);
        gbc.gridy = 7;
        txtMedical = createInputTextField();
        colAllowances.add(txtMedical, gbc);

        // Bonus
        gbc.gridy = 8;
        colAllowances.add(createFieldLabel("Performance Bonus ($)"), gbc);
        gbc.gridy = 9;
        txtBonus = createInputTextField();
        colAllowances.add(txtBonus, gbc);

        inputsPanel.add(colAllowances);

        // Deductions Column (Right)
        JPanel colDeductions = new JPanel(new GridBagLayout());
        colDeductions.setBackground(MainFrame.COLOR_CARD_BG);
        colDeductions.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                " Deductions & Taxes ",
                0, 0, MainFrame.FONT_HEADER, MainFrame.COLOR_DANGER
        ));

        // PF
        gbc.gridy = 0;
        colDeductions.add(createFieldLabel("Provident Fund - PF ($)"), gbc);
        gbc.gridy = 1;
        txtPf = createInputTextField();
        colDeductions.add(txtPf, gbc);

        // Income Tax
        gbc.gridy = 2;
        colDeductions.add(createFieldLabel("Income / Professional Tax ($)"), gbc);
        gbc.gridy = 3;
        txtTax = createInputTextField();
        colDeductions.add(txtTax, gbc);

        // LOP
        gbc.gridy = 4;
        colDeductions.add(createFieldLabel("Loss of Pay - LOP / Unpaid Leaves ($)"), gbc);
        gbc.gridy = 5;
        txtLop = createInputTextField();
        colDeductions.add(txtLop, gbc);

        // Summary Calculations Box
        JPanel summaryBox = new JPanel(new GridBagLayout());
        summaryBox.setBackground(MainFrame.COLOR_BACKGROUND);
        summaryBox.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(12, 12, 12, 12)
        ));
        GridBagConstraints gbcSum = new GridBagConstraints();
        gbcSum.fill = GridBagConstraints.HORIZONTAL;
        gbcSum.weightx = 1.0;
        gbcSum.insets = new Insets(4, 5, 4, 5);

        // Gross Display
        gbcSum.gridy = 0;
        summaryBox.add(new JLabel("Gross Earnings:"), gbcSum);
        lblGrossVal = new JLabel("$0.00");
        lblGrossVal.setFont(MainFrame.FONT_BODY_BOLD);
        lblGrossVal.setHorizontalAlignment(SwingConstants.RIGHT);
        summaryBox.add(lblGrossVal, gbcSum);

        // Deductions Display
        gbcSum.gridy = 1;
        summaryBox.add(new JLabel("Total Deductions:"), gbcSum);
        lblDeductionsVal = new JLabel("$0.00");
        lblDeductionsVal.setFont(MainFrame.FONT_BODY_BOLD);
        lblDeductionsVal.setForeground(MainFrame.COLOR_DANGER);
        lblDeductionsVal.setHorizontalAlignment(SwingConstants.RIGHT);
        summaryBox.add(lblDeductionsVal, gbcSum);

        // Net Display
        gbcSum.gridy = 2;
        JLabel lblNetTitle = new JLabel("Net Payable Salary:");
        lblNetTitle.setFont(MainFrame.FONT_HEADER);
        summaryBox.add(lblNetTitle, gbcSum);
        lblNetVal = new JLabel("$0.00");
        lblNetVal.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNetVal.setForeground(MainFrame.COLOR_SUCCESS);
        lblNetVal.setHorizontalAlignment(SwingConstants.RIGHT);
        summaryBox.add(lblNetVal, gbcSum);

        gbc.gridy = 6;
        gbc.gridheight = 4; // Span the remaining rows
        colDeductions.add(summaryBox, gbc);

        inputsPanel.add(colDeductions);

        mainCard.add(inputsPanel, BorderLayout.CENTER);

        // Footer Actions Panel
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        footerPanel.setBackground(MainFrame.COLOR_CARD_BG);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        JButton btnClear = MainFrame.createStyledButton("Clear Fields", MainFrame.COLOR_BORDER, MainFrame.COLOR_TEXT_DARK);
        JButton btnGenerate = MainFrame.createStyledButton("Generate Payslip", MainFrame.COLOR_SUCCESS, Color.WHITE);

        footerPanel.add(btnClear);
        footerPanel.add(btnGenerate);

        mainCard.add(footerPanel, BorderLayout.SOUTH);

        add(mainCard, BorderLayout.CENTER);

        // Event listeners for calculations
        btnClear.addActionListener(e -> clearFields());
        btnGenerate.addActionListener(e -> generatePayslip());

        // Attach document listeners for real-time updates
        DocumentListener recalcListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { recalculate(); }
            @Override
            public void removeUpdate(DocumentEvent e) { recalculate(); }
            @Override
            public void changedUpdate(DocumentEvent e) { recalculate(); }
        };

        txtBasicSalary.getDocument().addDocumentListener(recalcListener);
        txtHra.getDocument().addDocumentListener(recalcListener);
        txtDa.getDocument().addDocumentListener(recalcListener);
        txtMedical.getDocument().addDocumentListener(recalcListener);
        txtBonus.getDocument().addDocumentListener(recalcListener);
        txtPf.getDocument().addDocumentListener(recalcListener);
        txtTax.getDocument().addDocumentListener(recalcListener);
        txtLop.getDocument().addDocumentListener(recalcListener);
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MainFrame.FONT_SMALL);
        label.setForeground(MainFrame.COLOR_TEXT_MUTED);
        return label;
    }

    private JTextField createInputTextField() {
        JTextField tf = new JTextField("0");
        tf.setFont(MainFrame.FONT_BODY);
        tf.setPreferredSize(new Dimension(0, 30));
        return tf;
    }

    public void refreshEmployeeList() {
        Employee currentSel = (Employee) cbEmployees.getSelectedItem();
        cbEmployees.removeAllItems();

        List<Employee> list = repository.getAllEmployees();
        for (Employee emp : list) {
            cbEmployees.addItem(emp);
        }

        // Restore selection if employee still exists
        if (currentSel != null) {
            for (int i = 0; i < cbEmployees.getItemCount(); i++) {
                if (cbEmployees.getItemAt(i).getId().equals(currentSel.getId())) {
                    cbEmployees.setSelectedIndex(i);
                    break;
                }
            }
        } else if (cbEmployees.getItemCount() > 0) {
            cbEmployees.setSelectedIndex(0);
        }
    }

    private void autoFillDefaults() {
        Employee emp = (Employee) cbEmployees.getSelectedItem();
        if (emp == null || isUpdatingFields) return;

        isUpdatingFields = true;

        // Sensible default values based on job type to streamline operations
        double basic = 0;
        double hra = 0;
        double da = 0;
        double medical = 0;
        double pf = 0;
        double tax = 0;

        if (emp.getType().equals("Full-Time")) {
            basic = 4500;
            hra = 1200;
            da = 450;
            medical = 250;
            pf = 540; // 12% of basic
            tax = 400; // custom bracket estimate
        } else if (emp.getType().equals("Part-Time")) {
            basic = 2000;
            hra = 400;
            da = 150;
            medical = 100;
            pf = 240;
            tax = 100;
        } else if (emp.getType().equals("Contract")) {
            basic = 3500;
            hra = 0; // Contracts usually have fewer allowances
            da = 0;
            medical = 150;
            pf = 0; // Often self-paid/exempted
            tax = 250;
        }

        txtBasicSalary.setText(String.valueOf(basic));
        txtHra.setText(String.valueOf(hra));
        txtDa.setText(String.valueOf(da));
        txtMedical.setText(String.valueOf(medical));
        txtBonus.setText("0");
        txtPf.setText(String.valueOf(pf));
        txtTax.setText(String.valueOf(tax));
        txtLop.setText("0");

        isUpdatingFields = false;
        recalculate();
    }

    private void recalculate() {
        if (isUpdatingFields) return;

        try {
            double basic = parseDoubleSafe(txtBasicSalary.getText());
            double hra = parseDoubleSafe(txtHra.getText());
            double da = parseDoubleSafe(txtDa.getText());
            double medical = parseDoubleSafe(txtMedical.getText());
            double bonus = parseDoubleSafe(txtBonus.getText());

            double pf = parseDoubleSafe(txtPf.getText());
            double tax = parseDoubleSafe(txtTax.getText());
            double lop = parseDoubleSafe(txtLop.getText());

            double gross = basic + hra + da + medical + bonus;
            double deductions = pf + tax + lop;
            double net = gross - deductions;
            if (net < 0) net = 0;

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
            lblGrossVal.setText(currencyFormat.format(gross));
            lblDeductionsVal.setText(currencyFormat.format(deductions));
            lblNetVal.setText(currencyFormat.format(net));

        } catch (NumberFormatException e) {
            lblGrossVal.setText("-");
            lblDeductionsVal.setText("-");
            lblNetVal.setText("Invalid input");
        }
    }

    private double parseDoubleSafe(String val) {
        if (val == null || val.trim().isEmpty()) return 0.0;
        return Double.parseDouble(val.trim());
    }

    private void clearFields() {
        isUpdatingFields = true;
        txtBasicSalary.setText("0");
        txtHra.setText("0");
        txtDa.setText("0");
        txtMedical.setText("0");
        txtBonus.setText("0");
        txtPf.setText("0");
        txtTax.setText("0");
        txtLop.setText("0");
        isUpdatingFields = false;
        recalculate();
    }

    private void generatePayslip() {
        Employee emp = (Employee) cbEmployees.getSelectedItem();
        if (emp == null) {
            JOptionPane.showMessageDialog(this, "Please select or register an employee first.", "No Employee Selected", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String monthYear = txtMonthYear.getText().trim();
        if (monthYear.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please specify the pay period (e.g. June 2026).", "Pay Period Required", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            double basic = parseDoubleSafe(txtBasicSalary.getText());
            double hra = parseDoubleSafe(txtHra.getText());
            double da = parseDoubleSafe(txtDa.getText());
            double medical = parseDoubleSafe(txtMedical.getText());
            double bonus = parseDoubleSafe(txtBonus.getText());

            double pf = parseDoubleSafe(txtPf.getText());
            double tax = parseDoubleSafe(txtTax.getText());
            double lop = parseDoubleSafe(txtLop.getText());

            // Validate negative numbers
            if (basic < 0 || hra < 0 || da < 0 || medical < 0 || bonus < 0 || pf < 0 || tax < 0 || lop < 0) {
                JOptionPane.showMessageDialog(this, "Values cannot be negative.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Generate salary object
            SalaryDetails sd = new SalaryDetails(emp.getId(), basic, hra, da, medical, bonus, pf, tax, lop);

            // Generate next ID
            String psId = repository.getNextPayslipId();
            String genDate = LocalDate.now().toString();

            Payslip ps = new Payslip(psId, emp.getId(), monthYear, genDate, sd);

            // Save record
            repository.savePayslip(ps);

            JOptionPane.showMessageDialog(this, "Payslip generated and saved successfully!\nID: " + psId, "Success", JOptionPane.INFORMATION_MESSAGE);

            // Show payslip in main frame viewer
            mainFrame.displayPayslip(ps);

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numerical values for salary details.", "Invalid Input", JOptionPane.ERROR_MESSAGE);
        }
    }
}
