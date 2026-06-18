# PPPL UAS — Pengujian Otomatis Aplikasi Dental X-Ray

[![Java](https://img.shields.io/badge/Java-17-orange?logo=java)](https://www.java.com)
[![Appium](https://img.shields.io/badge/Appium-Android-blue?logo=android)](https://appium.io/)
[![Cucumber](https://img.shields.io/badge/Cucumber-BDD-brightgreen?logo=cucumber)](https://cucumber.io/)
[![Maven](https://img.shields.io/badge/Maven-Build-red?logo=apache-maven)](https://maven.apache.org/)

> **Mata Kuliah:** Praktikum Pengujian Perangkat Lunak (PPPL)
> **Tugas:** Ujian Akhir Semester (UAS) Kelompok 4
> **SUT (System Under Test):** Aplikasi Mobile Dental X-Ray (Android APK)

---

## 📌 Penjelasan Singkat SUT (System Under Test)

**SUT:** Aplikasi Mobile **Tentang Dental** — sebuah sistem manajemen klinik gigi berbasis Android yang digunakan oleh admin/tenaga medis untuk:

| Fitur | Deskripsi |
|---|---|
| **Login & Register** | Autentikasi pengguna untuk masuk ke sistem |
| **Dashboard** | Beranda utama yang menampilkan statistik pasien (Hadir, Rontgen, dsb.) |
| **Daftar Pasien** | Manajemen daftar pasien yang hadir, dilengkapi tab filter (Semua / Menunggu / Di Ruangan) |
| **Upload Foto Rontgen** | Admin dapat mengunggah foto rontgen pasien dalam format JPG/PNG |
| **Riwayat Pemeriksaan** | Histori pemeriksaan dan rontgen yang pernah dilakukan |
| **Profile** | Manajemen profil pengguna, ganti password, dan logout |

Aplikasi diuji secara **end-to-end (E2E)** menggunakan otomatisasi berbasis mobile, mulai dari alur Register → Login → Dashboard → Pilih Pasien → Upload Rontgen → Cek History.

---

## 🧪 Penjelasan Singkat Test Suite

Test suite dibangun menggunakan **Cucumber BDD (Behavior-Driven Development)** dengan **Appium Java Client & UiAutomator2 Options**, mengikuti pola **Page Object Model (POM)**.

### Framework & Tools
- **Bahasa:** Java 17
- **Build Tool:** Apache Maven
- **Automation Framework:** Appium (Android Driver) + UiAutomator2
- **Testing Framework:** Cucumber 7 + TestNG
- **Arsitektur:** Page Object Model (POM)
- **Laporan:** Cucumber HTML Report + Extent Reports (Spark & PDF)

### Fitur yang Diuji & Jumlah Skenario

| Feature File | Fitur | Jenis Test | Skenario |
|---|---|---|---|
| `Login.feature` | Autentikasi Login | Positive & Negative | 4 |
| `Register.feature` | Registrasi Akun | Positive & Negative | 4 |
| `Profile.feature` | Profil & Logout & Ganti Password / Email | Positive & Negative | 7 |
| `Dashboard.feature` | Tampilan Dashboard & Statistik | Positive | 2 |
| `PatientList.feature` | Daftar Pasien & Filter Tab | Positive & Negative | 5+ |
| `ExaminationHistory.feature` | Riwayat Pemeriksaan Rontgen | Positive & Negative | 3+ |
| `UploadRontgen.feature` | Upload Foto Rontgen JPG/PNG | Positive & Negative | 2+ |
| `EndToEnd.feature` | Alur Register → Login → Dashboard | E2E | 1 |

---

## 👥 Pembagian Tugas Kelompok

| Anggota | NIM | Branch | Tugas |
|---|---|---|---|
| **Gurveender J Kaur** | 24/544341/SV/25392 | `gurveen---testing` | Menulis test case untuk fitur **Login**, **Register**, dan **Profile** (Page Object, Step Definition, Feature file) |
| **Devia** | 24/542925/SV/25120 | `devia-testing` | Menulis test case untuk fitur **Dashboard**, **Daftar Pasien (Patient List)**, dan **Riwayat Pemeriksaan (Examination History)** |
| **Azizah** | 24/533921/SV/23957 | `zizah-testing` (Default) | Integrasi semua fitur, menambahkan test **Upload Foto Rontgen** dan **End-to-End flow**, serta review keseluruhan |

---

## 📁 Struktur Repository

```
PPPL-UAS/
├── pom.xml                          # Konfigurasi Maven & dependency
├── README.md                        # Dokumentasi proyek (file ini)
├── bug_report.md                    # Laporan bug yang ditemukan
└── src/
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
        │   └── runners/             # Test Runners (Cucumber)
        │       ├── TestRunner.java
        │       ├── RegisterRunner.java
        │       ├── LoginRunner.java
        │       ├── ProfileRunner.java
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
- Appium Server v2+ berjalan di host `http://127.0.0.1:4723/`
- Android Emulator running (sesuaikan deviceName di `Hooks.java`, default: `emulator-5554`)
- APK target di-download dan diletakkan di path yang sesuai (default: `Downloads/Tentang Dental (1).apk`)

### Menjalankan Semua Test
```bash
mvn test
```

### Menjalankan Test Berdasarkan Tag (Overriding)
```bash
# Hanya test Login
mvn test -Dcucumber.filter.tags="@Login"

# Hanya test Profile
mvn test -Dcucumber.filter.tags="@Profile"

# Hanya End-to-End
mvn test -Dcucumber.filter.tags="@EndToEnd"

# Hanya Upload Rontgen
mvn test -Dcucumber.filter.tags="@UploadRontgen"
```

### Menjalankan Lewat Runner Spesifik
Anda juga dapat mengeksekusi test runner secara terpisah di IDE Anda dengan menavigasi ke `src/test/java/runners/` dan men-run file berikut:
- **`TestRunner`**: Menjalankan semua skenario.
- **`RegisterRunner`**: Hanya menjalankan Register.
- **`LoginRunner`**: Hanya menjalankan Login.
- **`ProfileRunner`**: Hanya menjalankan Profile.

---

## 🐛 Bug Report

Lihat file [`bug_report.md`](./bug_report.md) untuk daftar lengkap bug yang ditemukan selama pengujian.
