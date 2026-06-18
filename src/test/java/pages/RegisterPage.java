package pages;

import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;
import java.util.List;

public class RegisterPage {
    AppiumDriver driver;
    WebDriverWait wait;

    private By inputNama = By.xpath("(//android.widget.EditText)[1]");
    private By inputEmail = By.xpath("(//android.widget.EditText)[2]");
    private By inputPassword = By.xpath("(//android.widget.EditText)[last()-1]");
    private By inputConfirmPassword = By.xpath("(//android.widget.EditText)[last()]");
    private By checkboxAgree = By.xpath("(//android.widget.TextView[contains(@text, 'Agree') or contains(@text, 'Term')])[1]");
    private By btnRegister = By.xpath("(//android.widget.TextView[contains(@text, 'Sign Up')])[1]");

    public static String lastGeneratedEmail = "";

    public RegisterPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    public void enterNama(String nama) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputNama));
        field.clear();
        field.sendKeys(nama);
    }

    public void enterEmail(String email) {
        if (email.contains("@")) {
            String[] parts = email.split("@");
            long random = System.currentTimeMillis() % 100000;
            email = parts[0] + random + "@" + parts[1];
        }
        lastGeneratedEmail = email;
        System.out.println("LOG: Email unik: " + email);
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail));
        field.clear();
        field.sendKeys(email);
    }

    public void enterPassword(String password) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputPassword));
        field.clear();
        field.sendKeys(password);
    }

    public void enterConfirmPassword(String confirmPassword) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputConfirmPassword));
        field.clear();
        field.sendKeys(confirmPassword);
    }

    public void clickCheckboxAgree() {
        wait.until(ExpectedConditions.elementToBeClickable(checkboxAgree)).click();
    }

    public void clickRegister() {
        wait.until(ExpectedConditions.elementToBeClickable(btnRegister)).click();
    }

    public boolean isRedirectedToLogin() {
        try {
            Thread.sleep(2000);
            // Klik OK di popup Alert kalau ada
            By btnOK = By.xpath("//*[@text='OK']");
            try {
                new WebDriverWait(driver, Duration.ofSeconds(5))
                        .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                System.out.println("LOG: Popup OK diklik.");
                Thread.sleep(2000);
            } catch (Exception ignored) {}

            // Cek halaman Login muncul
            By loginIndicator = By.xpath(
                    "//*[contains(@text,'Welcome Back') or contains(@text,'Log In') or contains(@text,'Login') or contains(@text,'Sign In')]"
            );
            wait.until(ExpectedConditions.visibilityOfElementLocated(loginIndicator));
            System.out.println("LOG: Redirect ke Login berhasil.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Redirect gagal: " + e.getMessage());
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            Thread.sleep(2000);
            // Cek popup Alert muncul
            By alertText = By.xpath(
                    "//*[contains(@text,'Password minimal') or contains(@text,'Password tidak sama') or contains(@text,'Semua field wajib') or contains(@text,'Alert')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.visibilityOfElementLocated(alertText));
            System.out.println("LOG: Popup error terdeteksi.");
            return true;
        } catch (Exception e) {
            System.out.println("LOG: Popup error tidak ditemukan: " + e.getMessage());
            return false;
        }
    }
}