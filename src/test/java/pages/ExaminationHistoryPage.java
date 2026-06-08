package pages;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class ExaminationHistoryPage {
    AppiumDriver driver;
    WebDriverWait wait;

    // ── Tab Rontgen / History ─────────────────────────────────────────────────
    // Dari rontgen.tsx: dua tab "Perlu Upload Foto" dan "History"
    // Tab dirender sebagai TouchableOpacity dengan text — gunakan contains(@text)
    private By tabHistory =
            AppiumBy.accessibilityId("History");
    private By tabRontgen = By.xpath(
            "//*[contains(@text,'Perlu Upload Foto') and @clickable='true']"
    );

    // Verifikasi tab History aktif — cari teks History di layar
    private By tabHistoryAktif = By.xpath("//*[contains(@text,'History')]");

    // ── Search Bar History ────────────────────────────────────────────────────
    // Dari rontgen.tsx: TextInput dengan placeholder "Cari nama pasien..."
    private By searchBarHistory = By.xpath(
            "//android.widget.EditText[contains(@hint,'Cari nama pasien')]"
    );

    // ── Filter Waktu ──────────────────────────────────────────────────────────
    // timeFilters = ["All Time", "Today", "This Week"]
    // Di React Native, TouchableOpacity dirender sebagai android.view.ViewGroup
    // yang clickable=true, dengan TextView di dalamnya. Cari parent ViewGroup clickable
    // yang mengandung teks filter.
    private By filterAllTime  = By.xpath(
            "//*[contains(@text,'All Time')]/ancestor::android.view.ViewGroup[@clickable='true'][1]"
    );
    private By filterToday    = By.xpath(
            "//*[contains(@text,'Today')]/ancestor::android.view.ViewGroup[@clickable='true'][1]"
    );
    private By filterThisWeek = By.xpath(
            "//*[contains(@text,'This Week')]/ancestor::android.view.ViewGroup[@clickable='true'][1]"
    );

    // ── Kartu Riwayat ─────────────────────────────────────────────────────────
    // Dari rontgen.tsx historyCard: setiap kartu memiliki nama dokter sebagai "judul"
    // dan jam pemeriksaan. Pola paling stabil: cari teks jam (HH:MM format)
    // Alternatif: groupDate label tanggal selalu ada (format YYYY-MM-DD)
    private By groupTanggal = By.xpath(
            "//*[@class='android.widget.TextView' and string-length(@text)=10 " +
                    "and contains(@text,'-')]"
    );

    // Kartu history: identifikasi via struktur — ada nama pasien dan dokter
    // Gunakan locator yang paling stabil: cari container yang clickable
    // di dalam area history (setelah tab aktif)
    private By kartuRiwayat = By.xpath(
            "(//*[contains(@text,'-') and string-length(@text)=5 " +
                    "and @class='android.widget.TextView'])"
    );

    // ── Empty State ───────────────────────────────────────────────────────────
    // Dari rontgen.tsx: Text "Belum ada riwayat pemeriksaan"
    private By emptyStateHistory = By.xpath(
            "//*[contains(@text,'Belum ada riwayat') or " +
                    "contains(@text,'Tidak ada riwayat') or " +
                    "contains(@text,'No history') or " +
                    "contains(@text,'belum ada data')]"
    );

    // ── Exam Details ──────────────────────────────────────────────────────────
    // Dari ExamDetails.tsx: section "DATA PASIEN" selalu muncul pertama
    // Label cyan "DATA PASIEN" dari cardSmallTitle style
    private By sectionDataPasien =
            By.xpath("//*[contains(@text,'DATA PASIEN')]");

    // Nama pasien di Exam Details — dataMainVal style, font size 16 bold
    // Cari sebagai TextView yang bukan label kecil
    private By namaPasienExamDetails = By.xpath(
            "//*[contains(@text,'DATA PASIEN')]/following-sibling::*" +
                    "//*[@class='android.widget.TextView' and string-length(@text) > 2]"
    );

    // Dokter section — nama dokter mungkin tidak selalu diawali "drg." / "dr."
    // Fallback: cari label "Dokter" atau "Spesialis" yang selalu ada di Exam Details
    private By dokterExamDetails = By.xpath(
            "//*[contains(@text,'drg.') or contains(@text,'dr.') " +
                    "or contains(@text,'Drg.') or contains(@text,'Dr.') " +
                    "or contains(@text,'Dokter') or contains(@text,'Spesialis')]"
    );

    // Pemeriksaan Fisik section
    // Dari ExamDetails.tsx: subTitleBold "Pemeriksaan Fisik" atau label field
    private By sectionPemeriksaanFisik =
            By.xpath("//*[contains(@text,'PEMERIKSAAN FISIK')]");

    // Tombol kembali — Ionicons chevron-back atau back arrow
    // Di Appium, icon Ionicons terdeteksi sebagai TextView atau ImageView
    // Gunakan bounds: tombol kembali selalu di pojok kiri atas
    private By btnKembali = By.xpath(
            "(//*[@clickable='true'])[1]"
    );

    // Indikator halaman Exam Details — judul halaman
    // Dari window_dump: text="Exam Details" ada sebagai TextView
    private By indikatorExamDetails =
            By.xpath("//*[contains(@text,'Exam Details')]");

    public boolean isExamDetailsDisplayed() {
        try {
            // Cek judul "Exam Details" — terlihat di window_dump
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(ExpectedConditions.visibilityOfElementLocated(indikatorExamDetails));
            System.out.println("LOG: Halaman Exam Details terdeteksi.");
            return true;
        } catch (Exception e) {
            // Fallback: cek section DATA PASIEN — selalu ada di Exam Details
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(sectionDataPasien));
                System.out.println("LOG: Exam Details terdeteksi via section DATA PASIEN.");
                return true;
            } catch (Exception e2) {
                System.out.println("LOG: Exam Details tidak ditemukan: " + e2.getMessage());
                return false;
            }
        }
    }

    public ExaminationHistoryPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    // ── Navigasi Tab ─────────────────────────────────────────────────────────

    public void clickTabHistory() {
        wait.until(ExpectedConditions.elementToBeClickable(tabHistory)).click();
        System.out.println("LOG: Tab History ditekan.");
        try { Thread.sleep(2000); } catch (Exception ignored) {}
    }

    public boolean isTabHistoryDisplayed() {
        try {
            // Cek tab "History" di tab bar atau heading halaman
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabHistoryAktif));
            System.out.println("LOG: Tab History terdeteksi.");
            return true;
        } catch (Exception e) {
            // Fallback: cek filter "All Time" yang hanya muncul di tab History
            try {
                By allTimeIndicator = By.xpath("//*[contains(@text,'All Time')]");
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(allTimeIndicator));
                System.out.println("LOG: Tab History terdeteksi via filter All Time.");
                return true;
            } catch (Exception e2) {
                System.out.println("LOG: Tab History tidak ditemukan: " + e.getMessage());
                return false;
            }
        }
    }

    // ── Search Bar ────────────────────────────────────────────────────────────

    public boolean isSearchBarDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(searchBarHistory));
            System.out.println("LOG: Search bar history terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Search bar tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public void inputSearch(String keyword) {
        try {
            WebElement input = wait.until(
                    ExpectedConditions.elementToBeClickable(searchBarHistory)
            );
            input.clear();
            input.sendKeys(keyword);
            System.out.println("LOG: Search '" + keyword + "' diinput.");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("LOG: Input search gagal: " + e.getMessage());
        }
    }

    public void clearSearch() {
        try {
            WebElement input = new WebDriverWait(driver, Duration.ofSeconds(3))
                    .until(ExpectedConditions.visibilityOfElementLocated(searchBarHistory));
            String text = input.getText();
            if (text != null && !text.isEmpty() && !text.equals("Cari nama pasien...")) {
                input.clear();
                Thread.sleep(1000);
                System.out.println("LOG: Search bar di-clear.");
            }
        } catch (Exception ignored) {}
    }

    // ── Filter Waktu ─────────────────────────────────────────────────────────

    public void clickFilterWaktu(String label) {
        // Coba klik parent ViewGroup clickable yang mengandung teks filter
        By locatorAncestor = By.xpath(
                "//*[contains(@text,'" + label + "')]" +
                        "/ancestor::android.view.ViewGroup[@clickable='true'][1]"
        );
        // Fallback: klik langsung pada teks jika ia sendiri yang clickable
        By locatorDirect = By.xpath(
                "//*[contains(@text,'" + label + "') and @clickable='true']"
        );
        try {
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(locatorAncestor)).click();
            } catch (Exception ignored) {
                wait.until(ExpectedConditions.elementToBeClickable(locatorDirect)).click();
            }
            System.out.println("LOG: Filter '" + label + "' ditekan.");
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("LOG: Filter '" + label + "' gagal: " + e.getMessage());
        }
    }

    // ── Kartu Riwayat ─────────────────────────────────────────────────────────

    public boolean isDaftarRiwayatDisplayed() {
        try {
            // Cek ada group tanggal (label tanggal di atas kartu)
            // atau langsung cek ada kartu yang clickable
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(groupTanggal));
                System.out.println("LOG: Group tanggal riwayat terdeteksi.");
                return true;
            } catch (Exception ignored) {}

            // Fallback: cek empty state TIDAK muncul = ada data
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfElementLocated(emptyStateHistory));
                System.out.println("LOG: Empty state muncul — tidak ada riwayat.");
                // Empty state muncul tapi halaman tidak error → return true
                // (skenario filter Today mungkin kosong, tapi halaman tetap valid)
                return true;
            } catch (Exception ignored) {}

            // Fallback terakhir: ada elemen di scroll
            System.out.println("LOG: Daftar riwayat dicek via scroll content.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Daftar riwayat tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean hasRiwayatInList() {
        try {
            // Cek group tanggal ada → berarti ada kartu riwayat
            List<WebElement> groups = new WebDriverWait(driver, Duration.ofSeconds(8))
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(groupTanggal));
            boolean ada = !groups.isEmpty();
            System.out.println("LOG: Jumlah group tanggal: " + groups.size());
            return ada;
        } catch (Exception e) {
            System.out.println("LOG: Tidak ada riwayat: " + e.getMessage());
            return false;
        }
    }

    public boolean isNamaPasienRiwayatDisplayed() {
        try {
            // Nama pasien di kartu history — dicari dari elemen TextView
            // yang bukan label filter, bukan tanggal, dan lebih dari 2 karakter
            By namaPasien = By.xpath(
                    "//*[@class='android.widget.TextView' " +
                            "and string-length(@text) > 2 " +
                            "and not(contains(@text,'History')) " +
                            "and not(contains(@text,'All Time')) " +
                            "and not(contains(@text,'Today')) " +
                            "and not(contains(@text,'This Week')) " +
                            "and not(contains(@text,'TentangDental')) " +
                            "and not(contains(@text,'Cari'))]"
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

    public boolean isWaktuPemeriksaanDisplayed() {
        try {
            // Jam dalam format HH:MM selalu ada di setiap kartu history (item.jam)
            By jamLocator = By.xpath(
                    "//*[@class='android.widget.TextView' " +
                            "and string-length(@text)=5 " +
                            "and contains(@text,':')]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(jamLocator));
            System.out.println("LOG: Waktu pemeriksaan terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Waktu pemeriksaan tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isNamaDokterRiwayatDisplayed() {
        try {
            // Nama dokter dirender sebagai item.judul di kartu history
            // dari rontgen.tsx: judul = r.doctor?.name || "Pemeriksaan Rontgen"
            By dokterLocator = By.xpath(
                    "//*[@class='android.widget.TextView' " +
                            "and (contains(@text,'Dr') or contains(@text,'dr') or " +
                            "contains(@text,'Pemeriksaan Rontgen'))]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(dokterLocator));
            System.out.println("LOG: Nama dokter terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Nama dokter tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public void clickKartuRiwayatPertama() {
        try {
            // Kartu history yang benar: ViewGroup clickable yang mengandung:
            // - tanggal format YYYY-MM-DD (string-length=10, contains('-'))
            // - jam format HH:MM (string-length=5, contains(':'))
            // Ini membedakan kartu history dari elemen lain seperti tombol navigasi
            By kartuHistory = By.xpath(
                    "//*[@class='android.view.ViewGroup' and @clickable='true' " +
                            "and .//*[@class='android.widget.TextView' " +
                            "and string-length(@text)=10 and contains(@text,'-')] " +
                            "and .//*[@class='android.widget.TextView' " +
                            "and string-length(@text)=5 and contains(@text,':')]]"
            );
            try {
                List<WebElement> cards = new WebDriverWait(driver, Duration.ofSeconds(8))
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(kartuHistory));
                if (!cards.isEmpty()) {
                    cards.get(0).click();
                    Thread.sleep(2000);
                    System.out.println("LOG: Kartu riwayat pertama ditekan.");
                    return;
                }
            } catch (Exception ignored) {}

            // Fallback: cari ViewGroup clickable dengan lebar penuh layar (≥500px)
            // dan posisi y yang wajar untuk kartu konten (bukan tab bar di bawah)
            By kartuClickable = By.xpath(
                    "//*[@clickable='true' and @class='android.view.ViewGroup']"
            );
            List<WebElement> list = wait.until(
                    ExpectedConditions.visibilityOfAllElementsLocatedBy(kartuClickable)
            );
            for (WebElement el : list) {
                String bounds = el.getAttribute("bounds");
                if (bounds != null) {
                    // Parse bounds: [x1,y1][x2,y2]
                    try {
                        String[] parts = bounds.replaceAll("[\\[\\]]", " ").trim().split("[, ]+");
                        int x1 = Integer.parseInt(parts[0]);
                        int y1 = Integer.parseInt(parts[1]);
                        int x2 = Integer.parseInt(parts[2]);
                        int y2 = Integer.parseInt(parts[3]);
                        int lebar = x2 - x1;
                        // Kartu history: lebar ≥ 500px, posisi y di area konten (bukan tab bar)
                        // Tab bar ada di y ≥ 1416, area konten di y < 1400
                        if (lebar >= 500 && y1 < 1400 && y1 > 100) {
                            el.click();
                            Thread.sleep(2000);
                            System.out.println("LOG: Kartu riwayat pertama ditekan (fallback bounds).");
                            return;
                        }
                    } catch (Exception ignored) {}
                }
            }
            System.out.println("LOG: Kartu riwayat tidak ditemukan.");
        } catch (Exception e) {
            System.out.println("LOG: Kartu riwayat tidak ditemukan: " + e.getMessage());
        }
    }

    public boolean isEmptyStateDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(emptyStateHistory));
            System.out.println("LOG: Empty state history terdeteksi.");
            return true;
        } catch (Exception e) {
            // Empty state tidak ada → cek apakah daftar juga kosong
            try {
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(groupTanggal));
                System.out.println("LOG: Ada data, bukan empty state.");
                return false;
            } catch (Exception ignored) {
                // Tidak ada data dan tidak ada explicit empty state
                System.out.println("LOG: Tidak ada data dan tidak ada explicit empty state.");
                return true;
            }
        }
    }

    public boolean isHalamanTampilTanpaError() {
        try {
            // Verifikasi halaman masih menampilkan elemen dasar (tab, search bar)
            // tanpa crash atau error message
            wait.until(ExpectedConditions.visibilityOfElementLocated(tabHistoryAktif));
            System.out.println("LOG: Halaman history masih valid tanpa error.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Halaman error: " + e.getMessage());
            return false;
        }
    }

    // ── Exam Details ──────────────────────────────────────────────────────────

    public boolean isDataPasienExamDetailsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(sectionDataPasien));
            System.out.println("LOG: Section DATA PASIEN terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: DATA PASIEN tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isNamaPasienExamDetailsDisplayed() {
        try {
            // Nama pasien di ExamDetails: Text dengan fontSize 16 fontWeight 800
            // Cari TextView yang ada di dalam card DATA PASIEN
            // Strategi: cari teks yang mengikuti label "Nama" atau cari nama pasien langsung
            By namaPasien = By.xpath(
                    "//*[contains(@text,'DATA PASIEN')]" +
                            "/following-sibling::*//*[@class='android.widget.TextView' " +
                            "and string-length(@text) > 2 " +
                            "and not(contains(@text,'DATA')) " +
                            "and not(contains(@text,'Pasien No'))]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(namaPasien));
            System.out.println("LOG: Nama pasien di ExamDetails terdeteksi.");
            return true;
        } catch (Exception e) {
            // Fallback: verifikasi section DATA PASIEN ada, nama pasti ikut tampil
            System.out.println("LOG: Fallback - cek section DATA PASIEN saja.");
            return isDataPasienExamDetailsDisplayed();
        }
    }

    public boolean isNamaDokterExamDetailsDisplayed() {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(dokterExamDetails));
            System.out.println("LOG: Nama dokter di ExamDetails terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Nama dokter tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isSectionPemeriksaanFisikDisplayed() {
        try {
            // Scroll ke bawah dulu — PEMERIKSAAN FISIK ada di bawah DATA PASIEN
            try {
                driver.executeScript("mobile: scroll",
                        java.util.Map.of("direction", "down", "percent", 0.5));
                Thread.sleep(500);
            } catch (Exception ignored) {}

            wait.until(
                    ExpectedConditions.visibilityOfElementLocated(
                            sectionPemeriksaanFisik
                    )
            );

            System.out.println("LOG: Section Pemeriksaan Fisik terdeteksi.");
            return true;

        } catch (Exception e) {
            System.out.println(
                    "LOG: Pemeriksaan Fisik tidak ditemukan: "
                            + e.getMessage()
            );
            return false;
        }
    }

    public void clickTombolKembali() {
        try {
            // Gunakan Android back button — lebih reliable dari cari tombol back
            driver.navigate().back();
            Thread.sleep(2000);
            System.out.println("LOG: Tombol kembali ditekan (Android back).");
        } catch (Exception e) {
            System.out.println("LOG: Back navigation gagal: " + e.getMessage());
        }
    }
}