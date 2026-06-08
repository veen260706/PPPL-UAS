Feature: Daftar Pasien Hadir Dental X-Ray

  # =================== POSITIVE TEST ===================

  @PatientList @Positive
  Scenario: Halaman daftar pasien berhasil ditampilkan
    Given User sudah login dan berada di halaman Daftar Pasien
    Then Judul "Daftar Pasien Hadir" ditampilkan di halaman pasien
    And Search bar pasien ditampilkan

  @PatientList @Positive
  Scenario: Tab filter pasien ditampilkan dengan benar
    Given User sudah login dan berada di halaman Daftar Pasien
    Then Tab filter "Semua" ditampilkan
    And Tab filter "Menunggu" ditampilkan
    And Tab filter "Di Ruangan" ditampilkan

  @PatientList @Positive
  Scenario: Data pasien ditampilkan dalam daftar
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    Then Kartu pasien menampilkan nama pasien
    And Kartu pasien menampilkan nomor antrean

  @PatientList @Positive
  Scenario: Filter tab Semua menampilkan semua pasien
    Given User sudah login dan berada di halaman Daftar Pasien
    When User menekan tab filter "Semua" di halaman pasien
    Then Daftar pasien ditampilkan sesuai filter

  @PatientList @Positive
  Scenario: Filter tab Menunggu menampilkan pasien berstatus menunggu
    Given User sudah login dan berada di halaman Daftar Pasien
    When User menekan tab filter "Menunggu" di halaman pasien
    Then Daftar pasien ditampilkan sesuai filter

  @PatientList @Positive
  Scenario: Pencarian pasien berdasarkan nama
    Given User sudah login dan berada di halaman Daftar Pasien
    When User mengetik nama pada search bar pasien
    Then Daftar pasien ditampilkan sesuai filter

  @PatientList @Positive
  Scenario: Tap kartu pasien membuka modal ubah status
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    Then Modal ubah status pasien muncul di halaman pasien

  @PatientList @Positive
  Scenario: Opsi status ditampilkan dalam modal
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    Then Opsi status "Menunggu" ditampilkan dalam modal pasien
    And Opsi status "Masuk Ruangan" ditampilkan dalam modal pasien
    And Opsi status "Upload Foto" ditampilkan dalam modal pasien
    And Opsi status "Selesai (Tanpa Foto)" ditampilkan dalam modal pasien

  @PatientList @Positive
  Scenario: Mengubah status pasien menjadi Menunggu dari halaman pasien
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    And User memilih opsi "Menunggu" pada modal pasien
    And User menekan simpan pada modal pasien
    Then Modal ubah status berhasil ditutup

  @PatientList @Positive
  Scenario: Mengubah status pasien menjadi Masuk Ruangan dari halaman pasien
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    And User memilih opsi "Masuk Ruangan" pada modal pasien
    And User menekan simpan pada modal pasien
    Then Modal ubah status berhasil ditutup

  @PatientList @Positive
  Scenario: Modal ubah status dapat ditutup dengan tombol Batal
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    And User menekan batal pada modal pasien
    Then Modal ubah status berhasil ditutup

  # =================== NEGATIVE TEST ===================

  @PatientList @Negative
  Scenario: Pencarian nama yang tidak ada menampilkan empty state
    Given User sudah login dan berada di halaman Daftar Pasien
    When User mengetik nama tidak valid pada search bar
    Then Pesan pasien tidak ditemukan ditampilkan

  @PatientList @Negative
  Scenario: Tombol Simpan Status tidak aktif sebelum memilih opsi
    Given User sudah login dan berada di halaman Daftar Pasien
    And Terdapat minimal satu pasien dalam daftar pasien
    When User menekan kartu pasien pertama di halaman pasien
    Then Tombol simpan status tidak dapat ditekan
