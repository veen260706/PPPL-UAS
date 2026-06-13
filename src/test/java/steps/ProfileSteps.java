package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.ProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class ProfileSteps {

    private AndroidDriver driver;
    private ProfilePage profilePage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    // =========================================================================
    // ⚠️ PENTING: Ganti kedua string di bawah ini dengan Kredensial Akun Riil
    // yang SUDAH PASTI TERDAFTAR & bisa digunakan login di aplikasi kalian!
    // =========================================================================
    private final String EMAIL_VALID_SEBENARNYA = "profilesuccess@gmail.com";
    private final String PASSWORD_VALID_SEBENARNYA = "Password123";

    @Given("User sudah login dan berada di halaman Profile")
    public void user_sudah_login_dan_berada_di_halaman_profile() {
        driver = Hooks.driver;
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(12));

        System.out.println("LOG: Menjalankan prasyarat login Halaman Profile murni.");

        // Step 1: Penanganan layar Onboarding (Get Started) jika ada
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started') or contains(@text,'Mulai')]");
            List<WebElement> onboarding = driver.findElements(btnGetStarted);
            if (!onboarding.isEmpty()) {
                onboarding.get(0).click();
                System.out.println("LOG: Klik Get Started berhasil.");
                Thread.sleep(1500);
            }
        } catch (Exception ignored) {}

        // Step 2: Langsung Eksekusi Login Menggunakan Akun Fix Valid
        try {
            System.out.println("LOG: Mengisi form login dengan akun terdaftar: " + EMAIL_VALID_SEBENARNYA);

            // Tunggu field email siap diisi
            WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("(//android.widget.EditText)[1]")));
            emailField.clear();
            emailField.sendKeys(EMAIL_VALID_SEBENARNYA);

            loginPage.enterPassword(PASSWORD_VALID_SEBENARNYA);
            loginPage.clickLogin();
            System.out.println("LOG: Perintah klik tombol login dikirim.");

            // Beri jeda waktu aplikasi memproses token otentikasi ke server internal/Firebase
            Thread.sleep(4000);
        } catch (Exception e) {
            System.out.println("LOG [CRITICAL]: Gagal mengisi/mengirim data login: " + e.getMessage());
        }

        // Step 3: Pindah ke Tab Profile di Navigasi Bawah (Bottom Navigation Bar)
        try {
            // Menggunakan pola deteksi XPath universal (Content-Desc / Text)
            By tabProfile = By.xpath("//*[contains(@content-desc,'Profile') or contains(@text,'Profile') or contains(@text,'Akun') or contains(@content-desc,'Account') or contains(@content-desc,'Profil')]");
            wait.until(ExpectedConditions.elementToBeClickable(tabProfile)).click();
            System.out.println("LOG: Sukses beralih ke halaman Profile.");
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("LOG [WARNING]: Deteksi lokasi tab objek via XPath gagal. Mengaktifkan injeksi tap pojok kanan bawah.");
            try {
                // Skenario cadangan jika menu berupa gambar tanpa teks/ID terdeteksi
                int width = driver.manage().window().getSize().getWidth();
                int height = driver.manage().window().getSize().getHeight();
                org.openqa.selenium.interactions.PointerInput finger = new org.openqa.selenium.interactions.PointerInput(org.openqa.selenium.interactions.PointerInput.Kind.TOUCH, "finger");
                org.openqa.selenium.interactions.Sequence tap = new org.openqa.selenium.interactions.Sequence(finger, 1);
                tap.addAction(finger.createPointerMove(Duration.ZERO, org.openqa.selenium.interactions.PointerInput.Origin.viewport(), (int)(width * 0.9), (int)(height * 0.95)));
                tap.addAction(finger.createPointerDown(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                tap.addAction(finger.createPointerUp(org.openqa.selenium.interactions.PointerInput.MouseButton.LEFT.asArg()));
                driver.perform(java.util.Arrays.asList(tap));
                Thread.sleep(2500);
            } catch (Exception ex) {
                System.out.println("LOG [FATAL]: Navigasi halaman profile buntu total: " + ex.getMessage());
            }
        }
    }

    // =================== LOGOUT ===================

    @When("User menekan tombol Logout")
    public void user_menekan_tombol_logout() {
        System.out.println("LOG: Memproses aksi penekanan tombol Logout.");
        profilePage.clickLogout();
    }

    @Then("User berhasil logout dan diarahkan ke halaman Login")
    public void user_berhasil_logout() {
        boolean loggedOut = profilePage.isLoggedOut();
        Assert.assertTrue(loggedOut, "Gagal! Aplikasi tidak kembali ke form Login/Welcome screen setelah logout.");
        System.out.println("LOG: Skenario Logout Sukses Terpenuhi.");
    }

    // =================== CHANGE PASSWORD ===================

    @When("User menekan tombol Change Password")
    public void user_menekan_tombol_change_password() {
        profilePage.clickChangePassword();
    }

    @When("User memasukkan current password {string}")
    public void user_memasukkan_current_password(String password) {
        profilePage.enterCurrentPassword(password);
    }

    @When("User memasukkan new password {string}")
    public void user_memasukkan_new_password(String password) {
        profilePage.enterNewPassword(password);
    }

    @When("User memasukkan confirm new password {string}")
    public void user_memasukkan_confirm_new_password(String password) {
        profilePage.enterConfirmNewPassword(password);
    }

    @When("User menekan tombol Save Password")
    public void user_menekan_tombol_save_password() {
        profilePage.clickSavePassword();
    }

    @Then("Password berhasil diubah")
    public void password_berhasil_diubah() {
        boolean success = profilePage.isChangePasswordSuccess();
        Assert.assertTrue(success, "Gagal! Indikator sukses ganti password tidak ditemukan.");
        System.out.println("LOG: Skenario Ganti Password Positif Berhasil.");
    }

    @Then("Sistem menampilkan pesan error profile")
    public void sistem_menampilkan_pesan_error_profile() {
        boolean error = profilePage.isChangePasswordError();
        Assert.assertTrue(error, "Gagal! Validasi error sistem saat password salah tidak muncul.");
        System.out.println("LOG: Skenario Negatif Ganti Password Sukses Ditangkap.");
    }

    // =================== CHANGE EMAIL ===================

    @When("User menekan tombol Change Email")
    public void user_menekan_tombol_change_email() {
        profilePage.clickChangeEmail();
    }

    @When("User memasukkan verify password {string}")
    public void user_memasukkan_verify_password(String password) {
        profilePage.enterVerifyPassword(password);
    }

    @When("User memasukkan new email {string}")
    public void user_memasukkan_new_email(String email) {
        profilePage.enterNewEmail(email);
    }

    @When("User menekan tombol Save Email")
    public void user_menekan_tombol_save_email() {
        profilePage.clickSaveEmail();
    }

    @Then("Email berhasil diubah")
    public void email_berhasil_diubah() {
        boolean success = profilePage.isChangeEmailSuccess();
        Assert.assertTrue(success, "Gagal! Email baru gagal diperbarui.");
        System.out.println("LOG: Skenario Ubah Email Berhasil.");
    }
}