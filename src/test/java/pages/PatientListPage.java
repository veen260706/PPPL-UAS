package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class PatientListPage {
    AppiumDriver driver;
    WebDriverWait wait;

    // ── Header & Judul ───────────────────────────────────────────────────────
    // Dari pasien.tsx: Text "Daftar Pasien Hadir" sebagai pageTitle
    private By judulHalaman = By.xpath("//*[contains(@text,'Daftar Pasien Hadir')]");

    // ── Search Bar ───────────────────────────────────────────────────────────
    // EditText dengan hint "Cari nama pasien..." — terlihat di ui.xml
    private By searchBar = By.xpath(
            "//android.widget.EditText[contains(@hint,'Cari nama pasien')]"
    );

    // ── Tab Filter ───────────────────────────────────────────────────────────
    // Filter: ["Semua", "Menunggu", "Di Ruangan", "Upload foto", "Selesai"]
    // Dari ui.xml: content-desc="Semua", content-desc="Menunggu", dll.
    // Gunakan content-desc ATAU text — keduanya ada di ui.xml
    private By tabSemua     = By.xpath(
            "//*[@content-desc='Semua' or (@text='Semua' and @clickable='true')]"
    );
    private By tabMenunggu  = By.xpath(
            "//*[@content-desc='Menunggu' or (@text='Menunggu' and @clickable='true')]"
    );
    private By tabDiRuangan = By.xpath(
            "//*[@content-desc='Di Ruangan' or (@text='Di Ruangan' and @clickable='true')]"
    );
    private By tabUploadFoto = By.xpath(
            "//*[@content-desc='Upload foto' or (@text='Upload foto' and @clickable='true')]"
    );
    private By tabSelesai   = By.xpath(
            "//*[@content-desc='Selesai' or (@text='Selesai' and @clickable='true')]"
    );

    // ── Kartu Pasien ─────────────────────────────────────────────────────────
    // Pola: "No. 001 · 09:00 · 25 th" — teks sub selalu ada di setiap kartu
    private By daftarKartuPasien = By.xpath("//*[contains(@text,'No.')]");

    // Nama pasien: TextView dengan fontWeight bold di dalam kartu
    // Fallback: ambil semua elemen yang ada di antara header dan footer
    private By namaPasienPertama = By.xpath("(//*[contains(@text,'No.')])[1]");

    // ── Empty State ──────────────────────────────────────────────────────────
    // Dari pasien.tsx: Text "Pasien tidak ditemukan" — dari ui.xml sudah terbukti ada
    private By emptyState = By.xpath(
            "//*[contains(@text,'Pasien tidak ditemukan') or " +
                    "contains(@text,'tidak ada pasien') or " +
                    "contains(@text,'Tidak ada pasien')]"
    );

    // ── Modal Ubah Status ────────────────────────────────────────────────────
    // Dari UbahStatusPasien.tsx: Text "Ubah Status Pasien"
    private By modalTitle      = By.xpath("//*[contains(@text,'Ubah Status Pasien')]");
    private By btnSimpanStatus = By.xpath("(//*[contains(@text,'Simpan Status')])[1]");
    private By btnBatalModal   = By.xpath("(//*[contains(@text,'Batal')])[1]");

    // ── Opsi Status di Modal ─────────────────────────────────────────────────
    // Label dari statusOptions di UbahStatusPasien.tsx
    private By optMenunggu         = By.xpath("//*[contains(@text,'Menunggu')]");
    private By optMasukRuangan     = By.xpath("//*[contains(@text,'Masuk Ruangan')]");
    private By optUploadFoto       = By.xpath("//*[contains(@text,'Upload Foto')]");
    private By optSelesaiTanpaFoto = By.xpath("//*[contains(@text,'Selesai (Tanpa Foto)')]");

    public PatientListPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Verifikasi Halaman ───────────────────────────────────────────────────

    public boolean isJudulHalamanDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(judulHalaman));
            System.out.println("LOG: Judul 'Daftar Pasien Hadir' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Judul halaman tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isSearchBarDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBar));
            System.out.println("LOG: Search bar terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Search bar tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    // ── Verifikasi Tab Filter ────────────────────────────────────────────────

    public boolean isTabFilterDisplayed(String label) {
        try {
            // Gunakan content-desc dari ui.xml yang sudah terbukti ada
            By locator = By.xpath(
                    "//*[@content-desc='" + label + "' or " +
                            "(@text='" + label + "' and @clickable='true')]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("LOG: Tab filter '" + label + "' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Tab filter '" + label + "' tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public void clickTabFilter(String label) {
        By locator = By.xpath(
                "//*[@content-desc='" + label + "' or " +
                        "(@text='" + label + "' and @clickable='true')]"
        );
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        System.out.println("LOG: Tab filter '" + label + "' ditekan.");
        try { Thread.sleep(1000); } catch (Exception ignored) {}
    }

    // ── Verifikasi Kartu Pasien ──────────────────────────────────────────────

    public boolean hasPasienInList() {
        try {
            List<WebElement> list = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(daftarKartuPasien));
            boolean ada = !list.isEmpty();
            System.out.println("LOG: Jumlah pasien dalam daftar: " + list.size());
            return ada;
        } catch (Exception e) {
            System.out.println("LOG: Daftar pasien kosong: " + e.getMessage());
            return false;
        }
    }

    public boolean isNamaPasienDisplayed() {
        try {
            // Nama pasien ada di atas teks "No." dalam setiap kartu
            // Cari TextView yang tidak mengandung "No." dan bukan label status
            By namaPasien = By.xpath(
                    "//*[string-length(@text) > 3 " +
                            "and not(contains(@text,'No.')) " +
                            "and not(contains(@text,'Hadir')) " +
                            "and not(contains(@text,'Semua')) " +
                            "and not(contains(@text,'Menunggu')) " +
                            "and not(contains(@text,'Daftar')) " +
                            "and not(contains(@text,'TentangDental')) " +
                            "and @class='android.widget.TextView']"
            );
            List<WebElement> list = new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(namaPasien));
            System.out.println("LOG: Nama pasien terdeteksi, jumlah elemen: " + list.size());
            return !list.isEmpty();
        } catch (Exception e) {
            System.out.println("LOG: Nama pasien tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isNomorAntreanDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(daftarKartuPasien));
            System.out.println("LOG: Nomor antrean (No.) terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Nomor antrean tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isDaftarPasienDisplayed() {
        try {
            // Cek apakah ada kartu pasien ATAU empty state — keduanya valid
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(daftarKartuPasien));
                System.out.println("LOG: Daftar pasien ditampilkan.");
                return true;
            } catch (Exception ignored) {}

            new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(emptyState));
            System.out.println("LOG: Empty state ditampilkan (filter aktif, hasil 0).");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Daftar pasien tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isEmptyStateDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emptyState));
            System.out.println("LOG: Empty state 'Pasien tidak ditemukan' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Empty state tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    // ── Aksi Search ──────────────────────────────────────────────────────────

    public void inputSearch(String keyword) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(searchBar));
        input.clear();
        input.sendKeys(keyword);
        System.out.println("LOG: Search keyword '" + keyword + "' diinput.");
        try { Thread.sleep(1500); } catch (Exception ignored) {}
    }

    // ── Aksi Kartu Pasien ────────────────────────────────────────────────────

    public void clickKartuPasienPertama() {
        List<WebElement> list = wait.until(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(daftarKartuPasien)
        );
        if (!list.isEmpty()) {
            list.get(0).click();
            System.out.println("LOG: Kartu pasien pertama ditekan.");
        } else {
            System.out.println("LOG: Tidak ada kartu pasien.");
        }
        try { Thread.sleep(1000); } catch (Exception ignored) {}
    }

    // ── Verifikasi Modal ─────────────────────────────────────────────────────

    public boolean isModalDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
            System.out.println("LOG: Modal 'Ubah Status Pasien' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Modal tidak muncul: " + e.getMessage());
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

    public boolean isOpsiStatusDisplayed(String label) {
        try {
            By locator = By.xpath("//*[contains(@text,'" + label + "')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
            System.out.println("LOG: Opsi status '" + label + "' terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Opsi status '" + label + "' tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isSimpanStatusTidakAktif() {
        try {
            WebElement btn = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(btnSimpanStatus)
            );
            String clickable = btn.getAttribute("clickable");
            System.out.println("LOG: Simpan Status clickable=" + clickable);

            if ("false".equalsIgnoreCase(clickable)) {
                return true;
            }

            // Fallback: klik lalu cek modal masih terbuka
            try { btn.click(); Thread.sleep(1000); } catch (Exception ignored) {}
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(modalTitle));
                System.out.println("LOG: Modal masih terbuka — tombol tidak aktif.");
                return true;
            } catch (Exception ignored) {}

            return false;
        } catch (Exception e) {
            System.out.println("LOG: Tidak dapat cek tombol: " + e.getMessage());
            return false;
        }
    }

    // ── Aksi Modal ───────────────────────────────────────────────────────────

    public void pilihOpsiStatus(String label) {
        By locator = By.xpath("//*[contains(@text,'" + label + "')]");
        wait.until(ExpectedConditions.elementToBeClickable(locator)).click();
        System.out.println("LOG: Opsi '" + label + "' dipilih.");
        try { Thread.sleep(500); } catch (Exception ignored) {}
    }

    public void clickSimpanStatus() {
        wait.until(ExpectedConditions.elementToBeClickable(btnSimpanStatus)).click();
        System.out.println("LOG: Tombol Simpan Status ditekan.");
    }

    public void clickBatalModal() {
        wait.until(ExpectedConditions.elementToBeClickable(btnBatalModal)).click();
        System.out.println("LOG: Tombol Batal ditekan.");
    }
}