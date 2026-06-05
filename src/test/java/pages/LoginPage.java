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
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
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
            List<WebElement> clickableGroups = driver.findElements(
                    By.xpath("//android.view.ViewGroup[@clickable='true']")
            );
            System.out.println("[LoginPage] ViewGroup clickable: " + clickableGroups.size());

            WebElement tombol = null;
            for (WebElement el : clickableGroups) {
                String desc = el.getAttribute("content-desc");
                if (desc != null && (
                        desc.toLowerCase().contains("log") ||
                                desc.toLowerCase().contains("masuk") ||
                                desc.toLowerCase().contains("sign"))) {
                    tombol = el;
                    break;
                }
            }

            if (tombol == null && !clickableGroups.isEmpty()) {
                tombol = clickableGroups.get(clickableGroups.size() - 1);
                System.out.println("[LoginPage] Fallback: ViewGroup terakhir");
            }

            if (tombol != null) {
                tombol.click();
            } else {
                throw new RuntimeException("Tombol Login tidak ditemukan!");
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error klik login: " + e.getMessage());
        }
    }

    public boolean isLoginSuccess() {
        try {
            Thread.sleep(3000);
            By dashboard = By.xpath(
                    "//*[contains(@text,'Dashboard') or contains(@text,'Home') or contains(@text,'Pasien') or contains(@text,'Patient')]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(dashboard));
            System.out.println("LOG: Login sukses, Dashboard terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Dashboard tidak ditemukan: " + e.getMessage());
            return false;
        }
    }

    public boolean isLoginFailed() {
        try {
            Thread.sleep(2000);
            By alertText = By.xpath(
                    "//*[contains(@text,'Email atau password') or contains(@text,'Email tidak terdaftar') or contains(@text,'wajib diisi') or contains(@text,'Alert')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(alertText));
            System.out.println("LOG: Popup error login terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Popup error tidak ditemukan: " + e.getMessage());
            return false;
        }
    }
}