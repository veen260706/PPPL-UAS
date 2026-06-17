# 🦷 PPPL UAS — Pengujian Otomatis Aplikasi Dental X-Ray

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.java.com)
[![Selenium](https://img.shields.io/badge/Selenium-4.x-green?logo=selenium)](https://www.selenium.dev/)
[![Cucumber](https://img.shields.io/badge/Cucumber-BDD-brightgreen?logo=cucumber)](https://cucumber.io/)
[![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apache-maven)](https://maven.apache.org/)

> **Mata Kuliah:** Praktik dan Pengujian Perangkat Lunak (PPPL)
> **Tugas:** Ujian Akhir Semester (UAS) — Pengujian Otomatis Aplikasi Web

---

## 📌 Penjelasan Singkat SUT (System Under Test)

**SUT:** Aplikasi Web **Dental X-Ray** — sebuah sistem manajemen klinik gigi berbasis web yang digunakan oleh admin/tenaga medis untuk:

| Fitur | Deskripsi |
|---|---|
| **Login & Register** | Autentikasi pengguna untuk masuk ke sistem |
| **Dashboard** | Beranda utama yang menampilkan statistik pasien (Hadir, Rontgen, dsb.) |
| **Daftar Pasien** | Manajemen daftar pasien yang hadir, dilengkapi tab filter (Semua / Menunggu / Di Ruangan) |
| **Upload Foto Rontgen** | Admin dapat mengunggah foto rontgen pasien dalam format JPG/PNG |
| **Riwayat Pemeriksaan** | Histori pemeriksaan dan rontgen yang pernah dilakukan |
| **Profile** | Manajemen profil pengguna, ganti password, dan logout |

Aplikasi diuji secara **end-to-end** menggunakan browser otomatis, mulai dari alur Register → Login → Dashboard → Pilih Pasien → Upload Rontgen → Cek History.

---

## 🧪 Penjelasan Singkat Test Suite

Test suite dibangun menggunakan **Cucumber BDD (Behavior-Driven Development)** dengan **Selenium WebDriver**, mengikuti pola **Page Object Model (POM)**.

### Framework & Tools
- **Bahasa:** Java 17
- **Build Tool:** Apache Maven
- **Testing Framework:** Cucumber 7 + JUnit 5
- **Browser Automation:** Selenium WebDriver 4
- **Arsitektur:** Page Object Model (POM)
- **Laporan:** Cucumber HTML Report

### Fitur yang Diuji & Jumlah Skenario

| Feature File | Fitur | Jenis Test | Skenario |
|---|---|---|---|
| `Login.feature` | Autentikasi Login | Positive & Negative | 2 |
| `Register.feature` | Registrasi Akun | Positive & Negative | 2 |
| `Profile.feature` | Profil & Logout & Ganti Password | Positive & Negative | 2 |
| `Dashboard.feature` | Tampilan Dashboard & Statistik | Positive | 2 |
| `PatientList.feature` | Daftar Pasien & Filter Tab | Positive & Negative | 5+ |
| `ExaminationHistory.feature` | Riwayat Pemeriksaan Rontgen | Positive & Negative | 3+ |
| `UploadRontgen.feature` | Upload Foto Rontgen JPG/PNG | Positive & Negative | 2+ |
| `EndToEnd.feature` | Alur Register → Login → Dashboard | E2E | 1 |

**Total:** ±20 skenario uji mencakup **positive test**, **negative test**, dan **end-to-end test**.

### Strategi Pengujian
- ✅ **Positive Test** — verifikasi fungsionalitas berjalan sesuai ekspektasi
- ❌ **Negative Test** — verifikasi sistem menangani input/kondisi tidak valid dengan benar
- 🔄 **End-to-End Test** — verifikasi alur lengkap pengguna dari awal hingga akhir

---

## 👥 Pembagian Tugas Kelompok

| Anggota | NIM | Branch | Tugas |
|---|---|---|---|
| **[Nama Anggota 1 / kamu]** | [NIM] | `main` | Menulis test case untuk fitur **Login**, **Register**, dan **Profile** (Page Object, Step Definition, Feature file) |
| **Devia** | [NIM] | `devia-testing` | Menulis test case untuk fitur **Dashboard**, **Daftar Pasien (Patient List)**, dan **Riwayat Pemeriksaan (Examination History)** |
| **Zizah** | [NIM] | `zizah-testing` | Integrasi semua fitur, menambahkan test **Upload Foto Rontgen** dan **End-to-End flow**, serta review keseluruhan |

> **Catatan:** Branch `zizah-testing` merupakan branch final yang berisi seluruh hasil pekerjaan kelompok yang telah diintegrasikan.

---

## 📁 Struktur Repository

```
PPPL-UAS/
├── pom.xml                          # Konfigurasi Maven & dependency
├── README.md                        # Dokumentasi proyek (file ini)
├── bug_report.md                    # Laporan bug yang ditemukan
└── src/
    ├── main/
    │   └── java/org/example/
    │       └── Main.java
    └── test/
        ├── java/
        │   ├── pages/               # Page Object Model
        │   │   ├── LoginPage.java
        │   │   ├── RegisterPage.java
        │   │   ├── ProfilePage.java
        │   │   ├── DashboardPage.java
        │   │   ├── PatientListPage.java
        │   │   ├── ExaminationHistoryPage.java
        │   │   └── UploadPage.java
        │   ├── steps/               # Step Definitions (auth & profile)
        │   │   ├── AuthSteps.java
        │   │   ├── ProfileSteps.java
        │   │   └── Hooks.java
        │   ├── stepsdashboard/      # Step Definitions (dashboard & fitur lanjutan)
        │   │   ├── DashboardSteps.java
        │   │   ├── PatientListSteps.java
        │   │   ├── ExaminationHistorySteps.java
        │   │   └── HooksDashboard.java
        │   └── runners/             # Test Runner (Cucumber)
        │       ├── TestRunner.java
        │       ├── TestRunnerDashboard.java
        │       ├── TestRunnerPatientList.java
        │       └── TestRunnerExaminationHistory.java
        └── resources/
            └── features/            # File Gherkin (.feature)
                ├── Login.feature
                ├── Register.feature
                ├── Profile.feature
                ├── Dashboard.feature
                ├── PatientList.feature
                ├── ExaminationHistory.feature
                ├── UploadRontgen.feature
                └── EndToEnd.feature
```

---

## 🚀 Cara Menjalankan Test

### Prasyarat
- Java 17+
- Maven 3.8+
- Google Chrome + ChromeDriver (sesuaikan versi)

### Menjalankan Semua Test
```bash
mvn test
```

### Menjalankan Test Berdasarkan Tag
```bash
# Hanya test Login
mvn test -Dcucumber.filter.tags="@Login"

# Hanya test Positive
mvn test -Dcucumber.filter.tags="@Positive"

# Hanya End-to-End
mvn test -Dcucumber.filter.tags="@EndToEnd"

# Hanya Upload Rontgen
mvn test -Dcucumber.filter.tags="@UploadRontgen"
```

### Melihat Laporan
Setelah test selesai, laporan HTML tersedia di:
```
target/cucumber-reports/
```

---

## 🐛 Bug Report

Lihat file [`bug_report.md`](./bug_report.md) untuk daftar lengkap bug yang ditemukan selama pengujian.

---

## 📚 Referensi
- [Cucumber Documentation](https://cucumber.io/docs)
- [Selenium WebDriver](https://www.selenium.dev/documentation/)
- [Page Object Model](https://www.selenium.dev/documentation/test_practices/encouraged/page_object_models/)
