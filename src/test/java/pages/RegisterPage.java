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
    private String finalEmail;

    private By inputNama = By.xpath("(//android.widget.EditText)[1]");
    private By inputEmail = By.xpath("(//android.widget.EditText)[2]");
    private By inputPassword = By.xpath("(//android.widget.EditText)[last()-1]");
    private By inputConfirmPassword = By.xpath("(//android.widget.EditText)[last()]");
    private By checkboxAgree = By.xpath("//android.widget.CheckBox | //*[contains(@text, 'Agree') or contains(@text, 'Term') or contains(@text, 'Saya setuju')]");
    private By btnRegister = By.xpath("//android.widget.Button | (//android.widget.TextView[contains(@text, 'Sign Up') or contains(@text,'Register') or contains(@text,'Daftar')])[last()]");

    public RegisterPage(AppiumDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void enterNama(String nama) {
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputNama));
        field.clear();
        field.sendKeys(nama);
    }

    public void enterEmail(String email) {
        if (email.contains("@") && !email.isEmpty()) {
            String[] parts = email.split("@");
            long random = System.currentTimeMillis() % 1000000;
            email = parts[0] + random + "@" + parts[1];
        }
        this.finalEmail = email;
        System.out.println("LOG: Email unik untuk Register: " + email);
        WebElement field = wait.until(ExpectedConditions.visibilityOfElementLocated(inputEmail));
        field.clear();
        field.sendKeys(email);
    }

    public String getFinalEmail() {
        return finalEmail;
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
        try {
            driver.findElement(checkboxAgree).click();
        } catch (Exception e) {
            // Rencana B klik via label teks luar
            driver.findElement(By.xpath("//*[contains(@text,'Agree') or contains(@text,'Syarat')]")).click();
        }
    }

    public void clickRegister() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(btnRegister)).click();
        } catch (Exception e) {
            // Paksa klik text penanda tombol register paling bawah
            driver.findElement(By.xpath("//android.widget.TextView[@text='Sign Up' or @text='Register' or @text='Daftar']")).click();
        }
    }

    public boolean isRedirectedToLogin() {
        try {
            By btnOK = By.xpath("//*[@text='OK'] | //*[@text='Ok'] | //*[contains(@text,'Berhasil')]");
            try {
                new WebDriverWait(driver, Duration.ofSeconds(4))
                        .until(ExpectedConditions.elementToBeClickable(btnOK)).click();
                Thread.sleep(1000);
            } catch (Exception ignored) {}

            By loginIndicator = By.xpath(
                    "//*[contains(@text,'Welcome Back') or contains(@text,'Log In') or contains(@text,'Login') or contains(@text,'Sign In')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(loginIndicator));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isErrorDisplayed() {
        try {
            By alertText = By.xpath(
                    "//*[contains(@text,'minimal') or contains(@text,'tidak sama') or contains(@text,'wajib') or contains(@text,'Alert') or contains(@text,'Error') or contains(@text,'salah')]"
            );
            new WebDriverWait(driver, Duration.ofSeconds(8)).until(ExpectedConditions.visibilityOfElementLocated(alertText));
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}