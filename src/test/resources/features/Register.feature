Feature: Register Akun Dental X-Ray

  # =================== POSITIVE TEST ===================

  @Register @Positive
  Scenario: Register dengan data lengkap dan valid
    Given User berada di halaman Register
    When User memasukkan nama "Dr. Andi"
    And User memasukkan email "andi@gmail.com"
    And User memasukkan password "Password123"
    And User memasukkan konfirmasi password "Password123"
    And User menyetujui syarat dan ketentuan
    And User menekan tombol Register
    Then Akun berhasil dibuat dan diarahkan ke halaman Login

  # =================== NEGATIVE TEST ===================

  @Register @Negative
  Scenario: Register dengan password kurang dari 8 karakter
    Given User berada di halaman Register
    When User memasukkan nama "Dr. Andi"
    And User memasukkan email "andi2@gmail.com"
    And User memasukkan password "Pass1"
    And User memasukkan konfirmasi password "Pass1"
    And User menyetujui syarat dan ketentuan
    And User menekan tombol Register
    Then Sistem menampilkan pesan error register

  @Register @Negative
  Scenario: Register dengan konfirmasi password tidak cocok
    Given User berada di halaman Register
    When User memasukkan nama "Dr. Andi"
    And User memasukkan email "andi3@gmail.com"
    And User memasukkan password "Password123"
    And User memasukkan konfirmasi password "Password999"
    And User menyetujui syarat dan ketentuan
    And User menekan tombol Register
    Then Sistem menampilkan pesan error register

  @Register @Negative
  Scenario: Register tanpa mengisi nama
    Given User berada di halaman Register
    When User memasukkan nama ""
    And User memasukkan email "andi4@gmail.com"
    And User memasukkan password "Password123"
    And User memasukkan konfirmasi password "Password123"
    And User menyetujui syarat dan ketentuan
    And User menekan tombol Register
    Then Sistem menampilkan pesan error register