package model;

public class Payslip {
    private String payslipId;
    private String employeeId;
    private String monthYear; // e.g. "June 2026"
    private String generationDate; // e.g. "2026-06-08"
    private SalaryDetails salaryDetails;

    public Payslip() {}

    public Payslip(String payslipId, String employeeId, String monthYear, String generationDate, SalaryDetails salaryDetails) {
        this.payslipId = payslipId;
        this.employeeId = employeeId;
        this.monthYear = monthYear;
        this.generationDate = generationDate;
        this.salaryDetails = salaryDetails;
    }

    // Getters and Setters
    public String getPayslipId() { return payslipId; }
    public void setPayslipId(String payslipId) { this.payslipId = payslipId; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getMonthYear() { return monthYear; }
    public void setMonthYear(String monthYear) { this.monthYear = monthYear; }

    public String getGenerationDate() { return generationDate; }
    public void setGenerationDate(String generationDate) { this.generationDate = generationDate; }

    public SalaryDetails getSalaryDetails() { return salaryDetails; }
    public void setSalaryDetails(SalaryDetails salaryDetails) { this.salaryDetails = salaryDetails; }

    // Serialize to CSV
    public String toCSV() {
        return escapeSpecialCharacters(payslipId) + "," +
               escapeSpecialCharacters(employeeId) + "," +
               escapeSpecialCharacters(monthYear) + "," +
               escapeSpecialCharacters(generationDate) + "," +
               (salaryDetails != null ? salaryDetails.toCSVString() : "0,0,0,0,0,0,0,0");
    }

    // Parse from CSV row
    public static Payslip fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by commas not inside quotes
        if (tokens.length < 12) return null;

        Payslip ps = new Payslip();
        ps.setPayslipId(unescapeSpecialCharacters(tokens[0]));
        ps.setEmployeeId(unescapeSpecialCharacters(tokens[1]));
        ps.setMonthYear(unescapeSpecialCharacters(tokens[2]));
        ps.setGenerationDate(unescapeSpecialCharacters(tokens[3]));

        SalaryDetails sd = SalaryDetails.fromCSVTokens(ps.getEmployeeId(), tokens, 4);
        ps.setSalaryDetails(sd);

        return ps;
    }

    private static String escapeSpecialCharacters(String data) {
        if (data == null) return "";
        String escapedData = data.replaceAll("\\R", " ");
        if (data.contains(",") || data.contains("\"") || data.contains("'")) {
            escapedData = escapedData.replace("\"", "\"\"");
            escapedData = "\"" + escapedData + "\"";
        }
        return escapedData;
    }

    private static String unescapeSpecialCharacters(String data) {
        if (data == null || data.isEmpty()) return "";
        if (data.startsWith("\"") && data.endsWith("\"")) {
            data = data.substring(1, data.length() - 1);
            data = data.replace("\"\"", "\"");
        }
        return data;
    }
}
