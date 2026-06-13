package steps;

import io.cucumber.java.en.*;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.*;

import java.time.Duration;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;

import static org.testng.Assert.*;

public class EndToEndSteps {

    AndroidDriver driver = Hooks.driver;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));

    private static final String APP_PACKAGE = "com.tentangdental.app";

    RegisterPage registerPage = new RegisterPage(driver);
    LoginPage loginPage = new LoginPage(driver);
    DashboardPage dashboardPage = new DashboardPage(driver);
    PatientListPage patientListPage = new PatientListPage(driver);
    UploadPage uploadPage = new UploadPage(driver);
    ExaminationHistoryPage historyPage = new ExaminationHistoryPage(driver);

    private String registeredEmail = "e2e" + System.currentTimeMillis() + "@test.com";
    private final String registeredPassword = "Password123!";
    private String namaPasienTerpilih = "Agus Suharno";

    // ===================== REGISTER =====================

    @Given("user berada di halaman Register")
    public void user_berada_di_halaman_register() {
        try {
            Thread.sleep(2000);

            try {
                By btnGetStarted = By.xpath("//*[contains(@text,'Get Started') or contains(@text,'Mulai')]");
                driver.findElement(btnGetStarted).click();
                System.out.println("LOG: Tombol 'Get Started' diklik.");
                Thread.sleep(2000);
            } catch (Exception ignored) {}

            try {
                By linkSignUp = By.xpath(
                        "//*[contains(@text,'Sign up') or contains(@text,'Sign Up') " +
                                "or contains(@text,'Daftar') or contains(@text,'Register')]"
                );
                driver.findElement(linkSignUp).click();
                System.out.println("LOG: Link ke halaman Register diklik.");
                Thread.sleep(2000);
            } catch (Exception ignored) {}

        } catch (Exception e) {
            System.out.println("LOG: Error saat setup halaman Register: " + e.getMessage());
        }
    }

    @When("user mendaftar dengan data yang valid")
    public void user_mendaftar_dengan_data_valid() {
        registerPage.enterNama("E2E Tester");
        registerPage.enterEmail(registeredEmail);
        registerPage.enterPassword(registeredPassword);
        registerPage.enterConfirmPassword(registeredPassword);

        try {
            registerPage.clickCheckboxAgree();
        } catch (Exception e) {
            System.out.println("LOG: Mencoba mengklik checkbox.");
        }

        registerPage.clickRegister();

        registeredEmail = registerPage.getFinalEmail();
        System.out.println("LOG: Email final yang akan dipakai login: " + registeredEmail);
    }

    @Then("user berhasil register dan diarahkan ke halaman Login")
    public void user_berhasil_register() {
        assertTrue(registerPage.isRedirectedToLogin(),
                "Tidak redirect ke halaman Login setelah register");
    }

    // ===================== LOGIN =====================

    @When("user login dengan akun yang baru didaftarkan")
    public void user_login_dengan_akun_baru() {
        try {
            By inputEmail = By.xpath("//android.widget.EditText[1]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail));
        } catch (Exception ignored) {}

        loginPage.enterEmail(registeredEmail);
        loginPage.enterPassword(registeredPassword);
        loginPage.clickLogin();
    }

    @Then("user berhasil masuk ke Dashboard")
    public void user_berhasil_masuk_dashboard() {
        assertTrue(loginPage.isLoginSuccess(),
                "Login gagal, Dashboard tidak terdeteksi");
        assertTrue(dashboardPage.isHeaderDisplayed(),
                "Header Dashboard tidak ditemukan");
    }

    // ===================== PILIH PASIEN =====================

    @When("user memilih salah satu pasien dari daftar pasien hadir")
    public void user_memilih_pasien() throws InterruptedException {
        assertTrue(dashboardPage.hasPasienInList(),
                "Tidak ada pasien dalam daftar di Dashboard");

        System.out.println("LOG: Pasien target di-lock ke: " + namaPasienTerpilih);

        dashboardPage.clickKartuPasienPertama();
        Thread.sleep(1500);

        if (dashboardPage.isModalUbahStatusDisplayed()) {
            dashboardPage.pilihStatusModal("Upload Foto");
            Thread.sleep(800);

            try {
                By checkboxRontgen = By.xpath(
                        "//*[contains(@text,'Rontgen')]/following-sibling::android.widget.CheckBox" +
                                " | //*[contains(@text,'Rontgen')]"
                );
                wait.until(ExpectedConditions.elementToBeClickable(checkboxRontgen)).click();
                System.out.println("LOG: Checkbox 'Rontgen' berhasil dicentang.");
                Thread.sleep(800);
            } catch (Exception e) {
                System.out.println("LOG: Gagal klik checkbox Rontgen, mencoba alternatif...");
                try {
                    driver.findElement(By.xpath("//*[contains(@text,'Rontgen')]")).click();
                } catch (Exception ignored) {}
            }

            try {
                dashboardPage.clickSimpanStatus();
                System.out.println("LOG: Tombol Simpan Status di modal berhasil diklik.");
            } catch (Exception e) {
                try {
                    driver.findElement(By.xpath("//*[contains(@text,'Simpan') or contains(@text,'SIMPAN') or @text='Simpan Status']")).click();
                } catch (Exception ignored) {}
            }
            System.out.println("LOG: Menunggu transisi halaman form upload... ");
            Thread.sleep(4000);
        }
    }

    // ===================== UPLOAD & FORM DATA =====================

    @Then("halaman form pemeriksaan\\/upload rontgen terbuka")
    public void halaman_upload_terbuka() {
        boolean isLoaded = false;
        try {
            try {
                By modalTitle = By.xpath("//*[contains(@text,'Ubah Status') or contains(@text,'Status')]");
                new WebDriverWait(driver, Duration.ofSeconds(5)).until(ExpectedConditions.invisibilityOfElementLocated(modalTitle));
            } catch (Exception ignored) {}

            By indikatorHalaman = By.xpath("//*[contains(@text,'Upload') or contains(@text,'Keluhan') or contains(@text,'Rontgen') or contains(@text,'Pemeriksaan')]");
            wait.until(ExpectedConditions.visibilityOfElementLocated(indikatorHalaman));
            System.out.println("LOG: Halaman Form Pemeriksaan / Upload Rontgen terdeteksi aktif.");
            isLoaded = true;
        } catch (Exception e) {
            System.out.println("LOG: Gagal mendeteksi halaman upload: " + e.getMessage());
        }

        assertTrue(isLoaded, "Halaman Upload/Form Pemeriksaan tidak terbuka");
    }

    @When("user mengupload foto rontgen dan mengisi data pemeriksaan")
    public void user_upload_dan_isi_data() throws InterruptedException {
        Thread.sleep(1000);

        System.out.println("LOG: Membuka galeri foto langsung via Page Object...");
        try {
            uploadPage.openGallery();
            Thread.sleep(3000);

            try {
                By btnIzinkan = By.xpath("//*[contains(@text,'Izinkan') or contains(@text,'Allow') or contains(@text,'ALLOW') or contains(@text,'IZINKAN')]");
                List<WebElement> dialogIzin = driver.findElements(btnIzinkan);
                if (!dialogIzin.isEmpty()) {
                    dialogIzin.get(0).click();
                    Thread.sleep(2000);
                    uploadPage.openGallery();
                    Thread.sleep(3000);
                }
            } catch (Exception ignored) {}
        } catch (Exception e) {
            System.out.println("LOG [WARNING]: Error saat memicu openGallery(): " + e.getMessage());
        }

        pastikanKembaliKeApp();
        Thread.sleep(1500);

        // =========================================================================
        // 1. ISI PEMERIKSAAN FISIK DULUAN
        // =========================================================================
        System.out.println("LOG: [URUTAN BARU] Mengisi data Pemeriksaan Fisik (Tanda Vital) terlebih dahulu...");
        isiDataPemeriksaanFisikPaksa();
        Thread.sleep(1000);

        // Menghilangkan numpad angka dengan klik label 'KELUHAN UTAMA'
        System.out.println("LOG: Menghilangkan numpad angka dengan klik label 'KELUHAN UTAMA'...");
        try {
            By labelKeluhan = By.xpath("//android.widget.TextView[@text='KELUHAN UTAMA' or contains(@text, 'KELUHAN')]");
            driver.findElement(labelKeluhan).click();
        } catch (Exception ignored) {}
        Thread.sleep(1000);

        // =========================================================================
        // 2. INPUT KELUHAN UTAMA
        // =========================================================================
        System.out.println("LOG: Membuka dan mengisi kolom Keluhan Utama...");
        try {
            By selectorKeluhan = By.xpath("//android.widget.EditText[contains(@text, 'Contoh :') or contains(@text, 'goyang')]");
            WebElement fieldKeluhan = wait.until(ExpectedConditions.elementToBeClickable(selectorKeluhan));
            fieldKeluhan.click();
            Thread.sleep(500);
            fieldKeluhan.sendKeys("Gigi depan goyang, nyeri saat dipakai makan");
            System.out.println("LOG: Kolom Keluhan Utama berhasil diisi.");
        } catch (Exception e) {
            System.out.println("LOG [WARNING]: Gagal pakai placeholder, jalankan rencana cadangan (index EditText)...");
            try {
                List<WebElement> semuaInput = driver.findElements(By.className("android.widget.EditText"));
                if (!semuaInput.isEmpty()) {
                    WebElement fieldAlternatif = semuaInput.get(semuaInput.size() - 1);
                    fieldAlternatif.click();
                    fieldAlternatif.sendKeys("Gigi depan goyang, nyeri saat dipakai makan");
                }
            } catch (Exception ex) {
                System.out.println("LOG [ERROR]: Gagal total mengisi keluhan utama: " + ex.getMessage());
            }
        }
        Thread.sleep(1000);

        // Klik kembali label untuk menyembunyikan keyboard teks secara natural (Anti-Back)
        try {
            By labelKeluhan = By.xpath("//android.widget.TextView[@text='KELUHAN UTAMA' or contains(@text, 'KELUHAN')]");
            driver.findElement(labelKeluhan).click();
            System.out.println("LOG: Menutup keyboard teks dengan aman melalui klik label.");
        } catch (Exception ignored) {}
        Thread.sleep(1000);

        System.out.println("LOG: Menggulir layar ke area Extra Oral Examination...");
        gulirLayarKeBawahSangatAman();
        Thread.sleep(1500);

        // =========================================================================
        // 3. TEKAN PILIHAN EXTRA ORAL EXAMINATION
        // =========================================================================
        isiExtraOralButtons();
        Thread.sleep(1000);

        System.out.println("LOG: Menggulir layar ke bagian paling bawah untuk mencari Dokter...");
        gulirLayarKeBawahAgresif();
        Thread.sleep(1500);

        // =========================================================================
        // 4. DROPDOWN SELEKSI DOKTER
        // =========================================================================
        pilihDokterWajib();
    }

    @When("user menekan tombol simpan")
    public void user_tekan_simpan() throws InterruptedException {
        System.out.println("LOG: Memulai langkah pencarian tombol Save Data asli.");

        By btnSimpanForm = By.xpath(
                "//android.widget.Button[@text='Save Data' or @text='SAVE DATA' or @text='Simpan']" +
                        " | //*[contains(@text,'Save Data') or contains(@text,'SAVE DATA')]" +
                        " | //android.view.ViewGroup[@clickable='true']//*[contains(@text,'Save') or contains(@text,'Simpan')]"
        );

        boolean ketemu = false;
        for (int i = 0; i < 4; i++) {
            try {
                List<WebElement> elements = driver.findElements(btnSimpanForm);
                if (!elements.isEmpty()) {
                    WebElement tombolAsli = elements.get(elements.size() - 1);
                    tombolAsli.click();
                    System.out.println("LOG: Sukses menekan tombol Save Data utama!");
                    ketemu = true;
                    break;
                }
            } catch (Exception e) {
                System.out.println("LOG: Error klik tombol simpan: " + e.getMessage());
            }

            System.out.println("LOG: Tombol belum terjangkau, scroll tipis ke bawah...");
            gulirLayarKeBawahSangatAman();
            Thread.sleep(1000);
        }
    }

    @Then("data pemeriksaan berhasil tersimpan")
    public void data_berhasil_tersimpan() {
        boolean isSuccess = false;
        try {
            By txtSuccessMsg = By.xpath(
                    "//*[contains(@text,'berhasil') or contains(@text,'Berhasil') or contains(@text,'tersimpan') " +
                            "or contains(@text,'Tersimpan') or contains(@text,'Success') or contains(@text,'Selesai')]"
            );
            WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(12));
            shortWait.until(ExpectedConditions.visibilityOfElementLocated(txtSuccessMsg));
            System.out.println("LOG: Toast/Notifikasi sukses berhasil ditemukan.");
            isSuccess = true;

            try {
                By btnOK = By.xpath("//*[@text='OK'] | //*[@text='Ok'] | //*[@text='ok'] | //*[@text='Oke']");
                driver.findElement(btnOK).click();
            } catch (Exception ignored) {}
        } catch (Exception e) {
            System.out.println("LOG: Memeriksa apakah halaman otomatis berpindah ke History...");
            if (historyPage.isTabHistoryDisplayed() || historyPage.isSearchBarDisplayed()) {
                isSuccess = true;
            }
        }

        assertTrue(isSuccess, "Pesan sukses simpan tidak terdeteksi.");
    }

    // ===================== HISTORY =====================

    @When("user membuka halaman Examination History")
    public void user_buka_history() {
        historyPage.clickTabHistory();
        assertTrue(historyPage.isTabHistoryDisplayed(),
                "Tab History tidak terdeteksi");
    }

    @When("user mencari data pasien yang baru disimpan")
    public void user_cari_data_pasien() {
        System.out.println("LOG: Mencari nama pasien wajib di History: " + namaPasienTerpilih);
        historyPage.inputSearch(namaPasienTerpilih);
    }

    @Then("data riwayat pemeriksaan pasien tersebut muncul dengan benar")
    public void data_riwayat_muncul() {
        assertTrue(historyPage.isDaftarRiwayatDisplayed(),
                "Daftar riwayat tidak tampil");
        assertTrue(historyPage.isNamaPasienRiwayatDisplayed(),
                "Nama pasien tidak ditemukan di riwayat");
    }

    // =========================================================================
    // HELPER LOGIC INPUT & SELEKSI
    // =========================================================================

    private void isiDataPemeriksaanFisikPaksa() {
        inputUlangFieldSetelahLabel("Tekanan Darah", "120", 1);
        inputUlangFieldSetelahLabel("Nadi", "80", 1);
        inputUlangFieldSetelahLabel("Respirasi", "18", 1);
        inputUlangFieldSetelahLabel("Suhu", "36.5", 1);
        inputUlangFieldSetelahLabel("TB/BB", "170", 1);
        inputUlangFieldSetelahLabel("TB/BB", "60", 2);
    }

    private void inputUlangFieldSetelahLabel(String labelTeks, String nilaiInput, int occurrence) {
        try {
            By inputField = By.xpath(
                    "(//*[contains(translate(@text, 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz'), '" + labelTeks.toLowerCase() + "')]/following::android.widget.EditText)[" + occurrence + "]"
            );
            List<WebElement> elements = driver.findElements(inputField);
            if (!elements.isEmpty()) {
                WebElement field = elements.get(0);
                field.click();
                field.clear();
                field.sendKeys(nilaiInput);
                System.out.println("LOG: Sukses isi '" + labelTeks + "' -> " + nilaiInput);
            }
        } catch (Exception e) {
            System.out.println("LOG: Lewati field label '" + labelTeks + "'");
        }
    }

    private void isiExtraOralButtons() {
        System.out.println("LOG: Mengeksekusi penekanan opsi Extra Oral Examination...");
        try {
            By opsiKelenjar = By.xpath("//*[contains(@text,'Tidak teraba') or contains(@text,'Tidak Teraba') or contains(@text,'Normal')]");
            List<WebElement> listKelenjar = driver.findElements(opsiKelenjar);
            if (!listKelenjar.isEmpty()) {
                listKelenjar.get(0).click();
                System.out.println("LOG: Opsi Extra Oral 1 (Kelenjar) berhasil dicentang.");
            }

            By opsiBengkak = By.xpath("//*[contains(@text,'Tidak ada') or contains(@text,'Tidak Ada') or contains(@text,'Asimetris')]");
            List<WebElement> listBengkak = driver.findElements(opsiBengkak);
            if (!listBengkak.isEmpty()) {
                listBengkak.get(listBengkak.size() - 1).click();
                System.out.println("LOG: Opsi Extra Oral 2 (Pembengkakan) berhasil dicentang.");
            }
        } catch (Exception e) {
            System.out.println("LOG [WARNING]: Opsi teks gagal, eksekusi Rencana B...");
            try {
                List<WebElement> sirkularOpsi = driver.findElements(By.xpath("//android.view.ViewGroup[@clickable='true']"));
                if (sirkularOpsi.size() > 4) {
                    sirkularOpsi.get(2).click();
                    Thread.sleep(500);
                    sirkularOpsi.get(sirkularOpsi.size() - 2).click();
                    System.out.println("LOG: Sukses menekan opsi Extra Oral via koordinat elemen dinamis.");
                }
            } catch (Exception ex) {
                System.out.println("LOG [ERROR]: Gagal total menekan opsi Extra Oral: " + ex.getMessage());
            }
        }
    }

    private void pilihDokterWajib() {
        System.out.println("LOG: [TAMBAHAN] Melakukan ekstra scroll tipis ke bawah agar dropdown terlihat sempurna...");
        gulirLayarKeBawahSangatAman();
        try { Thread.sleep(1000); } catch (Exception ignored) {}

        System.out.println("LOG: Membuka Dropdown Pemilihan Dokter...");

        By dropdownDokter = By.xpath(
                "//*[contains(@text,'Pilih Dokter') or @text='Pilih Dokter']" +
                        " | //android.widget.Spinner" +
                        " | //android.view.ViewGroup[@clickable='true']//*[contains(@text,'Dokter')]" +
                        " | //android.widget.EditText[contains(@text,'Pilih Dokter')]"
        );

        try {
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(dropdownDokter));
            dropdown.click();
            System.out.println("LOG: Berhasil klik dropdown Pilih Dokter.");
            Thread.sleep(2000);

            By listDokterReal = By.xpath(
                    "//*[contains(@text,'drg') or contains(@text,'Drg') or contains(@text,'drg.') or contains(@text,'Sp.')]"
            );
            List<WebElement> opsiList = driver.findElements(listDokterReal);

            WebElement targetDokter = null;
            for (WebElement opsi : opsiList) {
                String nama = opsi.getText();
                if (nama != null && !nama.contains("Upload") && !nama.contains("Foto") && !nama.contains("SELECT")) {
                    targetDokter = opsi;
                    break;
                }
            }

            if (targetDokter != null) {
                String namaDokterFix = targetDokter.getText();
                targetDokter.click();
                System.out.println("LOG: Sukses memilih nama dokter -> " + namaDokterFix);
            } else if (!opsiList.isEmpty()) {
                String namaFallback = opsiList.get(0).getText();
                opsiList.get(0).click();
                System.out.println("LOG: Memilih opsi dokter fallback pertama -> " + namaFallback);
            }
            Thread.sleep(1500);
        } catch (Exception e) {
            System.out.println("LOG [ERROR]: Dropdown dokter tidak merespon/tidak ketemu: " + e.getMessage());

            System.out.println("LOG: Mencoba rencana cadangan klik koordinat area dropdown...");
            try {
                int lebar = driver.manage().window().getSize().width;
                int tinggi = driver.manage().window().getSize().height;

                PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
                Sequence clickDropdown = new Sequence(finger, 1);
                clickDropdown.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), lebar / 2, (int) (tinggi * 0.82)));
                clickDropdown.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
                clickDropdown.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(Collections.singletonList(clickDropdown));

                Thread.sleep(2000);
                List<WebElement> opsiList = driver.findElements(By.xpath("//*[contains(@text,'drg') or contains(@text,'Drg') or contains(@text,'Sp.')]"));
                if (!opsiList.isEmpty()) {
                    opsiList.get(0).click();
                    System.out.println("LOG: Sukses memilih dokter via rencana cadangan.");
                }
            } catch (Exception ex) {
                System.out.println("LOG [ERROR]: Rencana cadangan klik dropdown pun gagal: " + ex.getMessage());
            }
        }
    }

    // =========================================================================
    // SCROLL & GESTURE UTILITIES
    // =========================================================================

    private void pastikanKembaliKeApp() {
        try {
            WebDriverWait pkgWait = new WebDriverWait(driver, Duration.ofSeconds(10));
            pkgWait.until(d -> APP_PACKAGE.equals(((AndroidDriver) d).getCurrentPackage()));
        } catch (Exception e) {
            try {
                driver.activateApp(APP_PACKAGE);
                Thread.sleep(1500);
            } catch (Exception ignored) {}
        }
    }

    private void gulirLayarKeBawahSangatAman() {
        try {
            int lebar = driver.manage().window().getSize().width;
            int tinggi = driver.manage().window().getSize().height;

            int startX = lebar / 2;
            int startY = (int) (tinggi * 0.55);
            int endY = (int) (tinggi * 0.30);

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1);

            scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), startX, startY));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(800), PointerInput.Origin.viewport(), startX, endY));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(scroll));
        } catch (Exception e) {
            System.out.println("LOG: Swipe gagal: " + e.getMessage());
        }
    }

    private void gulirLayarKeBawahAgresif() {
        try {
            int lebar = driver.manage().window().getSize().width;
            int tinggi = driver.manage().window().getSize().height;

            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence scroll = new Sequence(finger, 1);

            scroll.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), lebar / 2, (int) (tinggi * 0.55)));
            scroll.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            scroll.addAction(finger.createPointerMove(Duration.ofMillis(700), PointerInput.Origin.viewport(), lebar / 2, (int) (tinggi * 0.20)));
            scroll.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));

            driver.perform(Collections.singletonList(scroll));
            Thread.sleep(1000);
        } catch (Exception e) {
            System.out.println("LOG: Swipe agresif gagal: " + e.getMessage());
        }
    }
}