package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.appium.java_client.android.AndroidDriver;
import pages.LoginPage;
import pages.ProfilePage;
import pages.RegisterPage;
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
    private RegisterPage registerPage;
    private WebDriverWait wait;
    public static String changedEmail = "";
    public static String lastNewPasswordEntered = "Password123";
    private boolean emailResetSuccess = false;
    private boolean passwordResetSuccess = false;

    private static final String EMAIL_FILE_PATH = "target/active_test_email.txt";
    private static final String PASSWORD_FILE_PATH = "target/active_test_password.txt";

    private static void saveActiveEmail(String email) {
        try {
            java.io.File file = new java.io.File(EMAIL_FILE_PATH);
            file.getParentFile().mkdirs();
            java.nio.file.Files.writeString(file.toPath(), email);
            System.out.println("LOG [Auto-Recovery]: Menyimpan email aktif ke file: " + email);
        } catch (Exception e) {
            System.out.println("LOG [Auto-Recovery]: Gagal menyimpan email aktif: " + e.getMessage());
        }
    }

    private static void saveActivePassword(String password) {
        try {
            java.io.File file = new java.io.File(PASSWORD_FILE_PATH);
            file.getParentFile().mkdirs();
            java.nio.file.Files.writeString(file.toPath(), password);
            System.out.println("LOG [Auto-Recovery]: Menyimpan password aktif ke file: " + password);
        } catch (Exception e) {
            System.out.println("LOG [Auto-Recovery]: Gagal menyimpan password aktif: " + e.getMessage());
        }
    }

    private static String getActivePassword() {
        try {
            java.io.File file = new java.io.File(PASSWORD_FILE_PATH);
            if (file.exists()) {
                String password = java.nio.file.Files.readString(file.toPath()).trim();
                if (!password.isEmpty()) {
                    return password;
                }
            }
        } catch (Exception e) {
            System.out.println("LOG [Auto-Recovery]: Gagal membaca password aktif: " + e.getMessage());
        }
        return "Password123";
    }

    private static String getActiveEmail() {
        try {
            java.io.File file = new java.io.File(EMAIL_FILE_PATH);
            if (file.exists()) {
                String email = java.nio.file.Files.readString(file.toPath()).trim();
                if (!email.isEmpty()) {
                    return email;
                }
            }
        } catch (Exception e) {
            System.out.println("LOG [Auto-Recovery]: Gagal membaca email aktif: " + e.getMessage());
        }
        return "testlogin@gmail.com";
    }

    private void navigateToProfile() {
        try { Thread.sleep(3000); } catch (Exception ignored) {}
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

    @Given("User sudah login dan berada di halaman Profile")
    public void user_sudah_login_dan_berada_di_halaman_profile() {
        driver = Hooks.driver;
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);
        registerPage = new RegisterPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        // Step 1: Klik Get Started onboarding jika ada
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started')]");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
            System.out.println("LOG: Klik Get Started onboarding.");
        } catch (Exception e) {
            System.out.println("LOG: Get Started tidak muncul / terlewat.");
        }

        // Step 2: Login
        String loginEmail = getActiveEmail();
        String loginPassword = getActivePassword();
        System.out.println("LOG [Auto-Recovery]: Mencoba login dengan email aktif: " + loginEmail + " dan password: " + loginPassword);
        loginPage.enterEmail(loginEmail);
        loginPage.enterPassword(loginPassword);
        loginPage.clickLogin();

        boolean loginSuccess = false;
        boolean wasLoggedInWithAlternativePassword = false;
        String alternativePassword = loginPassword.equals("Password123") ? "NewPassword123" : "Password123";

        // Cek login sukses dengan loginPassword
        try {
            By dashboard = By.xpath(
                    "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
            loginSuccess = true;
            System.out.println("LOG: Login berhasil dengan password aktif: " + loginPassword);
        } catch (Exception e) {
            loginSuccess = false;
        }

        // Jika gagal, coba dengan alternativePassword
        if (!loginSuccess) {
            System.out.println("LOG [Auto-Recovery]: Gagal login dengan password " + loginPassword + ". Mencoba dengan " + alternativePassword + "...");
            
            // Tutup alert error login terlebih dahulu agar text field di belakangnya bisa diklik
            try {
                By btnOK = By.xpath("//*[@text='OK']");
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                System.out.println("LOG [Auto-Recovery]: Popup OK error login diklik.");
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            loginPage.enterEmail(loginEmail);
            loginPage.enterPassword(alternativePassword);
            loginPage.clickLogin();

            try {
                By dashboard = By.xpath(
                        "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
                );
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
                loginSuccess = true;
                wasLoggedInWithAlternativePassword = true;
                saveActivePassword(alternativePassword);
                System.out.println("LOG [Auto-Recovery]: Login berhasil dengan password alternatif: " + alternativePassword);
            } catch (Exception ex) {
                loginSuccess = false;
            }
        }

        // Jika masih gagal, coba fallback email
        if (!loginSuccess) {
            String fallbackEmail = loginEmail.equals("testlogin@gmail.com") ? "testloginbaru@gmail.com" : "testlogin@gmail.com";
            System.out.println("LOG [Auto-Recovery]: Login gagal. Mencoba fallback email ke: " + fallbackEmail);
            
            // Tutup alert error login terlebih dahulu agar text field di belakangnya bisa diklik
            try {
                By btnOK = By.xpath("//*[@text='OK']");
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                System.out.println("LOG [Auto-Recovery]: Popup OK error login diklik.");
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            String currentPwd = getActivePassword();
            loginPage.enterEmail(fallbackEmail);
            loginPage.enterPassword(currentPwd);
            loginPage.clickLogin();

            try {
                By dashboard = By.xpath(
                        "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
                );
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
                loginSuccess = true;
                saveActiveEmail(fallbackEmail);
                loginEmail = fallbackEmail;
                System.out.println("LOG [Auto-Recovery]: Login berhasil dengan fallback email: " + fallbackEmail + " dan password: " + currentPwd);
            } catch (Exception ex) {
                loginSuccess = false;
            }

            if (!loginSuccess) {
                String otherPwd = currentPwd.equals("Password123") ? "NewPassword123" : "Password123";
                try {
                    By btnOK = By.xpath("//*[@text='OK']");
                    new WebDriverWait(driver, Duration.ofSeconds(3))
                            .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                    System.out.println("LOG [Auto-Recovery]: Popup OK error login diklik.");
                    Thread.sleep(1000);
                } catch (Exception ignored) {}

                loginPage.enterEmail(fallbackEmail);
                loginPage.enterPassword(otherPwd);
                loginPage.clickLogin();

                try {
                    By dashboard = By.xpath(
                            "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
                    );
                    new WebDriverWait(driver, Duration.ofSeconds(5))
                            .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
                    loginSuccess = true;
                    saveActiveEmail(fallbackEmail);
                    saveActivePassword(otherPwd);
                    loginEmail = fallbackEmail;
                    System.out.println("LOG [Auto-Recovery]: Login berhasil dengan fallback email: " + fallbackEmail + " dan password alternatif: " + otherPwd);
                } catch (Exception ex2) {
                    loginSuccess = false;
                }
            }
        }

        // Step 3: Auto-Recovery password jika terdeteksi menggunakan NewPassword123
        String activePwd = getActivePassword();
        if ("NewPassword123".equals(activePwd)) {
            System.out.println("LOG [Auto-Recovery]: Melakukan recovery password ke Password123...");
            navigateToProfile();
            profilePage.clickChangePassword();
            profilePage.enterCurrentPassword("NewPassword123");
            profilePage.enterNewPassword("Password123");
            profilePage.enterConfirmNewPassword("Password123");
            profilePage.clickSavePassword();
            if (profilePage.isChangePasswordSuccess()) {
                System.out.println("LOG [Auto-Recovery]: Recovery password sukses!");
                saveActivePassword("Password123");
                profilePage.clickLogout();
                profilePage.isLoggedOut();
                loginPage.enterEmail(loginEmail);
                loginPage.enterPassword("Password123");
                loginPage.clickLogin();
            } else {
                System.out.println("LOG [Auto-Recovery]: Gagal recovery password.");
            }
        }

        // Step 4: Auto-Recovery email jika terdeteksi menggunakan email alternatif
        if (!"testlogin@gmail.com".equalsIgnoreCase(loginEmail)) {
            System.out.println("LOG [Auto-Recovery]: Terdeteksi sisa crash email. Melakukan recovery ke testlogin@gmail.com...");
            navigateToProfile();
            profilePage.clickChangeEmail();
            String recoveryPassword = getActivePassword();
            profilePage.enterVerifyPassword(recoveryPassword);
            profilePage.enterNewEmail("testlogin@gmail.com");
            profilePage.clickSaveEmail();
            
            if (profilePage.isChangeEmailSuccess()) {
                System.out.println("LOG [Auto-Recovery]: Recovery email sukses! Akun kembali ke testlogin@gmail.com.");
                saveActiveEmail("testlogin@gmail.com");
                loginEmail = "testlogin@gmail.com";
                profilePage.clickLogout();
                profilePage.isLoggedOut();
                loginPage.enterEmail("testlogin@gmail.com");
                loginPage.enterPassword(recoveryPassword);
                loginPage.clickLogin();
            } else {
                System.out.println("LOG [Auto-Recovery]: Gagal recovery email.");
            }
        }

        // Step 5: Navigasi ke Profile
        navigateToProfile();
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
        lastNewPasswordEntered = password;
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
        saveActivePassword(lastNewPasswordEntered);
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
        if ("testloginbaru@gmail.com".equalsIgnoreCase(email)) {
            email = "testloginbaru" + (System.currentTimeMillis() % 100000) + "@gmail.com";
            changedEmail = email;
            saveActiveEmail(email);
            System.out.println("LOG: Menggunakan new email unik: " + email);
        }
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

    // =================== CLEANUP / RESET DATA ===================

    @Given("User sudah login dengan email baru {string}")
    public void user_sudah_login_dengan_email_baru(String emailBaru) {
        driver = Hooks.driver;
        profilePage = new ProfilePage(driver);
        loginPage = new LoginPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));

        if ("testloginbaru@gmail.com".equalsIgnoreCase(emailBaru)) {
            String activeEmail = getActiveEmail();
            if (activeEmail.contains("testloginbaru")) {
                emailBaru = activeEmail;
            }
        }

        // Onboarding
        try {
            By btnGetStarted = By.xpath("//*[contains(@text,'Get Started')]");
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.elementToBeClickable(btnGetStarted)).click();
            System.out.println("LOG: Klik Get Started onboarding.");
        } catch (Exception e) {
            System.out.println("LOG: Sudah di halaman Login.");
        }

        // Login (coba active password dulu, kalau gagal coba alternative password)
        String currentPassword = getActivePassword();
        String otherPassword = currentPassword.equals("Password123") ? "NewPassword123" : "Password123";

        loginPage.enterEmail(emailBaru);
        loginPage.enterPassword(currentPassword);
        loginPage.clickLogin();

        boolean loginSuccess = false;
        try {
            By dashboard = By.xpath(
                    "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
            loginSuccess = true;
            System.out.println("LOG: Login berhasil with password: " + currentPassword);
        } catch (Exception e) {
            loginSuccess = false;
        }

        if (!loginSuccess) {
            // Tutup alert error login terlebih dahulu agar text field di belakangnya bisa diklik
            try {
                By btnOK = By.xpath("//*[@text='OK']");
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                System.out.println("LOG [Auto-Recovery]: Popup OK error login diklik.");
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            loginPage.enterEmail(emailBaru);
            loginPage.enterPassword(otherPassword);
            loginPage.clickLogin();
            
            try {
                By dashboard = By.xpath(
                        "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
                );
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.visibilityOfElementLocated(dashboard));
                saveActivePassword(otherPassword);
                System.out.println("LOG: Login berhasil dengan password alternatif: " + otherPassword);
            } catch (Exception ex) {
                throw new RuntimeException("Gagal login dengan email baru " + emailBaru);
            }
        }

        System.out.println("LOG: Login dengan email baru dilakukan: " + emailBaru);
        navigateToProfile();
    }

    @When("User mengganti kembali emailnya menjadi email awal")
    public void user_mengganti_kembali_emailnya_menjadi_email_awal() {
        profilePage.clickChangeEmail();
        String currentPwd = getActivePassword();
        profilePage.enterVerifyPassword(currentPwd);
        profilePage.enterNewEmail("testlogin@gmail.com");
        profilePage.clickSaveEmail();
        
        emailResetSuccess = profilePage.isChangeEmailSuccess();
        if (emailResetSuccess) {
            saveActiveEmail("testlogin@gmail.com");
            System.out.println("LOG: Email diganti kembali ke email awal.");
        } else {
            System.out.println("LOG: Gagal mengganti kembali email ke email awal!");
        }
    }

    @When("User mengganti kembali passwordnya menjadi {string}")
    public void user_mengganti_kembali_passwordnya_menjadi(String passwordAwal) {
        try {
            profilePage.clickChangePassword();
            String currentPwd = getActivePassword();
            profilePage.enterCurrentPassword(currentPwd);
            profilePage.enterNewPassword(passwordAwal);
            profilePage.enterConfirmNewPassword(passwordAwal);
            profilePage.clickSavePassword();
            
            passwordResetSuccess = profilePage.isChangePasswordSuccess();
            if (passwordResetSuccess) {
                saveActivePassword(passwordAwal);
                System.out.println("LOG: Password dipastikan kembali ke password awal.");
            } else {
                System.out.println("LOG: Gagal menyimpan password awal.");
            }
        } catch (Exception e) {
            System.out.println("LOG: Skip/Gagal mengganti password: " + e.getMessage());
        }
    }

    @Then("Data testing berhasil di-reset ke awal")
    public void data_testing_berhasil_di_reset_ke_awal() {
        Assert.assertTrue(emailResetSuccess, "Gagal! Email tidak berhasil di-reset ke awal.");
        Assert.assertTrue(passwordResetSuccess, "Gagal! Password tidak berhasil di-reset ke awal.");
        System.out.println("LOG: Data testing berhasil di-reset ke awal.");
    }
}