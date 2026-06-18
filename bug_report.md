# 🐛 Bug Report — Pengujian Aplikasi Dental X-Ray

**Proyek:** PPPL UAS — Automated Testing Dental X-Ray  
**Repository:** https://github.com/veen260706/PPPL-UAS (branch: `gurveen---testing` / `zizah-testing` / `devia-testing`)  
**Tanggal Pengujian:** Juni 2026  
**Framework:** Appium & Selenium WebDriver + Cucumber BDD  
**Tester:** Kelompok PPPL UAS (Gurveender J Kaur, Azizah, Devia)  

---

## Ringkasan Eksekutif

| Kategori | Jumlah |
|---|---|
| Total Bug Ditemukan | 6 |
| 🔴 Critical | 1 |
| 🟠 High | 0 |
| 🟡 Medium | 3 |
| 🟢 Low | 2 |
| ✅ Bug Fixed | 0 |
| ⏳ Bug Open | 6 |

---

## Detail Bug Report



### BUG-004 — Profile: Ganti Password dengan Current Password Salah Tidak Diblokir

| Field | Detail |
|---|---|
| **ID** | BUG-004 |
| **Feature** | Profile / Change Password |
| **Severity** | 🔴 Critical |
| **Status** | Open |
| **Test Case** | `Profile.feature` — Scenario: Ganti password dengan current password salah |

**Deskripsi:**
Fitur ganti password tidak memvalidasi "current password" dengan benar. User dapat mencoba mengganti password dengan current password yang salah tanpa sistem memblokir atau menampilkan pesan error yang tepat.

**Langkah Reproduksi:**
1. Login ke aplikasi
2. Buka halaman Profile
3. Klik tombol "Change Password"
4. Isi Current Password: `SalahPassword` *(salah)*
5. Isi New Password: `NewPassword123`
6. Submit form

**Expected Result:**
Sistem menampilkan pesan: _"Current password salah"_ dan tidak memproses perubahan password.

**Actual Result:**
Sistem memproses perubahan atau tidak memberikan feedback yang jelas, sehingga menimbulkan risiko keamanan.

**Impact:** Potensi celah keamanan — akun dapat diambil alih jika ada akses fisik ke device.

---

### BUG-005 — Dashboard: Grid Statistik Pasien Terkadang Tidak Load

| Field | Detail |
|---|---|
| **ID** | BUG-005 |
| **Feature** | Dashboard |
| **Severity** | 🟡 Medium |
| **Status** | Open |
| **Test Case** | `Dashboard.feature` — Scenario: Statistik pasien tampil dengan benar |

**Deskripsi:**
Grid statistik pada Dashboard (Hadir, Rontgen, dsb.) terkadang tidak muncul saat halaman pertama kali dibuka. Diperlukan refresh halaman untuk menampilkan data.

**Langkah Reproduksi:**
1. Login ke aplikasi
2. Amati halaman Dashboard
3. Perhatikan bagian Grid Statistik

**Expected Result:**
Grid statistik langsung tampil dengan data terkini setelah login.

**Actual Result:**
Grid statistik terkadang kosong/tidak load. Perlu refresh manual.

---

### BUG-006 — Patient List: Fitur Search Bar Tidak Berfungsi

| Field | Detail |
|---|---|
| **ID** | BUG-006 |
| **Feature** | Patient List |
| **Severity** | 🟡 Medium |
| **Status** | Open |
| **Test Case** | `PatientList.feature` — Scenario: Search pasien |

**Deskripsi:**
Search bar pada halaman Daftar Pasien tidak menyaring daftar saat user memasukkan kata kunci. Daftar pasien tetap menampilkan semua data tanpa filter.

**Langkah Reproduksi:**
1. Login dan buka halaman Daftar Pasien
2. Klik search bar
3. Ketik nama pasien yang ada, misal "Budi"

**Expected Result:**
Daftar pasien disaring dan hanya menampilkan pasien dengan nama "Budi".

**Actual Result:**
Seluruh daftar pasien tetap ditampilkan tanpa filtering.

---

### BUG-007 — Examination History: Data History Tidak Ter-update Real-time

| Field | Detail |
|---|---|
| **ID** | BUG-007 |
| **Feature** | Examination History |
| **Severity** | 🟡 Medium |
| **Status** | Open |
| **Test Case** | `ExaminationHistory.feature` — Scenario: Data pasien pada riwayat pemeriksaan muncul |

**Deskripsi:**
Setelah admin melakukan upload foto rontgen baru, data pada tab "History" tidak langsung diperbarui. User harus keluar dan masuk kembali ke halaman untuk melihat data terbaru.

**Langkah Reproduksi:**
1. Login, pilih pasien
2. Upload foto rontgen baru
3. Navigasi ke tab "History"

**Expected Result:**
Data riwayat langsung diperbarui menampilkan upload terbaru.

**Actual Result:**
Tab History menampilkan data lama. Perlu navigasi ulang.

---

### BUG-008 — Upload Rontgen: Format File Tidak Divalidasi di Frontend

| Field | Detail |
|---|---|
| **ID** | BUG-008 |
| **Feature** | Upload Foto Rontgen |
| **Severity** | 🟢 Low |
| **Status** | Open |
| **Test Case** | `UploadRontgen.feature` — Scenario TC_UPLOAD_002 |

**Deskripsi:**
Halaman Upload tidak memvalidasi format file sebelum upload. User dapat mencoba mengupload file dengan format yang tidak didukung (misal `.pdf`, `.txt`) tanpa mendapat peringatan di sisi frontend. Error baru muncul dari server.

**Langkah Reproduksi:**
1. Login dan buka halaman Upload Rontgen
2. Pilih file format `.pdf` atau `.txt`
3. Klik tombol Upload

**Expected Result:**
Validasi di frontend menampilkan: _"Format file tidak didukung. Harap upload JPG atau PNG."_

**Actual Result:**
File diterima di frontend, error baru ditampilkan dari sisi server setelah proses upload, pengalaman user kurang baik.

---

### BUG-009 — End-to-End: Alur Register → Login Terganggu oleh Redirect

| Field | Detail |
|---|---|
| **ID** | BUG-009 |
| **Feature** | End-to-End |
| **Severity** | 🟢 Low |
| **Status** | Open |
| **Test Case** | `EndToEnd.feature` — Full flow |

**Deskripsi:**
Dalam alur end-to-end, setelah registrasi berhasil, sistem tidak selalu mengarahkan user ke halaman Login secara otomatis. Terkadang user tetap di halaman Register atau diarahkan ke halaman yang tidak sesuai.

**Langkah Reproduksi:**
1. Buka halaman Register
2. Isi semua data dengan valid
3. Submit form registrasi

**Expected Result:**
Setelah berhasil register, user otomatis diarahkan ke halaman Login dengan pesan sukses.

**Actual Result:**
Redirect tidak konsisten — terkadang tetap di halaman Register, terkadang ke halaman lain.

---

## Matriks Pengujian

| ID | Feature | Skenario | Status Test | Bug? |
|---|---|---|---|---|
| TC-L-001 | Login | Login dengan kredensial valid | ✅ Pass | - |
| TC-L-002 | Login | Login dengan password salah | ✅ Pass | - |
| TC-R-001 | Register | Register dengan data valid | ✅ Pass | - |
| TC-R-002 | Register | Register dengan email duplikat | ✅ Pass | - |
| TC-R-003 | Register | Register dengan konfirmasi password beda | ✅ Pass | - |
| TC-P-001 | Profile | Logout berhasil | ✅ Pass | - |
| TC-P-002 | Profile | Ganti password dengan current password salah | ❌ Fail | BUG-004 |
| TC-D-001 | Dashboard | Melihat halaman dashboard setelah login | ✅ Pass | - |
| TC-D-002 | Dashboard | Statistik pasien tampil dengan benar | ⚠️ Flaky | BUG-005 |
| TC-PL-001 | Patient List | Halaman daftar pasien ditampilkan | ✅ Pass | - |
| TC-PL-002 | Patient List | Tab filter ditampilkan | ✅ Pass | - |
| TC-PL-003 | Patient List | Search bar pasien berfungsi | ❌ Fail | BUG-006 |
| TC-EH-001 | Exam History | Tab History berhasil diakses | ✅ Pass | - |
| TC-EH-002 | Exam History | Data history ter-update setelah upload | ❌ Fail | BUG-007 |
| TC-U-001 | Upload Rontgen | Upload foto JPG berhasil | ✅ Pass | - |
| TC-U-002 | Upload Rontgen | Validasi format file tidak didukung | ❌ Fail | BUG-008 |
| TC-E2E-001 | End-to-End | Alur Register → Login → Dashboard | ⚠️ Partial | BUG-009 |

---

## Kesimpulan

Dari 17 skenario yang diuji:
- ✅ **11 Pass** — Fungsionalitas berjalan sesuai ekspektasi
- ❌ **4 Fail** — Ditemukan bug yang perlu diperbaiki
- ⚠️ **2 Flaky/Partial** — Hasil tidak konsisten

**Rekomendasi Prioritas Perbaikan:**
1. 🔴 **BUG-004** (Critical) — Keamanan ganti password harus diperbaiki segera
2. 🟡 **BUG-005, BUG-006, BUG-007** (Medium) — UI/UX improvements
3. 🟢 **BUG-008, BUG-009** (Low) — Perbaikan alur & validasi file

---

*Bug report ini dibuat berdasarkan hasil eksekusi automated test menggunakan Selenium WebDriver + Cucumber BDD.*
