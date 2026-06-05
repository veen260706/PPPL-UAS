package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.ProfilePage;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import java.time.Duration;
import java.util.Arrays;

public class ProfileSteps {

    private AndroidDriver driver;
    private ProfilePage profilePage;
    private LoginPage loginPage;
    private WebDriverWait wait;

    @Given("User sudah login dan berada di halaman Profile")
    public void user_sudah_login_dan_berada_di_halaman_profile() {
        driver = Hooks.driver;
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Step 1: Get Started kalau ada
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started')]");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
            System.out.println("LOG: Klik Get Started.");
        } catch (Exception e) {
            System.out.println("LOG: Sudah di halaman Login.");
        }

        // Step 2: Login
        loginPage.enterEmail("testlogin@gmail.com");
        loginPage.enterPassword("Password123");
        loginPage.clickLogin();
        System.out.println("LOG: Login dilakukan.");

        // Step 3: Tunggu Dashboard load
        try { Thread.sleep(6000); } catch (Exception ignored) {}

        // Step 4: Tap koordinat icon Profile di bottom nav (paling kanan)
        try {
            PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
            Sequence tap = new Sequence(finger, 1);
            tap.addAction(finger.createPointerMove(Duration.ZERO,
                    PointerInput.Origin.viewport(), 1176, 2820));
            tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
            tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
            driver.perform(Arrays.asList(tap));
            Thread.sleep(2000);
            System.out.println("LOG: Navigasi ke Profile berhasil.");
        } catch (Exception e) {
            System.out.println("LOG: Navigasi ke Profile gagal: " + e.getMessage());
        }
    }

    // =================== LOGOUT ===================

    @When("User menekan tombol Logout")
    public void user_menekan_tombol_logout() {
        profilePage.clickLogout();
    }

    @Then("User berhasil logout dan diarahkan ke halaman Login")
    public void user_berhasil_logout() {
        boolean loggedOut = profilePage.isLoggedOut();
        Assert.assertTrue(loggedOut, "Gagal! Tidak redirect ke Login setelah logout.");
        System.out.println("LOG: Logout sukses.");
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
        Assert.assertTrue(success, "Gagal! Password tidak berhasil diubah.");
        System.out.println("LOG: Change password sukses.");
    }

    @Then("Sistem menampilkan pesan error profile")
    public void sistem_menampilkan_pesan_error_profile() {
        boolean error = profilePage.isChangePasswordError();
        Assert.assertTrue(error, "Gagal! Seharusnya muncul error.");
        System.out.println("LOG: Error profile terdeteksi.");
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
        Assert.assertTrue(success, "Gagal! Email tidak berhasil diubah.");
        System.out.println("LOG: Change email sukses.");
    }
}