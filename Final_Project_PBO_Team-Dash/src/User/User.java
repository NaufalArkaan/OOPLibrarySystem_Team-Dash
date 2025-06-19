package User;


public class User {
    private int userId;
    protected String username;
    protected String password;
    protected String role;
    protected String name;
    protected String email;
    protected String major;
    protected String studentId;


    public User(int userId, String username, String password, String role, String name, String email, String major, String studentId) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
    }


    public User(String username, String password, String role, String name, String email, String major, String studentId) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.name = name;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
    }


    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getMajor() { return major; }
    public void setMajor(String major) { this.major = major; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }


}