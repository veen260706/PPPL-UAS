Feature: End-to-End Dental X-Ray Flow

  Scenario: User register, login, pilih pasien, upload rontgen, simpan data, dan cek history
    Given user berada di halaman Register
    When user mendaftar dengan data yang valid
    Then user berhasil register dan diarahkan ke halaman Login

    When user login dengan akun yang baru didaftarkan
    Then user berhasil masuk ke Dashboard

    When user memilih salah satu pasien dari daftar pasien hadir
    Then halaman form pemeriksaan/upload rontgen terbuka

    When user mengupload foto rontgen dan mengisi data pemeriksaan
    And user menekan tombol simpan
    Then data pemeriksaan berhasil tersimpan

    When user membuka halaman Examination History
    And user mencari data pasien yang baru disimpan
    Then data riwayat pemeriksaan pasien tersebut muncul dengan benar