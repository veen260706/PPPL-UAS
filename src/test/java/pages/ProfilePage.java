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

    private By btnChangePassword = By.xpath("//android.widget.TextView[contains(@text,'Change Password')]");
    private By btnChangeEmail = By.xpath("//android.widget.TextView[contains(@text,'Change Email')]");
    private By btnLogout = By.xpath("//android.widget.TextView[contains(@text,'Logout')]");

    public ProfilePage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    private void tapElement(WebElement el) throws Exception {
        int x = el.getLocation().getX() + el.getSize().getWidth() / 2;
        int y = el.getLocation().getY() + el.getSize().getHeight() / 2;
        PointerInput finger = new PointerInput(PointerInput.Kind.TOUCH, "finger");
        Sequence tap = new Sequence(finger, 1);
        tap.addAction(finger.createPointerMove(Duration.ZERO,
                PointerInput.Origin.viewport(), x, y));
        tap.addAction(finger.createPointerDown(PointerInput.MouseButton.LEFT.asArg()));
        tap.addAction(finger.createPointerUp(PointerInput.MouseButton.LEFT.asArg()));
        driver.perform(Arrays.asList(tap));
        System.out.println("LOG: Tap koordinat x=" + x + " y=" + y);
    }

    // =================== LOGOUT ===================

    public void clickLogout() {
        wait.until(ExpectedConditions.elementToBeClickable(btnLogout)).click();
        System.out.println("LOG: Tombol Logout diklik.");
    }

    public boolean isLoggedOut() {
        try {
            Thread.sleep(2000);
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
                    By.xpath("//android.widget.TextView[@text='Save Password']")));
            tapElement(btn);
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException("Tombol Save Password tidak bisa diklik: " + e.getMessage());
        }
    }

    public boolean isChangePasswordSuccess() {
        try {
            Thread.sleep(2000);
            List<WebElement> allTexts = driver.findElements(By.className("android.widget.TextView"));
            System.out.println("[DEBUG] Teks setelah Save Password:");
            for (WebElement el : allTexts) {
                System.out.println("  >> '" + el.getText() + "'");
            }
            By successAlert = By.xpath(
                    "//*[contains(@text,'Sukses') or contains(@text,'berhasil') or contains(@text,'success')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            System.out.println("LOG: Popup sukses terdeteksi.");
            return true;
        } catch (Exception e) {
            // Cek dialog tertutup
            try {
                By dialogTitle = By.xpath("//android.widget.TextView[@text='Change Password']");
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.invisibilityOfElementLocated(dialogTitle));
                System.out.println("LOG: Dialog tertutup = sukses.");
                return true;
            } catch (Exception e2) {
                System.out.println("LOG: Dialog masih terbuka.");
                return false;
            }
        }
    }

    public boolean isChangePasswordError() {
        try {
            Thread.sleep(2000);
            List<WebElement> allTexts = driver.findElements(By.className("android.widget.TextView"));
            System.out.println("[DEBUG] Teks setelah Save Password error:");
            for (WebElement el : allTexts) {
                System.out.println("  >> '" + el.getText() + "'");
            }
            // Cek popup Gagal
            By errorAlert = By.xpath(
                    "//*[contains(@text,'Gagal') or contains(@text,'tidak sesuai') or contains(@text,'tidak cocok') or contains(@text,'salah') or contains(@text,'wajib')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            // Kalau tidak ada popup, cek dialog masih terbuka = error
            try {
                By saveBtn = By.xpath("//android.widget.TextView[@text='Save Password']");
                driver.findElement(saveBtn);
                System.out.println("LOG: Dialog masih terbuka = error terdeteksi.");
                return true;
            } catch (Exception e2) {
                return false;
            }
        }
    }

    // =================== CHANGE EMAIL ===================

    public void clickChangeEmail() {
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
                    By.xpath("(//android.widget.TextView[@text='Change Email'])[last()]")));
            tapElement(btn);
            Thread.sleep(2000);
        } catch (Exception e) {
            throw new RuntimeException("Tombol Save Email tidak bisa diklik: " + e.getMessage());
        }
    }

    public boolean isChangeEmailSuccess() {
        try {
            Thread.sleep(2000);
            List<WebElement> allTexts = driver.findElements(By.className("android.widget.TextView"));
            System.out.println("[DEBUG] Teks setelah Save Email:");
            for (WebElement el : allTexts) {
                System.out.println("  >> '" + el.getText() + "'");
            }
            By successAlert = By.xpath(
                    "//*[contains(@text,'Sukses') or contains(@text,'berhasil') or contains(@text,'success')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(successAlert));
            System.out.println("LOG: Email sukses terdeteksi.");
            return true;
        } catch (Exception e) {
            try {
                By dialogTitle = By.xpath("//android.widget.TextView[@text='Change Email']");
                new WebDriverWait(driver, Duration.ofSeconds(3))
                        .until(ExpectedConditions.invisibilityOfElementLocated(dialogTitle));
                System.out.println("LOG: Dialog Email tertutup = sukses.");
                return true;
            } catch (Exception e2) {
                System.out.println("LOG: Dialog masih terbuka.");
                return false;
            }
        }
    }

    public boolean isChangeEmailError() {
        try {
            Thread.sleep(2000);
            List<WebElement> allTexts = driver.findElements(By.className("android.widget.TextView"));
            System.out.println("[DEBUG] Teks setelah Save Email error:");
            for (WebElement el : allTexts) {
                System.out.println("  >> '" + el.getText() + "'");
            }
            By errorAlert = By.xpath(
                    "//*[contains(@text,'Gagal') or contains(@text,'salah') or contains(@text,'tidak') or contains(@text,'wajib') or contains(@text,'invalid')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(errorAlert));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}