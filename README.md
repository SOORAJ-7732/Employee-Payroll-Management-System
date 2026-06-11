# Employee Payroll Management System

A sleek, premium desktop application built in Java Swing to calculate, manage, and track employee payroll data.

This application is designed as a lightweight, zero-dependency management portal with full database persistence using local CSV storage.

---

## Technical Stack & Libraries

To ensure portability, rapid startup, and zero installation friction, the project is constructed using native Java Standard Edition libraries:

1. **Language & Runtime**: **Java SE 17** (LTS).
2. **User Interface Framework**: **Java Swing & AWT** (Abstract Window Toolkit)
   - Used for structural framing, input fields, visual tables, dynamic menus, and card panels.
   - Built-in theme adjustments inherit native Windows operating system styles (via System Look-and-Feel).
3. **Data Persistence**: **Comma-Separated Values (CSV)** file repository
   - Eliminates the need to install or configure external SQL servers (MySQL, PostgreSQL, etc.).
   - Data is stored in human-readable files under the `data/` directory, allowing straightforward audit checks.

---

## System Mechanics & Features

### 1. Secure Admin Login
- Login authentication is checked against `data/admin.txt`.
- **Default Credentials**: 
  - **Username**: `admin`
  - **Password**: `admin123`

### 2. Active Employee Profiles (CRUD)
- Register new employees and assign specific attributes (ID, Department, Designation, Email, Phone, Employment Type, Joining Date).
- Search employee records dynamically via an interactive table search bar.
- Edit existing employee profiles by double-clicking rows, or remove records using the "Delete Selected" function.

### 3. Smart Salary Adjustments
- Automatically pre-fills salary structures (Basic Salary, HRA, DA, Medical, PF, Tax) based on the employee's status (`Full-Time`, `Part-Time`, or `Contract`) to expedite data entry.
- **Real-Time Computations**: Utilizes Swing `DocumentListener` hooks to update Gross Earnings, Deductions, and Net Payable Salary instantaneously as the user edits numbers.
- Prevents negative inputs and protects records from erroneous submissions.

### 4. Interactive Payslip Generation
- Saves historical pay receipts with auto-incrementing IDs (e.g. `PS0001`, `PS0002`).
- Renders an aligned, monospaced ASCII receipt displaying itemized earnings and deductions.
- Supports **Exporting** (saves the ASCII layout as a clean `.txt` file via a file chooser) and **Printing** (sends the receipt to native physical or PDF printers using Java's Print API).

---

## Directory Structure

```
C:\Users\Sooraj S\OneDrive\Desktop\Tech_Vedhu_Internship_Individual_Proj\soo\soo
├── bin/                       # Compiled bytecode classes
├── data/                      # Local CSV data storage (Auto-generated)
│   ├── admin.txt
│   ├── employees.csv
│   └── payslips.csv
├── src/                       # Java source files
│   ├── model/
│   │   ├── Employee.java
│   │   ├── Payslip.java
│   │   └── SalaryDetails.java
│   ├── repository/
│   │   └── DataRepository.java
│   ├── ui/
│   │   ├── DashboardPanel.java
│   │   ├── EmployeePanel.java
│   │   ├── LoginPanel.java
│   │   ├── MainFrame.java
│   │   ├── PayslipPanel.java
│   │   └── SalaryPanel.java
│   └── Main.java
└── README.md                  # System instructions
```

---

## How to Compile & Run

Open your command prompt inside the workspace directory (`C:\Users\Sooraj S\OneDrive\Desktop\Tech_Vedhu_Internship_Individual_Proj\soo\soo`) and execute the following commands:

### Step 1: Compile the Code
Compile all package components into a binaries folder (`bin/`):
javac -d bin src\Main.java src\model\*.java src\repository\*.java src\ui\*.java
```

### Step 2: Launch the App
Run the application launcher class:
java -cp bin Main
```
