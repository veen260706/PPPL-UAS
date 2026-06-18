Feature: Profile Dental X-Ray

  @Profile @Positive
  Scenario: Logout berhasil
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Logout
    Then User berhasil logout dan diarahkan ke halaman Login

  @Profile @Negative
  Scenario: Ganti password dengan current password salah
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Password
    And User memasukkan current password "SalahPassword"
    And User memasukkan new password "NewPassword123"
    And User memasukkan confirm new password "NewPassword123"
    And User menekan tombol Save Password
    Then Sistem menampilkan pesan error profile

  @Profile @Negative
  Scenario: Ganti password dengan konfirmasi tidak cocok
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Password
    And User memasukkan current password "Password123"
    And User memasukkan new password "NewPassword123"
    And User memasukkan confirm new password "BedaPassword"
    And User menekan tombol Save Password
    Then Sistem menampilkan pesan error profile

  @Profile @Positive
  Scenario: Ganti password dengan data valid
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Password
    And User memasukkan current password "Password123"
    And User memasukkan new password "NewPassword123"
    And User memasukkan confirm new password "NewPassword123"
    And User menekan tombol Save Password
    Then Password berhasil diubah

  @Profile @Negative
  Scenario: Ganti email dengan password verifikasi salah
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Email
    And User memasukkan verify password "SalahPassword"
    And User memasukkan new email "newemail@gmail.com"
    And User menekan tombol Save Email
    Then Sistem menampilkan pesan error profile

  @Profile @Negative
  Scenario: Ganti email dengan format email tidak valid
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Email
    And User memasukkan verify password "Password123"
    And User memasukkan new email "emailtidakvalid"
    And User menekan tombol Save Email
    Then Sistem menampilkan pesan error profile

  @Profile @Positive
  Scenario: Ganti email dengan data valid
    Given User sudah login dan berada di halaman Profile
    When User menekan tombol Change Email
    And User memasukkan verify password "Password123"
    And User memasukkan new email "testloginbaru@gmail.com"
    And User menekan tombol Save Email
    Then Email berhasil diubah

  @Profile @Positive @Cleanup
    Scenario: Mengembalikan email dan password ke data awal untuk testing berikutnya
      Given User sudah login dengan email baru "testloginbaru@gmail.com"
      When User mengganti kembali emailnya menjadi email awal
      And User mengganti kembali passwordnya menjadi "Password123"
      Then Data testing berhasil di-reset ke awal