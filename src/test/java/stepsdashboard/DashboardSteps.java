package stepsdashboard;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.DashboardPage;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.Arrays;

public class DashboardSteps {

    private AndroidDriver driver;
    private DashboardPage dashboardPage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    // ===================== NAVIGASI =====================

    @Given("User sudah login dan berada di halaman Dashboard")
    public void user_sudah_login_dan_berada_di_halaman_dashboard() {
        driver = HooksDashboard.driver;
        dashboardPage = new DashboardPage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        // Cek apakah sudah di Dashboard
        boolean sudahDiDashboard = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'TentangDental')]")
                    ));
            sudahDiDashboard = true;
            System.out.println("LOG: Sudah di Dashboard.");
        } catch (Exception ignored) {}

        // Kalau tidak di Dashboard, tap tab Beranda (koordinat dari ui.xml device OPPO CPH2711)
        // bounds tab Beranda: [0,1416][144,1516] → titik tengah x=72, y=1466
        if (!sudahDiDashboard) {
            System.out.println("LOG: Tidak di Dashboard, tap tab Beranda.");
            try {
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), 72, 1466));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Arrays.asList(tap));
                Thread.sleep(3000);

                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(@text,'TentangDental')]")
                        ));
                sudahDiDashboard = true;
                System.out.println("LOG: Berhasil ke Dashboard via tap tab Beranda.");
            } catch (Exception e) {
                System.out.println("LOG: Tap tab Beranda gagal: " + e.getMessage());
            }
        }

        // Fallback: belum login sama sekali → login dulu
        if (!sudahDiDashboard) {
            try {
                By btnGetStarted = By.xpath("//*[contains(@text,'Get Started')]");
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
                Thread.sleep(1500);
                System.out.println("LOG: Klik Get Started.");
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

        // Scroll ke atas agar semua elemen Dashboard visible
        try {
            driver.executeScript("mobile: scroll",
                    java.util.Map.of("direction", "up", "percent", 1.0));
            Thread.sleep(500);
        } catch (Exception ignored) {}

        boolean dashboardMuncul = dashboardPage.isHeaderDisplayed();
        Assert.assertTrue(dashboardMuncul, "Gagal! Dashboard tidak muncul setelah login.");
        System.out.println("LOG: Dashboard berhasil dimuat.");
    }

    @And("Terdapat minimal satu pasien dalam daftar hadir")
    public void terdapat_minimal_satu_pasien_dalam_daftar_hadir() {
        boolean ada = dashboardPage.hasPasienInList();
        Assert.assertTrue(ada, "Tidak ada pasien dalam daftar hadir hari ini.");
        System.out.println("LOG: Daftar pasien tidak kosong.");
    }

    // ===================== VERIFIKASI TAMPILAN =====================

    @Then("Header {string} ditampilkan")
    public void header_ditampilkan(String judulHeader) {
        Assert.assertTrue(dashboardPage.isHeaderDisplayed(),
                "Gagal! Header '" + judulHeader + "' tidak ditemukan.");
        System.out.println("LOG: Header '" + judulHeader + "' terverifikasi.");
    }

    @Then("Kartu selamat datang dengan nama admin ditampilkan")
    public void kartu_selamat_datang_ditampilkan() {
        Assert.assertTrue(dashboardPage.isKartuSelamatDatangDisplayed(),
                "Gagal! Kartu selamat datang tidak ditemukan.");
        System.out.println("LOG: Kartu selamat datang terverifikasi.");
    }

    @Then("Grid statistik pasien ditampilkan")
    public void grid_statistik_pasien_ditampilkan() {
        Assert.assertTrue(dashboardPage.isStatsGridDisplayed(),
                "Gagal! Grid statistik tidak ditemukan.");
        System.out.println("LOG: Grid statistik terverifikasi.");
    }

    @Then("Grid statistik menampilkan label {string}")
    public void grid_statistik_menampilkan_label(String label) {
        Assert.assertTrue(dashboardPage.isStatLabelDisplayed(label),
                "Gagal! Label statistik '" + label + "' tidak ditemukan.");
        System.out.println("LOG: Label statistik '" + label + "' terverifikasi.");
    }

    @Then("Judul {string} ditampilkan")
    public void judul_ditampilkan(String judul) {
        Assert.assertTrue(dashboardPage.isJudulPasienHadirDisplayed(),
                "Gagal! Judul '" + judul + "' tidak ditemukan.");
        System.out.println("LOG: Judul '" + judul + "' terverifikasi.");
    }

    @Then("Tombol {string} untuk navigasi ke daftar pasien tersedia")
    public void tombol_untuk_navigasi_tersedia(String labelBtn) {
        Assert.assertTrue(dashboardPage.isBtnSemuaDisplayed(),
                "Gagal! Tombol '" + labelBtn + "' tidak ditemukan.");
        System.out.println("LOG: Tombol '" + labelBtn + "' terverifikasi.");
    }

    // ===================== AKSI USER =====================

    @When("User menekan tombol {string} di bagian Pasien Hadir")
    public void user_menekan_tombol_semua(String labelBtn) {
        dashboardPage.clickBtnSemua();
        System.out.println("LOG: Tombol '" + labelBtn + "' ditekan.");
    }

    @When("User menekan kartu pasien pertama")
    public void user_menekan_kartu_pasien_pertama() {
        dashboardPage.clickKartuPasienPertama();
        try { Thread.sleep(1000); } catch (Exception ignored) {}
        System.out.println("LOG: Kartu pasien pertama ditekan.");
    }

    @When("User memilih status {string} pada modal")
    public void user_memilih_status_pada_modal(String statusLabel) {
        dashboardPage.pilihStatusModal(statusLabel);
        System.out.println("LOG: Status '" + statusLabel + "' dipilih.");
    }

    @When("User menekan tombol {string} pada modal")
    public void user_menekan_tombol_pada_modal(String labelBtn) {
        switch (labelBtn) {
            case "Simpan Status":
                dashboardPage.clickSimpanStatus();
                break;
            case "Batal":
                dashboardPage.clickBatalModal();
                break;
            default:
                System.out.println("LOG: Tombol modal tidak dikenali: " + labelBtn);
        }
        System.out.println("LOG: Tombol modal '" + labelBtn + "' ditekan.");
    }

    // ===================== VERIFIKASI HASIL =====================

    @Then("Halaman daftar pasien ditampilkan")
    public void halaman_daftar_pasien_ditampilkan() {
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        Assert.assertTrue(dashboardPage.isHalamanPasienDisplayed(),
                "Gagal! Halaman daftar pasien tidak muncul.");
        System.out.println("LOG: Navigasi ke halaman pasien sukses.");
    }

    @Then("Modal {string} ditampilkan")
    public void modal_ditampilkan(String judulModal) {
        Assert.assertTrue(dashboardPage.isModalUbahStatusDisplayed(),
                "Gagal! Modal '" + judulModal + "' tidak muncul.");
        System.out.println("LOG: Modal '" + judulModal + "' terverifikasi.");
    }

    @Then("Modal berhasil ditutup")
    public void modal_berhasil_ditutup() {
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        Assert.assertTrue(dashboardPage.isModalClosed(),
                "Gagal! Modal masih terbuka setelah aksi.");
        System.out.println("LOG: Modal tertutup dengan sukses.");
    }

    @Then("Tombol {string} dalam kondisi tidak aktif")
    public void tombol_dalam_kondisi_tidak_aktif(String labelBtn) {
        Assert.assertTrue(dashboardPage.isSimpanStatusDisabled(),
                "Gagal! Tombol '" + labelBtn + "' seharusnya tidak aktif.");
        System.out.println("LOG: Tombol '" + labelBtn + "' terverifikasi tidak aktif.");
    }
}