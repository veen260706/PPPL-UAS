Feature: Login Dental X-Ray

  # =================== POSITIVE TEST ===================

  @Login @Positive
  Scenario: Login dengan kredensial valid
    Given User berada di halaman Login
    When User memasukkan email "testlogin@gmail.com" dan password "Password123"
    And User menekan tombol Login
    Then User berhasil masuk dan melihat Dashboard

  # =================== NEGATIVE TEST ===================

  @Login @Negative
  Scenario: Login dengan password salah
    Given User berada di halaman Login
    When User memasukkan email "testlogin@gmail.com" dan password "SalahPass99"
    And User menekan tombol Login
    Then Login gagal dan muncul pesan error

  @Login @Negative
  Scenario: Login dengan email tidak terdaftar
    Given User berada di halaman Login
    When User memasukkan email "tidakada@gmail.com" dan password "Password123"
    And User menekan tombol Login
    Then Login gagal dan muncul pesan error

  @Login @Negative
  Scenario: Login dengan email dan password kosong
    Given User berada di halaman Login
    When User memasukkan email "" dan password ""
    And User menekan tombol Login
    Then Login gagal dan muncul pesan error