package ui;

import model.Employee;
import repository.DataRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class EmployeePanel extends JPanel {
    private MainFrame mainFrame;
    private DataRepository repository;

    // Form fields
    private JTextField txtId;
    private JTextField txtName;
    private JComboBox<String> cbDepartment;
    private JTextField txtDesignation;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JComboBox<String> cbType;
    private JTextField txtJoiningDate;

    private JButton btnSave;
    private JButton btnClear;
    private JButton btnDelete;

    private JTable tableEmployees;
    private DefaultTableModel tableModel;
    private JTextField txtSearch;

    private boolean isEditMode = false;

    public EmployeePanel(MainFrame mainFrame, DataRepository repository) {
        this.mainFrame = mainFrame;
        this.repository = repository;

        setLayout(new BorderLayout());
        setBackground(MainFrame.COLOR_BACKGROUND);

        // --- Header ---
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(MainFrame.COLOR_BACKGROUND);
        headerPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblTitle = new JLabel("Employee Management");
        lblTitle.setFont(MainFrame.FONT_TITLE);
        lblTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        headerPanel.add(lblTitle, BorderLayout.WEST);

        JLabel lblSub = new JLabel("Add, edit, or remove employee records");
        lblSub.setFont(MainFrame.FONT_BODY);
        lblSub.setForeground(MainFrame.COLOR_TEXT_MUTED);
        headerPanel.add(lblSub, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);

        // --- Split Pane Layout ---
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setBorder(null);
        splitPane.setDividerLocation(360);
        splitPane.setDividerSize(6);

        // Form Panel (Left)
        JPanel formContainer = new JPanel(new BorderLayout());
        formContainer.setBackground(MainFrame.COLOR_CARD_BG);
        formContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel lblFormTitle = new JLabel("Employee Profile");
        lblFormTitle.setFont(MainFrame.FONT_SUBTITLE);
        lblFormTitle.setForeground(MainFrame.COLOR_TEXT_DARK);
        lblFormTitle.setBorder(new EmptyBorder(0, 0, 15, 0));
        formContainer.add(lblFormTitle, BorderLayout.NORTH);

        JPanel formGrid = new JPanel(new GridBagLayout());
        formGrid.setBackground(MainFrame.COLOR_CARD_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(6, 0, 6, 0);
        gbc.weightx = 1.0;

        // ID
        gbc.gridy = 0;
        formGrid.add(createFieldLabel("Employee ID"), gbc);
        gbc.gridy = 1;
        txtId = new JTextField();
        txtId.setFont(MainFrame.FONT_BODY);
        txtId.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtId, gbc);

        // Name
        gbc.gridy = 2;
        formGrid.add(createFieldLabel("Full Name"), gbc);
        gbc.gridy = 3;
        txtName = new JTextField();
        txtName.setFont(MainFrame.FONT_BODY);
        txtName.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtName, gbc);

        // Department
        gbc.gridy = 4;
        formGrid.add(createFieldLabel("Department"), gbc);
        gbc.gridy = 5;
        String[] departments = {"IT", "HR", "Finance", "Marketing", "Sales", "Operations", "Legal", "Other"};
        cbDepartment = new JComboBox<>(departments);
        cbDepartment.setFont(MainFrame.FONT_BODY);
        cbDepartment.setBackground(Color.WHITE);
        cbDepartment.setPreferredSize(new Dimension(0, 32));
        formGrid.add(cbDepartment, gbc);

        // Designation
        gbc.gridy = 6;
        formGrid.add(createFieldLabel("Designation"), gbc);
        gbc.gridy = 7;
        txtDesignation = new JTextField();
        txtDesignation.setFont(MainFrame.FONT_BODY);
        txtDesignation.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtDesignation, gbc);

        // Email
        gbc.gridy = 8;
        formGrid.add(createFieldLabel("Email Address"), gbc);
        gbc.gridy = 9;
        txtEmail = new JTextField();
        txtEmail.setFont(MainFrame.FONT_BODY);
        txtEmail.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtEmail, gbc);

        // Phone
        gbc.gridy = 10;
        formGrid.add(createFieldLabel("Phone Number"), gbc);
        gbc.gridy = 11;
        txtPhone = new JTextField();
        txtPhone.setFont(MainFrame.FONT_BODY);
        txtPhone.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtPhone, gbc);

        // Type
        gbc.gridy = 12;
        formGrid.add(createFieldLabel("Employment Type"), gbc);
        gbc.gridy = 13;
        String[] types = {"Full-Time", "Part-Time", "Contract"};
        cbType = new JComboBox<>(types);
        cbType.setFont(MainFrame.FONT_BODY);
        cbType.setBackground(Color.WHITE);
        cbType.setPreferredSize(new Dimension(0, 32));
        formGrid.add(cbType, gbc);

        // Joining Date
        gbc.gridy = 14;
        formGrid.add(createFieldLabel("Joining Date (YYYY-MM-DD)"), gbc);
        gbc.gridy = 15;
        txtJoiningDate = new JTextField();
        txtJoiningDate.setFont(MainFrame.FONT_BODY);
        txtJoiningDate.setPreferredSize(new Dimension(0, 32));
        formGrid.add(txtJoiningDate, gbc);

        // Buttons Panel at bottom of Form Panel
        JPanel formButtons = new JPanel(new GridLayout(1, 2, 10, 0));
        formButtons.setBackground(MainFrame.COLOR_CARD_BG);
        formButtons.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnSave = MainFrame.createStyledButton("Save", MainFrame.COLOR_PRIMARY, Color.WHITE);
        btnClear = MainFrame.createStyledButton("Clear", MainFrame.COLOR_BORDER, MainFrame.COLOR_TEXT_DARK);

        formButtons.add(btnSave);
        formButtons.add(btnClear);

        // Wrap grid and button
        JPanel centerWrap = new JPanel(new BorderLayout());
        centerWrap.setBackground(MainFrame.COLOR_CARD_BG);
        centerWrap.add(formGrid, BorderLayout.CENTER);
        centerWrap.add(formButtons, BorderLayout.SOUTH);

        formContainer.add(centerWrap, BorderLayout.CENTER);
        splitPane.setLeftComponent(formContainer);

        // Table Panel (Right)
        JPanel tableContainer = new JPanel(new BorderLayout());
        tableContainer.setBackground(MainFrame.COLOR_CARD_BG);
        tableContainer.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(MainFrame.COLOR_BORDER, 1),
                new EmptyBorder(20, 20, 20, 20)
        ));

        // Search panel
        JPanel searchBarPanel = new JPanel(new BorderLayout(10, 0));
        searchBarPanel.setBackground(MainFrame.COLOR_CARD_BG);
        searchBarPanel.setBorder(new EmptyBorder(0, 0, 15, 0));

        JLabel lblSearch = new JLabel("Search: ");
        lblSearch.setFont(MainFrame.FONT_BODY_BOLD);
        lblSearch.setForeground(MainFrame.COLOR_TEXT_DARK);
        searchBarPanel.add(lblSearch, BorderLayout.WEST);

        txtSearch = new JTextField();
        txtSearch.setFont(MainFrame.FONT_BODY);
        txtSearch.setPreferredSize(new Dimension(0, 32));
        searchBarPanel.add(txtSearch, BorderLayout.CENTER);

        tableContainer.add(searchBarPanel, BorderLayout.NORTH);

        // JTable initialization
        String[] columns = {"ID", "Name", "Department", "Designation", "Type", "Email"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // read-only cells
            }
        };
        tableEmployees = new JTable(tableModel);
        tableEmployees.setFont(MainFrame.FONT_BODY);
        tableEmployees.setRowHeight(32);
        tableEmployees.setGridColor(MainFrame.COLOR_BORDER);
        tableEmployees.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Headers styling
        JTableHeader tableHeader = tableEmployees.getTableHeader();
        tableHeader.setFont(MainFrame.FONT_BODY_BOLD);
        tableHeader.setBackground(MainFrame.COLOR_SIDEBAR_ACTIVE);
        tableHeader.setForeground(Color.WHITE);
        tableHeader.setReorderingAllowed(false);

        JScrollPane tableScroll = new JScrollPane(tableEmployees);
        tableScroll.setBorder(BorderFactory.createLineBorder(MainFrame.COLOR_BORDER));
        tableContainer.add(tableScroll, BorderLayout.CENTER);

        // Action Buttons at bottom of Table Panel
        JPanel tableButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        tableButtons.setBackground(MainFrame.COLOR_CARD_BG);
        tableButtons.setBorder(new EmptyBorder(15, 0, 0, 0));

        btnDelete = MainFrame.createStyledButton("Delete Selected", MainFrame.COLOR_DANGER, Color.WHITE);
        tableButtons.add(btnDelete);
        tableContainer.add(tableButtons, BorderLayout.SOUTH);

        splitPane.setRightComponent(tableContainer);
        add(splitPane, BorderLayout.CENTER);

        // Add event handlers
        btnSave.addActionListener(e -> saveEmployee());
        btnClear.addActionListener(e -> clearForm());
        btnDelete.addActionListener(e -> deleteEmployee());

        // Double-click row to edit
        tableEmployees.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    populateFormFromSelectedRow();
                }
            }
        });

        // Search text field functionality
        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { filterTable(); }
        });

        clearForm(); // Set initial form state
    }

    private JLabel createFieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(MainFrame.FONT_SMALL);
        label.setForeground(MainFrame.COLOR_TEXT_MUTED);
        return label;
    }

    public void refreshTable() {
        tableModel.setRowCount(0);
        List<Employee> list = repository.getAllEmployees();
        for (Employee emp : list) {
            tableModel.addRow(new Object[]{
                    emp.getId(),
                    emp.getName(),
                    emp.getDepartment(),
                    emp.getDesignation(),
                    emp.getType(),
                    emp.getEmail()
            });
        }
    }

    private void filterTable() {
        String query = txtSearch.getText().trim();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        tableEmployees.setRowSorter(sorter);
        if (query.length() == 0) {
            sorter.setRowFilter(null);
        } else {
            sorter.setRowFilter(RowFilter.regexFilter("(?i)" + query)); // Case-insensitive filter
        }
    }

    private void populateFormFromSelectedRow() {
        int selectedRow = tableEmployees.getSelectedRow();
        if (selectedRow != -1) {
            // Adjust row index in case table is sorted
            int modelRow = tableEmployees.convertRowIndexToModel(selectedRow);
            String id = (String) tableModel.getValueAt(modelRow, 0);
            Employee emp = repository.getEmployeeById(id);

            if (emp != null) {
                txtId.setText(emp.getId());
                txtId.setEditable(false);
                txtName.setText(emp.getName());
                cbDepartment.setSelectedItem(emp.getDepartment());
                txtDesignation.setText(emp.getDesignation());
                txtEmail.setText(emp.getEmail());
                txtPhone.setText(emp.getPhone());
                cbType.setSelectedItem(emp.getType());
                txtJoiningDate.setText(emp.getJoiningDate());

                isEditMode = true;
                btnSave.setText("Update Profile");
            }
        }
    }

    private void saveEmployee() {
        String id = txtId.getText().trim();
        String name = txtName.getText().trim();
        String dept = (String) cbDepartment.getSelectedItem();
        String designation = txtDesignation.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        String type = (String) cbType.getSelectedItem();
        String joiningDate = txtJoiningDate.getText().trim();

        // Validate
        if (id.isEmpty() || name.isEmpty() || designation.isEmpty() || email.isEmpty() || phone.isEmpty() || joiningDate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Basic Email validation
        if (!email.contains("@") || !email.contains(".")) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Date validation
        try {
            LocalDate.parse(joiningDate, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please use YYYY-MM-DD date format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // If not edit mode, make sure ID is unique
        if (!isEditMode) {
            if (repository.getEmployeeById(id) != null) {
                JOptionPane.showMessageDialog(this, "An employee with ID '" + id + "' already exists.", "Validation Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        Employee emp = new Employee(id, name, dept, designation, email, phone, type, joiningDate);
        repository.saveEmployee(emp);

        JOptionPane.showMessageDialog(this, "Employee profile " + (isEditMode ? "updated" : "saved") + " successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

        clearForm();
        refreshTable();
    }

    private void deleteEmployee() {
        int selectedRow = tableEmployees.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select an employee to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int modelRow = tableEmployees.convertRowIndexToModel(selectedRow);
        String id = (String) tableModel.getValueAt(modelRow, 0);
        String name = (String) tableModel.getValueAt(modelRow, 1);

        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete employee: " + name + " (" + id + ")?\nThis will not delete their historical payslips.",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean success = repository.deleteEmployee(id);
            if (success) {
                JOptionPane.showMessageDialog(this, "Employee record deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                clearForm();
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to delete employee record.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtId.setText("");
        txtId.setEditable(true);
        txtName.setText("");
        cbDepartment.setSelectedIndex(0);
        txtDesignation.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        cbType.setSelectedIndex(0);

        // Pre-fill current date in YYYY-MM-DD format
        txtJoiningDate.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));

        isEditMode = false;
        btnSave.setText("Save Profile");
        tableEmployees.clearSelection();
    }
}
