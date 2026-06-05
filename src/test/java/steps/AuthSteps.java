package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.RegisterPage;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;

public class AuthSteps {

    private AndroidDriver driver;
    private LoginPage loginPage;
    private RegisterPage registerPage;
    private WebDriverWait wait;

    // ===================== NAVIGASI =====================

    @Given("User berada di halaman Login")
    public void user_berada_di_halaman_login() {
        driver = Hooks.driver;
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            By btnGetStarted = By.xpath(
                    "//*[contains(@text,'Get Started')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
            System.out.println("LOG: Navigasi ke Login via Get Started.");
        } catch (Exception e) {
            System.out.println("LOG: Sudah di halaman Login.");
        }
    }

    @Given("User berada di halaman Register")
    public void user_berada_di_halaman_register() {
        driver = Hooks.driver;
        registerPage = new RegisterPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        try {
            // Coba dari onboarding dulu
            By btnSignIn = By.xpath("//*[contains(@text,'Sign in')]");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(btnSignIn)).click();
            System.out.println("LOG: Navigasi via Sign in onboarding.");
        } catch (Exception e) {
            try {
                // Sudah di halaman Login, cari link ke Register
                By btnRegisterLink = By.xpath(
                        "//*[contains(@text,'Register') or contains(@text,'Sign Up') or contains(@text,'Daftar')]"
                );
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(btnRegisterLink)).click();
                System.out.println("LOG: Navigasi via link Register di halaman Login.");
            } catch (Exception e2) {
                System.out.println("LOG: Sudah di halaman Register.");
            }
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
        registerPage.clickCheckboxAgree();
        System.out.println("LOG: Checkbox agree dicentang.");
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