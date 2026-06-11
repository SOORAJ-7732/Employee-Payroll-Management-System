package repository;

import model.Employee;
import model.Payslip;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class DataRepository {
    private static final String DATA_DIR = "data";
    private static final String EMPLOYEES_FILE = DATA_DIR + "/employees.csv";
    private static final String PAYSLIPS_FILE = DATA_DIR + "/payslips.csv";
    private static final String ADMIN_FILE = DATA_DIR + "/admin.txt";

    public DataRepository() {
        initStorage();
    }

    private void initStorage() {
        try {
            // Create data directory if it doesn't exist
            Files.createDirectories(Paths.get(DATA_DIR));

            // Create files with headers if they don't exist
            File empFile = new File(EMPLOYEES_FILE);
            if (!empFile.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(empFile))) {
                    pw.println("id,name,department,designation,email,phone,type,joiningDate");
                }
            }

            File payFile = new File(PAYSLIPS_FILE);
            if (!payFile.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(payFile))) {
                    pw.println("payslipId,employeeId,monthYear,generationDate,basicSalary,hra,da,medical,bonus,pf,tax,lop");
                }
            }

            // Set up default admin file if not present
            File adminFile = new File(ADMIN_FILE);
            if (!adminFile.exists()) {
                try (PrintWriter pw = new PrintWriter(new FileWriter(adminFile))) {
                    pw.println("admin:admin123");
                }
            }
        } catch (IOException e) {
            System.err.println("Error initializing data storage: " + e.getMessage());
        }
    }

    // ================= ADMIN AUTHENTICATION =================

    public boolean authenticate(String username, String password) {
        try (BufferedReader br = new BufferedReader(new FileReader(ADMIN_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(":", 2);
                if (parts.length == 2 && parts[0].trim().equals(username) && parts[1].trim().equals(password)) {
                    return true;
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading admin credentials: " + e.getMessage());
        }
        return false;
    }

    // ================= EMPLOYEES CRUD =================

    public synchronized List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(EMPLOYEES_FILE))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Employee emp = Employee.fromCSV(line);
                if (emp != null) {
                    list.add(emp);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading employees: " + e.getMessage());
        }
        return list;
    }

    public synchronized Employee getEmployeeById(String id) {
        if (id == null) return null;
        for (Employee emp : getAllEmployees()) {
            if (emp.getId().equalsIgnoreCase(id.trim())) {
                return emp;
            }
        }
        return null;
    }

    public synchronized void saveEmployee(Employee employeeToSave) {
        List<Employee> list = getAllEmployees();
        boolean found = false;

        // If employee exists, update them in place
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).getId().equalsIgnoreCase(employeeToSave.getId().trim())) {
                list.set(i, employeeToSave);
                found = true;
                break;
            }
        }

        if (!found) {
            list.add(employeeToSave);
        }

        writeEmployeesToFile(list);
    }

    public synchronized boolean deleteEmployee(String id) {
        List<Employee> list = getAllEmployees();
        boolean removed = list.removeIf(emp -> emp.getId().equalsIgnoreCase(id.trim()));
        if (removed) {
            writeEmployeesToFile(list);
        }
        return removed;
    }

    private void writeEmployeesToFile(List<Employee> employees) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(EMPLOYEES_FILE))) {
            pw.println("id,name,department,designation,email,phone,type,joiningDate"); // Header
            for (Employee emp : employees) {
                pw.println(emp.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing employees to file: " + e.getMessage());
        }
    }

    // ================= PAYSLIPS CRUD =================

    public synchronized List<Payslip> getAllPayslips() {
        List<Payslip> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PAYSLIPS_FILE))) {
            String line = br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                Payslip ps = Payslip.fromCSV(line);
                if (ps != null) {
                    list.add(ps);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading payslips: " + e.getMessage());
        }
        return list;
    }

    public synchronized List<Payslip> getPayslipsForEmployee(String employeeId) {
        List<Payslip> results = new ArrayList<>();
        for (Payslip ps : getAllPayslips()) {
            if (ps.getEmployeeId().equalsIgnoreCase(employeeId.trim())) {
                results.add(ps);
            }
        }
        return results;
    }

    public synchronized void savePayslip(Payslip payslip) {
        List<Payslip> list = getAllPayslips();
        list.add(payslip);
        writePayslipsToFile(list);
    }

    public synchronized String getNextPayslipId() {
        List<Payslip> list = getAllPayslips();
        int maxId = 0;
        for (Payslip ps : list) {
            String idStr = ps.getPayslipId();
            if (idStr.startsWith("PS")) {
                try {
                    int numericVal = Integer.parseInt(idStr.substring(2));
                    if (numericVal > maxId) {
                        maxId = numericVal;
                    }
                } catch (NumberFormatException e) {
                    // skip
                }
            }
        }
        return String.format("PS%04d", maxId + 1);
    }

    private void writePayslipsToFile(List<Payslip> payslips) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PAYSLIPS_FILE))) {
            pw.println("payslipId,employeeId,monthYear,generationDate,basicSalary,hra,da,medical,bonus,pf,tax,lop"); // Header
            for (Payslip ps : payslips) {
                pw.println(ps.toCSV());
            }
        } catch (IOException e) {
            System.err.println("Error writing payslips to file: " + e.getMessage());
        }
    }
}
