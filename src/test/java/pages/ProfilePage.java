package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.PointerInput;
import org.openqa.selenium.interactions.Sequence;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

public class ProfilePage {
    AppiumDriver driver;
    WebDriverWait wait;

    private By btnChangePassword = By.xpath("//*[contains(@text,'Change Password') or contains(@text,'Ubah Password')]");
    private By btnChangeEmail = By.xpath("//*[contains(@text,'Change Email') or contains(@text,'Ubah Email')]");
    private By btnLogout = By.xpath("//*[contains(@text,'Logout') or contains(@text,'Keluar')]");

    public ProfilePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Fungsi bantu untuk scroll layar ke bawah secara otomatis hingga teks target terlihat
    private void scrollToText(String text) {
        try {
            driver.findElement(io.appium.java_client.AppiumBy.androidUIAutomator(
                    "new UiScrollable(new UiSelector().scrollable(true)).scrollIntoView(" +
                            "new UiSelector().textContains(\"" + text + "\"));"
            ));
            Thread.sleep(800); // Beri jeda sangat singkat agar UI stabil pasca-scroll
        } catch (Exception ignored) {
            System.out.println("LOG [WARNING]: Gagal scroll otomatis ke teks: " + text + ". Mencoba posisi layar saat ini.");
        }
    }

    private void tapElement(WebElement el) throws Exception {
        int x = el.getLocation().getX() + el.getSize().getWidth() / 2;
        int y = el.getLocation().getY() + el.getSize().getHeight() / 2;
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO, PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
        System.out.println("LOG: Tap koordinat x=" + x + " y=" + y);
    }

    // =================== LOGOUT ===================

    public void clickLogout() {
        scrollToText("Logout"); // Gulir ke bawah dulu mencari tombol Logout
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
        System.out.println("LOG: Tombol Logout diklik.");
    }

    public boolean isLoggedOut() {
        try {
            Thread.sleep(1500);
            By loginIndicator = By.xpath(
                    "//*[contains(@text,'Welcome Back') or contains(@text,'Log In') or contains(@text,'Get Started')]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginIndicator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // =================== CHANGE PASSWORD ===================

    public void clickChangePassword() {
        scrollToText("Change Password"); // Gulir ke bawah dulu mencari tombol Change Password
        wait.until(ExpectedConditions.elementToBeClickable(btnChangePassword)).click();
        System.out.println("LOG: Tombol Change Password diklik.");
    }

    public void enterCurrentPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//android.widget.EditText)[1]")));
        field.clear();
        field.sendKeys(password);
    }

    public void enterNewPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//android.widget.EditText)[2]")));
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmNewPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//android.widget.EditText)[3]")));
        field.clear();
        field.sendKeys(password);
    }

    public void clickSavePassword() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("//*[@text='Save Password' or contains(@text,'Save') or contains(@text,'Simpan')]")));
            tapElement(btn);
            System.out.println("LOG: Perintah simpan password dikirim.");
        } catch (Exception e) {
            throw new RuntimeException("Tombol Save Password tidak bisa diklik: " + e.getMessage());
        }
    }

    public boolean isChangePasswordSuccess() {
        try {
            System.out.println("LOG: Menunggu respons pembaruan password dari server...");
            // Beri waktu napas tambahan bagi server/firebase untuk memproses penggantian password valid
            Thread.sleep(3500);

            // Coba deteksi alert/toast sukses yang mengandung kata kunci positif
            By successAlert = By.xpath(
                    "//*[contains(@text,'Sukses') or contains(@text,'berhasil') or contains(@text,'success') or contains(@text,'Success')]"
            );
            List<WebElement> alerts = driver.findElements(successAlert);
            if (!alerts.isEmpty()) {
                System.out.println("LOG: Popup/Toast sukses ganti password terdeteksi.");
                return true;
            }

            // Fallback Cerdas: Cek apakah modal form dialog otomatis menutup diri (berarti input valid)
            // Menggunakan deteksi keberadaan input fields ke-2 (New Password)
            By fieldNewPassword = By.xpath("(//android.widget.EditText)[2]");
            int checkField = driver.findElements(fieldNewPassword).size();

            if (checkField == 0) {
                System.out.println("LOG: Form dialog ganti password berhasil menutup otomatis. Skenario Sukses.");
                return true;
            }
        } catch (Exception ignored) {}

        System.out.println("LOG: Dialog masih terbuka.");
        return false;
    }

    public boolean isChangePasswordError() {
        try {
            By errorAlert = By.xpath(
                    "//*[contains(@text,'Gagal') or contains(@text,'tidak sesuai') or contains(@text,'tidak cocok') or contains(@text,'salah') or contains(@text,'wajib') or contains(@text,'Error')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            try {
                By fieldCurrentPassword = By.xpath("(//android.widget.EditText)[1]");
                driver.findElement(fieldCurrentPassword);
                System.out.println("LOG: Form masih terbuka pasca submit salah = error terkonfirmasi.");
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    // =================== CHANGE EMAIL ===================

    public void clickChangeEmail() {
        scrollToText("Change Email"); // Gulir ke bawah dulu mencari tombol Change Email
        wait.until(ExpectedConditions.elementToBeClickable(btnChangeEmail)).click();
        System.out.println("LOG: Tombol Change Email diklik.");
    }

    public void enterVerifyPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//android.widget.EditText)[1]")));
        field.clear();
        field.sendKeys(password);
    }

    public void enterNewEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.xpath("(//android.widget.EditText)[2]")));
        field.clear();
        field.sendKeys(email);
    }

    public void clickSaveEmail() {
        try {
            WebElement btn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                    By.xpath("(//*[@text='Change Email' or contains(@text,'Save') or contains(@text,'Simpan')])[last()]")));
            tapElement(btn);
            System.out.println("LOG: Perintah simpan email dikirim.");
        } catch (Exception e) {
            throw new RuntimeException("Tombol Save Email tidak bisa diklik: " + e.getMessage());
        }
    }

    public boolean isChangeEmailSuccess() {
        try {
            System.out.println("LOG: Menunggu respons pembaruan email dari server...");
            Thread.sleep(3500);

            By successAlert = By.xpath(
                    "//*[contains(@text,'Sukses') or contains(@text,'berhasil') or contains(@text,'success') or contains(@text,'Success')]"
            );
            List<WebElement> alerts = driver.findElements(successAlert);
            if (!alerts.isEmpty()) {
                System.out.println("LOG: Popup/Toast sukses ganti email terdeteksi.");
                return true;
            }

            // Fallback Cerdas: Cek apakah modal form dialog ganti email menutup diri
            By fieldVerifyPassword = By.xpath("(//android.widget.EditText)[1]");
            int checkField = driver.findElements(fieldVerifyPassword).size();

            if (checkField == 0) {
                System.out.println("LOG: Form dialog ganti email berhasil menutup otomatis. Skenario Sukses.");
                return true;
            }
        } catch (Exception ignored) {}

        System.out.println("LOG: Dialog email masih terbuka.");
        return false;
    }

    public boolean isChangeEmailError() {
        try {
            By errorAlert = By.xpath(
                    "//*[contains(@text,'Gagal') or contains(@text,'salah') or contains(@text,'tidak') or contains(@text,'wajib') or contains(@text,'invalid') or contains(@text,'Error')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}