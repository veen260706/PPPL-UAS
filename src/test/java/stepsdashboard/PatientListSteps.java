package stepsdashboard;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.And;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import pages.LoginPage;
import pages.PatientListPage;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.Arrays;

public class PatientListSteps {

    private AndroidDriver driver;
    private PatientListPage patientListPage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    // ===================== NAVIGASI =====================

    @Given("User sudah login dan berada di halaman Daftar Pasien")
    public void user_sudah_login_dan_berada_di_halaman_daftar_pasien() {
        driver = HooksDashboard.driver;
        patientListPage = new PatientListPage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try { Thread.sleep(2000); } catch (Exception ignored) {}

        boolean sudahDiPasien = false;
        try {
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(
                            By.xpath("//*[contains(@text,'Daftar Pasien Hadir')]")
                    ));
            sudahDiPasien = true;
            System.out.println("LOG: Sudah di halaman Daftar Pasien.");
        } catch (Exception ignored) {}

        if (!sudahDiPasien) {
            boolean sudahLogin = false;
            try {
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(@text,'TentangDental')]")
                        ));
                sudahLogin = true;
                System.out.println("LOG: Ada di Dashboard.");
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

            System.out.println("LOG: Tap tab Pasien via koordinat.");
            try {
                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence tap = new Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ZERO,
                        PointerInput.Origin.viewport(), 216, 1466));
                tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Arrays.asList(tap));
                Thread.sleep(3000);

                new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.visibilityOfElementLocated(
                                By.xpath("//*[contains(@text,'Daftar Pasien Hadir')]")
                        ));
                sudahDiPasien = true;
                System.out.println("LOG: Berhasil ke halaman Daftar Pasien.");
            } catch (Exception e) {
                System.out.println("LOG: Tap tab Pasien gagal: " + e.getMessage());
            }
        }

        // ← TAMBAHAN: reset search bar dan filter ke kondisi awal
        resetHalamanPasien();

        try {
            driver.executeScript("mobile: scroll",
                    java.util.Map.of("direction", "up", "percent", 1.0));
            Thread.sleep(500);
        } catch (Exception ignored) {}

        Assert.assertTrue(sudahDiPasien, "Gagal! Halaman Daftar Pasien tidak muncul.");
        System.out.println("LOG: Halaman Daftar Pasien berhasil dimuat.");
    }

    // ── Method helper reset state halaman ────────────────────────────────────────
    private void resetHalamanPasien() {
        // 1. Clear search bar kalau ada teks
        try {
            By searchBar = By.xpath(
                    "//android.widget.EditText[contains(@hint,'Cari nama pasien')]"
            );
            WebElement input = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(searchBar));
            String currentText = input.getText();
            if (currentText != null && !currentText.isEmpty()
                    && !currentText.equals("Cari nama pasien...")) {
                input.clear();
                Thread.sleep(1000);
                System.out.println("LOG: Search bar di-clear.");
            }
        } catch (Exception ignored) {}

        // 2. Reset filter ke "Semua" kalau tidak aktif
        try {
            By tabSemua = By.xpath(
                    "//*[@content-desc='Semua' or (@text='Semua' and @clickable='true')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.elementToBeClickable(tabSemua)).click();
            Thread.sleep(1000);
            System.out.println("LOG: Filter direset ke Semua.");
        } catch (Exception ignored) {}
    }

    @And("Terdapat minimal satu pasien dalam daftar pasien")
    public void terdapat_minimal_satu_pasien_dalam_daftar_pasien() {
        boolean ada = patientListPage.hasPasienInList();
        Assert.assertTrue(ada, "Tidak ada pasien dalam daftar. Pastikan ada data reservasi hari ini.");
        System.out.println("LOG: Daftar pasien tidak kosong.");
    }

    // ===================== VERIFIKASI TAMPILAN =====================

    @Then("Judul {string} ditampilkan di halaman pasien")
    public void judul_ditampilkan_di_halaman_pasien(String judul) {
        Assert.assertTrue(patientListPage.isJudulHalamanDisplayed(),
                "Gagal! Judul '" + judul + "' tidak ditemukan.");
        System.out.println("LOG: Judul '" + judul + "' terverifikasi.");
    }

    @Then("Search bar pasien ditampilkan")
    public void search_bar_pasien_ditampilkan() {
        Assert.assertTrue(patientListPage.isSearchBarDisplayed(),
                "Gagal! Search bar tidak ditemukan.");
        System.out.println("LOG: Search bar terverifikasi.");
    }

    @Then("Tab filter {string} ditampilkan")
    public void tab_filter_ditampilkan(String label) {
        Assert.assertTrue(patientListPage.isTabFilterDisplayed(label),
                "Gagal! Tab filter '" + label + "' tidak ditemukan.");
        System.out.println("LOG: Tab filter '" + label + "' terverifikasi.");
    }

    @Then("Kartu pasien menampilkan nama pasien")
    public void kartu_pasien_menampilkan_nama_pasien() {
        Assert.assertTrue(patientListPage.isNamaPasienDisplayed(),
                "Gagal! Nama pasien tidak ditemukan di kartu.");
        System.out.println("LOG: Nama pasien terverifikasi.");
    }

    @Then("Kartu pasien menampilkan nomor antrean")
    public void kartu_pasien_menampilkan_nomor_antrean() {
        Assert.assertTrue(patientListPage.isNomorAntreanDisplayed(),
                "Gagal! Nomor antrean tidak ditemukan.");
        System.out.println("LOG: Nomor antrean terverifikasi.");
    }

    @Then("Daftar pasien ditampilkan sesuai filter")
    public void daftar_pasien_ditampilkan_sesuai_filter() {
        Assert.assertTrue(patientListPage.isDaftarPasienDisplayed(),
                "Gagal! Daftar pasien tidak ditampilkan setelah filter.");
        System.out.println("LOG: Daftar pasien sesuai filter terverifikasi.");
    }

    @Then("Pesan pasien tidak ditemukan ditampilkan")
    public void pesan_pasien_tidak_ditemukan_ditampilkan() {
        Assert.assertTrue(patientListPage.isEmptyStateDisplayed(),
                "Gagal! Pesan 'Pasien tidak ditemukan' tidak muncul.");
        System.out.println("LOG: Empty state terverifikasi.");
    }

    // ===================== AKSI USER =====================

    @When("User menekan tab filter {string} di halaman pasien")
    public void user_menekan_tab_filter(String label) {
        patientListPage.clickTabFilter(label);
        System.out.println("LOG: Tab filter '" + label + "' ditekan.");
    }

    @When("User mengetik nama pada search bar pasien")
    public void user_mengetik_nama_pada_search_bar_pasien() {
        // Ketik nama valid yang kemungkinan besar ada — ambil keyword umum
        patientListPage.inputSearch("a");
        System.out.println("LOG: Keyword 'a' diinput ke search bar.");
    }

    @When("User mengetik nama tidak valid pada search bar")
    public void user_mengetik_nama_tidak_valid_pada_search_bar() {
        patientListPage.inputSearch("zzzzzzzzzznotfound99999");
        System.out.println("LOG: Keyword tidak valid diinput ke search bar.");
    }

    @When("User menekan kartu pasien pertama di halaman pasien")
    public void user_menekan_kartu_pasien_pertama_di_halaman_pasien() {
        patientListPage.clickKartuPasienPertama();
        System.out.println("LOG: Kartu pasien pertama ditekan.");
    }

    @When("User memilih opsi {string} pada modal pasien")
    public void user_memilih_opsi_pada_modal_pasien(String label) {
        patientListPage.pilihOpsiStatus(label);
        System.out.println("LOG: Opsi '" + label + "' dipilih.");
    }

    @When("User menekan simpan pada modal pasien")
    public void user_menekan_simpan_pada_modal_pasien() {
        patientListPage.clickSimpanStatus();
        System.out.println("LOG: Tombol Simpan Status ditekan.");
    }

    @When("User menekan batal pada modal pasien")
    public void user_menekan_batal_pada_modal_pasien() {
        patientListPage.clickBatalModal();
        System.out.println("LOG: Tombol Batal ditekan.");
    }

    // ===================== VERIFIKASI HASIL =====================

    @Then("Modal ubah status pasien muncul di halaman pasien")
    public void modal_ubah_status_pasien_muncul() {
        Assert.assertTrue(patientListPage.isModalDisplayed(),
                "Gagal! Modal 'Ubah Status Pasien' tidak muncul.");
        System.out.println("LOG: Modal terverifikasi muncul.");
    }

    @Then("Opsi status {string} ditampilkan dalam modal pasien")
    public void opsi_status_ditampilkan_dalam_modal_pasien(String label) {
        Assert.assertTrue(patientListPage.isOpsiStatusDisplayed(label),
                "Gagal! Opsi status '" + label + "' tidak ditemukan.");
        System.out.println("LOG: Opsi '" + label + "' terverifikasi.");
    }

    @Then("Modal ubah status berhasil ditutup")
    public void modal_ubah_status_berhasil_ditutup() {
        try { Thread.sleep(2000); } catch (Exception ignored) {}
        Assert.assertTrue(patientListPage.isModalClosed(),
                "Gagal! Modal masih terbuka setelah aksi.");
        System.out.println("LOG: Modal berhasil ditutup.");
    }

    @Then("Tombol simpan status tidak dapat ditekan")
    public void tombol_simpan_status_tidak_dapat_ditekan() {
        Assert.assertTrue(patientListPage.isSimpanStatusTidakAktif(),
                "Gagal! Tombol Simpan Status seharusnya tidak aktif.");
        System.out.println("LOG: Tombol Simpan Status tidak aktif terverifikasi.");
    }
}