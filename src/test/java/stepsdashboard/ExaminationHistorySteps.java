package stepsdashboard;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.ExaminationHistoryPage;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.Arrays;

public class ExaminationHistorySteps {

    private AndroidDriver driver;
    private ExaminationHistoryPage examHistoryPage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    // ===================== NAVIGASI =====================

    @Given("User sudah login dan berada di halaman Rontgen")
    public void user_sudah_login_dan_berada_di_halaman_rontgen() {
        driver = HooksDashboard.driver;
        examHistoryPage = new ExaminationHistoryPage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        navigasiKeTabRontgen();

        // Verifikasi tab Rontgen/History wrapper tampil
        boolean halamanRontgenMuncul = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'History') or " +
                                    "contains(@text,'Perlu Upload Foto')]")
                    ));
            halamanRontgenMuncul = true;
            System.out.println("LOG: Halaman Rontgen berhasil dimuat.");
        } catch (Exception e) {
            System.out.println("LOG: Halaman Rontgen tidak terdeteksi: " + e.getMessage());
        }

        Assert.assertTrue(halamanRontgenMuncul, "Gagal! Halaman Rontgen tidak muncul.");
    }

    @Given("User sudah login dan berada di tab History")
    public void user_sudah_login_dan_berada_di_tab_history() {
        driver = HooksDashboard.driver;
        examHistoryPage = new ExaminationHistoryPage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        // Cek apakah sudah di tab History
        boolean sudahDiHistory = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'All Time')]")
                    ));
            sudahDiHistory = true;
            System.out.println("LOG: Sudah di tab History.");
        } catch (Exception ignored) {}

        if (!sudahDiHistory) {
            // Navigasi ke halaman Rontgen dulu
            navigasiKeTabRontgen();

            // Lalu tap tab History
            try {
                examHistoryPage.clickTabHistory();
                new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(@text,'All Time')]")
                        ));
                sudahDiHistory = true;
                System.out.println("LOG: Berhasil masuk tab History.");
            } catch (Exception e) {
                System.out.println("LOG: Tab History gagal: " + e.getMessage());
            }
        }

        // Reset search bar dan filter ke kondisi awal
        resetTabHistory();

        Assert.assertTrue(sudahDiHistory, "Gagal! Tab History tidak muncul.");
        System.out.println("LOG: Tab History berhasil dimuat.");
    }

    // ── Helper navigasi ke tab Rontgen (tab ke-3) ────────────────────────────
    // Dari ui.xml: bounds tab ke-3 [288,1416][432,1516] → titik tengah x=360, y=1466
    private void navigasiKeTabRontgen() {
        // Cek apakah sudah di halaman Rontgen
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'History') or " +
                                    "contains(@text,'Perlu Upload Foto')]")
                    ));
            System.out.println("LOG: Sudah di halaman Rontgen.");
            return;
        } catch (Exception ignored) {}

        // Jika app sedang di halaman dalam (nested screen), tekan back sampai ke root
        // Cek apakah ada tombol back / bukan di halaman utama
        for (int i = 0; i < 3; i++) {
            boolean diHalamanDalam = false;
            try {
                // Indikator halaman dalam: ada tombol back pojok kiri (bukan tab bar)
                // dan tidak ada teks "TentangDental" atau tab bar
                new WebDriverWait(driver, Duration.ofSeconds(2))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(@text,'TentangDental') or " +
                                        "contains(@text,'History') or " +
                                        "contains(@text,'Perlu Upload Foto')]")
                        ));
                System.out.println("LOG: Sudah di halaman utama, hentikan back loop.");
                break;
            } catch (Exception ignored) {
                diHalamanDalam = true;
            }
            if (diHalamanDalam) {
                try {
                    driver.navigate().back();
                    Thread.sleep(1500);
                    System.out.println("LOG: Menekan back untuk kembali ke halaman utama (loop " + (i+1) + ").");
                } catch (Exception ignored) {}
            }
        }

        // Cek apakah perlu login dulu
        boolean sudahLogin = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'TentangDental')]")
                    ));
            sudahLogin = true;
        } catch (Exception ignored) {}

        if (!sudahLogin) {
            try {
                By btnGetStarted = By.xpath("//*[contains(@text,'Get Started')]");
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
                Thread.sleep(1500);
            } catch (Exception ignored) {}

            try {
                loginPage.enterEmail("admin@dental.gmail.com");
                loginPage.enterPassword("admin123");
                loginPage.clickLogin();
                System.out.println("LOG: Login dilakukan.");
                Thread.sleep(5000);
            } catch (Exception e) {
                System.out.println("LOG: Login gagal: " + e.getMessage());
            }
        }

        // Tap tab Rontgen (tab ke-3, bounds [288,1416][432,1516], titik tengah x=360, y=1466)
        System.out.println("LOG: Tap tab Rontgen via koordinat.");
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO,
                    PointerInput.Origin.viewport(), 360, 1466));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Arrays.asList(tap));
            Thread.sleep(3000);
            System.out.println("LOG: Tab Rontgen ditekan.");
        } catch (Exception e) {
            System.out.println("LOG: Tap tab Rontgen gagal: " + e.getMessage());
        }
    }

    // ── Helper reset state tab History ───────────────────────────────────────
    private void resetTabHistory() {
        // Clear search bar
        examHistoryPage.clearSearch();

        // Reset filter ke All Time
        try {
            By filterAllTime = By.xpath(
                    "//*[contains(@text,'All Time')]" +
                            "/ancestor::android.view.ViewGroup[@clickable='true'][1]"
            );
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(filterAllTime)).click();
            } catch (Exception ignored) {
                // Fallback: cari langsung elemen yang clickable dengan teks All Time
                By filterDirect = By.xpath(
                        "//*[contains(@text,'All Time') and @clickable='true']"
                );
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(filterDirect)).click();
            }
            Thread.sleep(1000);
            System.out.println("LOG: Filter direset ke All Time.");
        } catch (Exception ignored) {}

        // Scroll ke atas
        try {
            driver.executeScript("mobile: scroll",
                    java.util.Map.of("direction", "up", "percent", 1.0));
            Thread.sleep(500);
        } catch (Exception ignored) {}
    }

    @And("Terdapat minimal satu riwayat pemeriksaan")
    public void terdapat_minimal_satu_riwayat_pemeriksaan() {
        boolean ada = examHistoryPage.hasRiwayatInList();
        Assert.assertTrue(ada,
                "Tidak ada riwayat pemeriksaan. Pastikan ada data dengan status 'selesai'.");
        System.out.println("LOG: Riwayat pemeriksaan tidak kosong.");
    }

    // ===================== VERIFIKASI TAMPILAN =====================

    @Then("Tab History aktif dan ditampilkan")
    public void tab_history_aktif_dan_ditampilkan() {
        Assert.assertTrue(examHistoryPage.isTabHistoryDisplayed(),
                "Gagal! Tab History tidak terdeteksi.");
        System.out.println("LOG: Tab History terverifikasi aktif.");
    }

    @Then("Daftar riwayat pemeriksaan ditampilkan")
    public void daftar_riwayat_pemeriksaan_ditampilkan() {
        Assert.assertTrue(examHistoryPage.isDaftarRiwayatDisplayed(),
                "Gagal! Daftar riwayat pemeriksaan tidak ditampilkan.");
        System.out.println("LOG: Daftar riwayat terverifikasi.");
    }

    @Then("Kartu riwayat menampilkan nama pasien")
    public void kartu_riwayat_menampilkan_nama_pasien() {
        Assert.assertTrue(examHistoryPage.isNamaPasienRiwayatDisplayed(),
                "Gagal! Nama pasien tidak ditemukan di kartu riwayat.");
        System.out.println("LOG: Nama pasien di riwayat terverifikasi.");
    }

    @Then("Kartu riwayat menampilkan waktu pemeriksaan")
    public void kartu_riwayat_menampilkan_waktu_pemeriksaan() {
        Assert.assertTrue(examHistoryPage.isWaktuPemeriksaanDisplayed(),
                "Gagal! Waktu pemeriksaan tidak ditemukan.");
        System.out.println("LOG: Waktu pemeriksaan terverifikasi.");
    }

    @Then("Kartu riwayat menampilkan nama dokter")
    public void kartu_riwayat_menampilkan_nama_dokter() {
        Assert.assertTrue(examHistoryPage.isNamaDokterRiwayatDisplayed(),
                "Gagal! Nama dokter tidak ditemukan di kartu riwayat.");
        System.out.println("LOG: Nama dokter terverifikasi.");
    }

    @Then("Tidak ada hasil riwayat yang ditampilkan")
    public void tidak_ada_hasil_riwayat_yang_ditampilkan() {
        Assert.assertTrue(examHistoryPage.isEmptyStateDisplayed(),
                "Gagal! Seharusnya tidak ada hasil tapi data masih tampil.");
        System.out.println("LOG: Empty state terverifikasi.");
    }

    @Then("Halaman history dapat ditampilkan tanpa error")
    public void halaman_history_dapat_ditampilkan_tanpa_error() {
        Assert.assertTrue(examHistoryPage.isHalamanTampilTanpaError(),
                "Gagal! Halaman history error atau crash.");
        System.out.println("LOG: Halaman history valid tanpa error.");
    }

    // ===================== AKSI USER =====================

    @When("User menekan tab {string}")
    public void user_menekan_tab(String namaTab) {
        if (namaTab.equalsIgnoreCase("History")) {
            examHistoryPage.clickTabHistory();
        }
        System.out.println("LOG: Tab '" + namaTab + "' ditekan.");
    }

    @When("User menekan filter waktu {string}")
    public void user_menekan_filter_waktu(String label) {
        examHistoryPage.clickFilterWaktu(label);
        System.out.println("LOG: Filter waktu '" + label + "' ditekan.");
    }

    @When("User mengetik nama pada search bar history")
    public void user_mengetik_nama_pada_search_bar_history() {
        examHistoryPage.inputSearch("a");
        System.out.println("LOG: Keyword 'a' diinput ke search bar history.");
    }

    @When("User mengetik nama tidak valid pada search bar history")
    public void user_mengetik_nama_tidak_valid_pada_search_bar_history() {
        examHistoryPage.inputSearch("zzzzzzzzzznotfound99999");
        System.out.println("LOG: Keyword tidak valid diinput ke search bar history.");
    }

    @When("User menekan kartu riwayat pertama")
    public void user_menekan_kartu_riwayat_pertama() {
        examHistoryPage.clickKartuRiwayatPertama();
        System.out.println("LOG: Kartu riwayat pertama ditekan.");
    }

    @When("User menekan tombol kembali dari Exam Details")
    public void user_menekan_tombol_kembali_dari_exam_details() {
        examHistoryPage.clickTombolKembali();
        System.out.println("LOG: Tombol kembali dari Exam Details ditekan.");
    }

    // ===================== VERIFIKASI EXAM DETAILS =====================

    @Then("Halaman Exam Details ditampilkan")
    public void halaman_exam_details_ditampilkan() {
        Assert.assertTrue(examHistoryPage.isExamDetailsDisplayed(),
                "Gagal! Halaman Exam Details tidak muncul.");
        System.out.println("LOG: Halaman Exam Details terverifikasi.");
    }

    @Then("Data pasien pada Exam Details ditampilkan")
    public void data_pasien_pada_exam_details_ditampilkan() {
        Assert.assertTrue(examHistoryPage.isDataPasienExamDetailsDisplayed(),
                "Gagal! Section DATA PASIEN tidak ditemukan.");
        System.out.println("LOG: Section DATA PASIEN terverifikasi.");
    }

    @Then("Nama pasien ditampilkan di Exam Details")
    public void nama_pasien_ditampilkan_di_exam_details() {
        Assert.assertTrue(examHistoryPage.isNamaPasienExamDetailsDisplayed(),
                "Gagal! Nama pasien tidak ditemukan di Exam Details.");
        System.out.println("LOG: Nama pasien di Exam Details terverifikasi.");
    }

    @Then("Nama dokter ditampilkan di Exam Details")
    public void nama_dokter_ditampilkan_di_exam_details() {
        Assert.assertTrue(examHistoryPage.isNamaDokterExamDetailsDisplayed(),
                "Gagal! Nama dokter tidak ditemukan di Exam Details.");
        System.out.println("LOG: Nama dokter di Exam Details terverifikasi.");
    }

    @Then("Bagian pemeriksaan fisik ditampilkan di Exam Details")
    public void bagian_pemeriksaan_fisik_ditampilkan_di_exam_details() {
        Assert.assertTrue(examHistoryPage.isSectionPemeriksaanFisikDisplayed(),
                "Gagal! Section Pemeriksaan Fisik tidak ditemukan.");
        System.out.println("LOG: Section Pemeriksaan Fisik terverifikasi.");
    }
}