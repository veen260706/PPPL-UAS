package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class DashboardPage {
    AppiumDriver driver;
    WebDriverWait wait;

    // ── Header ───────────────────────────────────────────────────────────────
    // Menggunakan text-based locator agar tidak bergantung pada koordinat/resolusi
    private By headerTitle = By.xpath("//*[contains(@text,'TentangDental')]");

    // ── Kartu Selamat Datang ─────────────────────────────────────────────────
    private By labelSelamatDatang = By.xpath("//*[contains(@text,'SELAMAT DATANG')]");

    // ── Grid Statistik ───────────────────────────────────────────────────────
    // Label teks statis — lebih robust daripada index posisi
    private By statHadir     = By.xpath("//*[contains(@text,'Hadir')]");
    private By statRontgen   = By.xpath("//*[contains(@text,'Rontgen')]");
    private By statSelesai   = By.xpath("//*[contains(@text,'Selesai')]");
    private By statDiRuangan = By.xpath("//*[contains(@text,'Di ruangan')]");

    // ── Pasien Hadir ─────────────────────────────────────────────────────────
    private By judulPasienHadir = By.xpath("//*[contains(@text,'Pasien Hadir Hari Ini')]");
    // Tombol "Semua" — pilih yang pertama muncul untuk menghindari ambiguitas
    private By btnSemua = By.xpath("(//*[contains(@text,'Semua')])[1]");

    // ── Kartu Pasien ─────────────────────────────────────────────────────────
    // Fallback: kartu pasien diidentifikasi dari elemen chevron yang selalu ada
    // Alternatif: cari semua item yang mengandung pola No. XXX
    private By daftarKartuPasien = By.xpath(
            "//*[contains(@text,'No.')]"
    );

    // ── Modal Ubah Status ────────────────────────────────────────────────────
    private By modalTitle        = By.xpath("//*[contains(@text,'Ubah Status Pasien')]");
    private By btnSimpanStatus   = By.xpath("(//*[contains(@text,'Simpan Status')])[1]");
    private By btnBatalModal     = By.xpath("(//*[contains(@text,'Batal')])[1]");

    // ── Opsi Status di Modal ──────────────────────────────────────────────────
    // Menggunakan contains agar tetap cocok walaupun ada teks ekstra
    private By optMenunggu      = By.xpath("//*[contains(@text,'Menunggu') and not(contains(@text,'Hadir'))]");
    private By optMasukRuangan  = By.xpath("//*[contains(@text,'Masuk Ruangan')]");
    private By optUploadFoto    = By.xpath("//*[contains(@text,'Upload Foto')]");
    private By optSelesaiTanpaFoto = By.xpath("//*[contains(@text,'Selesai (Tanpa Foto)')]");

    // ── Indikator Halaman Pasien ──────────────────────────────────────────────
    // Muncul setelah tap tombol Semua → navigasi ke tab Pasien
    private By halamanPasienIndicator = By.xpath(
            "//*[contains(@text,'Daftar Pasien') or contains(@text,'Semua Pasien') or contains(@text,'Pasien')]"
    );

    public DashboardPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Verifikasi elemen ────────────────────────────────────────────────────

    public boolean isHeaderDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(headerTitle));
            System.out.println("LOG: Header TentangDental terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Header tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isKartuSelamatDatangDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(labelSelamatDatang));
            System.out.println("LOG: Kartu SELAMAT DATANG terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Kartu selamat datang tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isStatsGridDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(statHadir));
            System.out.println("LOG: Grid statistik terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Grid statistik tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isStatLabelDisplayed(String label) {
        try {
            By locator = By.xpath("//*[contains(@text,'" + label + "')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("LOG: Label statistik '" + label + "' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Label '" + label + "' tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isJudulPasienHadirDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(judulPasienHadir));
            System.out.println("LOG: Judul 'Pasien Hadir Hari Ini' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Judul pasien hadir tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isBtnSemuaDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(btnSemua));
            System.out.println("LOG: Tombol Semua terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Tombol Semua tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    // ── Aksi ─────────────────────────────────────────────────────────────────

    public void clickBtnSemua() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSemua)).click();
        System.out.println("LOG: Tombol Semua ditekan.");
    }

    /**
     * Menekan kartu pasien pertama dalam daftar.
     * Menggunakan locator berbasis teks (No.) sehingga tidak bergantung
     * pada indeks posisi absolut maupun resolusi layar.
     */
    public void clickKartuPasienPertama() {
        List<WebElement> list = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(daftarKartuPasien)
        );
        if (!list.isEmpty()) {
            list.get(0).click();
            System.out.println("LOG: Kartu pasien pertama ditekan.");
        } else {
            System.out.println("LOG: Tidak ada kartu pasien ditemukan.");
        }
    }

    public boolean hasPasienInList() {
        try {
            List<WebElement> list = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(daftarKartuPasien));
            boolean ada = !list.isEmpty();
            System.out.println("LOG: Jumlah pasien dalam daftar: " + list.size());
            return ada;
        } catch (Exception e) {
            System.out.println("LOG: Daftar pasien kosong atau tidak ditemukan.");
            return false;
        }
    }

    // ── Modal ────────────────────────────────────────────────────────────────

    public boolean isModalUbahStatusDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
            System.out.println("LOG: Modal 'Ubah Status Pasien' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Modal tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isModalClosed() {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
            System.out.println("LOG: Modal sudah tertutup.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Modal masih terbuka: " + e.getMessage());
            return false;
        }
    }

    /**
     * Memilih salah satu opsi status di modal.
     * @param label Salah satu dari: "Menunggu", "Masuk Ruangan", "Upload Foto",
     *              "Selesai (Tanpa Foto)"
     */
    public void pilihStatusModal(String label) {
        By locator = By.xpath("//*[contains(@text,'" + label + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        System.out.println("LOG: Opsi status '" + label + "' dipilih.");
    }

    public void clickSimpanStatus() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSimpanStatus)).click();
        System.out.println("LOG: Tombol Simpan Status ditekan.");
    }

    public void clickBatalModal() {
        wait.until(ExpectedConditions.elementToBeClickable(btnBatalModal)).click();
        System.out.println("LOG: Tombol Batal ditekan.");
    }

    /**
     * Mengecek apakah tombol Simpan Status dalam kondisi tidak aktif (disabled).
     * Pada React Native Expo, elemen disabled biasanya tidak clickable tetapi
     * masih visible. Kita verifikasi dengan mencoba klik dan cek modal masih terbuka.
     */
    public boolean isSimpanStatusDisabled() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(btnSimpanStatus)
            );

            // React Native disabled button: attribute "clickable" = false
            // atau "enabled" bisa tetap true, tapi "clickable" yang berubah
            String clickable = btn.getAttribute("clickable");
            String enabled = btn.getAttribute("enabled");
            System.out.println("LOG: Simpan Status clickable=" + clickable + ", enabled=" + enabled);

            // Cek clickable dulu
            if ("false".equalsIgnoreCase(clickable)) {
                System.out.println("LOG: Tombol tidak aktif via clickable=false.");
                return true;
            }

            // Fallback: coba klik, kalau modal masih terbuka berarti tombol memang disabled
            try {
                btn.click();
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            // Kalau modal masih ada = klik tidak bereaksi = tombol disabled
            boolean modalMasihAda = false;
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
                modalMasihAda = true;
            } catch (Exception ignored) {}

            System.out.println("LOG: Modal masih terbuka setelah klik = " + modalMasihAda);
            return modalMasihAda;

        } catch (Exception e) {
            System.out.println("LOG: Tidak dapat cek status tombol: " + e.getMessage());
            return false;
        }
    }

    public boolean isHalamanPasienDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(halamanPasienIndicator));
            System.out.println("LOG: Halaman Daftar Pasien terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Halaman pasien tidak ditemukan: " + e.getMessage());
            return false;
        }
    }
}