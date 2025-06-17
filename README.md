# Kelompok: Team Dash 💻🐧

# Nama Anggota:
### 1. Naufal Arkaan | Lead Developer | 202410370110020 | 2B
### 2. Akhmad Arjuan Syuhada | Backend Specialist | 202410370110043 | 2B
### 3. Muhamad Robi Ardita | Frontend Specialist | 202410370110002 | 2B

---

# 📚 Campus Library Information System (Sistem Informasi Perpustakaan Kampus)

## 🚀 Deskripsi Proyek
Sistem Informasi Perpustakaan Kampus ini dikembangkan menggunakan bahasa pemrograman **Java** dengan antarmuka grafis berbasis **JavaFX**. Proyek ini bertujuan untuk memudahkan pengelolaan perpustakaan kampus, mulai dari autentikasi pengguna, manajemen katalog buku, registrasi anggota, proses peminjaman dan pengembalian buku, hingga pembuatan laporan.

Fitur utama yang dikembangkan meliputi:
- Autentikasi pengguna (Member & Admin)
- CRUD (Create, Read, Update, Delete) untuk buku dan anggota
- Peminjaman dan pengembalian buku dengan perhitungan denda otomatis
- Visualisasi laporan menggunakan tabel JavaFX 
- Antarmuka pengguna yang responsif dan mudah digunakan

---

## 📅 Timeline Proyek
Proyek ini dikerjakan selama 4 minggu dengan pembagian tugas sebagai berikut:

| Minggu | Lead Developer               | Backend Specialist           | Frontend Specialist          |
|--------|-----------------------------|-----------------------------|------------------------------|
| 1      | Setup proyek, desain UML, koordinasi tim | Desain model data, setup backend | Desain mockup UI, setup JavaFX dasar |
| 2      | Review kode, integrasi modul | Implementasi CRUD, validasi data | Integrasi UI dengan backend CRUD |
| 3      | Koordinasi integrasi fitur, debugging | Logika peminjaman, laporan statistik | UI fitur peminjaman, grafik statistik |
| 4      | Testing, dokumentasi, presentasi | Testing backend, dokumentasi API | Testing UI, dokumentasi penggunaan |

---

## 📂 Struktur Repository
- **/data**  
  Folder ini menyimpan file CSV yang berisi data sampel buku dan anggota yang digunakan untuk testing dan demo aplikasi, namun kami tidak menggunakan CSV, melainkan mengambil data dari database MySQL

- **/demo**  
  Folder ini berisi video demo yang memperlihatkan cara penggunaan aplikasi secara langsung.

- **/docs**  
  Berisi dokumentasi teknis lengkap yang menjelaskan desain sistem, diagram UML, dan panduan penggunaan aplikasi.

- **/presentation**  
  Berisi file presentasi yang digunakan untuk memaparkan hasil proyek di depan dosen atau penguji.

- **/src**  
  Berikut adalah penjelasan masing-masing folder dan file dalam struktur proyek src:
  
- **/src/Action/**  
  Berisi class-class yang menangani aksi/event tertentu dalam aplikasi. Saat ini `Action/` berisi class `Transaction.java`, yang merepresentasikan entitas data transaksi pembayaran denda.

- **/src/controller/**  
   Berfungsi sebagai penghubung antara view (tampilan) dan model (data). Folder ini mengelola logika bisnis dan mengatur alur data dari dan ke tampilan.

- **/src/Data/**  
   Berisi data-data statis, dan konfigurasi awal yang digunakan dalam aplikasi. Berisi data Book, dan Loan.
  
- **/src/ExceptionHandle/**  
    Menyimpan class-class untuk menangani pengecualian (exception) dalam aplikasi agar program lebih robust dan tidak crash ketika terjadi kesalahan.
  
- **/src/main/**  
    Berisi class utama untuk menjalankan aplikasi. Biasanya ada file `Main.java` atau `App.java` yang merupakan entry point dari program.
  
- **/src/model/**  
     Menyimpan representasi data (Model) yang digunakan dalam aplikasi, biasanya class seperti `Book`, `HistoryRecord`, `ReturnRecord`, dll. Ini adalah bagian dari arsitektur MVC.
  
- **/src/SQL_DATA/**  
     Folder ini berisi file atau class yang mengelola koneksi database, query SQL, atau data hasil query yang digunakan oleh aplikasi.
  
- **/src/view/**  
      Berisi tampilan (UI) aplikasi seperti file FXML atau Java GUI (JavaFX). Ini adalah bagian yang dilihat dan digunakan oleh pengguna akhir.
  
- **/src/Main.java**  
       File entry point utama dari aplikasi. Digunakan untuk meluncurkan aplikasi JavaFX dan memanggil tampilan awal.
  
- **README.md**  
  File ini berisi dokumentasi utama yang menjelaskan proyek, struktur, cara menjalankan, dan informasi penting lainnya.

---

## 🛠️ Library dan Tools yang Digunakan
- **Java 23** – Bahasa pemrograman utama
- **JavaFX** – Framework GUI untuk antarmuka pengguna
- **Git & GitHub** – Version control dan repository hosting
- **Railway** - Menghubungkan ke database MySQL, dan hosting MySQL di railway
- **MySQL Workbench** - Mengelola user, hak akses, dan konfigurasi server
- **Figma** - Desain tampilan UI
  
---

## 📲 Database yang Digunakan
🧩 Pengantar
Pada project Sistem Perpustakaan Berbasis GUI ini, database yang digunakan adalah MySQL. MySQL adalah sistem manajemen basis data relasional (RDBMS) yang populer dan handal, digunakan untuk menyimpan dan mengelola data aplikasi secara efisien.

🗄️ Apa Itu MySQL?
MySQL adalah sistem manajemen basis data open-source yang menggunakan bahasa SQL (Structured Query Language) untuk mengelola data. MySQL dikenal karena performa tinggi, skalabilitas, dan kemudahan integrasi dengan berbagai bahasa pemrograman, termasuk Java yang digunakan dalam project ini.

🔧 Peran MySQL dalam Project Sistem Perpustakaan GUI

**Penyimpanan Data Buku:** Menyimpan informasi buku seperti judul, pengarang, penerbit, tahun terbit, dan stok.

**Manajemen Pengguna:** Menyimpan data anggota perpustakaan, termasuk data register, login dan histori peminjaman.

**Transaksi Peminjaman dan Pengembalian:** Mengelola data transaksi peminjaman dan pengembalian buku secara real-time.

**Query dan Reporting:** Memungkinkan pengambilan data untuk laporan dan analisis penggunaan perpustakaan.
  
---

## 📖 Dokumentasi Lengkap
Dokumentasi teknis proyek dapat diakses melalui link berikut:  
[Dokumentasi Final Project PBO](https://docs.google.com/document/d/1CtwHis7G_YfpyJPfrXR62pF8oCSVRRxLGQMwSSKJDkM/edit?usp=sharing)

Dokumentasi ini mencakup:
- Desain sistem dan diagram UML
- Panduan penggunaan GUI
- Penjelasan fitur dan alur kerja sistem

---

## 🎥 Demo Video
Untuk melihat demo penggunaan aplikasi, silakan tonton video berikut:  
`/demo/demo_video.mp4`

---

## 💡 Cara Menjalankan Proyek
1. Clone repository ini ke komputer Anda:
   ```bash
   git clone https://github.com/NaufalArkaan/OOPLibrarySystem_Team-Dash.git

---

## 🤝 Kontribusi
Proyek ini dikembangkan sebagai tugas akhir mata kuliah Pemrograman Berorientasi Objek (PBO). Jika anda ingin memberikan masukan atau kontribusi, silakan hubungi saya melalui email atau buat issue di repository ini.

---

## ✨ Penutup
Terima kasih telah mengunjungi repository ini! Semoga proyek ini bermanfaat dan dapat menjadi referensi yang baik untuk pengembangan aplikasi berbasis Java dan JavaFX. Jangan ragu untuk menghubungi saya jika ada pertanyaan atau butuh bantuan lebih lanjut! 🚀

---
