@UploadRontgen
Feature: Upload Foto Rontgen

  Sebagai admin klinik gigi
  Saya ingin mengunggah foto rontgen pasien
  Agar dapat disimpan ke riwayat pemeriksaan

  Background:
    Given admin sudah login
    And admin memilih pasien yang hadir
    And admin berada di halaman Upload Foto Rontgen

  @TC_UPLOAD_001
  Scenario: Upload foto JPG berhasil
    When admin upload foto JPG
    Then thumbnail foto berhasil ditampilkan

  @TC_UPLOAD_002
  Scenario: Upload foto PNG berhasil
    When admin upload foto PNG
    Then thumbnail foto berhasil ditampilkan

  @TC_UPLOAD_003
  Scenario: Upload file gambar GIF ditolak sistem
    When admin upload file gambar GIF
    Then sistem menolak upload file

  @TC_UPLOAD_004
  Scenario: Hapus foto sebelum save
    Given admin sudah mengupload foto
    When admin menghapus foto
    Then foto hilang dari preview

  @TC_UPLOAD_005
  Scenario: Upload 9 foto
    When admin upload 9 foto
    Then seluruh foto berhasil ditampilkan

  @TC_UPLOAD_006
  Scenario: Upload tepat 10 foto
    When admin upload 10 foto
    Then seluruh foto berhasil ditampilkan

  @TC_UPLOAD_007
  Scenario: Upload lebih dari batas maksimal
    When admin upload 11 foto
    Then muncul pesan batas maksimal foto tercapai