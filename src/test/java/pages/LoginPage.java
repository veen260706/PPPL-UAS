package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class LoginPage {
    AppiumDriver driver;
    WebDriverWait wait;

    private By inputEmail = By.xpath("(//android.widget.EditText)[1]");
    private By inputPassword = By.xpath("(//android.widget.EditText)[2]");

    public LoginPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterEmail(String email) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(inputEmail));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.elementToBeClickable(inputPassword));
        field.clear();
        field.sendKeys(password);
    }

    public void clickLogin() {
        try {
            // Beri jeda sangat singkat agar keyboard tidak menutupi tombol
            Thread.sleep(500);
            List<WebElement> clickableGroups = driver.findElements(
                    By.xpath("//android.view.ViewGroup[@clickable='true'] | //android.widget.Button")
            );

            WebElement tombol = null;
            for (WebElement el : clickableGroups) {
                String desc = el.getAttribute("content-desc");
                String txt = el.getText();
                String target = (desc != null ? desc : "") + (txt != null ? txt : "");

                if (!target.isEmpty() && (
                        target.toLowerCase().contains("log") ||
                                target.toLowerCase().contains("masuk") ||
                                target.toLowerCase().contains("sign"))) {
                    tombol = el;
                    break;
                }
            }

            if (tombol == null) {
                // Rencana Cadangan: Tembak teks langsung di dalam layar
                try {
                    tombol = driver.findElement(By.xpath("//android.widget.TextView[contains(@text,'Log In') or contains(@text,'Login') or contains(@text,'Masuk')]"));
                } catch (Exception ignored) {}
            }

            if (tombol == null && !clickableGroups.isEmpty()) {
                tombol = clickableGroups.get(clickableGroups.size() - 1);
            }

            if (tombol != null) {
                tombol.click();
            } else {
                throw new RuntimeException("Tombol Login tidak ditemukan!");
            }
        } catch (Exception e) {
            throw new RuntimeException("Error klik login: " + e.getMessage());
        }
    }

    public boolean isLoginSuccess() {
        try {
            By dashboard = By.xpath(
                    "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient') or contains(@text,'Tentang')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(10)).until(ExpectedConditions.visibilityOfElementLocated(dashboard));
            System.out.println("LOG: Login sukses, Dashboard terdeteksi.");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isLoginFailed() {
        try {
            By alertText = By.xpath(
                    "//*[contains(@text,'Email atau password') or contains(@text,'salah') or contains(@text,'tidak terdaftar') or contains(@text,'wajib diisi') or contains(@text,'Alert') or contains(@text,'Invalid')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(alertText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}