Feature: Examination History Dental X-Ray

  # =================== POSITIVE TEST ===================

  @ExamHistory @Positive
  Scenario: Tab History berhasil diakses dari halaman Rontgen
    Given User sudah login dan berada di halaman Rontgen
    When User menekan tab "History"
    Then Tab History aktif dan ditampilkan

  @ExamHistory @Positive
  Scenario: Daftar riwayat pemeriksaan ditampilkan
    Given User sudah login dan berada di tab History
    Then Daftar riwayat pemeriksaan ditampilkan

  @ExamHistory @Positive
  Scenario: Data pasien pada riwayat pemeriksaan muncul dengan benar
    Given User sudah login dan berada di tab History
    And Terdapat minimal satu riwayat pemeriksaan
    Then Kartu riwayat menampilkan nama pasien
    And Kartu riwayat menampilkan waktu pemeriksaan
    And Kartu riwayat menampilkan nama dokter

  @ExamHistory @Positive
  Scenario: Filter waktu All Time ditampilkan dan dapat ditekan
    Given User sudah login dan berada di tab History
    When User menekan filter waktu "All Time"
    Then Daftar riwayat pemeriksaan ditampilkan

  @ExamHistory @Positive
  Scenario: Filter waktu Today ditampilkan dan dapat ditekan
    Given User sudah login dan berada di tab History
    When User menekan filter waktu "Today"
    Then Daftar riwayat pemeriksaan ditampilkan

  @ExamHistory @Positive
  Scenario: Filter waktu This Week ditampilkan dan dapat ditekan
    Given User sudah login dan berada di tab History
    When User menekan filter waktu "This Week"
    Then Daftar riwayat pemeriksaan ditampilkan

  @ExamHistory @Positive
  Scenario: Pencarian riwayat berdasarkan nama pasien
    Given User sudah login dan berada di tab History
    When User mengetik nama pada search bar history
    Then Daftar riwayat pemeriksaan ditampilkan

  @ExamHistory @Positive
  Scenario: Tap kartu riwayat membuka halaman Exam Details
    Given User sudah login dan berada di tab History
    And Terdapat minimal satu riwayat pemeriksaan
    When User menekan kartu riwayat pertama
    Then Halaman Exam Details ditampilkan

  @ExamHistory @Positive
  Scenario: Informasi pasien ditampilkan di Exam Details
    Given User sudah login dan berada di tab History
    And Terdapat minimal satu riwayat pemeriksaan
    When User menekan kartu riwayat pertama
    Then Data pasien pada Exam Details ditampilkan
    And Nama pasien ditampilkan di Exam Details
    And Nama dokter ditampilkan di Exam Details

  @ExamHistory @Positive
  Scenario: Data pemeriksaan fisik ditampilkan di Exam Details
    Given User sudah login dan berada di tab History
    And Terdapat minimal satu riwayat pemeriksaan
    When User menekan kartu riwayat pertama
    Then Bagian pemeriksaan fisik ditampilkan di Exam Details

  @ExamHistory @Positive
  Scenario: Tombol kembali dari Exam Details berfungsi
    Given User sudah login dan berada di tab History
    And Terdapat minimal satu riwayat pemeriksaan
    When User menekan kartu riwayat pertama
    And User menekan tombol kembali dari Exam Details
    Then Tab History aktif dan ditampilkan

  # =================== NEGATIVE TEST ===================

  @ExamHistory @Negative
  Scenario: Pencarian nama tidak valid menampilkan hasil kosong
    Given User sudah login dan berada di tab History
    When User mengetik nama tidak valid pada search bar history
    Then Tidak ada hasil riwayat yang ditampilkan

  @ExamHistory @Negative
  Scenario: Filter Today saat tidak ada data menampilkan kondisi yang ditangani
    Given User sudah login dan berada di tab History
    When User menekan filter waktu "Today"
    Then Halaman history dapat ditampilkan tanpa error
