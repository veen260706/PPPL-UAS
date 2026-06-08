Feature: Dashboard Beranda Dental X-Ray

  # =================== POSITIVE TEST ===================

  @Dashboard @Positive
  Scenario: Melihat halaman Dashboard setelah login
    Given User sudah login dan berada di halaman Dashboard
    Then Header "TentangDental" ditampilkan
    And Kartu selamat datang dengan nama admin ditampilkan
    And Grid statistik pasien ditampilkan

  @Dashboard @Positive
  Scenario: Statistik pasien tampil dengan benar
    Given User sudah login dan berada di halaman Dashboard
    Then Grid statistik menampilkan label "Hadir"
    And Grid statistik menampilkan label "Rontgen"
    And Grid statistik menampilkan label "Selesai"
    And Grid statistik menampilkan label "Di ruangan"

  @Dashboard @Positive
  Scenario: Daftar pasien hadir hari ini ditampilkan
    Given User sudah login dan berada di halaman Dashboard
    Then Judul "Pasien Hadir Hari Ini" ditampilkan
    And Tombol "Semua" untuk navigasi ke daftar pasien tersedia

  @Dashboard @Positive
  Scenario: Navigasi ke halaman Daftar Pasien melalui tombol Semua
    Given User sudah login dan berada di halaman Dashboard
    When User menekan tombol "Semua" di bagian Pasien Hadir
    Then Halaman daftar pasien ditampilkan

  @Dashboard @Positive
  Scenario: Tap kartu pasien membuka modal ubah status
    Given User sudah login dan berada di halaman Dashboard
    And Terdapat minimal satu pasien dalam daftar hadir
    When User menekan kartu pasien pertama
    Then Modal "Ubah Status Pasien" ditampilkan

  @Dashboard @Positive
  Scenario: Mengubah status pasien menjadi Menunggu
    Given User sudah login dan berada di halaman Dashboard
    And Terdapat minimal satu pasien dalam daftar hadir
    When User menekan kartu pasien pertama
    And User memilih status "Menunggu" pada modal
    And User menekan tombol "Simpan Status" pada modal
    Then Modal berhasil ditutup

  @Dashboard @Positive
  Scenario: Mengubah status pasien menjadi Masuk Ruangan
    Given User sudah login dan berada di halaman Dashboard
    And Terdapat minimal satu pasien dalam daftar hadir
    When User menekan kartu pasien pertama
    And User memilih status "Masuk Ruangan" pada modal
    And User menekan tombol "Simpan Status" pada modal
    Then Modal berhasil ditutup

  # =================== NEGATIVE TEST ===================

  @Dashboard @Negative
  Scenario: Tombol Simpan Status tidak aktif sebelum memilih status
    Given User sudah login dan berada di halaman Dashboard
    And Terdapat minimal satu pasien dalam daftar hadir
    When User menekan kartu pasien pertama
    Then Tombol "Simpan Status" dalam kondisi tidak aktif

  @Dashboard @Negative
  Scenario: Modal ubah status dapat dibatalkan
    Given User sudah login dan berada di halaman Dashboard
    And Terdapat minimal satu pasien dalam daftar hadir
    When User menekan kartu pasien pertama
    And User menekan tombol "Batal" pada modal
    Then Modal berhasil ditutup