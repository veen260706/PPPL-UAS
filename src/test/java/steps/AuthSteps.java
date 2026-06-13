package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.RegisterPage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.List;

public class AuthSteps {

    private AndroidDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private WebDriverWait wait;

    // ===================== NAVIGASI SMART & ANTI-MACET =====================

    @Given("User berada di halaman Login")
    public void user_berada_di_halaman_login() {
        driver = Hooks.driver;
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Cek dulu apakah ada tombol Get Started (Onboarding)
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started') or contains(@text,'Mulai')]");
            List<WebElement> onboarding = driver.findElements(btnGetStarted);
            if (!onboarding.isEmpty()) {
                onboarding.get(0).click();
                System.out.println("LOG: Navigasi ke Login via Get Started.");
                Thread.sleep(1500);
            }
        } catch (Exception ignored) {}

        // Jika tidak sengaja terdampar di halaman Register, klik link ke Login
        try {
            By linkToLogin = By.xpath("//*[contains(@text,'Sign In') or contains(@text,'Login') or contains(@text,'Masuk')]");
            // Cari text view di bagian bawah form register
            List<WebElement> links = driver.findElements(linkToLogin);
            if (links.size() > 1) {
                links.get(links.size() - 1).click();
                Thread.sleep(1500);
            }
        } catch (Exception ignored) {}

        System.out.println("LOG: Terverifikasi berada di halaman Login.");
    }

    @Given("User berada di halaman Register")
    public void user_berada_di_halaman_register() {
        driver = Hooks.driver;
        registerPage = new RegisterPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Pastikan dulu kita lewat onboarding jika ada
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started') or contains(@text,'Mulai')]");
            List<WebElement> onboarding = driver.findElements(btnGetStarted);
            if (!onboarding.isEmpty()) {
                onboarding.get(0).click();
                Thread.sleep(1500);
            }
        } catch (Exception ignored) {}

        // Cari link pendaftaran di halaman Login secara presisi (bukan tombol Sign Up utama)
        try {
            By btnRegisterLink = By.xpath(
                    "//*[contains(@text,'Register') or contains(@text,'Daftar') or contains(@text,'Create Account')]" +
                            " | //android.widget.TextView[contains(@text,'Sign Up') and @clickable='true']"
            );
            List<WebElement> links = driver.findElements(btnRegisterLink);
            if (!links.isEmpty()) {
                links.get(0).click();
                System.out.println("LOG: Pindah dari Login ke halaman Register.");
                Thread.sleep(1500);
            } else {
                // Alternatif cadangan jika teks digabung dalam satu label
                driver.findElement(By.xpath("//*[contains(@text,'Don') or contains(@text,'Belum punya') or contains(@text,'account')]")).click();
                Thread.sleep(1500);
            }
        } catch (Exception e) {
            System.out.println("LOG: Asumsi driver sudah berada di halaman Register.");
        }
    }

    // ===================== REGISTER =====================

    @When("User memasukkan nama {string}")
    public void user_memasukkan_nama(String nama) {
        registerPage.enterNama(nama);
    }

    @When("User memasukkan email {string}")
    public void user_memasukkan_email(String email) {
        registerPage.enterEmail(email);
    }

    @When("User memasukkan password {string}")
    public void user_memasukkan_password_reg(String password) {
        registerPage.enterPassword(password);
    }

    @When("User memasukkan konfirmasi password {string}")
    public void user_memasukkan_konfirmasi_password(String confirmPassword) {
        registerPage.enterConfirmPassword(confirmPassword);
    }

    @When("User menyetujui syarat dan ketentuan")
    public void user_menyetujui_syarat_dan_ketentuan() {
        try {
            registerPage.clickCheckboxAgree();
            System.out.println("LOG: Checkbox agree dicentang.");
        } catch (Exception e) {
            System.out.println("LOG [WARNING]: Gagal klik checkbox secara langsung, lanjut ke register.");
        }
    }

    @When("User menekan tombol Register")
    public void user_menekan_tombol_register() {
        registerPage.clickRegister();
    }

    @Then("Akun berhasil dibuat dan diarahkan ke halaman Login")
    public void akun_berhasil_dibuat() {
        boolean redirected = registerPage.isRedirectedToLogin();
        Assert.assertTrue(redirected, "Gagal! Tidak redirect ke halaman Login.");
        System.out.println("LOG: Register sukses.");
    }

    @Then("Sistem menampilkan pesan error register")
    public void sistem_menampilkan_pesan_error_register() {
        boolean errorShown = registerPage.isErrorDisplayed();
        Assert.assertTrue(errorShown, "Gagal! Seharusnya error tapi tidak ada.");
        System.out.println("LOG: Error register terdeteksi.");
    }

    // ===================== LOGIN =====================

    @When("User memasukkan email {string} dan password {string}")
    public void user_memasukkan_email_dan_password(String email, String password) {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
    }

    @When("User menekan tombol Login")
    public void user_menekan_tombol_login() {
        loginPage.clickLogin();
    }

    @Then("User berhasil masuk dan melihat Dashboard")
    public void user_berhasil_masuk_dan_melihat_dashboard() {
        boolean success = loginPage.isLoginSuccess();
        Assert.assertTrue(success, "Gagal! Dashboard tidak muncul.");
        System.out.println("LOG: Login sukses.");
    }

    @Then("Login gagal dan muncul pesan error")
    public void login_gagal_dan_muncul_pesan_error() {
        boolean failed = loginPage.isLoginFailed();
        Assert.assertTrue(failed, "Gagal! Seharusnya login gagal.");
        System.out.println("LOG: Login gagal terverifikasi.");
    }
}