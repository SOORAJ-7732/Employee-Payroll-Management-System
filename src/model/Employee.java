package model;

public class Employee {
    private String id;
    private String name;
    private String department;
    private String designation;
    private String email;
    private String phone;
    private String type; // e.g., Full-Time, Part-Time, Contract
    private String joiningDate; // e.g., YYYY-MM-DD

    public Employee() {}

    public Employee(String id, String name, String department, String designation, String email, String phone, String type, String joiningDate) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.designation = designation;
        this.email = email;
        this.phone = phone;
        this.type = type;
        this.joiningDate = joiningDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    public String getDesignation() { return designation; }
    public void setDesignation(String designation) { this.designation = designation; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getJoiningDate() { return joiningDate; }
    public void setJoiningDate(String joiningDate) { this.joiningDate = joiningDate; }

    // Convert Employee object to CSV row
    public String toCSV() {
        return escapeSpecialCharacters(id) + "," +
               escapeSpecialCharacters(name) + "," +
               escapeSpecialCharacters(department) + "," +
               escapeSpecialCharacters(designation) + "," +
               escapeSpecialCharacters(email) + "," +
               escapeSpecialCharacters(phone) + "," +
               escapeSpecialCharacters(type) + "," +
               escapeSpecialCharacters(joiningDate);
    }

    // Parse Employee object from CSV row
    public static Employee fromCSV(String csvLine) {
        String[] tokens = csvLine.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Split by commas not inside quotes
        if (tokens.length < 8) return null;
        
        Employee emp = new Employee();
        emp.setId(unescapeSpecialCharacters(tokens[0]));
        emp.setName(unescapeSpecialCharacters(tokens[1]));
        emp.setDepartment(unescapeSpecialCharacters(tokens[2]));
        emp.setDesignation(unescapeSpecialCharacters(tokens[3]));
        emp.setEmail(unescapeSpecialCharacters(tokens[4]));
        emp.setPhone(unescapeSpecialCharacters(tokens[5]));
        emp.setType(unescapeSpecialCharacters(tokens[6]));
        emp.setJoiningDate(unescapeSpecialCharacters(tokens[7]));
        return emp;
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

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }
}
