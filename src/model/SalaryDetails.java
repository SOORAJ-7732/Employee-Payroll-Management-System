package model;

public class SalaryDetails {
    private String employeeId;
    private double basicSalary;
    private double hra; // House Rent Allowance
    private double da;  // Dearness Allowance
    private double medical;
    private double bonus;
    private double pf;  // Provident Fund
    private double tax; // Income Tax
    private double lop; // Loss of Pay / unpaid leaves

    public SalaryDetails() {}

    public SalaryDetails(String employeeId, double basicSalary, double hra, double da, double medical, double bonus, double pf, double tax, double lop) {
        this.employeeId = employeeId;
        this.basicSalary = basicSalary;
        this.hra = hra;
        this.da = da;
        this.medical = medical;
        this.bonus = bonus;
        this.pf = pf;
        this.tax = tax;
        this.lop = lop;
    }

    // Getters and Setters
    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public double getBasicSalary() { return basicSalary; }
    public void setBasicSalary(double basicSalary) { this.basicSalary = basicSalary; }

    public double getHra() { return hra; }
    public void setHra(double hra) { this.hra = hra; }

    public double getDa() { return da; }
    public void setDa(double da) { this.da = da; }

    public double getMedical() { return medical; }
    public void setMedical(double medical) { this.medical = medical; }

    public double getBonus() { return bonus; }
    public void setBonus(double bonus) { this.bonus = bonus; }

    public double getPf() { return pf; }
    public void setPf(double pf) { this.pf = pf; }

    public double getTax() { return tax; }
    public void setTax(double tax) { this.tax = tax; }

    public double getLop() { return lop; }
    public void setLop(double lop) { this.lop = lop; }

    // Computations
    public double getGrossSalary() {
        return basicSalary + hra + da + medical + bonus;
    }

    public double getTotalDeductions() {
        return pf + tax + lop;
    }

    public double getNetSalary() {
        double net = getGrossSalary() - getTotalDeductions();
        return net < 0 ? 0 : net; // Ensure no negative net salary
    }

    // CSV format: basicSalary,hra,da,medical,bonus,pf,tax,lop
    public String toCSVString() {
        return basicSalary + "," + hra + "," + da + "," + medical + "," + bonus + "," + pf + "," + tax + "," + lop;
    }

    public static SalaryDetails fromCSVTokens(String employeeId, String[] tokens, int startIndex) {
        if (tokens.length < startIndex + 8) return null;
        try {
            double basic = Double.parseDouble(tokens[startIndex]);
            double hra = Double.parseDouble(tokens[startIndex + 1]);
            double da = Double.parseDouble(tokens[startIndex + 2]);
            double medical = Double.parseDouble(tokens[startIndex + 3]);
            double bonus = Double.parseDouble(tokens[startIndex + 4]);
            double pf = Double.parseDouble(tokens[startIndex + 5]);
            double tax = Double.parseDouble(tokens[startIndex + 6]);
            double lop = Double.parseDouble(tokens[startIndex + 7]);
            return new SalaryDetails(employeeId, basic, hra, da, medical, bonus, pf, tax, lop);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
