// File: src/User/User.java
package User;

// ArrayList tidak lagi dibutuhkan di sini untuk menyimpan user
// Scanner juga tidak lagi relevan untuk metode register di sini

public class User {
    private int userId; // Ditambahkan untuk ID dari database
    protected String username;
    protected String password; // Pertimbangkan untuk menyimpan hash password, bukan plain text
    protected String role;
    protected String name; // full_name di database
    protected String email;
    protected String major;
    protected String studentId; // id_member di database

    /**
     * Konstruktor untuk membuat objek User dari data yang diambil dari database.
     *
     * @param userId ID unik pengguna dari database.
     * @param username Username pengguna.
     * @param password Password pengguna (sebaiknya hash).
     * @param role Peran pengguna ("Admin" atau "Member").
     * @param name Nama lengkap pengguna.
     * @param email Email pengguna.
     * @param major Jurusan pengguna (jika ada, relevan untuk Member).
     * @param studentId Student ID pengguna (jika ada, relevan untuk Member).
     */
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

    /**
     * Konstruktor untuk membuat objek User baru sebelum disimpan ke database
     * (misalnya saat registrasi). ID akan di-generate oleh database.
     *
     * @param username Username pengguna.
     * @param password Password pengguna (sebaiknya hash).
     * @param role Peran pengguna.
     * @param name Nama lengkap pengguna.
     * @param email Email pengguna.
     * @param major Jurusan pengguna.
     * @param studentId Student ID pengguna.
     */
    public User(String username, String password, String role, String name, String email, String major, String studentId) {
        // userId akan diatur setelah data disimpan ke database dan ID di-generate
        this.username = username;
        this.password = password;
        this.role = role; // Pastikan role diatur dengan benar ("Admin" atau "Member")
        this.name = name;
        this.email = email;
        this.major = major;
        this.studentId = studentId;
    }


    // Getter dan Setter
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; } // Berguna jika ID didapat setelah insert

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

    // ArrayList<User> users statis dihapus karena data akan dikelola di database.
    // Metode login statis dan register statis di kelas User ini sebaiknya
    // dipindahkan ke kelas DAO atau service yang berinteraksi dengan database.
}