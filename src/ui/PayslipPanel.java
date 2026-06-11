package ui;

import model.Employee;
import model.Payslip;
import model.SalaryDetails;
import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class PayslipPanel extends JPanel {
    private MainFrame mainFrame;
    private DataRepository repository;
    private Payslip currentPayslip;

    private JTextArea txtPayslipArea;
    private JButton btnExport;
    private JButton btnPrint;
    private JButton btnBack;

    public PayslipPanel(MainFrame mainFrame, DataRepository repository) {
        this.mainFrame = mainFrame;
        this.repository = repository;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_BACKGROUND);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainFrame.COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Payslip Preview");
        lblTitle.setFont(MainFrame.FONT_TITLE);
        lblTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Review, export, or print the generated employee pay record");
        lblSub.setFont(MainFrame.FONT_BODY);
        lblSub.setForeground(MainFrame.COLOR_TEXT_MUTED);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Content Panel (Payslip Display) ---
        JPanel contentCard = new JPanel(new BorderLayout());
        contentCard.setBackground(MainFrame.COLOR_CARD_BG);
        contentCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        txtPayslipArea = new JTextArea();
        txtPayslipArea.setFont(new Font("Courier New", Font.PLAIN, 12));
        txtPayslipArea.setEditable(false);
        txtPayslipArea.setMargin(new Insets(15, 20, 15, 20));
        txtPayslipArea.setBackground(new Color(253, 253, 253));
        txtPayslipArea.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230)));

        JScrollPane scrollPane = new JScrollPane(txtPayslipArea);
        contentCard.add(scrollPane, BorderLayout.CENTER);

        // Control Panel (Right Side or Bottom of Card)
        JPanel controls = new JPanel();
        controls.setLayout(new BoxLayout(controls, BoxLayout.Y_AXIS));
        controls.setBackground(MainFrame.COLOR_CARD_BG);
        controls.setBorder(new EmptyBorder(0, 20, 0, 0));
        controls.setPreferredSize(new Dimension(220, 0));

        JLabel lblActions = new JLabel("Document Actions");
        lblActions.setFont(MainFrame.FONT_SUBTITLE);
        lblActions.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblActions.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(lblActions);

        controls.add(Box.createRigidArea(new Dimension(0, 20)));

        btnExport = MainFrame.createStyledButton("Export as Text File", MainFrame.COLOR_PRIMARY, Color.WHITE);
        btnExport.setMaximumSize(new Dimension(200, 40));
        btnExport.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(btnExport);

        controls.add(Box.createRigidArea(new Dimension(0, 12)));

        btnPrint = MainFrame.createStyledButton("Print Payslip", MainFrame.COLOR_PRIMARY, Color.WHITE);
        btnPrint.setMaximumSize(new Dimension(200, 40));
        btnPrint.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(btnPrint);

        controls.add(Box.createRigidArea(new Dimension(0, 12)));

        btnBack = MainFrame.createStyledButton("Back to Salary", MainFrame.COLOR_BORDER, MainFrame.COLOR_TEXT_DARK);
        btnBack.setMaximumSize(new Dimension(200, 40));
        btnBack.setAlignmentX(Component.LEFT_ALIGNMENT);
        controls.add(btnBack);

        contentCard.add(controls, BorderLayout.EAST);

        add(contentCard, BorderLayout.CENTER);

        // Actions handlers
        btnBack.addActionListener(e -> mainFrame.showContentPanel("SALARY"));
        btnPrint.addActionListener(e -> printPayslip());
        btnExport.addActionListener(e -> exportPayslipToFile());
    }

    public void setPayslip(Payslip payslip) {
        this.currentPayslip = payslip;
        generatePayslipText();
    }

    private void generatePayslipText() {
        if (currentPayslip == null) {
            txtPayslipArea.setText("No payslip record loaded.");
            return;
        }

        Employee emp = repository.getEmployeeById(currentPayslip.getEmployeeId());
        String name = emp != null ? emp.getName() : "Unknown";
        String dept = emp != null ? emp.getDepartment() : "Unknown";
        String desig = emp != null ? emp.getDesignation() : "Unknown";
        String type = emp != null ? emp.getType() : "Unknown";

        SalaryDetails sd = currentPayslip.getSalaryDetails();
        NumberFormat cur = NumberFormat.getCurrencyInstance(new Locale("en", "US"));

        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append("                      EMPLOYEE PAYSLIP - ").append(currentPayslip.getMonthYear().toUpperCase()).append("\n");
        sb.append("========================================================================\n");
        sb.append(String.format(" Payslip ID : %-20s        Gen Date  : %-20s\n", currentPayslip.getPayslipId(), currentPayslip.getGenerationDate()));
        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format(" Employee ID: %-20s        Name      : %-20s\n", currentPayslip.getEmployeeId(), name));
        sb.append(String.format(" Department : %-20s        Designation: %-20s\n", dept, desig));
        sb.append(String.format(" Job Type   : %-20s\n", type));
        sb.append("========================================================================\n");
        sb.append(String.format(" %-36s %-36s\n", "EARNINGS & ALLOWANCES", "DEDUCTIONS & TAXES"));
        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format(" Basic Salary       : %-14s     Provident Fund (PF) : %-14s\n", cur.format(sd.getBasicSalary()), cur.format(sd.getPf())));
        sb.append(String.format(" House Rent (HRA)   : %-14s     Professional/Tax    : %-14s\n", cur.format(sd.getHra()), cur.format(sd.getTax())));
        sb.append(String.format(" Dearness (DA)      : %-14s     Loss of Pay (LOP)   : %-14s\n", cur.format(sd.getDa()), cur.format(sd.getLop())));
        sb.append(String.format(" Medical Allowance  : %-14s\n", cur.format(sd.getMedical())));
        sb.append(String.format(" Performance Bonus  : %-14s\n", cur.format(sd.getBonus())));
        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format(" Gross Earnings     : %-14s     Total Deductions    : %-14s\n", cur.format(sd.getGrossSalary()), cur.format(sd.getTotalDeductions())));
        sb.append("========================================================================\n");
        sb.append(String.format(" NET PAYABLE SALARY : %s\n", cur.format(sd.getNetSalary())));
        sb.append("========================================================================\n");
        sb.append("             This is a computer generated payslip, no signature required.\n");
        sb.append("========================================================================\n");

        txtPayslipArea.setText(sb.toString());
        // Scroll back to top
        txtPayslipArea.setCaretPosition(0);
    }

    private void exportPayslipToFile() {
        if (currentPayslip == null) return;

        String proposedFilename = "payslip_" + currentPayslip.getEmployeeId() + "_" + currentPayslip.getMonthYear().replace(" ", "_") + ".txt";

        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Export Payslip");
        fileChooser.setSelectedFile(new File(proposedFilename));

        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            // Ensure .txt extension
            if (!fileToSave.getName().toLowerCase().endsWith(".txt")) {
                fileToSave = new File(fileToSave.getParentFile(), fileToSave.getName() + ".txt");
            }

            try (FileWriter fw = new FileWriter(fileToSave)) {
                fw.write(txtPayslipArea.getText());
                JOptionPane.showMessageDialog(this,
                        "Payslip successfully exported to:\n" + fileToSave.getAbsolutePath(),
                        "Export Successful", JOptionPane.INFORMATION_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this,
                        "Error saving file:\n" + e.getMessage(),
                        "Export Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void printPayslip() {
        try {
            boolean complete = txtPayslipArea.print();
            if (complete) {
                JOptionPane.showMessageDialog(this, "Printing Completed", "Printer Status", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "Printing Cancelled", "Printer Status", JOptionPane.WARNING_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Printing Failed: " + e.getMessage(), "Printer Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
